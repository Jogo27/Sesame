package fr.irit.sesame.ui;

import java.lang.reflect.InvocationTargetException;

import fr.irit.sesame.tree.ChooserChangedEvent;
import fr.irit.sesame.tree.ChooserChangedListener;
import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.tree.ChooserNodeConstructor;
import fr.irit.sesame.tree.ChooserNodeDecorator;
import fr.irit.sesame.tree.ChooserNodeFactory;
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

  /**
   * Decorator for ChooserNode.
   * Store the associated ReplaceSubtreeAction in the ChooserNode
   * and organize them as a double-linked list.
   */
  static private class NavigableChooserNode
      extends ChooserNodeDecorator
  {
    final ReplaceSubtreeAction action;
    private NavigableChooserNode prev;
    private NavigableChooserNode next;
    boolean selected;

    NavigableChooserNode(ChooserNode node, ReplaceSubtreeAction action) {
      super(node);
      this.action = action;

      this.prev = null;
      this.next = null;
      this.selected = false;
    }

    NavigableChooserNode nextChooser() {
      if ((next == null) || (! next.selected)) // TODO: Fix this ! It's ugly !
        try {
          Node n;
          for (n = this.nextNode(this); !(n instanceof NavigableChooserNode); n = n.nextNode(n)) {}
          next = (NavigableChooserNode) n;
          next.prev = this;
        } catch (TraversalException e) {
          next = null;
        }
      return next;
    }

    NavigableChooserNode prevChooser() {
      if ((prev == null) || (! prev.selected)) // TODO: Fix this ! It's ugly !
        try {
          Node n;
          for (n = this.prevNode(this); !(n instanceof NavigableChooserNode); n = n.prevNode(n)) {}
          prev = (NavigableChooserNode) n;
          prev.next = this;
        } catch (TraversalException e) {
          prev = null;
        }
      return prev;
    }

    void disconnect() {
      prev = null;
      next = null;
    }

    void setSelected(boolean selected) {
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
  }

  // Implements interface ChooserNodeFactory

  public ChooserNode getChooser(ChooserNodeConstructor constructor, ReplaceSubtreeAction replacement)
  {
    NavigableChooserNode chooser = new NavigableChooserNode(constructor.makeChooserNode(replacement.getParentNode()), replacement);
    if (currentChooser == null) {
      currentChooser = chooser;
      currentChooser.setSelected(true);
    }
    return chooser;
  }

  // Implements interface TreeChangedListener

  public void onTreeChange(TreeChangedEvent evt) {
    // Currently, any change in the tree corresponds to a change in the GenericChooserModel.
    fireChooserChangedEvent(new ChooserChangedEvent(evt));
  }

  // For the UI.

  public ChooserNode getChooser() {
    return currentChooser;
  }

  public boolean hasPrevChooser() {
    return (currentChooser != null) && (currentChooser.prevChooser() != null);
  }

  public boolean hasNextChooser() {
    return (currentChooser != null) && (currentChooser.nextChooser() != null);
  }

  private void setCurrent(NavigableChooserNode newChooser) {
    if (currentChooser != null) currentChooser.setSelected(false);
    currentChooser = newChooser;
    if (currentChooser != null) currentChooser.setSelected(true);
  }

  public void goPrevChooser() {
    if (! hasPrevChooser()) return;
    NavigableChooserNode oldChooser = currentChooser;
    setCurrent(currentChooser.prevChooser());
    fireUndoRedoAction(new UndoNavigationAction(oldChooser, currentChooser));
  }

  public void goNextChooser() {
    if (! hasNextChooser()) return;
    NavigableChooserNode oldChooser = currentChooser;
    setCurrent(currentChooser.nextChooser());
    fireUndoRedoAction(new UndoNavigationAction(oldChooser, currentChooser));
  }

  public void choose(int pos) {
    if (currentChooser == null) return;

    NavigableChooserNode oldChooser = currentChooser;
    oldChooser.setSelected(false);
    currentChooser = null; // Allows the new subtree to update the current ChooserNode.

    NavigableChooserNode newChooser; // The chooser to make current if the replacement does not introduce a new chooser.
    newChooser = oldChooser.nextChooser();
    if (newChooser == null) newChooser = oldChooser.prevChooser();
    
    oldChooser.action.replaceSubtree(oldChooser.makeChoice(pos));
    
    if (currentChooser == null) { // If no ChooserNode has been constructed by the previous line.
      currentChooser = newChooser;
      if (currentChooser != null) //TODO; this is ugly !
        currentChooser.setSelected(true);
    }

    oldChooser.disconnect();
    fireUndoRedoAction(new UndoChooseAction(oldChooser.action, oldChooser, currentChooser));
  }

  public void addChooserChangedListener(ChooserChangedListener listener) {
    chooserChangedListeners.add(listener);
  }

  public void removeChooserChangedListener(ChooserChangedListener listener) {
    chooserChangedListeners.remove(listener);
  }

  // Each modification in GenericChooserModel is reflected by at least one change in the tree.
  // Hence, fireChooserChangedEvent is only called in onTreeChange.
  protected void fireChooserChangedEvent(ChooserChangedEvent event) {
    for (ChooserChangedListener listener : chooserChangedListeners)
      listener.onChooserChange(event);
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

  private class UndoChooseAction extends UndoReplaceSubtreeAction {

    NavigableChooserNode toChoose;

    UndoChooseAction(ReplaceSubtreeAction replaceSubtreeAction, NavigableChooserNode oldChooser, NavigableChooserNode newChooser) {
      super(replaceSubtreeAction, oldChooser);
      this.toChoose = newChooser;
    }

    @Override
    public void undo() {
      GenericChooserModel.this.setCurrent((NavigableChooserNode) oldSubtree);
      super.undo();
    }

    @Override
    public void redo() {
      super.redo();
      GenericChooserModel.this.setCurrent(toChoose);
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

}