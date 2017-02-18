package com.dnwiebe.orienteer.converters;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class LongConverter implements Converter<Long> {
  public Long convert(String stringValue) {
    if (stringValue == null) {return null;}
    return Long.parseLong (stringValue);
  }
}
