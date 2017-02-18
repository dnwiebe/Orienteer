package com.dnwiebe.orienteer;

import com.dnwiebe.orienteer.converters.Converters;
import com.dnwiebe.orienteer.lookups.Lookup;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class OrienteerTest {

  private static class NotAnInterface {
  }

  @Test
  public void rejectsClassThatIsNotInterface () {
    try {
      Orienteer.make (NotAnInterface.class);
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("Configuration singletons must be built on interfaces, not classes like " +
        NotAnInterface.class.getName () + ".", e.getMessage ());
    }
  }

  private interface MethodTakesAParameter {
    String takesAParameter (int x);
  }

  @Test
  public void rejectsInterfaceWithMethodTakingParameters () {
    try {
      Orienteer.make (MethodTakesAParameter.class);
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("The takesAParameter method of the " + MethodTakesAParameter.class.getName () + " interface " +
        "takes a parameter. Methods on configuration interfaces must be parameterless.", e.getMessage ());
    }
  }

  private interface MethodReturnsBadType {
    OrienteerTest returnsBadType ();
  }

  @Test
  public void rejectsInterfaceWithMethodReturningUnsupportedType () {
    Converters converters = new Converters();
    try {
      Orienteer.make (MethodReturnsBadType.class);
      fail ();
    }
    catch (IllegalArgumentException e) {
      StringBuilder buf = new StringBuilder ();
      for (Class type : converters.getTargetTypes()) {
        if (buf.length () > 0) {buf.append (", ");}
        buf.append (type.getName ());
      }
      assertEquals ("The returnsBadType method of the " + MethodReturnsBadType.class.getName () + " interface " +
          "returns type " + OrienteerTest.class.getName () + ". Methods on configuration interfaces must return " +
          "one of the following types: " + buf.toString (), e.getMessage ());
    }
  }

  private interface GoodInterface {
    String goodMethod ();
  }

  @Test
  public void acceptsInterfaceWithSingleGoodMethod () {
    Orienteer.make (GoodInterface.class);

    // no exception: test passes
  }

  @Test
  public void worksWithTwoConvertersWhereTheFirstFindsTheValueAndTheSecondDoesnt () {
    Orienteer.Fragmenter fragmenter = mock (Orienteer.Fragmenter.class);
    List<String> fragmentedName = Arrays.asList ("fragmented", "name");
    when (fragmenter.fragment ("goodMethod")).thenReturn (fragmentedName);
    Orienteer.Fragmenter OLD_FRAGMENTER = Orienteer.FRAGMENTER;
    Orienteer.FRAGMENTER = fragmenter;
    Logger logger = mock (Logger.class);
    Logger OLD_LOGGER = Orienteer.LOGGER;
    Orienteer.LOGGER = logger;
    try {
      Lookup first = mock(Lookup.class);
      when(first.getName ()).thenReturn ("First Lookup");
      when(first.nameFromFragments(fragmentedName)).thenReturn("firstProperty");
      when(first.valueFromName("firstProperty", GoodInterface.class)).thenReturn("firstValue");
      Lookup second = mock(Lookup.class);
      when(second.getName ()).thenReturn ("Second Lookup");
      when(second.nameFromFragments(fragmentedName)).thenReturn("secondProperty");
      when(second.valueFromName("secondProperty", GoodInterface.class)).thenReturn(null);

      GoodInterface singleton = Orienteer.make(GoodInterface.class, first, second);

      assertEquals("firstValue", singleton.goodMethod());
      verify (logger).info ("Seeking configuration value for 'goodMethod'");
      verify (logger).info ("  Consulting First Lookup for 'firstProperty': found 'firstValue'");
      verify (logger).info ("Configured: " + GoodInterface.class.getName () + ".goodMethod() -> firstValue");
    }
    finally {
      Orienteer.FRAGMENTER = OLD_FRAGMENTER;
      Orienteer.LOGGER = OLD_LOGGER;
    }
  }

  @Test
  public void worksWithTwoConvertersWhereTheFirstDoesntFindTheValueButTheSecondDoes () {
    Orienteer.Fragmenter fragmenter = mock (Orienteer.Fragmenter.class);
    List<String> fragmentedName = Arrays.asList ("fragmented", "name");
    when (fragmenter.fragment ("goodMethod")).thenReturn (fragmentedName);
    Orienteer.Fragmenter OLD_FRAGMENTER = Orienteer.FRAGMENTER;
    Orienteer.FRAGMENTER = fragmenter;
    Logger logger = mock (Logger.class);
    Logger OLD_LOGGER = Orienteer.LOGGER;
    Orienteer.LOGGER = logger;
    try {
      Lookup first = mock(Lookup.class);
      when(first.getName ()).thenReturn ("First Lookup");
      when(first.nameFromFragments(fragmentedName)).thenReturn("firstProperty");
      when(first.valueFromName("firstProperty", GoodInterface.class)).thenReturn(null);
      Lookup second = mock(Lookup.class);
      when(second.getName ()).thenReturn ("Second Lookup");
      when(second.nameFromFragments(fragmentedName)).thenReturn("secondProperty");
      when(second.valueFromName("secondProperty", GoodInterface.class)).thenReturn("secondValue");

      GoodInterface singleton = Orienteer.make(GoodInterface.class, first, second);

      assertEquals("secondValue", singleton.goodMethod());
      verify (logger).info ("Seeking configuration value for 'goodMethod'");
      verify (logger).info ("  Consulting First Lookup for 'firstProperty': not found");
      verify (logger).info ("  Consulting Second Lookup for 'secondProperty': found 'secondValue'");
      verify (logger).info ("Configured: " + GoodInterface.class.getName () + ".goodMethod() -> secondValue");
    }
    finally {
      Orienteer.FRAGMENTER = OLD_FRAGMENTER;
      Orienteer.LOGGER = OLD_LOGGER;
    }
  }

  @Test
  public void worksWithOneConverterWhereTheValueIsUnknown () {
    Orienteer.Fragmenter fragmenter = mock (Orienteer.Fragmenter.class);
    List<String> fragmentedName = Arrays.asList ("fragmented", "name");
    when (fragmenter.fragment ("goodMethod")).thenReturn (fragmentedName);
    Orienteer.Fragmenter OLD_FRAGMENTER = Orienteer.FRAGMENTER;
    Orienteer.FRAGMENTER = fragmenter;
    Logger logger = mock (Logger.class);
    Logger OLD_LOGGER = Orienteer.LOGGER;
    Orienteer.LOGGER = logger;
    try {
      Lookup lookup = mock(Lookup.class);
      when(lookup.getName ()).thenReturn ("Lookup");
      when(lookup.nameFromFragments(fragmentedName)).thenReturn("property");
      when(lookup.valueFromName("property", null)).thenReturn(null);

      GoodInterface singleton = Orienteer.make(GoodInterface.class, lookup);

      assertEquals(null, singleton.goodMethod());
      verify (logger).info ("Seeking configuration value for 'goodMethod'");
      verify (logger).info ("  Consulting Lookup for 'property': not found");
      verify (logger).warning ("NOT CONFIGURED: " + GoodInterface.class.getName () + ".goodMethod()");
    }
    finally {
      Orienteer.FRAGMENTER = OLD_FRAGMENTER;
      Orienteer.LOGGER = OLD_LOGGER;
    }
  }

  @Test
  public void worksWithOneConverterEvenAfterConverterIsRemovedFromArray () {
    Orienteer.Fragmenter fragmenter = mock (Orienteer.Fragmenter.class);
    List<String> fragmentedName = Arrays.asList ("fragmented", "name");
    when (fragmenter.fragment ("goodMethod")).thenReturn (fragmentedName);
    Orienteer.Fragmenter OLD_FRAGMENTER = Orienteer.FRAGMENTER;
    Orienteer.FRAGMENTER = fragmenter;
    try {
      Lookup lookup = mock(Lookup.class);
      when(lookup.getName ()).thenReturn ("Lookup");
      when(lookup.nameFromFragments(fragmentedName)).thenReturn("property");
      when(lookup.valueFromName("property", GoodInterface.class)).thenReturn("value");
      Lookup[] lookups = new Lookup[] {lookup};
      GoodInterface singleton = Orienteer.make(GoodInterface.class, lookups);

      lookups[0] = null;

      assertEquals("value", singleton.goodMethod());
    }
    finally {
      Orienteer.FRAGMENTER = OLD_FRAGMENTER;
    }
  }

  @Test
  public void fragmenterCanHandleNoCaps () {
    Orienteer.Fragmenter subject = new Orienteer.Fragmenter();

    List<String> result = subject.fragment ("stringwithnocapitals");

    assertEquals (Arrays.asList ("stringwithnocapitals"), result);
  }

  @Test
  public void fragmenterCanHandleAllCaps () {
    Orienteer.Fragmenter subject = new Orienteer.Fragmenter();

    List<String> result = subject.fragment ("STRINGWITHALLCAPITALS");

    assertEquals (Arrays.asList ("STRINGWITHALLCAPITALS"), result);
  }

  @Test
  public void fragmenterBreaksOnLowerToUpperTransitions () {
    Orienteer.Fragmenter subject = new Orienteer.Fragmenter();

    List<String> result = subject.fragment ("oneTwoTHREE");

    assertEquals (Arrays.asList ("one", "Two", "THREE"), result);
  }

  @Test
  public void fragmenterHandlesAllCapsSubstrings () {
    Orienteer.Fragmenter subject = new Orienteer.Fragmenter();

    List<String> result = subject.fragment ("oneTWOThree");

    assertEquals (Arrays.asList ("one", "TWO", "Three"), result);
  }
}
