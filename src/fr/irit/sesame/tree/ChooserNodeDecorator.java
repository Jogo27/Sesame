package fr.irit.sesame.tree;

public class ChooserNodeDecorator
  implements ChooserNode
{

  protected ChooserNode realNode;

  public ChooserNodeDecorator(ChooserNode node) {
    this.realNode = node;
  }


  // Implements ChooserNode

  public int getNbChoices() {
    return this.realNode.getNbChoices();
  }

  public String getChoice(int pos) {
    return this.realNode.getChoice(pos);
  }

  public Node makeChoice(int pos) {
    return this.realNode.makeChoice(pos);
  }


  // Implements Node
  
  public String getText() {
    return this.realNode.getText();
  }

  public ChooserNodeFactory getFactory() {
    return this.realNode.getFactory();
  }

  public InnerNode getParent() {
    return this.realNode.getParent();
  }

  public Node nextNode(Node from) throws TraversalException {
    return this.realNode.nextNode(from);
  }

  public Node prevNode(Node from) throws TraversalException {
    return this.realNode.prevNode(from);
  }

  public void addTreeChangedListener(TreeChangedListener listener) {
    this.realNode.addTreeChangedListener(listener);
  }

  public void removeTreeChangedListener(TreeChangedListener listener) {
    this.realNode.removeTreeChangedListener(listener);
  }

}
