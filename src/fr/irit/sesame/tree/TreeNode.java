package fr.irit.sesame.tree;

public interface TreeNode {

  InnerNode getParent();
  // void setParent(InnerNode parent);
  
  ChooserNodeFactory getFactory();
  
  static class TraversalException extends Exception {
    public TraversalException() { super(); }
    public TraversalException(String message) { super(message); }
  }

  /**
   * The next node in a depth-first traversal.
   * @param from last visited node
   */
  TreeNode nextNode(TreeNode from) throws TraversalException;

  /**
   * The previous node in a depth-first traversal.
   * @param from last visited node
   */
  TreeNode prevNode(TreeNode from) throws TraversalException;

  void addTreeChangedListener(TreeChangedListener listener);
  void removeTreeChangedListener(TreeChangedListener listener);

  String getText();

  //TODO: getter for formula

}
