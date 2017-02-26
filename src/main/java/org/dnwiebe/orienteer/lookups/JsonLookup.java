package org.dnwiebe.orienteer.lookups;

import com.fasterxml.jackson.jr.ob.JSON;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dnwiebe on 2/26/17.
 */
public abstract class JsonLookup extends Lookup {
  protected final Map<String, Object> tree;

  protected JsonLookup (final Reader rdr, String configRoot) {
    Supplier<Map<String, Object>> supplier = new Supplier<Map<String, Object>> () {
      public Map<String, Object> supply () throws Exception {
        return JSON.std.mapFrom (rdr);
      }
    };
    tree = makeTree (supplier, configRoot);
  }

  protected JsonLookup (final InputStream istr, String configRoot) {
    Supplier<Map<String, Object>> supplier = new Supplier<Map<String, Object>> () {
      public Map<String, Object> supply () throws Exception {
        return JSON.std.mapFrom (istr);
      }
    };
    tree = makeTree (supplier, configRoot);
  }

  protected JsonLookup (String resourceName, String configRoot) {
    this (JsonNestingLookup.class.getClassLoader ().getResourceAsStream (resourceName), configRoot);
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

  protected static class Pair {
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

  protected Object valueFromName (Pair name, Map<String, Object> map) {
    Object object = map.get (name.head);
    return recurse (name, object);
  }

  protected Object valueFromName (Pair name, List<Object> list) {
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
