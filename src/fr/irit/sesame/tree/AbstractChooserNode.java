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
   * The list of choices of the chooser.
   * Typically, the constructor of the subclass adds choices to this list.
   */
  protected List<NodeConstructor> choices;
  protected ReplaceSubtreeAction replaceAction;

  // Constructor

  protected AbstractChooserNode(InnerNode parent, ReplaceSubtreeAction replaceAction) {
    super(parent);
    choices = new ArrayList<NodeConstructor>();
    this.replaceAction = replaceAction;
  }

  /**
   * Register the ChooserNode by the ChooserNodeManager.
   * This method is usualy called in the method implementing {@link NodeConstructor#makeNode NodeConstructor.makeNode}.
   * The body of this method typically is
   * <pre>{@code { return (new MyChooserNode(parent, replaceAction)).register() } }</pre>
   */
  protected Node register() {
    return this.getChooserNodeManager().registerChooser(this);
  }

  // Implements ChooserNode

  public int getNbChoices() {
    return choices.size();
  }
  
  public String getChoice(int pos) throws IndexOutOfBoundsException {
    return choices.get(pos).getDescription();
  }

  public Node makeChoice(int pos) throws IndexOutOfBoundsException {
    return choices.get(pos).makeNode(getParent(), getReplacementAction());
  }

  public ReplaceSubtreeAction getReplacementAction() {
    return this.replaceAction;
  }

}
