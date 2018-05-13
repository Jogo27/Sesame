package fr.irit.sesame.tree;

/**
 * Error in the traversal of the tree.
 * Usually triggered in methods {@link Node#nextNode(Node)} and {@link Node#prevNode(Node)}.
 */
class TraversalException extends Exception {
  public TraversalException() { super(); }
  public TraversalException(String message) { super(message); }
}
