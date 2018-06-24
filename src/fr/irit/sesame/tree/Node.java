package fr.irit.sesame.tree;

import fr.irit.sesame.logic.LogicExpression;

/**
 * Any node in the tree.
 */
public interface Node {

  InnerNode getParent();
  
  ChooserNodeManager getChooserNodeManager();
  

  /**
   * The next node in a depth-first pre-order traversal (NLR).
   * In particular, <code>nextNode(this)</code> must return the node after this
   * and <code>nextNode(getParent())</code> must return <code>this</code>.
   * @param from last visited node
   */
  Node nextNode(Node from) throws TraversalException;

  /**
   * The previous node in a depth-first pre-order traversal (NLR).
   * In particular, <code>prevNode(this)</code> must return the node before this
   * and <code>prevNode(getParent())</code> must return the last node in the subtree from <code>this</code>,
   * i.e., its rightmost leaf.
   * @param from last visited node
   */
  Node prevNode(Node from) throws TraversalException;

  void addTreeChangedListener(TreeChangedListener listener);
  void removeTreeChangedListener(TreeChangedListener listener);

  String getText();

  LogicExpression getFormula();

}
