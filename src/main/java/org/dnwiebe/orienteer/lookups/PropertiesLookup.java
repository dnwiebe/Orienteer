package org.dnwiebe.orienteer.lookups;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by dnwiebe on 2/17/17.
 */

/**
 * Looks up values in the supplied Properties object.  Method name abcDEFGhi becomes abc.def.ghi.
 */
public class PropertiesLookup extends Lookup {

  private final Properties properties;

  public PropertiesLookup (String resourceName) {
    this (PropertiesLookup.class.getClassLoader ().getResourceAsStream (resourceName));
  }

  public PropertiesLookup (InputStream istr) {
    properties = new Properties ();
    try {
      properties.load (istr);
    }
    catch (Exception e) {
      throw new IllegalStateException (e);
    }
  }

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
