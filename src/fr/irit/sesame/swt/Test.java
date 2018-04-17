package fr.irit.sesame.swt;

import fr.irit.sesame.tree.Tree;
import fr.irit.sesame.tree.FakeChooserNodeFactory;
import fr.irit.sesame.tree.TreeChangedListener;
import fr.irit.sesame.tree.TreeChangedEvent;
import fr.irit.sesame.tree.ChooserNode;

class Test {

  static public void main(String[] args) {
    try {

      FakeChooserNodeFactory factory = new FakeChooserNodeFactory();
      Tree tree = new Tree(factory);
      System.out.println(tree.getText());
      tree.addTreeChangedListener(new TreeChangedListener() {
        public void onTreeChange(TreeChangedEvent event) { System.out.println(tree.getText());}
        });
      ChooserNode chooser = factory.getLastChooser();
      if (chooser != null) 
        chooser.choose(0);

    } catch (InstantiationException e) {
      System.err.println(e.toString());
    }
  }

}
