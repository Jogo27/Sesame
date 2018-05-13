package fr.irit.sesame.tree;

import java.util.EventListener;

/**
 * To receive TreeChangedEvent whenever the tree changes.
 */
public interface TreeChangedListener extends EventListener {

  void onTreeChange(TreeChangedEvent event);

}
