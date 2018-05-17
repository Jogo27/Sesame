package fr.irit.sesame.tree;

/**
 * Implementation of a LeafNode.
 *
 * Subclasses must implement only the {@link #getText() getText()} method.
 */
public abstract class AbstractLeafNode
extends AbstractNode
implements LeafNode
{

  protected AbstractLeafNode(InnerNode parent) {
    super(parent);
  }

  public Node nextNode(Node from) throws TraversalException {
    if (from == getParent()) return this;
    return getParent().nextNode(from);
  }

  public Node prevNode(Node from) throws TraversalException {
    if (from == getParent()) return this;
    return getParent().prevNode(from);
  }

}
