package org.dnwiebe.orienteer.lookups;

import java.util.List;
import java.util.Map;
import static org.dnwiebe.orienteer.helpers.Joiner.*;

/**
 * Created by dnwiebe on 2/17/17.
 */

/**
 * Looks up values in a supplied Map&lt;String, String&gt;s.  Method name abcDEFGhi becomes AbcDEFGhi.
 */
public class MapLookup extends Lookup {

  private Map<String, String> map;

  public MapLookup(Map<String, String> map) {
    super ();
    this.map = map;
  }

  public MapLookup(String name, Map<String, String> map) {
    super (name);
    this.map = map;
  }

  public String nameFromFragments(List<String> fragments) {
    return join (fragments, "", CAP_MAPPER);
  }

  public String valueFromName(String name, Class singletonType) {
    return map.get (name);
  }
}
