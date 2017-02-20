package org.dnwiebe.orienteer.examples;

import org.dnwiebe.orienteer.Orienteer;
import org.dnwiebe.orienteer.converters.Converter;
import org.dnwiebe.orienteer.lookups.Lookup;
import org.dnwiebe.orienteer.lookups.TestLookup;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/18/17.
 */
public class CustomLookupExample {

  public interface ConfigurationSingleton {
    String globalGreeting ();
    String localGreeting ();
    String surlyGreeting ();
  }

  private class HardCodedLookup extends Lookup {

    public String nameFromFragments(List<String> fragments) {
      // convert fragments to lower snake case
      StringBuilder buf = new StringBuilder ();
      for (String fragment : fragments) {
        if (buf.length () > 0) {buf.append ("_");}
        buf.append (fragment.toLowerCase ());
      }
      return buf.toString ();
    }

    public String valueFromName(String name, Class singletonType) {
      // values are hardcoded here, but could be looked up anywhere you choose
      if ("global_greeting".equals (name)) {return "'Hello, world!'";}
      else if ("local_greeting".equals (name)) {return "'Hello, you!'";}
      else if ("surly_greeting".equals (name)) {return "'Bugger off!'";}
      else {return null;}
    }
  }

  @Test
  public void customLookupsAreVeryEasyToUse () {
    Orienteer subject = new Orienteer ();

    ConfigurationSingleton singleton = subject.make (ConfigurationSingleton.class,
        new TestLookup("surlyGreeting", "Perhaps another time?"),
        new HardCodedLookup ()
    );

    assertEquals ("'Hello, world!'", singleton.globalGreeting ());
    assertEquals ("'Hello, you!'", singleton.localGreeting ());
    assertEquals ("Perhaps another time?", singleton.surlyGreeting());
  }

  private static class ShaveOuterCharactersConverter implements Converter<String> {
    public String convert (String stringValue) {
      return stringValue.substring (1, stringValue.length () - 1);
    }
  }

  @Test
  public void canProvideACustomConverterForUseOnlyWithAParticularLookup () {
    Orienteer subject = new Orienteer ();
    subject.addConverter (new ShaveOuterCharactersConverter(), HardCodedLookup.class);

    ConfigurationSingleton singleton = subject.make (ConfigurationSingleton.class,
        new TestLookup ("surlyGreeting", "Perhaps another time?"),
        new HardCodedLookup ()
    );

    assertEquals ("Hello, world!", singleton.globalGreeting ());
    assertEquals ("Hello, you!", singleton.localGreeting ());
    assertEquals ("Perhaps another time?", singleton.surlyGreeting());
  }
}
