package fr.irit.sesame.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ValuePicker;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import java.util.ArrayList;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {

  private static final ArrayList<String> POSSIBLE_VALUES = new ArrayList<String>();
  static {
    POSSIBLE_VALUES.add("foo");
    POSSIBLE_VALUES.add("bar");
  }


  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final ValuePicker<String> selector = new ValuePicker();
    final Label outputLabel = new Label();


    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    RootPanel.get("selectorContainer").add(selector);
    RootPanel.get("outputContainer").add(outputLabel);

    selector.setAcceptableValues(POSSIBLE_VALUES);
    selector.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        outputLabel.setText(event.getValue());
      }
    });
  }
}
