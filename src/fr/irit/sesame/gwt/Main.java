package fr.irit.sesame.gwt;

import java.util.Collection;
import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ValuePicker;
import com.google.gwt.text.shared.ToStringRenderer;

import fr.irit.sesame.tree.ChooserChangedEvent;
import fr.irit.sesame.tree.ChooserChangedListener;
import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.tree.GenericChooserModel;
import fr.irit.sesame.tree.TreeChangedEvent;
import fr.irit.sesame.tree.TreeChangedListener;
import fr.irit.sesame.lang.Tree;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {

  // Could be made its own class.
  static private class Choice {
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

  static private Collection<Choice> makeChoices(ChooserNode node) {
    if (node == null)
      return new ArrayList<Choice>(0);
    int size = node.getNbChoices();
    ArrayList<Choice> ret = new ArrayList<Choice>(size);
    for (int i=0; i < size; i++)
      ret.add(new Choice(i, node.getChoice(i)));
    return ret;
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final ValuePicker<Choice> selector = new ValuePicker<Choice>(new ToStringRenderer("-null error-"));
    final Label outputLabel = new Label();

    final GenericChooserModel factory = new GenericChooserModel();
    final Tree tree = new Tree(factory);

    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    RootPanel.get("selectorContainer").add(selector);
    RootPanel.get("outputContainer").add(outputLabel);

    ChooserChangedListener chooserListener = new ChooserChangedListener() {
      public void onChooserChange(ChooserChangedEvent event) {
        selector.setAcceptableValues(makeChoices(factory.getChooser()));
      }
    };
    chooserListener.onChooserChange(new ChooserChangedEvent(factory));
    factory.addChooserChangedListener(chooserListener);

    TreeChangedListener treeListener = new TreeChangedListener() {
      public void onTreeChange(TreeChangedEvent source) {
        outputLabel.setText(tree.getText());
      }
    };
    treeListener.onTreeChange(new TreeChangedEvent(tree));
    tree.addTreeChangedListener(treeListener);

    selector.addValueChangeHandler(new ValueChangeHandler<Choice>() {
      public void onValueChange(ValueChangeEvent<Choice> event) {
        factory.choose(event.getValue().id);
      }
    });
  }
}
