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

    NavigableChooserNode(ChooserNode node, ReplaceSubtreeAction action) {
      super(node);
      this.action = action;

      try {
        Node n;
        for (n = node.prevNode(node); !(n instanceof NavigableChooserNode); n = n.prevNode(n)) {}
        prev = (NavigableChooserNode) n;
      } catch (TraversalException e) {
        prev = null;
      }

      try {
        Node n;
        for (n = node.nextNode(node); !(n instanceof NavigableChooserNode); n = n.nextNode(n)) {}
        next = (NavigableChooserNode) n;
      } catch (TraversalException e) {
        next = null;
      }
    }

    NavigableChooserNode nextChooser() {
      return next;
    }

    NavigableChooserNode prevChooser() {
      return prev;
    }

    void disable() {
      if (prev != null)
        prev.next = this.next;
      if (next != null)
        next.prev = this.prev;
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

    NavigableChooserNode prevChooser = currentChooser;
    currentChooser = null; // Allows the new subtree to update the current ChooserNode.
    
    prevChooser.action.replaceSubtree(prevChooser.makeChoice(pos));
    
    if (currentChooser == null) { // If no ChooserNode has been constructed be the previous line.
      if (prevChooser.prevChooser() != null)
        currentChooser = prevChooser.prevChooser().nextChooser();
      else
        currentChooser = prevChooser.nextChooser();
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
