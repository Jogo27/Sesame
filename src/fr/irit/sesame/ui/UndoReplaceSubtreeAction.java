package fr.irit.sesame.ui;

import fr.irit.sesame.tree.Node;
import fr.irit.sesame.tree.ReplaceSubtreeAction;

public class UndoReplaceSubtreeAction
  extends AbstractUndoRedoAction
{

  protected Node oldSubtree;
  protected ReplaceSubtreeAction action;

  public UndoReplaceSubtreeAction(ReplaceSubtreeAction action, Node oldSubtree) {
    super();
    this.action = action;
    this.oldSubtree = oldSubtree;
  }

  private void act() {
    Node tmpNode = action.currentSubtree();
    action.replaceSubtree(oldSubtree);
    oldSubtree = tmpNode;
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
