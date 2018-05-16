package fr.irit.sesame.tree;

/**
 * A simple LeafNode with constant associated text.
 */
public class ConstantLeafNode extends AbstractLeafNode {

  static public class Constructor
    extends AbstractNode.Constructor
  {
    public Constructor(String text) {
      super(text);
    }

    public Node makeNode(InnerNode parent) {
      return new ConstantLeafNode(parent, description);
    }
  }

  protected String text;

  /**
   * @param text Associated text.
   */
  public ConstantLeafNode(InnerNode parent, String text) {
    super(parent);
    this.text = text;
  }

  public String getText() {
    return text;
  }

}
