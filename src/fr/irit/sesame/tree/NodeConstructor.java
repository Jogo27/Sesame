package fr.irit.sesame.tree;

/**
 * Each Node should have a static field of type NodeConstructor.
 * This is needed because GWT does not implements the full reflection API.
 */
public interface NodeConstructor {

  String getDescription();

  /**
   * Create a node.
   * Parameter <code>replaceAction</code> could be ignored by non-chooser nodes.
   */
  Node makeNode(InnerNode parent, ReplaceSubtreeAction replaceAction);

}
