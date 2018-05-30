package fr.irit.sesame.ui;

import java.util.EventListener;

/**
 * To receive UndoManagerChangedEvent whenever the undo and redo actions of the manager change.
 */
public interface UndoManagerChangedListener extends EventListener {

  void onUndoManagerChange(UndoManagerChangedEvent event);

}
