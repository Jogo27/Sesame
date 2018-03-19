package fr.irit.sesame.tree;

import java.util.EventObject;

public class TreeChangedEvent extends EventObject {

  public TreeChangedEvent(Object source) {
    super(source);
  }

  @Override
  public String toString() {
    return "Tree changed for " + this.getSource().toString();
  }

}
