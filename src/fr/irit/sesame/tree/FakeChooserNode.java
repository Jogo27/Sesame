package fr.irit.sesame.tree;

/**
 * Class to test chooser conception.
 */
public class FakeChooserNode extends AbstractChooserNode {

  private class FakeChoice implements Choice {
    String description;
    FakeChoice(String description) { this.description = description; }
    public String getDescription() { return description; }
    public TreeNode replacement(InnerNode parent)  {
      return new ConstantLeafNode(getParent(), description);
    }
  }

  public FakeChooserNode(InnerNode parent, ReplaceSubtreeAction action) {
    super(parent, action);
    choices.add(0, new FakeChoice("Hello World"));
    choices.add(1, new FakeChoice("Hello Foobar"));
  }

  public String getText() {
    return "<Fake>";
  }

}