package fr.irit.sesame.tree;

import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of a ChooserNode using a List for the choices.
 *
 * The only method to implement in the subclass is {@link #getText() getText()}.
 */
abstract public class AbstractChooserNode
  extends AbstractLeafNode
  implements ChooserNode 
{

  protected static abstract class Constructor
    implements ChooserNodeConstructor, NodeConstructor
  {
    public Node makeNode(InnerNode parent) {
      return this.makeChooserNode(parent);
    }
  }

  /** 
   * The list of choices of the chooser.
   * Typically, the constructor of the subclass adds choices to this list.
   */
  protected List<NodeConstructor> choices;

  protected AbstractChooserNode(InnerNode parent) {
    super(parent);
    choices = new ArrayList<NodeConstructor>();
  }

  public int getNbChoices() {
    return choices.size();
  }
  
  public String getChoice(int pos) throws IndexOutOfBoundsException {
    return choices.get(pos).getDescription();
  }

  public Node makeChoice(int pos) throws IndexOutOfBoundsException {
    return choices.get(pos).makeNode(getParent());
  }

}
