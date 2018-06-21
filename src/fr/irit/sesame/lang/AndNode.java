package fr.irit.sesame.lang;

import fr.irit.sesame.tree.AbstractInnerNode;
import fr.irit.sesame.tree.InnerNode;
import fr.irit.sesame.tree.Node;
import fr.irit.sesame.tree.NodeConstructor;
import fr.irit.sesame.tree.ReplaceSubtreeAction;

public class AndNode
  extends AbstractInnerNode
{

  // Constructors
  
  private AndNode(InnerNode parent) {
    super(parent,
        PrincipleChooser.constructor,
        PrincipleChooser.constructor);
  }

  static private class Constructor implements NodeConstructor {
    public String getDescription() { return makeDescription(PrincipleChooser.description, PrincipleChooser.description); }
    public Node makeNode(InnerNode parent, ReplaceSubtreeAction action) { return new AndNode(parent); }
  }

  static public NodeConstructor constructor = new Constructor();

  // implements InnerNode

  static private String makeDescription(String left, String right) {
    return left + " and " + right;
  }

  public String getText() {
    return makeDescription(children[0].getText(), children[1].getText());
  }

}
