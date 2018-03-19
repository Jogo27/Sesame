package fr.irit.sesame.tree;

public interface ReplaceSubtreeAction {

  InnerNode getParentNode();

  TreeNode currentSubtree();

  void replaceSubtree(TreeNode subtree);

}
