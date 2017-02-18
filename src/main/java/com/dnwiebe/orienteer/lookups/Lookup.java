package com.dnwiebe.orienteer.lookups;

import java.util.List;

/**
 * Created by dnwiebe on 2/17/17.
 */
public abstract class Lookup {

  public String getName () {
    return getClass ().getName ();
  }

  public abstract String nameFromFragments (List<String> fragments);

  public abstract String valueFromName(String name, Class singletonType);

  protected String capitalize (String string) {
    if ((string == null) || (string.length () == 0)) {return string;}
    return Character.toUpperCase (string.charAt (0)) + string.substring (1);
  }
}
