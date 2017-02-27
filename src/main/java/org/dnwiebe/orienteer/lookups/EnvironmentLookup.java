package org.dnwiebe.orienteer.lookups;

import java.util.List;

import static org.dnwiebe.orienteer.helpers.Joiner.UC_MAPPER;
import static org.dnwiebe.orienteer.helpers.Joiner.join;

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

  public EnvironmentLookup (String name) {
    super (name, System.getenv ());
  }

  @Override
  public String nameFromFragments(List<String> fragments) {
    return join (fragments, "_", UC_MAPPER);
  }
}
