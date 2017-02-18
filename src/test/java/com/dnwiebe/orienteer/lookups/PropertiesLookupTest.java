package com.dnwiebe.orienteer.lookups;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class PropertiesLookupTest {

  private PropertiesLookup subject;

  @Before
  public void setup () {
    Properties properties = new Properties();
    properties.put ("single", "single value");
    properties.put ("one.two.three.four", "multi-fragment value");
    subject = new PropertiesLookup (properties);
  }

  @Test
  public void joinsFragmentsAroundDots () {

    String name = subject.nameFromFragments (Arrays.asList ("one", "TWO", "Three", "fOUr"));

    assertEquals ("one.two.three.four", name);
  }

  @Test
  public void retrievesValues () {

    String single = subject.valueFromName ("single", null);
    String multi = subject.valueFromName ("one.two.three.four", null);
    String none = subject.valueFromName ("booga", null);

    assertEquals ("single value", single);
    assertEquals ("multi-fragment value", multi);
    assertEquals (null, none);
  }
}
