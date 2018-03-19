package fr.irit.sesame.tree;

public abstract class AbstractTreeNode implements TreeNode {

  // Fields
  
  protected InnerNode parent;
  private ListenerHandler<TreeChangedListener> treeChangedListeners;

  // Constructor

  protected AbstractTreeNode(InnerNode parent) {
    this.parent = parent;
    treeChangedListeners = new ListenerHandler<TreeChangedListener>();
  }

  // TreeNode's implementation

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
