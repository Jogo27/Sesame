package fr.irit.sesame.ui;

import fr.irit.sesame.lang.Tree;
import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.tree.Node;
import fr.irit.sesame.tree.TreeChangedEvent;
import fr.irit.sesame.tree.TreeChangedListener;
import fr.irit.sesame.tree.TreeConstructor;

public class Application
  implements TreeChangedListener, ChooserChangedListener, UndoManagerChangedListener
{

  static public enum ButtonId {
    BUT_PREV,
    BUT_NEXT,
    BUT_UNDO,
    BUT_REDO;
//    BUT_CLEAR
  }

  // View

  static public interface View {

    void setButtonEnabled(ButtonId button, boolean enabled);

    void setChooser(ChooserNode chooser);

    void setNaturalLanguage(String html);

  }

  // Fields
  
  private View view;
  private Node tree;
  private UndoRedoManager undoManager;
  private GenericChooserModel chooserModel;

  // Constructors
  
  public Application(View view) {
    this(view, Tree.constructor);
  }

  public Application(View view, TreeConstructor treeConstructor) {
    this(view, treeConstructor, new GenericUndoManager());
  }

  public Application(View view, TreeConstructor treeConstructor, UndoRedoManager undoManager) {
    this.view = view;
    this.chooserModel = new GenericChooserModel();
    this.tree = treeConstructor.makeRoot(chooserModel);
    this.undoManager = undoManager;

    tree.addTreeChangedListener(this);
    chooserModel.addChooserChangedListener(this);
    chooserModel.addUndoRedoListener(undoManager);
    undoManager.addUndoManagerChangedListener(this);
    chooserModel.setTree(tree);

    updatedTree();
    updatedChooser();
    updatedUndoRedo();
  }

  // Controls

  public void clickButton(ButtonId button) {
    switch (button) {
      case BUT_PREV: chooserModel.goPrevChooser(); break;
      case BUT_NEXT: chooserModel.goNextChooser(); break;
      case BUT_UNDO: undoManager.undo(); break;
      case BUT_REDO: undoManager.redo(); break;
      default: throw new AssertionError("Unknown button ID");
    }
  }

  public void choose(int id) {
    chooserModel.choose(id);
  }

  // Implements listener interfaces
  
  public void onTreeChange(TreeChangedEvent event) {
    updatedTree();
  }

  private void updatedTree() {
    view.setNaturalLanguage(tree.getText());
  }

  public void onChooserChange(ChooserChangedEvent event) {
    updatedChooser();
  }

  private void updatedChooser() {
    view.setChooser(chooserModel.getChooser());
    view.setButtonEnabled(ButtonId.BUT_PREV, chooserModel.hasPrevChooser());
    view.setButtonEnabled(ButtonId.BUT_NEXT, chooserModel.hasNextChooser());
  }

  public void onUndoManagerChange(UndoManagerChangedEvent event) {
    updatedUndoRedo();
  }

  private void updatedUndoRedo() {
    view.setButtonEnabled(ButtonId.BUT_UNDO, undoManager.canUndo());
    view.setButtonEnabled(ButtonId.BUT_REDO, undoManager.canRedo());
  }

}
