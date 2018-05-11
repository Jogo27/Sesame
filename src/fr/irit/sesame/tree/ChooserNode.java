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
   * Replace itself in the tree with the choosen node. 
   * The object is still usuable after a call to choose,
   * but it is not a leaf of the tree anymore.
   */
  void choose(int pos);

}
