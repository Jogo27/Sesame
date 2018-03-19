package fr.irit.sesame.tree;

public abstract class AbstractInnerNode
  extends AbstractTreeNode implements InnerNode, TreeChangedListener
{

  protected int nbChildren;
  protected TreeNode[] children;

  protected AbstractInnerNode(int nbChildren, InnerNode parent) {
    super(parent);
    this.nbChildren = nbChildren;
    children = new TreeNode[this.nbChildren];
  }

  protected void attachSubtree(int pos, TreeNode subtree) {
    children[pos] = subtree;
    children[pos].addTreeChangedListener(this);
  }

  protected void detachSubtree(int pos) {
    children[pos].removeTreeChangedListener(this);
  }

  protected void initChooser(int pos, ChooserNodeType type) {
    ChooserNode chooser = getFactory().getChooser(type,
        new ReplaceSubtreeAction() {
          public InnerNode getParentNode() { return getParent(); }
          public TreeNode currentSubtree() { return children[pos]; }
          public void replaceSubtree(TreeNode subtree) {
            detachSubtree(pos);
            attachSubtree(pos, subtree);
          }
        });
    attachSubtree(pos, chooser);
  }

  public TreeNode nextNode(TreeNode from) throws TraversalException {
    if (from == parent) return this;
    if ((from == this) || (from == null)) return children[0];
    if (from == children[nbChildren - 1]) return parent.nextNode(this);
    for (int i = 0; i < nbChildren - 1; i++) 
      if (from == children[i]) return children[i+1];
    throw new TreeNode.TraversalException("Wrong from for next");
  }

  public TreeNode prevNode(TreeNode from) throws TraversalException {
    if ((from == parent) || (from == null)) return children[nbChildren - 1];
    if (from == this) return parent.prevNode(this);
    if (from == children[1]) return this;
    for (int i = 1; i < nbChildren; i--) 
      if (from == children[i]) return children[i-1];
    throw new TreeNode.TraversalException("Wrong from for prev");
  }

  public void onTreeChange(TreeChangedEvent event) {
    fireTreeChangedEvent(event);
  }

}
