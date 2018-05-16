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
    if (from == this) return getParent().nextNode(this);
    if (from == getParent()) return this;
    throw new TraversalException("From somewhere else than this node");
  }

  public Node prevNode(Node from) throws TraversalException {
    if (from == this) return getParent().prevNode(this);
    if (from == getParent()) return this;
    throw new TraversalException("From somewhere else than this node");
  }

}
