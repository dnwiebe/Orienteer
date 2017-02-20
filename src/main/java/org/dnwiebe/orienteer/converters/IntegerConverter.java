package org.dnwiebe.orienteer.converters;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class IntegerConverter implements Converter<Integer> {
  public Integer convert(String stringValue) {
    if (stringValue == null) {return null;}
    return Integer.parseInt (stringValue);
  }
}
