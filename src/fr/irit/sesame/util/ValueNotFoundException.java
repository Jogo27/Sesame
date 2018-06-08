package fr.irit.sesame.util;

public class ValueNotFoundException
  extends Exception
{

  public ValueNotFoundException()                             { super(); }
  public ValueNotFoundException(String msg)                   { super(msg); }
  public ValueNotFoundException(String msg, Throwable cause)  { super(msg, cause); }

}
