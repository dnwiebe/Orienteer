package org.dnwiebe.orienteer.lookups;

import com.fasterxml.jackson.jr.ob.JSON;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class JsonNestingLookup extends Lookup {
  Map<String, Object> tree;

  public JsonNestingLookup (InputStream istr, String configRoot) {
    try {
      tree = JSON.std.mapFrom (istr);
      if (configRoot != null) {tree = (Map<String, Object>)valueFromName (new Pair ("." + configRoot), tree);}
    }
    catch (Exception e) {
      throw new IllegalStateException (e);
    }
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
