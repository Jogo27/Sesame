package fr.irit.sesame.swing;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.irit.sesame.lang.Tree;
import fr.irit.sesame.tree.ChooserChangedEvent;
import fr.irit.sesame.tree.ChooserChangedListener;
import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.tree.GenericChooserModel;
import fr.irit.sesame.tree.TreeChangedEvent;
import fr.irit.sesame.tree.TreeChangedListener;
import fr.irit.sesame.util.ListenerHandler;

public class Main 
  extends JPanel
{
  private GenericChooserModel factory;
  private Tree tree;
  
  private JList<String> list;
  private JEditorPane textOutput;

  private class MyListModel implements ListModel<String>, ChooserChangedListener  {

    private ListenerHandler<ListDataListener> handler;
    private int curSize;

    MyListModel() {
      handler = new ListenerHandler<ListDataListener>();
      curSize = getCurrentSize();
    }

    private int getCurrentSize() {
      if (factory == null) return 0;
      ChooserNode chooser = factory.getChooser();
      if (chooser == null) return 0;
      return chooser.getNbChoices();
    }

    public void addListDataListener(ListDataListener l) {
      handler.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
      handler.remove(l);
    }

    public void onChooserChange(ChooserChangedEvent source) {
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
      ChooserNode chooser = factory.getChooser();
      if (chooser == null) return "---";
      return chooser.getChoice(index);
    }
  }


  /**
   * Populate the GUI with widgets and connect them.
   */
  private Main() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

    factory = new GenericChooserModel();
    tree = new Tree(factory);

    MyListModel listModel = new MyListModel();
    factory.addChooserChangedListener(listModel);

    list = new JList<String>(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          int index = list.getSelectedIndex();
          if (index >= 0)
            factory.choose(index);
        }
      }
    });
    list.setVisibleRowCount(5);
    JScrollPane listScrollPane = new JScrollPane(list);

    textOutput = new JEditorPane();
    textOutput.setContentType("text/html");
    textOutput.setEditable(false);
    textOutput.setFocusable(false);
    textOutput.setBorder(BorderFactory.createEtchedBorder());

    textOutput.setText("<html>"+tree.getText());
    tree.addTreeChangedListener(new TreeChangedListener() {
      public void onTreeChange(TreeChangedEvent event) {
        list.clearSelection();
        textOutput.setText("<html>"+tree.getText());
      }
    });

    // Tool Bar
    JPanel toolbar = new JPanel();
    toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));

    JButton prevBut = new JButton("prev");
    JButton nextBut = new JButton("next");
    JButton undoBut = new JButton("undo");
    JButton redoBut = new JButton("redo");
    JButton clearBut = new JButton("clear");

    toolbar.add(prevBut);
    toolbar.add(nextBut);
    toolbar.add(new JSeparator(SwingConstants.VERTICAL));
    toolbar.add(undoBut);
    toolbar.add(redoBut);
    toolbar.add(new JSeparator(SwingConstants.VERTICAL));
    toolbar.add(clearBut);

    // Left Panel
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
    leftPanel.add(toolbar);
    leftPanel.add(list);

    // Main area
    add(leftPanel);
    add(textOutput);
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
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        createAndShowGUI();
      }
    });
  }
}
