package fr.irit.sesame.tree;

/**
 * Any node in the tree.
 */
public interface Node {

  InnerNode getParent();
  // void setParent(InnerNode parent);
  
  ChooserNodeFactory getFactory();
  

  /**
   * The next node in a depth-first traversal.
   * @param from last visited node
   */
  Node nextNode(Node from) throws TraversalException;

  /**
   * The previous node in a depth-first traversal.
   * @param from last visited node
   */
  Node prevNode(Node from) throws TraversalException;

  void addTreeChangedListener(TreeChangedListener listener);
  void removeTreeChangedListener(TreeChangedListener listener);

  String getText();

  //TODO: getter for formula

}
