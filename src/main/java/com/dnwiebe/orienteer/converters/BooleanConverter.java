package com.dnwiebe.orienteer.converters;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class BooleanConverter implements Converter<Boolean> {
  public Boolean convert(String stringValue) {
    if (stringValue == null) {return null;}
    if (stringValue.length () > 0) {
      char c = Character.toUpperCase (stringValue.charAt (0));
      if ("TY123456789".indexOf (c) >= 0) {
        return Boolean.TRUE;
      }
      if ("FN0".indexOf (c) >= 0) {
        return Boolean.FALSE;
      }
    }
    throw new IllegalArgumentException ("Can't convert '" + stringValue + "' to Boolean value");
  }
}
