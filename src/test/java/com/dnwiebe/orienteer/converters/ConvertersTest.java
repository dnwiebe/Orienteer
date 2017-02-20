package com.dnwiebe.orienteer.converters;

import com.dnwiebe.orienteer.Orienteer;
import com.dnwiebe.orienteer.lookups.Lookup;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class ConvertersTest {

  private Converters subject;

  @Before
  public void setup () {
    subject = new Converters ();
  }

  @Test
  public void complainsIfTargetTypeNotProvided () {
    try {
      subject.find (null, null);
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("You must provide a target type to find a Converter", e.getMessage ());
    }
  }

  @Test
  public void complainsIfTargetTypeIsPrimitive () {
    try {
      subject.find (Integer.TYPE, null);
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("Converters do not support primitive types like 'int', only object types", e.getMessage ());
    }
  }

  @Test
  public void findsDefaultConvertersUponConstruction () throws Exception {
    checkConverter (String.class, "Booga", "Booga");
    checkConverter (Integer.class, "42", 42);
    checkConverter (Long.class, "90807060504030201", 90807060504030201L);
    checkConverter (Double.class, "3.1415926535798", 3.1415926535798);
    checkConverter (Boolean.class, "true", true);
  }

  private static class FakeLookup extends Lookup {
    public String nameFromFragments(List<String> fragments) {
      throw new UnsupportedOperationException();
    }
    public String valueFromName(String name, Class singletonType) {
      throw new UnsupportedOperationException();
    }
  }

  private static class OneLookup extends FakeLookup {}
  private static class AnotherLookup extends FakeLookup {}

  @Test
  public void findsGenericConverterWhenNoLookupSpecificConverterExists () {

    Converter<Integer> result = subject.find (Integer.class, OneLookup.class);

    assertSame (IntegerConverter.class, result.getClass ());
  }

  @Test
  public void findsLookupSpecificConverterWhenOneExists () {
    Converter<Integer> converter = mock (Converter.class);
    subject.add (Integer.class, OneLookup.class, converter);

    Converter<Integer> result = subject.find (Integer.class, OneLookup.class);

    assertSame (converter, result);
  }

  @Test
  public void findsGenericConverterWhenSpecificConverterExistsButDifferentLookupIsSpecified () {
    Converter<Integer> converter = mock (Converter.class);
    subject.add (Integer.class, OneLookup.class, converter);

    Converter<Integer> result = subject.find (Integer.class, AnotherLookup.class);

    assertSame (IntegerConverter.class, result.getClass ());
  }

  @Test
  public void doesNotFindSpecificConverterWhenItIsForOtherLookupAndNoGenericConverter () {
    Converter<ConvertersTest> converter = mock (Converter.class);
    subject.add (ConvertersTest.class, OneLookup.class, converter);

    Converter<ConvertersTest> result = subject.find (ConvertersTest.class, AnotherLookup.class);

    assertEquals (null, result);
  }

  @Test
  public void handsOutCorrectSetOfTargetTypes () {
    Converter<ConvertersTest> converter = mock (Converter.class);
    subject.add (ConvertersTest.class, OneLookup.class, converter);

    Set<Class<?>> result = subject.getTargetTypes ();

    Set<Class<?>> expected = new HashSet<Class<?>>();
    expected.addAll (Arrays.asList (String.class, Integer.class, Long.class, Double.class, Boolean.class,
        ConvertersTest.class));
    assertEquals (expected, result);
  }

  @Test
  public void pairEquals () {
    Converters.Pair<String, Integer> a = new Converters.Pair<String, Integer> ("blah", 42);
    Converters.Pair<String, Integer> b = new Converters.Pair<String, Integer> ("blah", 42);
    Converters.Pair<String, Integer> c = new Converters.Pair<String, Integer> ("halb", 42);
    Converters.Pair<String, Integer> d = new Converters.Pair<String, Integer> ("blah", 24);

    assertFalse (a.equals (null));
    assertFalse (a.equals ("string"));
    assertTrue (a.equals (a));
    assertTrue (a.equals (b));
    assertFalse (a.equals (c));
    assertFalse (a.equals (d));
  }

  @Test
  public void pairEqualsNull () {
    Converters.Pair<String, Integer> a = new Converters.Pair<String, Integer> (null, null);
    Converters.Pair<String, Integer> b = new Converters.Pair<String, Integer> (null, 42);
    Converters.Pair<String, Integer> c = new Converters.Pair<String, Integer> ("blah", null);
    Converters.Pair<String, Integer> d = new Converters.Pair<String, Integer> ("blah", 42);

    assertFalse (a.equals (b));
    assertFalse (a.equals (c));
    assertFalse (a.equals (d));
    assertFalse (b.equals (a));
    assertFalse (b.equals (c));
    assertFalse (b.equals (d));
    assertFalse (c.equals (a));
    assertFalse (c.equals (b));
    assertFalse (c.equals (d));
    assertFalse (d.equals (a));
    assertFalse (d.equals (b));
    assertFalse (d.equals (c));
  }

  @Test
  public void pairHashCode () {
    Converters.Pair<String, Integer> a = new Converters.Pair<String, Integer> ("blah", 42);
    Converters.Pair<String, Integer> b = new Converters.Pair<String, Integer> ("blah", 42);
    Converters.Pair<String, Integer> c = new Converters.Pair<String, Integer> ("halb", 42);
    Converters.Pair<String, Integer> d = new Converters.Pair<String, Integer> ("blah", 24);

    assertEquals (a.hashCode (), a.hashCode ());
    assertEquals (a.hashCode (), b.hashCode ());
    assertNotEquals (a.hashCode (), c.hashCode ());
    assertNotEquals (a.hashCode (), d.hashCode ());
  }

  @Test
  public void pairHashCodeNull () {
    Converters.Pair<String, Integer> a = new Converters.Pair<String, Integer> (null, null);
    Converters.Pair<String, Integer> b = new Converters.Pair<String, Integer> (null, 42);
    Converters.Pair<String, Integer> c = new Converters.Pair<String, Integer> ("blah", null);
    Converters.Pair<String, Integer> d = new Converters.Pair<String, Integer> ("blah", 42);

    assertNotEquals (a.hashCode (), b.hashCode ());
    assertNotEquals (a.hashCode (), c.hashCode ());
    assertNotEquals (a.hashCode (), d.hashCode ());
    assertNotEquals (b.hashCode (), a.hashCode ());
    assertNotEquals (b.hashCode (), c.hashCode ());
    assertNotEquals (b.hashCode (), d.hashCode ());
    assertNotEquals (c.hashCode (), a.hashCode ());
    assertNotEquals (c.hashCode (), b.hashCode ());
    assertNotEquals (c.hashCode (), d.hashCode ());
    assertNotEquals (d.hashCode (), a.hashCode ());
    assertNotEquals (d.hashCode (), b.hashCode ());
    assertNotEquals (d.hashCode (), c.hashCode ());
  }

  private <T> void checkConverter (Class<T> targetType, String input, T expectedOutput) throws Exception {
    Converter<T> converter = subject.find (targetType, null);
    T actualOutput = converter.convert (input);
    assertEquals (expectedOutput, actualOutput);
  }
}
