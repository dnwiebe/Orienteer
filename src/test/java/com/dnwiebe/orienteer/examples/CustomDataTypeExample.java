package com.dnwiebe.orienteer.examples;

import com.dnwiebe.orienteer.Orienteer;
import com.dnwiebe.orienteer.converters.Converter;
import com.dnwiebe.orienteer.lookups.Lookup;
import com.dnwiebe.orienteer.lookups.TestLookup;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by dnwiebe on 2/18/17.
 */
public class CustomDataTypeExample {

  private static class URLConverter implements Converter<URL> {
    public URL convert(String stringValue) {
      try {
        return new URL(stringValue);
      }
      catch (MalformedURLException e) {
        throw new IllegalArgumentException (e);
      }
    }
  }

  private interface ConfigurationSingleton {
    URL webServiceUrl ();
  }

  @Test
  public void mustAddConvertersBeforeMakingSingleton () {
    Orienteer subject = new Orienteer ();
    Lookup lookup = new TestLookup("webServiceUrl", "http://www.google.com");

    try {
      subject.make (ConfigurationSingleton.class, lookup);
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertTrue (e.getMessage ().startsWith (
        "The webServiceUrl method of the com.dnwiebe.orienteer.examples.CustomDataTypeExample$ConfigurationSingleton " +
          "interface returns type java.net.URL. Methods on configuration interfaces must return one of the following " +
          "types:"
      ));
    }
  }

  @Test
  public void butIfYouDoThenItsOkay () {
    Orienteer subject = new Orienteer ();
    subject.addConverter (new URLConverter ());
    Lookup lookup = new TestLookup("webServiceUrl", "http://www.google.com");

    ConfigurationSingleton result = subject.make (ConfigurationSingleton.class, lookup);

    URL url = result.webServiceUrl();
    assertEquals ("www.google.com", url.getHost());
  }
}
