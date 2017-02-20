package org.dnwiebe.orienteer.lookups;

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
