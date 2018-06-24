package fr.irit.sesame.logic;

public final class AndConstruct
  implements Formula
{

  private Formula left;
  private Formula right;

  public AndConstruct(Formula left, Formula right) {
    this.left = left;
    this.right = right;
  }

  public String asLatex() {
    return left.asLatex() + " \\wedge " + right.asLatex();
  }

}
