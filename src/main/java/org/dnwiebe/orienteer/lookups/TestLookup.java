package org.dnwiebe.orienteer.lookups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dnwiebe on 2/18/17.
 */

/**
 * Mostly for testing.  Looks up values in a series of pairs supplied at construction time. Method name abcDEFGhi
 * stays abcDEFGhi.
 */
public class TestLookup extends Lookup {

  private Map<String, String> map;

  /**
   * Create a TestLookup with specified data.
   * @param nameValuePairs Name, value, name, value.  Like so: "threadPoolSize", "40", "weatherServiceBaseUrl",
   *                       "http://92.134.10.47/weather/v3"
   */
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
