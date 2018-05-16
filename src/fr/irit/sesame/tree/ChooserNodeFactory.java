package fr.irit.sesame.tree;

/**
 * Provides ChooserNode for the leaves of the tree.
 */
public interface ChooserNodeFactory {

  public ChooserNode getChooser(ChooserNodeConstructor constructor, ReplaceSubtreeAction replacement);

}
