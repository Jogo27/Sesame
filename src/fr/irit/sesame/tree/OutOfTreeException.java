package fr.irit.sesame.tree;

public class OutOfTreeException
  extends TraversalException 
{
  public OutOfTreeException() { super(); }
  public OutOfTreeException(String msg) { super(msg); }
}
