package fr.irit.sesame.tree;

import java.lang.reflect.InvocationTargetException;

public class FakeChooserNodeFactory
  implements ChooserNodeFactory
{

  private ChooserNode lastChooser;

  public FakeChooserNodeFactory() {
    lastChooser = null;
  }

  public ChooserNode getChooser(Class<? extends ChooserNode> type, ReplaceSubtreeAction replacement) throws InstantiationException {
    try {
      lastChooser = type.getConstructor(InnerNode.class, ReplaceSubtreeAction.class).newInstance(replacement.getParentNode(), replacement);
    } catch (NoSuchMethodException e) {
      throw new InstantiationException("No constructor " + type.getName() + "(InnerNode, ReplaceSubtreeAction)");
    } catch (IllegalAccessException|InvocationTargetException e) {
      throw new InstantiationException("Chooser instanciation catches " +
          e.getClass().getName() + ": " + e.toString());
    }
    return lastChooser;
  }

  public ChooserNode getLastChooser() {
    return lastChooser;
  }

  public void replaceSubtree(TreeNode toBeReplaced, TreeNode replaceBy, ReplaceSubtreeAction replacement) {
    lastChooser = null;
    replacement.replaceSubtree(replaceBy);
  }

}
