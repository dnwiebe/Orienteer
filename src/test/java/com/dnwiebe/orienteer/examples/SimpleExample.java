package com.dnwiebe.orienteer.examples;

import com.dnwiebe.orienteer.Orienteer;
import com.dnwiebe.orienteer.lookups.MapLookup;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class SimpleExample {

  private interface SimpleExampleConfiguration {
    String someString ();
    Integer someInt ();
    Long someLong ();
    Boolean someBoolean ();
  }

  @Test
  public void singletonWithOneConverterAndFullPopulation () {
    Map<String, String> map = new HashMap<String, String>();
    map.put ("SomeString", "Booga");
    map.put ("SomeInt", "42");
    map.put ("SomeLong", "9080706050403020100");
    map.put ("SomeBoolean", "true");

    SimpleExampleConfiguration configurationSingleton = new Orienteer ().make (SimpleExampleConfiguration.class,
        new MapLookup(map)
    );

    assertEquals ("Booga", configurationSingleton.someString ());
    assertEquals (Integer.valueOf (42), configurationSingleton.someInt ());
    assertEquals (Long.valueOf (9080706050403020100L), configurationSingleton.someLong ());
    assertEquals (Boolean.TRUE, configurationSingleton.someBoolean ());
  }
}
