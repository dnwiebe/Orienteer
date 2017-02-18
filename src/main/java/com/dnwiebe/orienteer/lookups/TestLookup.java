package com.dnwiebe.orienteer.lookups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dnwiebe on 2/18/17.
 */
public class TestLookup extends Lookup {

  private Map<String, String> map;

  public TestLookup (String... nameValuePairs) {
    if (nameValuePairs.length % 2 != 0) {
      throw new IllegalArgumentException("Name/value pairs require an even number of parameters, not " +
          nameValuePairs.length);
    }
    map = new HashMap<String, String>();
    for (int i = 0; i < nameValuePairs.length; i += 2) {
      map.put (nameValuePairs[i], nameValuePairs[i + 1]);
    }
  }

  public String nameFromFragments(List<String> fragments) {
    StringBuilder buf = new StringBuilder ();
    for (String fragment : fragments) {
      buf.append (fragment);
    }
    return buf.toString ();
  }

  public String valueFromName(String name, Class singletonType) {
    return map.get (name);
  }
}
