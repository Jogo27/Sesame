package fr.irit.sesame.logic;

public class SimplePredicate
  implements Formula
{

  protected String latex;

  public SimplePredicate(String latex)
  { this.latex = latex; }

  public String asLatex()
  { return latex; }

}
