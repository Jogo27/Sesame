package fr.irit.sesame.ui;

import java.util.EventListener;

/**
 * To receive ChooserChangedEvent whenever the current chooser changes.
 */
public interface ChooserChangedListener extends EventListener {

  void onChooserChange(ChooserChangedEvent event);

}
