package fr.irit.sesame.tree;

/**
 * Methods to change a child of an InnerNode.
 * An object of this type is given by the parent Node to its ChooserNode child,
 * for the ChooserNode to be replaced by a concrete node.
 * This action could also be used when undoing user choices.
 */
public interface ReplaceSubtreeAction {

  //TODO: remove this method.
  InnerNode getParentNode();

  Node currentSubtree();

  void replaceSubtree(Node subtree);

}
