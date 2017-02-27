package org.dnwiebe.orienteer;

import org.dnwiebe.orienteer.converters.Converters;
import org.dnwiebe.orienteer.helpers.Fragmenter;
import org.dnwiebe.orienteer.helpers.Fragments;
import org.dnwiebe.orienteer.lookups.FailingLookup;
import org.dnwiebe.orienteer.lookups.Lookup;
import org.dnwiebe.orienteer.lookups.TestLookup;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.dnwiebe.orienteer.helpers.Joiner.*;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class OrienteerTest {

  private static class NotAnInterface {
  }

  @Test
  public void rejectsClassThatIsNotInterface () {
    try {
      new Orienteer ().make (NotAnInterface.class);
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
      new Orienteer ().make (MethodTakesAParameter.class);
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
      new Orienteer ().make (MethodReturnsBadType.class);
      fail ();
    }
    catch (IllegalArgumentException e) {
      String typeNames = join (converters.getTargetTypes (), ", ", CLASS_MAPPER);
      assertEquals ("The returnsBadType method of the " + MethodReturnsBadType.class.getName () + " interface " +
          "returns type " + OrienteerTest.class.getName () + ". Methods on configuration interfaces must return " +
          "one of the following types: " + typeNames, e.getMessage ());
    }
  }

  private interface GoodInterface {
    String goodMethod ();
  }

  @Test
  public void acceptsInterfaceWithSingleGoodMethod () {
    new Orienteer ().make (GoodInterface.class);

    // no exception: test passes
  }

  @Test
  public void worksWithTwoConvertersWhereTheFirstFindsTheValueAndTheSecondDoesnt () {
    Orienteer orienteer = new Orienteer ();
    Fragmenter fragmenter = mock (Fragmenter.class);
    List<String> fragmentedName = Arrays.asList ("fragmented", "name");
    when (fragmenter.fragment ("goodMethod")).thenReturn (fragmentedName);
    orienteer.fragmenter = fragmenter;
    Logger logger = mock (Logger.class);
    orienteer.logger = logger;
    Lookup first = mock(Lookup.class);
    when(first.getName ()).thenReturn ("First Lookup");
    when(first.nameFromFragments(fragmentedName)).thenReturn("firstProperty");
    when(first.valueFromName("firstProperty", GoodInterface.class)).thenReturn("firstValue");
    Lookup second = mock(Lookup.class);
    when(second.getName ()).thenReturn ("Second Lookup");
    when(second.nameFromFragments(fragmentedName)).thenReturn("secondProperty");
    when(second.valueFromName("secondProperty", GoodInterface.class)).thenReturn(null);

    GoodInterface singleton = orienteer.make(GoodInterface.class, first, second);

    assertEquals("firstValue", singleton.goodMethod());
    verify (logger, atLeastOnce ()).info ("Seeking configuration value for 'goodMethod'");
    verify (logger, atLeastOnce ()).info ("  Consulting First Lookup for 'firstProperty': found 'firstValue'");
    verify (logger, atLeastOnce ()).info ("Configured: " + GoodInterface.class.getName () + ".goodMethod() <- firstValue");
  }

  @Test
  public void worksWithTwoConvertersWhereTheFirstDoesntFindTheValueButTheSecondDoes () {
    Orienteer orienteer = new Orienteer ();
    Fragmenter fragmenter = mock (Fragmenter.class);
    List<String> fragmentedName = Arrays.asList ("fragmented", "name");
    when (fragmenter.fragment ("goodMethod")).thenReturn (fragmentedName);
    orienteer.fragmenter = fragmenter;
    Logger logger = mock (Logger.class);
    orienteer.logger = logger;
    Lookup first = mock(Lookup.class);
    when(first.getName ()).thenReturn ("First Lookup");
    when(first.nameFromFragments(fragmentedName)).thenReturn("firstProperty");
    when(first.valueFromName("firstProperty", GoodInterface.class)).thenReturn(null);
    Lookup second = mock(Lookup.class);
    when(second.getName ()).thenReturn ("Second Lookup");
    when(second.nameFromFragments(fragmentedName)).thenReturn("secondProperty");
    when(second.valueFromName("secondProperty", GoodInterface.class)).thenReturn("secondValue");

    GoodInterface singleton = orienteer.make(GoodInterface.class, first, second);

    assertEquals("secondValue", singleton.goodMethod());
    verify (logger, atLeastOnce ()).info ("Seeking configuration value for 'goodMethod'");
    verify (logger, atLeastOnce ()).info ("  Consulting First Lookup for 'firstProperty': not found");
    verify (logger, atLeastOnce ()).info ("  Consulting Second Lookup for 'secondProperty': found 'secondValue'");
    verify (logger, atLeastOnce ()).info ("Configured: " + GoodInterface.class.getName () + ".goodMethod() <- secondValue");
  }

  @Test
  public void worksWithOneConverterWhereTheValueIsUnknown () {
    Orienteer orienteer = new Orienteer ();
    Fragmenter fragmenter = mock (Fragmenter.class);
    List<String> fragmentedName = Arrays.asList ("fragmented", "name");
    when (fragmenter.fragment ("goodMethod")).thenReturn (fragmentedName);
    orienteer.fragmenter = fragmenter;
    Logger logger = mock (Logger.class);
    orienteer.logger = logger;
    Lookup lookup = mock(Lookup.class);
    when(lookup.getName ()).thenReturn ("Lookup");
    when(lookup.nameFromFragments(fragmentedName)).thenReturn("property");
    when(lookup.valueFromName("property", null)).thenReturn(null);

    GoodInterface singleton = orienteer.make(GoodInterface.class, lookup);

    assertEquals(null, singleton.goodMethod());
    verify (logger, atLeastOnce ()).info ("Seeking configuration value for 'goodMethod'");
    verify (logger, atLeastOnce ()).info ("  Consulting Lookup for 'property': not found");
    verify (logger, atLeastOnce ()).warning ("NOT CONFIGURED: " + GoodInterface.class.getName () + ".goodMethod()");
  }

  @Test
  public void worksWithOneConverterEvenAfterConverterIsRemovedFromArray () {
    Orienteer orienteer = new Orienteer ();
    Fragmenter fragmenter = mock (Fragmenter.class);
    List<String> fragmentedName = Arrays.asList ("fragmented", "name");
    when (fragmenter.fragment ("goodMethod")).thenReturn (fragmentedName);
    orienteer.fragmenter = fragmenter;
    orienteer.logger = mock (Logger.class);
    Lookup lookup = mock(Lookup.class);
    when(lookup.getName ()).thenReturn ("Lookup");
    when(lookup.nameFromFragments(fragmentedName)).thenReturn("property");
    when(lookup.valueFromName("property", GoodInterface.class)).thenReturn("value");
    Lookup[] lookups = new Lookup[] {lookup};
    GoodInterface singleton = orienteer.make(GoodInterface.class, lookups);

    lookups[0] = null;

    assertEquals("value", singleton.goodMethod());
  }

  @Test
  public void verifiesAccessibilityOfFieldsDuringMake () {
    Orienteer subject = new Orienteer ();
    subject.logger = mock (Logger.class);

    try {
      subject.make (GoodInterface.class, new FailingLookup());
      fail ();
    }
    catch (IllegalStateException e) {
      assertEquals ("Couldn't retrieve configurations: goodMethod", e.getMessage ());
    }
  }

  public interface AlternateFragmentation {
    @Fragments ({"alter", "Nate", "Fragmen", "Tation"})
    String methodName ();
  }

  @Test
  public void usesAlternateFragmentationIfPresent () {
    AlternateFragmentation singleton = new Orienteer ()
      .make (AlternateFragmentation.class, new TestLookup ("methodName", "nope",
        "alterNateFragmenTation", "yep"));

    String result = singleton.methodName ();

    assertEquals ("yep", result);
  }
}
