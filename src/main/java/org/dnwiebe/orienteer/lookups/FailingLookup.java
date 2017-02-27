package org.dnwiebe.orienteer.lookups;

import java.util.List;
import static org.dnwiebe.orienteer.helpers.Joiner.*;

/**
 * Created by dnwiebe on 2/17/17.
 */

/**
 * Throws an exception no matter what value it is asked to look up. Put this at the end of your list of Lookups if you
 * want it to be an error for configuration values to be missing.
 */
public class FailingLookup extends Lookup {

  public FailingLookup () {
    super ();
  }

  public FailingLookup (String name) {
    super (name);
  }



  public String nameFromFragments(List<String> fragments) {
    return join (fragments, "");
  }

  public String valueFromName(String name, Class singletonType) {
    throw new IllegalStateException ("No configuration value found anywhere for property '" + name + "' of " +
      singletonType.getName ());
  }
}
