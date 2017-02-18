package com.dnwiebe.orienteer.converters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class LongConverterTest {

  private Converter<Long> subject;

  @Before
  public void setup () {
    subject = new LongConverter ();
  }

  @Test
  public void convertsNullNumber () {

    Long result = subject.convert (null);

    assertEquals (null, result);
  }

  @Test
  public void convertsNonNullNumber () {

    Long result = subject.convert ("-90807060504030201");

    assertEquals (Long.valueOf (-90807060504030201L), result);
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
