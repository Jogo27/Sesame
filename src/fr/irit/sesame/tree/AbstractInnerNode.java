package fr.irit.sesame.tree;

/**
 * Basic implementation of an inner node with a fixed number of children.
 *
 * Subclasses must implement only the getText method.
 *
 * TreeChangedEvent are propagated from the leaves (children) to the root (parent).
 */
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

  /**
   * Attach a new chooser as child.
   * To be used in subclasses' constructors.
   */
  protected void initChooser(final int pos, ChooserNodeConstructor constructor)
  {
    ChooserNode chooser = getFactory().getChooser(constructor,
        new ReplaceSubtreeAction() {
          public InnerNode getParentNode() { return AbstractInnerNode.this; }
          public TreeNode currentSubtree() { return children[pos]; }
          public void replaceSubtree(TreeNode subtree) {
            detachSubtree(pos);
            attachSubtree(pos, subtree);
            fireTreeChangedEvent(new TreeChangedEvent(AbstractInnerNode.this));
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
