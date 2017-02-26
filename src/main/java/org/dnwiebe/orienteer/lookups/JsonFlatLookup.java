package org.dnwiebe.orienteer.lookups;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * Created by dnwiebe on 2/26/17.
 */

/**
 * Looks up configuration values in a JSON object. Method name abcDEFGhi becomes abc.def.ghi.
 */
public class JsonFlatLookup extends JsonLookup {

  /**
   * Create a JsonNestingLookup from a Reader containing JSON.
   * @param rdr Reader containing a complete JSON structure with an unnamed JSON object at the top level.
   * @param configRoot JavaScript notation for the location in the JSON structure of the object containing the
   *                   configuration.
   */
  public JsonFlatLookup (Reader rdr, String configRoot) {
    super (rdr, configRoot);
  }

  /**
   * Create a JsonNestingLookup from an InputStream containing JSON.
   * @param istr InputStream containing a complete JSON structure with an unnamed JSON object at the top level.
   * @param configRoot JavaScript notation for the location in the JSON structure of the object containing the
   *                   configuration.
   */
  public JsonFlatLookup (InputStream istr, String configRoot) {
    super (istr, configRoot);
  }

  /**
   * Create a JsonNestingLookup from JSON in a resource file.
   * @param resourceName Name of a resource file containing a JSON structure with an unnamed JSON object at the top level.
   * @param configRoot JavaScript notation for the location in the JSON structure of the object containing the
   *                   configuration.
   */
  public JsonFlatLookup (String resourceName, String configRoot) {
    super (resourceName, configRoot);
  }

  public String nameFromFragments (List<String> fragments) {
    StringBuilder buf = new StringBuilder ();
    for (String fragment: fragments) {
      if (buf.length () > 0) {buf.append (".");}
      buf.append (fragment.toLowerCase ());
    }
    return buf.toString ();
  }

  public String valueFromName (String name, Class singletonType) {
    Object obj = tree.get (name);
    if (obj == null) {
      return null;
    }
    else if (obj instanceof Map || obj instanceof List) {
      return null;
    }
    else {
      return obj.toString ();
    }
  }
}
