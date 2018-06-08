package fr.irit.sesame.ui;

import java.util.EventObject;

/**
 * Any change of the current chooser.
 */
public class ChooserChangedEvent extends EventObject {

  public ChooserChangedEvent(Object source) {
    super(source);
  }

  @Override
  public String toString() {
    return "Chooser changed";
  }

}
