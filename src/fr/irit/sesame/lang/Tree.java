package fr.irit.sesame.lang;

import fr.irit.sesame.tree.AbstractInnerNode;
import fr.irit.sesame.tree.ChooserNodeFactory;
import fr.irit.sesame.tree.Node;
import fr.irit.sesame.tree.OutOfTreeException;
import fr.irit.sesame.tree.TraversalException;
import fr.irit.sesame.tree.TreeConstructor;

/**
 * A syntactic tree denoting a semantics.
 * Technically, a tree is a sentinel root node.
 */
public class Tree
  extends AbstractInnerNode
{
  // TODO: write an abstract class in the tree package.

  private ChooserNodeFactory factory;

  public Tree(ChooserNodeFactory factory) {
    super(null, 1);
    this.factory = factory;
    initChooser(0, PrincipleChooser.constructor);
  }

  static private class Constructor implements TreeConstructor {
    public Node makeRoot(ChooserNodeFactory factory) { return new Tree(factory); }
  }
  static public TreeConstructor constructor = new Constructor();

  @Override
  public ChooserNodeFactory getFactory() {
    return factory;
  }

  @Override
  public Node nextNode(Node from) throws TraversalException {
    if (from == null) return this;
    if (from == this) return children[0];
    if (from == children[0]) throw new OutOfTreeException();
    throw new TraversalException("Incorrect from in Tree.nextNode");
  }

  @Override
  public Node prevNode(Node from) throws TraversalException {
    if (from == null) return children[0].prevNode(this);
    if (from == children[0]) throw new OutOfTreeException();
    throw new TraversalException("Incorrect from in Tree.prevNode");
  }


  public String getText() {
    return children[0].getText();
  }

}
