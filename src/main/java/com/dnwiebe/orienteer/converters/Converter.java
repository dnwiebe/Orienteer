package com.dnwiebe.orienteer.converters;

/**
 * Created by dnwiebe on 2/17/17.
 */
public interface Converter<T> {
  T convert (String stringValue);
}
