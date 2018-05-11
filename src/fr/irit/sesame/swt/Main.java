package fr.irit.sesame.swt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import fr.irit.sesame.tree.*;

public class Main 
  extends JPanel
{
  private FakeChooserNodeFactory factory;
  private Tree tree;
  
  private ListModel<String> listModel;
  private JList<String> list;
  private JLabel label;

  private class MyListModel implements ListModel<String>, TreeChangedListener  {

    private ListenerHandler<ListDataListener> handler;
    private int curSize;

    private int getCurrentSize() {
      if (factory == null) return 0;
      ChooserNode chooser = factory.getLastChooser();
      if (chooser == null) return 0;
      return chooser.getNbChoices();
    }

    MyListModel() {
      handler = new ListenerHandler<ListDataListener>();
      curSize = getCurrentSize();
      if (tree != null)
        tree.addTreeChangedListener(this);
    }

    public void addListDataListener(ListDataListener l) {
      handler.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
      handler.remove(l);
    }

    public void onTreeChange(TreeChangedEvent source) {
      ListDataEvent event;
      int newSize = getCurrentSize();

      if (newSize > curSize) {
        System.out.println("ADDED " + curSize + "-" + (newSize - 1));
        event = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, curSize, newSize - 1);
        for (ListDataListener listener : handler)
          listener.intervalAdded(event);

      } else if (newSize < curSize) {
        System.out.println("REMOVED " + newSize + "-" + (curSize - 1));
        event = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, newSize, curSize - 1);
        for (ListDataListener listener : handler)
          listener.intervalRemoved(event);
      }

      if (newSize > 0) {
        System.out.println("CHANGED 0-" + (newSize - 1));
        event = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, 0, newSize - 1);
        for (ListDataListener listener : handler)
          listener.contentsChanged(event);
      }

      curSize = newSize;
    }

    public int getSize() {
      return curSize;
    }

    public String getElementAt(int index) {
      ChooserNode chooser = factory.getLastChooser();
      if (chooser == null) return "---";
      return chooser.getChoice(index);
    }
  }


  /**
   * Populate the GUI with widgets and connect them.
   */
  private Main() {
    super(new BorderLayout());

    label = new JLabel("Initialising...");

    factory = new FakeChooserNodeFactory();
    tree = new Tree(factory);

    listModel = new MyListModel();

    list = new JList<String>(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          int index = list.getSelectedIndex();
          if (index >= 0)
            factory.getLastChooser().choose(index);
        }
      }
    });
    list.setVisibleRowCount(5);
    JScrollPane listScrollPane = new JScrollPane(list);

    label.setText(tree.getText());
    tree.addTreeChangedListener(new TreeChangedListener() {
      public void onTreeChange(TreeChangedEvent event) {
        list.clearSelection();
        label.setText(tree.getText());
      }
    });

    add(list, BorderLayout.WEST);
    add(label, BorderLayout.EAST);
  }

  /**
    * Create the GUI and show it.
    * For thread safety, this method should be invoked from the event-dispatching thread.
    */
  private static void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("Sesame2");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    //Create and set up the content pane.
    JComponent newContentPane = new Main();
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }
}
