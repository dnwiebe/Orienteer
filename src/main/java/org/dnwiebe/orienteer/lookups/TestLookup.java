package org.dnwiebe.orienteer.lookups;

import org.dnwiebe.orienteer.helpers.MapLiteral;

import java.util.List;
import java.util.Map;
import static org.dnwiebe.orienteer.helpers.Joiner.*;

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
    super ();
    map = MapLiteral.convert (String.class, String.class, nameValuePairs);
  }

  public String nameFromFragments(List<String> fragments) {
    return join (fragments, "");
  }

  public String valueFromName(String name, Class singletonType) {
    return map.get (name);
  }
}
