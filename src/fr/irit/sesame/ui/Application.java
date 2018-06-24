package fr.irit.sesame.ui;

import fr.irit.sesame.lang.Tree;
import fr.irit.sesame.tree.ChooserNode;
import fr.irit.sesame.tree.Node;
import fr.irit.sesame.tree.TraversalException;
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
    BUT_REDO,
    BUT_CLEAR;
  }

  // View

  static public interface View {

    void setButtonEnabled(ButtonId button, boolean enabled);

    void setChooser(ChooserNode chooser);

    void setNaturalLanguage(String html);
    void setLatex(String latex);

  }

  // Fields
  
  private View view;
  private TreeConstructor treeConstructor;
  private Node tree;
  private UndoRedoManager undoManager;
  private ChooserModel chooserModel;

  // Constructors
  
  public Application(View view) {
    this(view, Tree.constructor);
  }

  public Application(View view, TreeConstructor treeConstructor) {
    this(view, treeConstructor, new GenericUndoManager());
  }

  public Application(View view, TreeConstructor treeConstructor, UndoRedoManager undoManager) {
    this.view = view;
    this.treeConstructor = treeConstructor;

    this.undoManager = undoManager;
    undoManager.addUndoManagerChangedListener(this);
    updatedUndoRedo();

    initTreeAndChooserModel();
  }

  private void initTreeAndChooserModel() {
    this.chooserModel = new ChooserModel();
    this.tree = treeConstructor.makeRoot(chooserModel);

    tree.addTreeChangedListener(this);
    chooserModel.addChooserChangedListener(this);
    chooserModel.addUndoRedoListener(undoManager);

    chooserModel.setTree(tree);
    updatedTree();
    updatedChooser();
    view.setButtonEnabled(ButtonId.BUT_CLEAR, false);
  }

  // Controls

  public void clickButton(ButtonId button) {
    switch (button) {
      case BUT_PREV: chooserModel.goPrevChooser(); break;
      case BUT_NEXT: chooserModel.goNextChooser(); break;
      case BUT_UNDO: undoManager.undo(); updateClearButton(); break;
      case BUT_REDO: undoManager.redo(); updateClearButton(); break;
      case BUT_CLEAR: clear(); break;
      default: throw new AssertionError("Unknown button ID");
    }
  }

  private void updateClearButton() {
    try {
      view.setButtonEnabled(ButtonId.BUT_CLEAR, tree.nextNode(tree) != chooserModel.getChooser());
    }
    catch (TraversalException e) {
      throw new AssertionError("Empty tree");
    }
  }

  public void choose(int id) {
    chooserModel.choose(id);
    view.setButtonEnabled(ButtonId.BUT_CLEAR, true);
  }

  // Clean action

  private final class ClearAction extends AbstractUndoRedoAction {

    private Node tree;
    private ChooserModel chooserModel;

    ClearAction(Node oldTree, ChooserModel oldChooserModel) {
      super(true);
      this.tree = oldTree;
      this.chooserModel = oldChooserModel;
    }

    public boolean isSignificant() { return true; }

    @Override
    public void undo() throws CannotUndoRedoException {
      super.undo();
      act();
    }

    @Override
    public void redo() throws CannotUndoRedoException {
      super.redo();
      act();
    }


    private void act() {
      Node oldTree = Application.this.tree;
      ChooserModel oldChooserModel = Application.this.chooserModel;
      Application.this.tree = this.tree;
      Application.this.chooserModel = this.chooserModel;
      this.tree = oldTree;
      this.chooserModel = oldChooserModel;

      Application.this.updatedTree();
      Application.this.updatedChooser();
    }
  }

  private void clear() {
    ClearAction action = new ClearAction(tree, chooserModel);
    initTreeAndChooserModel();
    undoManager.undoableActionHappened(action);
  }

  // Implements listener interfaces
  
  public void onTreeChange(TreeChangedEvent event) {
    updatedTree();
  }

  private void updatedTree() {
    view.setNaturalLanguage(tree.getText());
    view.setLatex(tree.getFormula().asLatex());
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
