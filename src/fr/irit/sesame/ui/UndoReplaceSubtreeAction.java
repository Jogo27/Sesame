package fr.irit.sesame.ui;

import fr.irit.sesame.tree.Node;
import fr.irit.sesame.tree.ReplaceSubtreeAction;

public class UndoReplaceSubtreeAction
  extends AbstractUndoRedoAction
{

  protected Node altSubtree;
  protected ReplaceSubtreeAction action;

  /**
   * Constructor.
   * @param altSubtree The subtree that is NOT currently in the main tree.
   */
  public UndoReplaceSubtreeAction(boolean hasBeenDone, ReplaceSubtreeAction action, Node altSubtree) {
    super(hasBeenDone);
    this.action = action;
    this.altSubtree = altSubtree;
  }

  public UndoReplaceSubtreeAction(ReplaceSubtreeAction action, Node altSubtree) {
    this(true, action, altSubtree);
  }

  private void act() {
    Node tmpNode = action.currentSubtree();
    action.replaceSubtree(altSubtree);
    altSubtree = tmpNode;
  }

  @Override
  public void undo() {
    super.undo();
    act();
  }

  @Override
  public void redo() {
    super.redo();
    act();
  }

  public boolean isSignificant() {
    return true;
  }

}
