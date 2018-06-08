package fr.irit.sesame.swing;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.util.ListenerHandler;

class ChooserListModel
  implements ListModel<String>
{

  private ChooserNode currentChooser;
  private ListenerHandler<ListDataListener> handler;

  ChooserListModel() {
    currentChooser = null;
    handler = new ListenerHandler<ListDataListener>();
  }

  static int chooserSize(ChooserNode chooser) {
    return (chooser == null ? 0 : chooser.getNbChoices());
  }

  public void setChooser(ChooserNode newChooser) {
    if (newChooser == currentChooser) return;

    int oldSize = chooserSize(currentChooser);
    int newSize = chooserSize(newChooser);
    currentChooser = newChooser;
    ListDataEvent event;

    if (newSize > oldSize) {
      System.out.println("ADDED " + oldSize + "-" + (newSize - 1));
      event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, oldSize, newSize - 1);
      for (ListDataListener listener : handler)
        listener.intervalAdded(event);

    } else if (newSize < oldSize) {
      System.out.println("REMOVED " + newSize + "-" + (oldSize - 1));
      event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, newSize, oldSize - 1);
      for (ListDataListener listener : handler)
        listener.intervalRemoved(event);
    }

    if (newSize > 0) {
      System.out.println("CHANGED 0-" + (newSize - 1));
      event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, newSize - 1);
      for (ListDataListener listener : handler)
        listener.contentsChanged(event);
    }
  }

  // Implements ListModel

  public void addListDataListener(ListDataListener l) {
    handler.add(l);
  }

  public void removeListDataListener(ListDataListener l) {
    handler.remove(l);
  }

  public int getSize() {
    return chooserSize(currentChooser);
  }

  public String getElementAt(int pos) {
    if (currentChooser == null) throw new IndexOutOfBoundsException();
    return currentChooser.getChoice(pos);
  }

}
