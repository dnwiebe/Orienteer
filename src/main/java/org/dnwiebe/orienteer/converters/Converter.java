package org.dnwiebe.orienteer.converters;

/**
 * Created by dnwiebe on 2/17/17.
 */

/**
 * Functional interface that describes a method to convert a String value to some other type T.  The String value
 * will be provided by an Orienteer Lookup object, and the resulting value of type T will make its way into the
 * return value of a method of the singleton object created by Orienteer.
 * @param <T> The target type of the conversion.
 */
public interface Converter<T> {
  /**
   * Convert a String to a value of another type.
   * @param stringValue String value to convert
   * @return Equivalent value of type T.  Do not return null unless the incoming String value is null.
   * @throws Exception if the conversion fails.
   */
  T convert (String stringValue) throws Exception;
}
