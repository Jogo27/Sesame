package fr.irit.sesame.tree;

/**
 * Provides ChooserNode for the leaves of the tree.
 */
public interface ChooserNodeFactory {

  public ChooserNode getChooser(Class<? extends ChooserNode> type, ReplaceSubtreeAction replacement) throws InstantiationException;

  public void replaceSubtree(TreeNode toBeReplaced, TreeNode replaceBy, ReplaceSubtreeAction replacement);

}
