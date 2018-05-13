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

  /**
   * A choice of the chooser.
   */
  static protected interface Choice {
    String getDescription();
    Node replacement(InnerNode parent);
  }

  /** 
   * The list of choices of the chooser.
   * Typically, the constructor of the subclass adds choices to this list.
   */
  protected List<Choice> choices;

  protected ReplaceSubtreeAction action;

  protected AbstractChooserNode(InnerNode parent, ReplaceSubtreeAction action) {
    super(parent);
    choices = new ArrayList<Choice>();
    this.action = action;
  }

  public int getNbChoices() {
    return choices.size();
  }
  
  public String getChoice(int pos) throws IndexOutOfBoundsException {
    return choices.get(pos).getDescription();
  }

  public void choose(int pos) throws IndexOutOfBoundsException {
    getFactory().replaceSubtree(this, choices.get(pos).replacement(getParent()), action);
  }

}
