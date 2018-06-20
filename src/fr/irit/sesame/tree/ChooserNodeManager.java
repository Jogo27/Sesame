package fr.irit.sesame.tree;

/**
 * Provides ChooserNode for the leaves of the tree.
 */
public interface ChooserNodeManager {

  /**
   * Give the opportunity to the ChooserNodeManager to replace the ChooserNode.
   */
  public ChooserNode registerChooser(ChooserNode chooserNode);

  /**
   * Associate a tree to the ChooserNodeManager.
   */
  public void setTree(Node tree);

}
