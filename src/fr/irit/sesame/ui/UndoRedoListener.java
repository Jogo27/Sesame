package fr.irit.sesame.ui;

import java.util.EventListener;

public interface UndoRedoListener
  extends EventListener
{

  /**
   * Notify that an undoable action occured, such that the {@see UndoRedoAction} can be stored.
   * @param action The action to store. It is supposed that {@see UndoRedoAction#canUndo() action.canUndo()} returns <code>true</code>.
   */
  void undoableActionHappened(UndoRedoAction action);

}
