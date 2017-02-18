package com.dnwiebe.orienteer.lookups;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class MapLookupTest {

  private MapLookup subject;

  @Before
  public void setup () {
    Map<String, String> map = new HashMap<String, String>();
    map.put ("Single", "single value");
    map.put ("OneTwoThreeFour", "multi-fragment value");
    subject = new MapLookup (map);
  }

  @Test
  public void capitalizesEachFragment () {

    String result = subject.nameFromFragments (Arrays.asList ("one", "TWO", "tHREE", "Four"));

    assertEquals ("OneTwoThreeFour", result);
  }

  @Test
  public void findsNamedValue () {
    String result = subject.valueFromName ("OneTwoThreeFour", null);

    assertEquals ("multi-fragment value", result);
  }
}
