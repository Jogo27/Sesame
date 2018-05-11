package fr.irit.sesame.tree;

/**
 * A syntactic tree denoting a semantics.
 * Technically, a tree is a sentinel root node.
 */
public class Tree
  extends AbstractInnerNode
{

  private ChooserNodeFactory factory;

  public Tree(ChooserNodeFactory factory) {
    super(1, null);
    this.factory = factory;
    initChooser(0, FakeChooserNode.constructor);
  }

  @Override
  public ChooserNodeFactory getFactory() {
    return factory;
  }

  public String getText() {
    return children[0].getText();
  }

}
