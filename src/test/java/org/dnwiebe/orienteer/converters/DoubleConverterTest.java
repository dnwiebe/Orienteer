package org.dnwiebe.orienteer.converters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class DoubleConverterTest {

  private Converter<Double> subject;

  @Before
  public void setup () {
    subject = new DoubleConverter ();
  }

  @Test
  public void convertsNullNumber () throws Exception {

    Double result = subject.convert (null);

    assertEquals (null, result);
  }

  @Test
  public void convertsNonNullNumber () throws Exception {

    Double result = subject.convert ("34.25");

    assertEquals (Double.valueOf (34.25), result);
  }

  @Test
  public void complainsAsExpectedForBadlyFormattedNumber () throws Exception {
    try {
      subject.convert ("Booga");
      fail ();
    }
    catch (NumberFormatException e) {
      assertEquals ("For input string: \"Booga\"", e.getMessage ());
    }
  }
}
