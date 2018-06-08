package fr.irit.sesame.ui;

import java.lang.reflect.InvocationTargetException;

import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.tree.ChooserNodeConstructor;
import fr.irit.sesame.tree.ChooserNodeDecorator;
import fr.irit.sesame.tree.ChooserNodeFactory;
import fr.irit.sesame.tree.LeafNode;
import fr.irit.sesame.tree.Node;
import fr.irit.sesame.tree.ReplaceSubtreeAction;
import fr.irit.sesame.tree.TraversalException;
import fr.irit.sesame.tree.TreeChangedEvent;
import fr.irit.sesame.tree.TreeChangedListener;
import fr.irit.sesame.util.ListenerHandler;

/**
 * ChooserNodeFactory for tests.
 */
public class GenericChooserModel
  implements ChooserNodeFactory, TreeChangedListener
{

  // Fields

  private Node root;
  private NavigableChooserNode currentChooser;
  private ListenerHandler<ChooserChangedListener> chooserChangedListeners;
  private ListenerHandler<UndoRedoListener> undoRedoListeners;


  // Constructor

  public GenericChooserModel() {
    root = null;
    currentChooser = null;
    chooserChangedListeners = new ListenerHandler<ChooserChangedListener>();
    undoRedoListeners = new ListenerHandler<UndoRedoListener>();
  }

  public void setTree(Node tree) {
    if (tree == root) return;
    if (root != null) root.removeTreeChangedListener(this);
    root = tree;
    root.addTreeChangedListener(this);

    ChooserNodesInterval interval = new ChooserNodesInterval(root);
    setCurrent(interval.first);
  }

  private void setCurrent(NavigableChooserNode newChooser) {
    if (currentChooser != null) { currentChooser.setSelected(false); }
    currentChooser = newChooser;
    if (currentChooser != null) { currentChooser.setSelected(true); }
    else fireChooserChangedEvent(new ChooserChangedEvent(this));
  }


  // Choices

  public ChooserNode getChooser() {
    return currentChooser;
  }

  public void choose(int pos) {
    UndoableChooseAction action = new UndoableChooseAction(currentChooser, currentChooser.makeChoice(pos));
    action.redo();
    fireUndoRedoAction(action);
  }

  public void addChooserChangedListener(ChooserChangedListener listener) {
    chooserChangedListeners.add(listener);
  }

  public void removeChooserChangedListener(ChooserChangedListener listener) {
    chooserChangedListeners.remove(listener);
  }

  protected void fireChooserChangedEvent(ChooserChangedEvent event) {
    for (ChooserChangedListener listener : chooserChangedListeners)
      listener.onChooserChange(event);
  }


  // Navigation

  /**
   * Decorator for ChooserNode.
   * Store the associated ReplaceSubtreeAction in the ChooserNode,
   * provide prev and next pointers and may be selected or not.
   */
  static private class NavigableChooserNode
      extends ChooserNodeDecorator
  {
    final ReplaceSubtreeAction action;
    NavigableChooserNode prev;
    NavigableChooserNode next;
    private boolean selected;

    NavigableChooserNode(ChooserNode node, ReplaceSubtreeAction action) {
      super(node);
      this.action = action;
      this.prev = null;
      this.next = null;
      this.selected = false;
    }

    void setSelected(boolean selected) {
      if (selected == this.selected) return;
      this.selected = selected;
      fireTreeChangedEvent(new TreeChangedEvent(this));
    }

    @Override
    public String getText() {
      if (selected)
        return "<b>" + realNode.getText() + "</b>";
      return realNode.getText();
    }

  }

  private class ChooserNodesInterval {
    NavigableChooserNode first;
    NavigableChooserNode last;

    /**
     * Find all the NavigableChooserNode in the subtree, chain them together and return the first and the last ones.
     */
    ChooserNodesInterval(Node subtree) {
      first = null;
      last  = null;

      try {
        Node firstLeaf;
        for (firstLeaf = subtree; !(firstLeaf instanceof LeafNode); firstLeaf = firstLeaf.nextNode(firstLeaf)) {}

        Node curNode = subtree.prevNode(subtree.getParent());
        while(true) {
          if (curNode instanceof NavigableChooserNode) {
            NavigableChooserNode curLeaf = (NavigableChooserNode) curNode;
            curLeaf.next = this.first;
            curLeaf.prev = null;
            if (this.first != null) {
              this.first.prev = curLeaf;
            }
            this.first = curLeaf;
            if (this.last == null) {
              this.last = curLeaf;
            }
          }
          if (curNode == firstLeaf) break;
          curNode = curNode.prevNode(curNode);
        }

      } catch (TraversalException e) {
        throw new Error("The subtree does not respect the specification.", e);
      }
    }
  }

  public boolean hasPrevChooser() {
    return (currentChooser != null) && (currentChooser.prev != null);
  }

  public boolean hasNextChooser() {
    return (currentChooser != null) && (currentChooser.next != null);
  }

  public void goPrevChooser() {
    if (! hasPrevChooser()) return;
    NavigableChooserNode oldChooser = currentChooser;
    setCurrent(currentChooser.prev);
    fireUndoRedoAction(new UndoNavigationAction(oldChooser, currentChooser));
  }

  public void goNextChooser() {
    if (! hasNextChooser()) return;
    NavigableChooserNode oldChooser = currentChooser;
    setCurrent(currentChooser.next);
    fireUndoRedoAction(new UndoNavigationAction(oldChooser, currentChooser));
  }


  // Undo Redo

  private class UndoNavigationAction extends AbstractUndoRedoAction {

    NavigableChooserNode undoSelect;
    NavigableChooserNode redoSelect;

    UndoNavigationAction(NavigableChooserNode oldChooser, NavigableChooserNode newChooser) {
      super();
      undoSelect = oldChooser;
      redoSelect = newChooser;
    }

    @Override
    public void undo() {
      super.undo();
      GenericChooserModel.this.setCurrent(undoSelect);
    }

    @Override
    public void redo() {
      super.redo();
      GenericChooserModel.this.setCurrent(redoSelect);
    }

    public boolean isSignificant() {
      return false;
    }

    // TODO: allow to merge UndoNavigationActions

  }

  private class UndoableChooseAction extends UndoReplaceSubtreeAction {

    ChooserNodesInterval interval;

    UndoableChooseAction(NavigableChooserNode oldChooser, Node newSubtree) {
      super(false, oldChooser.action, newSubtree);
      this.interval = new ChooserNodesInterval(newSubtree);
      if (interval.first != null) {
        interval.first.prev = oldChooser.prev;
        interval.last.next  = oldChooser.next;
      }
    }

    @Override
    public void undo() {
      super.undo();
      NavigableChooserNode soleChooser = (NavigableChooserNode) action.currentSubtree();
      if (soleChooser.prev != null) soleChooser.prev.next = soleChooser;
      if (soleChooser.next != null) soleChooser.next.prev = soleChooser;
      GenericChooserModel.this.setCurrent(soleChooser);
    }

    @Override
    public void redo() {
      super.redo();
      NavigableChooserNode soleChooser = (NavigableChooserNode) altSubtree;
      if (soleChooser.prev != null) soleChooser.prev.next = (interval.first != null ? interval.first : soleChooser.next);
      if (soleChooser.next != null) soleChooser.next.prev = (interval.last  != null ? interval.last  : soleChooser.prev);
      GenericChooserModel.this.setCurrent(  interval.first   != null ? interval.first :
                                          ( soleChooser.next != null ? soleChooser.next :
                                                                       soleChooser.prev ));
    }

  }

  public void addUndoRedoListener(UndoRedoListener listener) {
    undoRedoListeners.add(listener);
  }

  public void removeUndoRedoListener(UndoRedoListener listener) {
    undoRedoListeners.remove(listener);
  }

  private void fireUndoRedoAction(UndoRedoAction action) {
    for (UndoRedoListener listener : undoRedoListeners)
      listener.undoableActionHappened(action);
  }


  // Implements interface ChooserNodeFactory

  public ChooserNode getChooser(ChooserNodeConstructor constructor, ReplaceSubtreeAction replacement)
  {
    return new NavigableChooserNode(constructor.makeChooserNode(replacement.getParentNode()), replacement);
  }


  // Implements interface TreeChangedListener

  public void onTreeChange(TreeChangedEvent evt) {
    // Currently, any change in the tree corresponds to a change in the GenericChooserModel.
    fireChooserChangedEvent(new ChooserChangedEvent(evt));
  }

}
