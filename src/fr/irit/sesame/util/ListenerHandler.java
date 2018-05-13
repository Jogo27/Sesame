package fr.irit.sesame.util;

import java.util.EventListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Handle a list of Listener.
 * Kind of replacement for javax.swing.event.EventListenerList.
 */
public class ListenerHandler<T extends EventListener> implements Iterable<T> {

  private Collection<T> listeners;

  public ListenerHandler() {
    listeners = new ArrayList<T>(1);
  }

  public void add(T listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  public void remove(T listener) {
    listeners.remove(listener);
  }

  // Implements Iterable<T>
  
  public Iterator<T> iterator() {
    return listeners.iterator();
  }
}
