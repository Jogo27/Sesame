package fr.irit.sesame.gwt;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.ToStringRenderer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ValuePicker;

import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.ui.Application;
import fr.irit.sesame.util.EnumRevMap;
import fr.irit.sesame.util.ValueNotFoundException;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main
  implements EntryPoint, Application.View, ClickHandler, ValueChangeHandler<Main.Choice>
{

  private EnumRevMap<Application.ButtonId,Button> buttons = new EnumRevMap<Application.ButtonId,Button>(Application.ButtonId.class);
  private ValuePicker<Choice> chooserList = new ValuePicker<Choice>(new ToStringRenderer("-null error-"));
  private HTML textOutput = new HTML();

  private Application application = null;

  // Could be made its own class.
  static class Choice {
    int id;
    String text;

    Choice(int id, String text) {
      this.id = id;
      this.text = text;
    }

    public String toString() {
      return this.text;
    }
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

    RootPanel.get("selectorContainer").add(chooserList);
    chooserList.addValueChangeHandler(this);

    RootPanel.get("outputContainer").add(textOutput);

    // Toolbar
    newButton(Application.ButtonId.BUT_PREV, "prev", "navigation");
    newButton(Application.ButtonId.BUT_NEXT, "next", "navigation");
    newButton(Application.ButtonId.BUT_UNDO, "undo", "undoredo");
    newButton(Application.ButtonId.BUT_REDO, "redo", "undoredo");
    newButton(Application.ButtonId.BUT_CLEAR, "clear", "clearBut");

    application = new Application(this);
  }

  // Chooser

  public void onValueChange(ValueChangeEvent<Choice> event) {
    application.choose(event.getValue().id);
  }

  public void setChooser(ChooserNode chooser) {
    chooserList.setAcceptableValues(makeChoices(chooser));
  }

  static private Collection<Choice> makeChoices(ChooserNode node) {
    if (node == null)
      return new ArrayList<Choice>(0);
    int size = node.getNbChoices();
    ArrayList<Choice> ret = new ArrayList<Choice>(size);
    for (int i=0; i < size; i++)
      ret.add(new Choice(i, node.getChoice(i)));
    return ret;
  }

  // Buttons

  private void newButton(Application.ButtonId id, String label, String domContainer) {
    Button button = new Button(label, this);
    RootPanel.get(domContainer).add(button);
    buttons.put(id, button);
  }

  public void onClick(ClickEvent evt) {
    try {
      application.clickButton(buttons.getKey( (Button) evt.getSource() ));
    }
    catch (ValueNotFoundException exc) {
      throw new AssertionError("Click on unknown button", exc);
    }
  }

  public void setButtonEnabled(Application.ButtonId id, boolean enabled) {
    buttons.get(id).setEnabled(enabled);
  }

  // Output
  
  public void setNaturalLanguage(String descr) {
    textOutput.setHTML(descr);
  }

  public native void setLatex(String latex) /*-{
    $wnd.updateMathOutput(latex);
  }-*/;

}
