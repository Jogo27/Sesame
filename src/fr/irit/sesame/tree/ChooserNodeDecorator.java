package fr.irit.sesame.tree;

public class ChooserNodeDecorator
  extends AbstractTreeChangedHandler
  implements ChooserNode, TreeChangedListener
{

  protected ChooserNode realNode;

  public ChooserNodeDecorator(ChooserNode node) {
    super();
    this.realNode = node;
    this.realNode.addTreeChangedListener(this);
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
    if (from == getParent()) return this;
    return getParent().nextNode(from);
  }

  public Node prevNode(Node from) throws TraversalException {
    if (from == getParent()) return this;
    return getParent().prevNode(from);
  }

  public void onTreeChange(TreeChangedEvent event) {
    fireTreeChangedEvent(event);
  }

}
