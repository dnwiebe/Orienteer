package com.dnwiebe.orienteer.converters;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class DoubleConverter implements Converter<Double> {
  public Double convert(String stringValue) {
    if (stringValue == null) {return null;}
    return Double.parseDouble (stringValue);
  }
}
