package fr.irit.sesame.tree;

/**
 * A simple LeafNode with constant associated text.
 */
public class ConstantLeafNode
  extends AbstractLeafNode
{

  static public class Constructor
      implements NodeConstructor
  {
    private String description;

    public Constructor(String description) {
      this.description = description;
    }

    public String getDescription() {
      return description;
    }

    public Node makeNode(InnerNode parent, ReplaceSubtreeAction action) {
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
