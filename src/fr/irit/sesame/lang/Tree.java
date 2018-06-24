package fr.irit.sesame.lang;

import fr.irit.sesame.logic.LogicExpression;
import fr.irit.sesame.tree.AbstractInnerNode;
import fr.irit.sesame.tree.ChooserNodeManager;
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

  private ChooserNodeManager chooserNodeManager;

  public Tree(ChooserNodeManager chooserNodeManager) {
    super(null, 1);
    this.chooserNodeManager = chooserNodeManager;
    initBranch(0, PrincipleChooser.constructor); // must be done after chooserNodeManager has been initialized
  }

  static private class Constructor implements TreeConstructor {
    public Node makeRoot(ChooserNodeManager chooserNodeManager) { return new Tree(chooserNodeManager); }
  }
  static public TreeConstructor constructor = new Constructor();

  @Override
  public ChooserNodeManager getChooserNodeManager() {
    return chooserNodeManager;
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

  public LogicExpression getFormula() {
    return children[0].getFormula();
  }

}
