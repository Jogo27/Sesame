package fr.irit.sesame.ui;

public interface UndoRedoManager
  extends UndoRedoable, UndoRedoListener
{

  void addUndoManagerChangedListener(UndoManagerChangedListener listener);

  void removeUndoManagerChangedListener(UndoManagerChangedListener listener);

}
