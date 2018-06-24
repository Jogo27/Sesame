package fr.irit.sesame.lang;

import fr.irit.sesame.tree.AbstractChooserNode;
import fr.irit.sesame.tree.ConstantLeafNode;
import fr.irit.sesame.tree.InnerNode;
import fr.irit.sesame.tree.Node;
import fr.irit.sesame.tree.NodeConstructor;
import fr.irit.sesame.tree.ReplaceSubtreeAction;

public class ClassicalPrincipleChooser
  extends AbstractChooserNode
{

  static String description = "[classical principle]";

  // Constructors
  
  ClassicalPrincipleChooser(InnerNode parent, ReplaceSubtreeAction replaceAction) {
    super(parent, replaceAction);
    choices.add(0, new ConstantLeafNode.Constructor("A"));
    choices.add(1, new ConstantLeafNode.Constructor("B"));
  }

  private static class Constructor implements NodeConstructor {
    public String getDescription() { return description; }
    public Node makeNode(InnerNode parent, ReplaceSubtreeAction replaceAction)
    { return (new ClassicalPrincipleChooser(parent, replaceAction)).register(); }
  }

  public static NodeConstructor constructor = new Constructor();

  // Implentation of ChooserNode

  public String getText() {
    return description;
  }

}
