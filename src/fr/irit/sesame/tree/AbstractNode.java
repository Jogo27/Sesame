package fr.irit.sesame.tree;

/**
 * Implementation of a Node.
 *
 * Subclasses must implement the following methods: 
 * {@link #getText() getText()},
 * {@link #nextNode(Node) nextNode(Node)},
 * {@link #prevNode(Node) prevNode(Node)}.
 */
public abstract class AbstractNode
  extends AbstractTreeChangedHandler
{

  // Fields
  
  protected InnerNode parent;

  // Constructor

  static protected abstract class Constructor
    implements NodeConstructor
  {
    protected String description;

    public Constructor(String description) {
      this.description = description;
    }

    public String getDescription() {
      return description;
    }
  }

  protected AbstractNode(InnerNode parent) {
    super();
    this.parent = parent;
  }

  // Node's implementation

  public InnerNode getParent() {
    return parent;
  }

  public ChooserNodeFactory getFactory() {
    return getParent().getFactory();
  }

}
