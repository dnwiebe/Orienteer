package org.dnwiebe.orienteer.lookups;

import java.io.InputStream;
import java.util.List;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class JsonNestingLookup extends Lookup {
  public JsonNestingLookup (InputStream istr) {

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
    return null;
  }

  private boolean isNumber (String s) {
    for (int i = 0; i < s.length (); i++) {
      if (!Character.isDigit (s.charAt (i))) {return false;}
    }
    return true;
  }
}
