package fr.irit.sesame.tree;

import java.lang.reflect.InvocationTargetException;

import fr.irit.sesame.util.ListenerHandler;

/**
 * ChooserNodeFactory for tests.
 */
public class GenericChooserModel
  implements ChooserNodeFactory
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

      this.prev = this; // means unset
      this.next = this;
      this.selected = false;
    }

    NavigableChooserNode nextChooser() {
      if (next == this)
        try {
          Node n;
          for (n = this.nextNode(this); !(n instanceof NavigableChooserNode); n = n.nextNode(n)) {}
          next = (NavigableChooserNode) n;
        } catch (TraversalException e) {
          next = null;
        }
      return next;
    }

    NavigableChooserNode prevChooser() {
      if (prev == this)
        try {
          Node n;
          for (n = this.prevNode(this); !(n instanceof NavigableChooserNode); n = n.prevNode(n)) {}
          prev = (NavigableChooserNode) n;
        } catch (TraversalException e) {
          prev = null;
        }
      return prev;
    }

    void disable() {
      if (prevChooser() != null)
        prev.next = this.next;
      if (nextChooser() != null)
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

  private NavigableChooserNode currentChooser;
  private ListenerHandler<ChooserChangedListener> chooserChangedListeners;

  // Constructor

  public GenericChooserModel() {
    currentChooser = null;
    chooserChangedListeners = new ListenerHandler<ChooserChangedListener>();
  }

  // Implements the ChooserNodeFactory interface.

  public ChooserNode getChooser(ChooserNodeConstructor constructor, ReplaceSubtreeAction replacement)
  {
    NavigableChooserNode chooser = new NavigableChooserNode(constructor.makeChooserNode(replacement.getParentNode()), replacement);
    if (currentChooser == null) {
      currentChooser = chooser;
      currentChooser.setSelected(true);
      fireChooserChangedEvent(new ChooserChangedEvent(this));
    }
    return chooser;
  }

  // For the UI.

  public ChooserNode getChooser() {
    return currentChooser;
  }

  public void choose(int pos) {
    if (currentChooser == null) return;

    NavigableChooserNode oldChooser = currentChooser;
    oldChooser.setSelected(false);
    currentChooser = null; // Allows the new subtree to update the current ChooserNode.

    NavigableChooserNode newChooser; // The chooser to make current if the replacement does not introduce a new chooser.
    if (oldChooser.prevChooser() != null)
      newChooser = oldChooser.prevChooser().nextChooser();
    else
      newChooser = oldChooser.nextChooser();
    
    oldChooser.action.replaceSubtree(oldChooser.makeChoice(pos));
    
    if (currentChooser == null) { // If no ChooserNode has been constructed by the previous line.
      currentChooser = newChooser;
      if (currentChooser != null) //TODO; this is ugly !
        currentChooser.setSelected(true);
      fireChooserChangedEvent(new ChooserChangedEvent(this));
    }
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

}
