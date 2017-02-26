package org.dnwiebe.orienteer.examples;

import org.dnwiebe.orienteer.Orienteer;
import org.dnwiebe.orienteer.lookups.MapLookup;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class SimpleExample {

  public interface SimpleExampleConfiguration {
    String someString ();
    Integer someInt ();
    Long someLong ();
    Boolean someBoolean ();
    Double missingDouble ();
  }

  @Test
  public void singletonWithOneConverterAndFullPopulation () {
    Map<String, String> map = new HashMap<String, String>();
    map.put ("SomeString", "Booga");
    map.put ("SomeInt", "42");
    map.put ("SomeLong", "9080706050403020100");
    map.put ("SomeBoolean", "true");
    // no value for MissingDouble

    SimpleExampleConfiguration configurationSingleton = new Orienteer().make (SimpleExampleConfiguration.class,
        new MapLookup(map)
    );

    assertEquals ("Booga", configurationSingleton.someString ());
    assertEquals (Integer.valueOf (42), configurationSingleton.someInt ());
    assertEquals (Long.valueOf (9080706050403020100L), configurationSingleton.someLong ());
    assertEquals (Boolean.TRUE, configurationSingleton.someBoolean ());
    assertEquals (null, configurationSingleton.missingDouble ());
  }
}
