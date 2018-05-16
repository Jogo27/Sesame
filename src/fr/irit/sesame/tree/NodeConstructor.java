package fr.irit.sesame.tree;

//TODO check whether the abstract class could be enough.

public interface NodeConstructor {

  String getDescription();

  Node makeNode(InnerNode parent);

}
