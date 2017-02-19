package com.dnwiebe.orienteer.lookups;

import java.util.List;
import java.util.Properties;

/**
 * Created by dnwiebe on 2/17/17.
 */

/**
 * Looks up values in System.getProperties().  Method name abcDEFGhi becomes abc.def.ghi.
 */
public class SystemPropsLookup extends PropertiesLookup {

  public SystemPropsLookup () {
    super (System.getProperties ());
  }
}
