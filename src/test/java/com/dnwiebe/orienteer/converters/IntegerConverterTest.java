package com.dnwiebe.orienteer.converters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class IntegerConverterTest {

  private Converter<Integer> subject;

  @Before
  public void setup () {
    subject = new IntegerConverter ();
  }

  @Test
  public void convertsNullNumber () {

    Integer result = subject.convert (null);

    assertEquals (null, result);
  }

  @Test
  public void convertsNonNullNumber () {

    Integer result = subject.convert ("-42");

    assertEquals (Integer.valueOf (-42), result);
  }

  @Test
  public void complainsAsExpectedForBadlyFormattedNumber () {
    try {
      subject.convert ("Booga");
      fail ();
    }
    catch (NumberFormatException e) {
      assertEquals ("For input string: \"Booga\"", e.getMessage ());
    }
  }
}
