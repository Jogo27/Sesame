package fr.irit.sesame.tree;

public abstract class AbstractLeafNode
extends AbstractTreeNode
implements LeafNode
{

  protected AbstractLeafNode(InnerNode parent) {
    super(parent);
  }

  public TreeNode nextNode(TreeNode from) throws TreeNode.TraversalException {
    if (from == this) return getParent().nextNode(this);
    throw new TreeNode.TraversalException("From somewhere else than this node");
  }

  public TreeNode prevNode(TreeNode from) throws TreeNode.TraversalException {
    if (from == this) return getParent().prevNode(this);
    throw new TreeNode.TraversalException("From somewhere else than this node");
  }

}
