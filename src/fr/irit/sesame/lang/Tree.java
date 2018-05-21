package fr.irit.sesame.lang;

import fr.irit.sesame.tree.AbstractInnerNode;
import fr.irit.sesame.tree.ChooserNodeFactory;
import fr.irit.sesame.tree.Node;
import fr.irit.sesame.tree.TraversalException;

/**
 * A syntactic tree denoting a semantics.
 * Technically, a tree is a sentinel root node.
 */
public class Tree
  extends AbstractInnerNode
{

  private ChooserNodeFactory factory;

  public Tree(ChooserNodeFactory factory) {
    super(null, 1);
    this.factory = factory;
    factory.setTree(this);
    initChooser(0, PrincipleChooser.constructor);
  }

  @Override
  public ChooserNodeFactory getFactory() {
    return factory;
  }

  @Override
  public Node nextNode(Node from) throws TraversalException {
    if (from == children[0]) throw new TraversalException("Reached the end of the tree");
    throw new TraversalException("Incorrect from in Tree.nextNode");
  }

  @Override
  public Node prevNode(Node from) throws TraversalException {
    if (from == children[0]) throw new TraversalException("Reached the begining of the tree");
    throw new TraversalException("Incorrect from in Tree.prevNode");
  }


  public String getText() {
    return children[0].getText();
  }

}
