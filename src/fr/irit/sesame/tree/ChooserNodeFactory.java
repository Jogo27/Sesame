package fr.irit.sesame.tree;

/**
 * Provides ChooserNode for the leaves of the tree.
 */
public interface ChooserNodeFactory {

  /**
   * Provides the ChooserNode for the root of the tree
   * (in an empty tree)
   */
  public ChooserNode getRootChooser(ReplaceSubtreeAction placement);

  public ChooserNode getChooser(ChooserNodeType type, ReplaceSubtreeAction replacement);

  public void replaceSubtree(TreeNode subtree, ReplaceSubtreeAction replacement);
}
