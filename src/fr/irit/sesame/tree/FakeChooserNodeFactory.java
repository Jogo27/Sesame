package fr.irit.sesame.tree;

import java.lang.reflect.InvocationTargetException;

/**
 * ChooserNodeFactory for tests.
 */
public class FakeChooserNodeFactory
  implements ChooserNodeFactory
{

  static private class CNWithAction
      extends ChooserNodeDecorator
  {
    ReplaceSubtreeAction action;

    CNWithAction(ChooserNode node, ReplaceSubtreeAction action) {
      super(node);
      this.action = action;
    }
  }

  private CNWithAction lastChooser;

  public FakeChooserNodeFactory() {
    lastChooser = null;
  }

  public ChooserNode getChooser(ChooserNodeConstructor constructor, ReplaceSubtreeAction replacement)
  {
    lastChooser = new CNWithAction(constructor.makeChooserNode(replacement.getParentNode()), replacement);
    return lastChooser;
  }

  public ChooserNode getLastChooser() {
    return lastChooser;
  }

  public void choose(int pos) {
    if (lastChooser == null) return;
    CNWithAction prevChooser = lastChooser;
    lastChooser = null;
    prevChooser.action.replaceSubtree(prevChooser.makeChoice(pos));
  }

}
