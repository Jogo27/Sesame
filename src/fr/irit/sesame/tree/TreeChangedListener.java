package fr.irit.sesame.tree;

import java.util.EventListener;

public interface TreeChangedListener extends EventListener {

  void onTreeChange(TreeChangedEvent event);

}
