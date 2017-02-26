package org.dnwiebe.orienteer.helpers;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dnwiebe on 2/25/17.
 */
public class MapLiteralTest {

  @Test
  public void complainsAboutWrongKeyTypes () {
    try {
      MapLiteral.convert (String.class, Integer.class, 5.6, 1, "string", 4, true, 3);
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("Cannot convert keys to type java.lang.String: 5.6 (java.lang.Double), " +
        "true (java.lang.Boolean)", e.getMessage ());
    }
  }

  @Test
  public void complainsAboutWrongValueTypes () {
    try {
      MapLiteral.convert (String.class, Integer.class, "double", 5.6, "integer", 42, "boolean", true);
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("Cannot convert values to type java.lang.Integer: double -> 5.6 (java.lang.Double), " +
        "boolean -> true (java.lang.Boolean)", e.getMessage ());
    }
  }

  @Test
  public void complainsAboutBoth () {
    try {
      MapLiteral.convert (String.class, Integer.class, 5.6, 1, "string", "booga", true, 3);
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals (
        "Cannot convert keys to type java.lang.String: 5.6 (java.lang.Double), true (java.lang.Boolean)\n" +
          "Cannot convert values to type java.lang.Integer: string -> booga (java.lang.String)",
        e.getMessage ()
      );
    }
  }

  @Test
  public void complainsAboutOddNumberOfParameters () {
    try {
      MapLiteral.convert (String.class, Integer.class, "piggles");
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals (
        "Need value for every key, but found odd number (1) of key-and-value parameters",
        e.getMessage ()
      );
    }
  }

  @Test
  public void createsExpectedMapFromGoodParameters () {
    Map<String, Integer> result = MapLiteral.convert (String.class, Integer.class,
      "forty-seven", 47, "twenty-two", 22, "negative three", -3);

    assertEquals (Integer.valueOf (47), result.get ("forty-seven"));
    assertEquals (Integer.valueOf (22), result.get ("twenty-two"));
    assertEquals (Integer.valueOf (-3), result.get ("negative three"));
    assertEquals (3, result.size ());
  }
}
