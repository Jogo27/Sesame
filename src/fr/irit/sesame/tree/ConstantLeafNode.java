package fr.irit.sesame.tree;

public class ConstantLeafNode extends AbstractLeafNode {

  protected String text;

  public ConstantLeafNode(InnerNode parent, String text) {
    super(parent);
    this.text = text;
  }

  public String getText() {
    return text;
  }

}
