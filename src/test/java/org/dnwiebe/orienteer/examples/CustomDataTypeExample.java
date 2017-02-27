package org.dnwiebe.orienteer.examples;

import org.dnwiebe.orienteer.Orienteer;
import org.dnwiebe.orienteer.converters.Converter;
import org.dnwiebe.orienteer.lookups.Lookup;
import org.dnwiebe.orienteer.lookups.TestLookup;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by dnwiebe on 2/18/17.
 */
public class CustomDataTypeExample {

  private static class URLConverter implements Converter<URL> {
    public URL convert(String stringValue) throws Exception {
      return new URL(stringValue);
    }
  }

  public interface ConfigurationSingleton {
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
      assertTrue (e.getMessage (), e.getMessage ().startsWith (
        "The webServiceUrl method of the org.dnwiebe.orienteer.examples.CustomDataTypeExample$ConfigurationSingleton " +
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
