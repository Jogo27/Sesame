package fr.irit.sesame.ui;

import java.util.EventObject;

/**
 * Any change in the list of undo and redo actions handled by the manager.
 */
public class UndoManagerChangedEvent extends EventObject {

  public UndoManagerChangedEvent(Object source) {
    super(source);
  }

  @Override
  public String toString() {
    return "UndoManager changed";
  }

}
