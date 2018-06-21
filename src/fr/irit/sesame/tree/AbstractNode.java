package fr.irit.sesame.tree;

/**
 * Implementation of a Node.
 *
 * Subclasses must implement the following methods: 
 * {@link #getText() getText()},
 * {@link #nextNode(Node) nextNode(Node)},
 * {@link #prevNode(Node) prevNode(Node)}.
 */
public abstract class AbstractNode
  extends AbstractTreeChangedHandler
{

  // Fields
  
  protected InnerNode parent;

  // Constructor

  protected AbstractNode(InnerNode parent) {
    super();
    this.parent = parent;
  }

  // Node's implementation

  public InnerNode getParent() {
    return parent;
  }

  public ChooserNodeManager getChooserNodeManager() {
    return getParent().getChooserNodeManager();
  }

}
