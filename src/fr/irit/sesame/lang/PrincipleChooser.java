package fr.irit.sesame.lang;

import fr.irit.sesame.tree.AbstractChooserNode;
import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.tree.ChooserNodeConstructor;
import fr.irit.sesame.tree.ConstantLeafNode;
import fr.irit.sesame.tree.InnerNode;

public class PrincipleChooser
  extends AbstractChooserNode
{

  static String description = "<principle>";

  // Constructors
  
  PrincipleChooser(InnerNode parent) {
    super(parent);
    choices.add(0, new ConstantLeafNode.Constructor("A"));
    choices.add(1, new ConstantLeafNode.Constructor("B"));
    choices.add(2, AndNode.constructor);
  }

  private static class Constructor extends AbstractChooserNode.Constructor {
    public String getDescription() { return description; }
    public ChooserNode makeChooserNode(InnerNode parent) { return new PrincipleChooser(parent); }
  }

  public static ChooserNodeConstructor constructor = new Constructor();

  // Implentation of ChooserNode

  public String getText() {
    return description;
  }

}
