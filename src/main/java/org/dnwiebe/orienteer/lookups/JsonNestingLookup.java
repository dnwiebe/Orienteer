package org.dnwiebe.orienteer.lookups;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;

/**
 * Created by dnwiebe on 2/17/17.
 */

/**
 * Looks up configuration values in a JSON structure. Names are significantly mangled: subsection3ServiceURL becomes
 * subsection[3].service.url .  This name will be interpreted in the JavaScript fashion to find the configuration value
 * in the JSON.
 */
public class JsonNestingLookup extends JsonLookup {

  /**
   * Create a JsonNestingLookup from a Reader containing JSON.
   * @param rdr Reader containing a complete JSON structure with an unnamed JSON object at the top level.
   * @param configRoot JavaScript notation for the location in the structure at which the configuration starts,
   *                   or null if the configuration starts at the root of the structure.
   */
  public JsonNestingLookup (final Reader rdr, String configRoot) {
    super (rdr, configRoot);
  }

  /**
   * Create a JsonNestingLookup from an InputStream containing JSON.
   * @param istr InputStream containing a complete JSON structure with an unnamed JSON object at the top level.
   * @param configRoot JavaScript notation for the location in the structure at which the configuration starts,
   *                   or null if the configuration starts at the root of the structure.
   */
  public JsonNestingLookup (final InputStream istr, String configRoot) {
    super (istr, configRoot);
  }

  /**
   * Create a JsonNestingLookup from JSON in a resource file.
   * @param resourceName Name of a resource file containing a JSON structure with an unnamed JSON object at the top level.
   * @param configRoot JavaScript notation for the location in the structure at which the configuration starts,
   *                   or null if the configuration starts at the root of the structure.
   */
  public JsonNestingLookup (String resourceName, String configRoot) {
    super (resourceName, configRoot);
  }

  /**
   * Create a JsonNestingLookup from a Reader containing JSON.
   * @param name Name to be used in forensics for this Lookup
   * @param rdr Reader containing a complete JSON structure with an unnamed JSON object at the top level.
   * @param configRoot JavaScript notation for the location in the structure at which the configuration starts,
   *                   or null if the configuration starts at the root of the structure.
   */
  public JsonNestingLookup (String name, final Reader rdr, String configRoot) {
    super (name, rdr, configRoot);
  }

  /**
   * Create a JsonNestingLookup from an InputStream containing JSON.
   * @param name Name to be used in forensics for this Lookup
   * @param istr InputStream containing a complete JSON structure with an unnamed JSON object at the top level.
   * @param configRoot JavaScript notation for the location in the structure at which the configuration starts,
   *                   or null if the configuration starts at the root of the structure.
   */
  public JsonNestingLookup (String name, final InputStream istr, String configRoot) {
    super (name, istr, configRoot);
  }

  /**
   * Create a JsonNestingLookup from JSON in a resource file.
   * @param name Name to be used in forensics for this Lookup
   * @param resourceName Name of a resource file containing a JSON structure with an unnamed JSON object at the top level.
   * @param configRoot JavaScript notation for the location in the structure at which the configuration starts,
   *                   or null if the configuration starts at the root of the structure.
   */
  public JsonNestingLookup (String name, String resourceName, String configRoot) {
    super (name, resourceName, configRoot);
  }

  public String nameFromFragments(List<String> fragments) {
    StringBuilder buf = new StringBuilder ();
    for (String fragment : fragments) {
      if (isNumber (fragment)) {
        buf.append ("[").append (fragment).append ("]");
      }
      else {
        if (buf.length () > 0) {buf.append (".");}
        buf.append (fragment.toLowerCase ());
      }
    }
    return buf.toString ();
  }

  public String valueFromName(String name, Class singletonType) {
    Object value = valueFromName (new Pair ("." + name), tree);
    return (value == null) ? null : value.toString ();
  }

  private boolean isNumber (String s) {
    for (int i = 0; i < s.length (); i++) {
      if (!Character.isDigit (s.charAt (i))) {return false;}
    }
    return true;
  }
}
