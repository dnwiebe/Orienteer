package com.dnwiebe.orienteer.converters;

import com.dnwiebe.orienteer.lookups.Lookup;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
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
  public void findsDefaultConvertersUponConstruction () {
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

  private <T> void checkConverter (Class<T> targetType, String input, T expectedOutput) {
    Converter<T> converter = subject.find (targetType, null);
    T actualOutput = converter.convert (input);
    assertEquals (expectedOutput, actualOutput);
  }
}
