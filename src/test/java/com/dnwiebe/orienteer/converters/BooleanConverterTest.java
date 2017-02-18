package com.dnwiebe.orienteer.converters;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class BooleanConverterTest {

  private Converter<Boolean> subject;

  @Before
  public void setup () {
    subject = new BooleanConverter ();
  }

  @Test
  public void convertsNullValue () {

    Boolean result = subject.convert (null);

    assertEquals (null, result);
  }

  @Test
  public void convertsStringBeginningWithT () {

    Boolean result = subject.convert ("T string");

    assertEquals (Boolean.TRUE, result);
  }

  @Test
  public void convertsStringBeginningWithY () {

    Boolean result = subject.convert ("Y string");

    assertEquals (Boolean.TRUE, result);
  }

  @Test
  public void convertsStringBeginningWithNonZeroDigit () {
    String[] inputs = {"12", "22", "32", "42", "52", "62", "72", "82", "92"};
    for (String input : inputs) {
      Boolean result = subject.convert (input);
      assertEquals (input, Boolean.TRUE, result);
    }
  }

  @Test
  public void convertsStringBeginningWithF () {

    Boolean result = subject.convert ("F string");

    assertEquals (Boolean.FALSE, result);
  }

  @Test
  public void convertsStringBeginningWithN () {

    Boolean result = subject.convert ("N string");

    assertEquals (Boolean.FALSE, result);
  }

  @Test
  public void convertsStringBeginningWithZeroDigit () {

    Boolean result = subject.convert ("0 string");

    assertEquals (Boolean.FALSE, result);
  }

  @Test
  public void complainsAsExpectedForBadlyFormattedValue () {
    try {
      subject.convert ("Booga");
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("Can't convert 'Booga' to Boolean value", e.getMessage ());
    }
  }

  @Test
  public void complainsAsExpectedForEmptyValue () {
    try {
      subject.convert ("");
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("Can't convert '' to Boolean value", e.getMessage ());
    }
  }
}
