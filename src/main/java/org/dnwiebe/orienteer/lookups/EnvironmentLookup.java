package org.dnwiebe.orienteer.lookups;

import java.util.List;

/**
 * Created by dnwiebe on 2/17/17.
 */

/**
 * Looks up values in the environment.  Method name abcDEFGhi becomes ABC_DEF_GHI.
 */
public class EnvironmentLookup extends MapLookup {

  public EnvironmentLookup () {
    super (System.getenv ());
  }

  @Override
  public String nameFromFragments(List<String> fragments) {
    StringBuilder buf = new StringBuilder ();
    for (String fragment : fragments) {
      if (buf.length () > 0) {buf.append ("_");}
      buf.append (fragment.toUpperCase());
    }
    return buf.toString ();
  }
}
