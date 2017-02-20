package org.dnwiebe.orienteer.lookups;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
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

  @Test
  public void resourcePropertiesFileConstructor () {
    PropertiesLookup subject = new PropertiesLookup ("properties/lookup.properties");

    String result = subject.valueFromName ("brand", PropertiesLookupTest.class);

    assertEquals ("Respironics", result);
  }

  @Test
  public void inputStreamConstructor () {
    ByteArrayInputStream istr = new ByteArrayInputStream ("brand: Respironics\n".getBytes());
    PropertiesLookup subject = new PropertiesLookup (istr);

    String result = subject.valueFromName ("brand", PropertiesLookupTest.class);

    assertEquals ("Respironics", result);
  }
}