package fr.irit.sesame.ui;

public interface UndoRedoable {

  boolean canUndo();
  void    undo();

  boolean canRedo();
  void    redo();

}
