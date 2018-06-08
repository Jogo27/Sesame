package fr.irit.sesame.util;

import java.util.EnumMap;
import java.util.Map;

/**
 * Turn EnumMap into a reverse map by adding a {@link #getKey() getKey()}.
 */
public class EnumRevMap<K extends Enum<K>, V>
  extends EnumMap<K,V>
{

  public EnumRevMap(Class<K> type)            { super(type); }
  public EnumRevMap(EnumMap<K,? extends V> m) { super(m);    }
  public EnumRevMap(Map<K,? extends V> m)     { super(m);    }

  /**
   * The first key associated with the given value.
   */
  public K getKey(V value) throws ValueNotFoundException {
    for (Map.Entry<K,V> entry : entrySet())
      if (entry.getValue() == value) return entry.getKey();
    throw new ValueNotFoundException();
  }

}
