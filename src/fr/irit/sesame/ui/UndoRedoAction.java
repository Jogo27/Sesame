package fr.irit.sesame.ui;

public interface UndoRedoAction
  extends UndoRedoable
{

  boolean canUndo();
  void    undo();

  boolean canRedo();
  void    redo();

  boolean isSignificant();

  //TODO: add "boolean mergeAction(UndoRedoAction)"

}
