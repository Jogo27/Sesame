package fr.irit.sesame.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.ui.Application;
import fr.irit.sesame.util.EnumRevMap;
import fr.irit.sesame.util.ValueNotFoundException;


public class Main 
  extends JPanel
  implements Application.View, ListSelectionListener, ActionListener
{

  private EnumRevMap<Application.ButtonId, JButton> buttons;
  private JList<String> chooserList;
  private ChooserListModel chooserListModel;
  private JEditorPane textOutput;

  private Application application;


  /**
   * Populate the GUI with widgets and connect them.
   */
  private Main() {
    super();

    chooserListModel = new ChooserListModel();
    chooserList = new JList<String>(chooserListModel);
    chooserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    chooserList.addListSelectionListener(this);
    chooserList.setVisibleRowCount(5);
    JScrollPane listScrollPane = new JScrollPane(chooserList);

    textOutput = new JEditorPane();
    textOutput.setContentType("text/html");
    textOutput.setEditable(false);
    textOutput.setFocusable(false);
    textOutput.setBorder(BorderFactory.createEtchedBorder());

    // Toolbar

    buttons = new EnumRevMap<Application.ButtonId,JButton>(Application.ButtonId.class);

    JButton prevBut = newButton(Application.ButtonId.BUT_PREV, "prev");
    JButton nextBut = newButton(Application.ButtonId.BUT_NEXT, "next");

    JSeparator sep1 = new JSeparator(SwingConstants.VERTICAL);

    JButton undoBut = newButton(Application.ButtonId.BUT_UNDO, "undo");
    JButton redoBut = newButton(Application.ButtonId.BUT_REDO, "redo");
    JSeparator sep2 = new JSeparator(SwingConstants.VERTICAL);

    JButton clearBut = newButton(Application.ButtonId.BUT_CLEAR, "clear");

    // Layout
    GroupLayout layout = new GroupLayout(this);
    setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);

    layout.setHorizontalGroup(
      layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(prevBut)
            .addComponent(nextBut)
            .addComponent(sep1, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(undoBut)
            .addComponent(redoBut)
            .addComponent(sep2, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(clearBut)
          )
          .addComponent(chooserList)
        )
        .addComponent(textOutput)
    );

    layout.setVerticalGroup(
      layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE,false)
            .addComponent(prevBut)
            .addComponent(nextBut)
            .addComponent(sep1)
            .addComponent(undoBut)
            .addComponent(redoBut)
            .addComponent(sep2)
            .addComponent(clearBut)
          )
          .addComponent(chooserList)
        )
        .addComponent(textOutput)
    );

    // Launch
    application = new Application(this);

  }

  // Chooser

  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      int index = chooserList.getSelectedIndex();
      if (index >= 0) {
        application.choose(index);
        chooserList.clearSelection();
      }
    }
  }

  public void setChooser(ChooserNode chooser) {
    chooserListModel.setChooser(chooser);
  }

  // Buttons

  private JButton newButton(Application.ButtonId id, String label) {
    JButton button = new JButton(label);
    buttons.put(id, button);
    button.addActionListener(this);
    return button;
  }

  public void actionPerformed(ActionEvent evt) {
    try {
      application.clickButton(buttons.getKey( (JButton) evt.getSource() ));
    }
    catch (ValueNotFoundException exc) {
      throw new AssertionError("Click on unknown button", exc);
    }
  }

  public void setButtonEnabled(Application.ButtonId id, boolean enabled) {
    buttons.get(id).setEnabled(enabled);
  }

  // Natural language output

  public void setNaturalLanguage(String descr) {
    textOutput.setText(descr);
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
