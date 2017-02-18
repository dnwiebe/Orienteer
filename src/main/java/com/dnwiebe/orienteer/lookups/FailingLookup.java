package com.dnwiebe.orienteer.lookups;

import java.util.List;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class FailingLookup extends Lookup {
  public String nameFromFragments(List<String> fragments) {
    StringBuilder buf = new StringBuilder ();
    for (String fragment : fragments) {
      buf.append (fragment);
    }
    return buf.toString ();
  }

  public String valueFromName(String name, Class singletonType) {
    throw new IllegalStateException ("No configuration value found anywhere for property '" + name + "' of " +
      singletonType.getName ());
  }
}
