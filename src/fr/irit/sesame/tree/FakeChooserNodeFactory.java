package fr.irit.sesame.tree;

import java.lang.reflect.InvocationTargetException;

/**
 * ChooserNodeFactory for tests.
 */
public class FakeChooserNodeFactory
  implements ChooserNodeFactory
{

  private ChooserNode lastChooser;

  public FakeChooserNodeFactory() {
    lastChooser = null;
  }

  public ChooserNode getChooser(ChooserNodeConstructor constructor, ReplaceSubtreeAction replacement)
  {
    lastChooser = constructor.make(replacement.getParentNode(), replacement);
    return lastChooser;
  }

  public ChooserNode getLastChooser() {
    return lastChooser;
  }

  public void replaceSubtree(Node toBeReplaced, Node replaceBy, ReplaceSubtreeAction replacement) {
    lastChooser = null;
    replacement.replaceSubtree(replaceBy);
  }

}
