package fr.irit.sesame.tree;

import java.lang.reflect.InvocationTargetException;

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
      if ((next == null) || (! next.selected))
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
      if ((prev == null) || (! prev.selected))
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

    void disable() {
      if (prev != null)
        prev.next = this.next;
      if (next != null)
        next.prev = this.prev;
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

  // Constructor

  public GenericChooserModel() {
    root = null;
    currentChooser = null;
    chooserChangedListeners = new ListenerHandler<ChooserChangedListener>();
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

  public void goPrevChooser() {
    if (! hasPrevChooser()) return;
    currentChooser.setSelected(false);
    currentChooser = currentChooser.prevChooser();
    currentChooser.setSelected(true);
  }

  public void goNextChooser() {
    if (! hasNextChooser()) return;
    currentChooser.setSelected(false);
    currentChooser = currentChooser.nextChooser();
    currentChooser.setSelected(true);
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

}
