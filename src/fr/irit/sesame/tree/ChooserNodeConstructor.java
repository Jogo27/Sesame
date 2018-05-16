package fr.irit.sesame.tree;

/**
 * Constructor of a ChooserNode.
 * This interface is needed because GWT does not implement all
 * Java's reflexion mecanisms.
 */
public interface ChooserNodeConstructor {

  ChooserNode makeChooserNode(InnerNode parent);

}
