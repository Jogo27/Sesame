package fr.irit.sesame.tree;

import java.util.EventObject;

/**
 * Any change in the tree.
 */
public class TreeChangedEvent extends EventObject {

  public TreeChangedEvent(Node source) {
    super(source);
  }

  @Override
  public String toString() {
    return "Tree changed for " + this.getSource().toString();
  }

}
