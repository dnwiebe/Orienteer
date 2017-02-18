package com.dnwiebe.orienteer.lookups;

import java.util.List;
import java.util.Map;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class MapLookup extends Lookup {

  private Map<String, String> map;

  public MapLookup(Map<String, String> map) {
    this.map = map;
  }

  public String nameFromFragments(List<String> fragments) {
    StringBuilder buf = new StringBuilder ();
    for (String fragment : fragments) {
      buf.append (capitalize (fragment.toLowerCase ()));
    }
    return buf.toString();
  }

  public String valueFromName(String name, Class singletonType) {
    return map.get (name);
  }
}
