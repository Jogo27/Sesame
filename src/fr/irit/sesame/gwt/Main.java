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

import fr.irit.sesame.lang.Tree;
import fr.irit.sesame.tree.ChooserChangedEvent;
import fr.irit.sesame.tree.ChooserChangedListener;
import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.tree.GenericChooserModel;
import fr.irit.sesame.tree.TreeChangedEvent;
import fr.irit.sesame.tree.TreeChangedListener;

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
    final HTML outputLabel = new HTML();

    final GenericChooserModel factory = new GenericChooserModel();
    final Tree tree = new Tree(factory);

    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    RootPanel.get("selectorContainer").add(selector);
    RootPanel.get("outputContainer").add(outputLabel);

    // Toolbar
    final Button prevBut = new Button("prev", new ClickHandler() {
      public void onClick(ClickEvent evt) {
        factory.goPrevChooser();
      }
    });
    final Button nextBut = new Button("next", new ClickHandler() {
      public void onClick(ClickEvent evt) {
        factory.goNextChooser();
      }
    });

    RootPanel.get("prevBut").add(prevBut);
    RootPanel.get("nextBut").add(nextBut);

    ChooserChangedListener chooserListener = new ChooserChangedListener() {
      public void onChooserChange(ChooserChangedEvent event) {
        selector.setAcceptableValues(makeChoices(factory.getChooser()));
        prevBut.setEnabled(factory.hasPrevChooser());
        nextBut.setEnabled(factory.hasNextChooser());
      }
    };
    chooserListener.onChooserChange(new ChooserChangedEvent(factory));
    factory.addChooserChangedListener(chooserListener);

    TreeChangedListener treeListener = new TreeChangedListener() {
      public void onTreeChange(TreeChangedEvent source) {
        outputLabel.setHTML(tree.getText());
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
