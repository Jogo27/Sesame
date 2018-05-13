package fr.irit.sesame.tree;

/**
 * A simple LeafNode with constant associated text.
 */
public class ConstantLeafNode extends AbstractLeafNode {

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
