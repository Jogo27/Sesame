package fr.irit.sesame.tree;

import fr.irit.sesame.util.ListenerHandler;

abstract class AbstractTreeChangedHandler
  implements Node
{

  private ListenerHandler<TreeChangedListener> treeChangedListeners;

  protected AbstractTreeChangedHandler() {
    treeChangedListeners = new ListenerHandler<TreeChangedListener>();
  }

  public void addTreeChangedListener(TreeChangedListener listener) {
    treeChangedListeners.add(listener);
  }
  
  public void removeTreeChangedListener(TreeChangedListener listener) {
    treeChangedListeners.remove(listener);
  }

  protected void fireTreeChangedEvent(TreeChangedEvent event) {
    for (TreeChangedListener listener : treeChangedListeners)
      listener.onTreeChange(event);
  }

}
