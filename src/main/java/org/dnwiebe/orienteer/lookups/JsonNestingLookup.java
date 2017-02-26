package org.dnwiebe.orienteer.lookups;

import com.fasterxml.jackson.jr.ob.JSON;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dnwiebe on 2/17/17.
 */

/**
 * Looks up configuration values in a JSON structure. Names are significantly mangled: subsection3ServiceURL becomes
 * subsection[3].service.url .  This name will be interpreted in the JavaScript fashion to find the configuration value
 * in the JSON.
 */
public class JsonNestingLookup extends Lookup {
  private final Map<String, Object> tree;

  /**
   * Create a JsonNestingLookup from a Reader containing JSON.
   * @param rdr Reader containing a complete JSON structure with an unnamed JSON object at the top level.
   * @param configRoot JavaScript notation for the location in the structure at which the configuration starts,
   *                   or null if the configuration starts at the root of the structure.
   */
  public JsonNestingLookup (final Reader rdr, String configRoot) {
    Supplier<Map<String, Object>> supplier = new Supplier<Map<String, Object>> () {
      public Map<String, Object> supply () throws Exception {
        return JSON.std.mapFrom (rdr);
      }
    };
    tree = makeTree (supplier, configRoot);
  }

  public JsonNestingLookup (final InputStream istr, String configRoot) {
    Supplier<Map<String, Object>> supplier = new Supplier<Map<String, Object>> () {
      public Map<String, Object> supply () throws Exception {
        return JSON.std.mapFrom (istr);
      }
    };
    tree = makeTree (supplier, configRoot);
  }

  public JsonNestingLookup (String resourceName, String configRoot) {
    this (JsonNestingLookup.class.getClassLoader ().getResourceAsStream (resourceName), configRoot);
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

  private interface Supplier<T> {
    T supply () throws Exception;
  }

  private Map<String, Object> makeTree (Supplier<Map<String, Object>> supplier, String configRoot) {
    Map<String, Object> result;
    try {
      result = supplier.supply ();
    }
    catch (Exception e) {
      throw new IllegalStateException (e);
    }
    if (configRoot != null) {
      result = (Map<String, Object>)valueFromName (new Pair ("." + configRoot), result);
      if (result == null) {
        throw new IllegalArgumentException ("Could not find config root '" + configRoot + "' in JSON structure");
      }
    }
    return result;
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

  private static class Pair {
    public final String head;
    public final String tail;
    private final static Pattern[] PATTERNS = {
      Pattern.compile ("^\\[([A-Za-z0-9_]*)](\\..*)$"),
      Pattern.compile ("^\\[([A-Za-z0-9_]*)](\\[.*)$"),
      Pattern.compile ("^\\[([A-Za-z0-9_]*)]()$"),
      Pattern.compile ("^\\.([A-Za-z0-9_]*)(\\..*)$"),
      Pattern.compile ("^\\.([A-Za-z0-9_]*)(\\[.*)$"),
      Pattern.compile ("^\\.([A-Za-z0-9_]*)()$")
    };

    public Pair (String name) {
      Matcher matcher = null;
      boolean matched = false;
      for (Pattern pattern: PATTERNS) {
        matcher = pattern.matcher (name);
        if (matcher.matches ()) {matched = true; break;}
      }
      if (!matched) {
        head = "";
        tail = "";
      }
      else {
        head = matcher.group (1);
        tail = matcher.group (2);
      }
    }
  }

  private Object valueFromName (Pair name, Map<String, Object> map) {
    Object object = map.get (name.head);
    return recurse (name, object);
  }

  private Object valueFromName (Pair name, List<Object> list) {
    try {
      int index = Integer.parseInt (name.head);
      if (index >= list.size ()) {return null;}
      Object object = list.get (index);
      return recurse (name, object);
    }
    catch (NumberFormatException e) {
      return null;
    }
  }

  private Object recurse (Pair name, Object object) {
    if (object == null) {return null;}
    if (name.tail.length () == 0) {return object;}
    if (object instanceof Map) {return valueFromName (new Pair (name.tail), (Map)object);}
    if (object instanceof List) {return valueFromName (new Pair (name.tail), (List)object);}
    throw new UnsupportedOperationException ("Should I return null here?");
  }
}
