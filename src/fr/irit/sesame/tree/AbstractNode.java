package fr.irit.sesame.tree;

import fr.irit.sesame.util.ListenerHandler;

/**
 * Implementation of a Node.
 *
 * Subclasses must implement the following methods: 
 * {@link #getText() getText()},
 * {@link #nextNode(Node) nextNode(Node)},
 * {@link #prevNode(Node) prevNode(Node)}.
 */
public abstract class AbstractNode
  implements Node
{

  // Fields
  
  protected InnerNode parent;
  private ListenerHandler<TreeChangedListener> treeChangedListeners;

  // Constructor

  protected AbstractNode(InnerNode parent) {
    this.parent = parent;
    treeChangedListeners = new ListenerHandler<TreeChangedListener>();
  }

  // Node's implementation

  public InnerNode getParent() {
    return parent;
  }

  public ChooserNodeFactory getFactory() {
    return getParent().getFactory();
  }

  public void addTreeChangedListener(TreeChangedListener listener) {
    treeChangedListeners.add(listener);
  }
  
  public void removeTreeChangedListener(TreeChangedListener listener) {
    treeChangedListeners.remove(listener);
  }

  protected void fireTreeChangedEvent(TreeChangedEvent event) {
    for (TreeChangedListener listener : treeChangedListeners)
      listener.onTreeChange(event);
  }
}
