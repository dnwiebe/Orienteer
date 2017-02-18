package com.dnwiebe.orienteer.lookups;

import java.util.List;
import java.util.Properties;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class PropertiesLookup extends Lookup {

  private final Properties properties;

  public PropertiesLookup (Properties properties) {
    this.properties = properties;
  }

  public String nameFromFragments(List<String> fragments) {
    StringBuilder buf = new StringBuilder ();
    for (String fragment : fragments) {
      if (buf.length () > 0) {buf.append (".");}
      buf.append (fragment.toLowerCase());
    }
    return buf.toString ();
  }

  public String valueFromName(String name, Class singletonType) {
    return properties.getProperty (name);
  }
}
