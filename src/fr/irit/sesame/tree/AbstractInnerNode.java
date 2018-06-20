package fr.irit.sesame.tree;

/**
 * Implementation of an InnerNode with a fixed number of children.
 *
 * Subclasses must implement only the {@link #getText() getText()} method.
 *
 * {@link TreeChangedEvent} are propagated from the leaves (children) to the root (parent).
 */
public abstract class AbstractInnerNode
  extends AbstractNode implements InnerNode, TreeChangedListener
{

  protected int nbChildren;
  protected Node[] children;

  protected AbstractInnerNode(InnerNode parent, int nbChildren) {
    super(parent);
    this.nbChildren = nbChildren;
    children = new Node[this.nbChildren];
  }

  protected void attachSubtree(int pos, Node subtree) {
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
  protected void initBranch(final int pos, NodeConstructor constructor)
  {
    Node branch = constructor.makeNode(this,
        new ReplaceSubtreeAction() {
          public InnerNode getParentNode() { return AbstractInnerNode.this; }
          public Node currentSubtree() { return children[pos]; }
          public void replaceSubtree(Node subtree) {
            detachSubtree(pos);
            attachSubtree(pos, subtree);
            fireTreeChangedEvent(new TreeChangedEvent(AbstractInnerNode.this));
          }
        });
    attachSubtree(pos, branch);
  }

  public Node nextNode(Node from) throws TraversalException {
    if (from == parent) return this;
    if (from == this) return children[0].nextNode(this);
    if (from == children[nbChildren - 1]) return parent.nextNode(this);
    for (int i = 0; i < nbChildren - 1; i++) 
      if (from == children[i]) return children[i+1].nextNode(this);
    throw new TraversalException("Wrong from for next");
  }

  public Node prevNode(Node from) throws TraversalException {
    if (from == parent) return children[nbChildren - 1].prevNode(this);
    if (from == this) return parent.prevNode(this);
    if (from == children[0]) return this;
    for (int i = 1; i < nbChildren; i++) 
      if (from == children[i]) return children[i-1].prevNode(this);
    throw new TraversalException("Wrong from for prev");
  }

  public void onTreeChange(TreeChangedEvent event) {
    fireTreeChangedEvent(event);
  }

}
