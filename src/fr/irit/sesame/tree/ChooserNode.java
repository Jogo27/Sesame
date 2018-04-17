package fr.irit.sesame.tree;

public interface ChooserNode extends LeafNode {

  int getNbChoices();
  
  String getChoice(int pos);

  void choose(int pos);

}
