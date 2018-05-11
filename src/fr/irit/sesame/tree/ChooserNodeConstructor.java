package fr.irit.sesame.tree;

/**
 * Constructor for a ChooserNode.
 * This interface is needed because GWT does not implement all
 * Java's reflexion mecanisms.
 */
public interface ChooserNodeConstructor {

  ChooserNode make(InnerNode parent, ReplaceSubtreeAction replacement);

}
