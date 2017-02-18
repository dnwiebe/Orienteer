package com.dnwiebe.orienteer.lookups;

import java.util.List;
import java.util.Properties;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class SystemPropsLookup extends PropertiesLookup {

  public SystemPropsLookup () {
    super (System.getProperties ());
  }
}
