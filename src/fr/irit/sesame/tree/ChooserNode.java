package fr.irit.sesame.tree;

/** 
 * A pseudo-node at the leaves of the tree for the user to choose a node.
 *
 * Both logical classes and UI classes (typically decorators) should implement this interface.
 * The available choices are static, hence there is no need to listen for changes in the choices.
 */
public interface ChooserNode extends LeafNode {

  int getNbChoices();
  
  String getChoice(int pos);

  /** 
   * Create the node corresponding to the given choice.
   * @param pos Id of the choice.
   */
  Node makeChoice(int pos);

}
