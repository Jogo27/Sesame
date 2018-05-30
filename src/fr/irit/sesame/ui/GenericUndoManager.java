package fr.irit.sesame.ui;

import java.util.ArrayList;
import java.util.List;

import fr.irit.sesame.util.ListenerHandler;

public class GenericUndoManager
  implements UndoRedoListener
{

  protected List<UndoRedoAction> stack;
  protected int curId;
  protected int maxId;
  protected ListenerHandler<UndoManagerChangedListener> undoManagerChangedListeners;

  public GenericUndoManager() {
    this.stack = new ArrayList<UndoRedoAction>();
    this.curId = 0;
    this.maxId = 0;
    this.undoManagerChangedListeners = new ListenerHandler<UndoManagerChangedListener>();
  }

  public void addUndoAction(UndoRedoAction action) {
    if (curId == stack.size()) { stack.add(action); }
    else stack.set(curId, action);
    curId += 1;
    maxId = curId;
    System.out.println("ADD  " + curId + "/" + maxId);
    fireUndoManagerChangedAction(new UndoManagerChangedEvent(this));
  }

  public boolean canUndo() {
    return curId > 0;
  }

  public boolean canRedo() {
    return curId < maxId;
  }

  public void undo() {
    while (curId > 0) {
      curId -= 1;
      UndoRedoAction action = stack.get(curId);
      if (! action.canUndo()) {
        int limit = maxId - curId - 1;
        for (int i = 0; i < limit; i++)
          stack.set(i, stack.get(i + curId + 1));
        curId = 0;
        maxId = limit + 1;
        break;
      }
      action.undo();
      if (action.isSignificant()) break;
    }
    System.out.println("UNDO " + curId + "/" + maxId);
    fireUndoManagerChangedAction(new UndoManagerChangedEvent(this));
  }

  public void redo() {
    if (curId < maxId) do {
      UndoRedoAction action = stack.get(curId);
      if (! action.canRedo()) {
        maxId = curId;
        break;
      }
      action.redo();
      curId += 1;
    } while ((curId < maxId) && !(stack.get(curId).isSignificant()));
    System.out.println("REDO " + curId + "/" + maxId);
    fireUndoManagerChangedAction(new UndoManagerChangedEvent(this));
  }

  public void addUndoManagerChangedListener(UndoManagerChangedListener listener) {
    undoManagerChangedListeners.add(listener);
  }

  public void removeUndoManagerChangedListener(UndoManagerChangedListener listener) {
    undoManagerChangedListeners.remove(listener);
  }

  private void fireUndoManagerChangedAction(UndoManagerChangedEvent action) {
    for (UndoManagerChangedListener listener : undoManagerChangedListeners)
      listener.onUndoManagerChange(action);
  }

  // implements UndoRedoListener

  public void undoableActionHappened(UndoRedoAction action) {
    addUndoAction(action);
  }

}
