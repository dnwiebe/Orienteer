package com.dnwiebe.orienteer.examples;

import com.dnwiebe.orienteer.Orienteer;
import com.dnwiebe.orienteer.lookups.FailingLookup;
import com.dnwiebe.orienteer.lookups.MapLookup;
import com.dnwiebe.orienteer.lookups.PropertiesLookup;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dnwiebe on 2/18/17.
 */
public class MultilayerExample {

  public interface ConfigurationSingleton {
    String firstField ();
    String secondField ();
    String thirdField ();
    String fourthField ();
  }

  private Map<String, String> configMap;
  private Properties properties;

  @Before
  public void setup () {
    configMap = new HashMap<String, String>();
    configMap.put ("FirstField", "first-field value from map");
    configMap.put ("SecondField", "second-field value from map");

    properties = new Properties ();
    properties.put ("first.field", "first-field value from properties");
    properties.put ("third.field", "third-field value from properties");
  }

  @Test
  public void mapThenProperties () {
    ConfigurationSingleton subject = new Orienteer ().make (ConfigurationSingleton.class,
        new MapLookup(configMap),
        new PropertiesLookup(properties)
    );

    assertEquals ("first-field value from map", subject.firstField ());
    assertEquals ("second-field value from map", subject.secondField ());
    assertEquals ("third-field value from properties", subject.thirdField ());
    assertEquals (null, subject.fourthField ());
  }

  @Test
  public void propertiesThenMap () {
    ConfigurationSingleton subject = new Orienteer ().make (ConfigurationSingleton.class,
        new PropertiesLookup(properties),
        new MapLookup(configMap)
    );

    assertEquals ("first-field value from properties", subject.firstField ());
    assertEquals ("second-field value from map", subject.secondField ());
    assertEquals ("third-field value from properties", subject.thirdField ());
    assertEquals (null, subject.fourthField ());
  }

  @Test
  public void propertiesThenMapThenFailure () {
    ConfigurationSingleton subject = new Orienteer ().inhibitInitialCheck ().make (ConfigurationSingleton.class,
        new PropertiesLookup(properties),
        new MapLookup(configMap),
        new FailingLookup()
    );

    assertEquals ("first-field value from properties", subject.firstField ());
    assertEquals ("second-field value from map", subject.secondField ());
    assertEquals ("third-field value from properties", subject.thirdField ());
    try {
      subject.fourthField();
      fail();
    }
    catch (IllegalStateException e) {
      assertEquals ("No configuration value found anywhere for property 'fourthField' of " +
          ConfigurationSingleton.class.getName(), e.getMessage ());
    }
  }
}
