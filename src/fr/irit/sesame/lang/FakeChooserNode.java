package fr.irit.sesame.lang;

import fr.irit.sesame.tree.AbstractChooserNode;
import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.tree.ChooserNodeConstructor;
import fr.irit.sesame.tree.ConstantLeafNode;
import fr.irit.sesame.tree.InnerNode;
import fr.irit.sesame.tree.Node;
import fr.irit.sesame.tree.ReplaceSubtreeAction;

/**
 * Class to test chooser conception.
 */
public class FakeChooserNode
  extends AbstractChooserNode
{

  private static final String description = "<Fake>";

  // Constructor

  private FakeChooserNode(InnerNode parent) {
    super(parent);
    choices.add(0, new ConstantLeafNode.Constructor("Hello World"));
    choices.add(1, new ConstantLeafNode.Constructor("Hello Foobar"));
  }

  private static class Constructor extends AbstractChooserNode.Constructor {
    public String getDescription() { return description; }
    public ChooserNode makeChooserNode(InnerNode parent) { return new FakeChooserNode(parent); }
  }

  public static ChooserNodeConstructor constructor = new Constructor();

  // Implentation of ChooserNode

  public String getText() {
    return description;
  }

}
