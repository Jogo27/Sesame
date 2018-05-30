package fr.irit.sesame.ui;

public class CannotUndoRedoException
  extends RuntimeException
{

  protected boolean inUndo;

  public CannotUndoRedoException(boolean inUndo) {
    super(inUndo ? "Cannot undo" : "Cannot redo");
    this.inUndo = inUndo;
  }

  public boolean isInUndo() {
    return inUndo;
  }

}
