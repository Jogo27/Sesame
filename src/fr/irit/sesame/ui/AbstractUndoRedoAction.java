package fr.irit.sesame.ui;

public abstract class AbstractUndoRedoAction
  implements UndoRedoAction {

  protected boolean hasBeenDone;

  public AbstractUndoRedoAction(boolean hasBeenDone) {
    this.hasBeenDone = hasBeenDone;
  }

  public AbstractUndoRedoAction() {
    this(true);
  }

  public boolean canUndo() {
    return hasBeenDone;
  }
  
  public void undo() throws CannotUndoRedoException {
    if (!hasBeenDone) throw new CannotUndoRedoException(true);
    hasBeenDone = false;
  }

  public boolean canRedo() {
    return !hasBeenDone;
  }

  public void redo() throws CannotUndoRedoException {
    if (hasBeenDone) throw new CannotUndoRedoException(false);
    hasBeenDone = true;
  }

}
