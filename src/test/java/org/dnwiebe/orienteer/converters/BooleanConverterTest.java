package org.dnwiebe.orienteer.converters;

import org.junit.Before;
import org.junit.Test;

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
  public void convertsNullValue () throws Exception {

    Boolean result = subject.convert (null);

    assertEquals (null, result);
  }

  @Test
  public void convertsStringBeginningWithT () throws Exception {

    Boolean result = subject.convert ("T string");

    assertEquals (Boolean.TRUE, result);
  }

  @Test
  public void convertsStringBeginningWithY () throws Exception {

    Boolean result = subject.convert ("Y string");

    assertEquals (Boolean.TRUE, result);
  }

  @Test
  public void convertsStringBeginningWithNonZeroDigit () throws Exception {
    String[] inputs = {"12", "22", "32", "42", "52", "62", "72", "82", "92"};
    for (String input : inputs) {
      Boolean result = subject.convert (input);
      assertEquals (input, Boolean.TRUE, result);
    }
  }

  @Test
  public void convertsStringBeginningWithF () throws Exception {

    Boolean result = subject.convert ("F string");

    assertEquals (Boolean.FALSE, result);
  }

  @Test
  public void convertsStringBeginningWithN () throws Exception {

    Boolean result = subject.convert ("N string");

    assertEquals (Boolean.FALSE, result);
  }

  @Test
  public void convertsStringBeginningWithZeroDigit () throws Exception {

    Boolean result = subject.convert ("0 string");

    assertEquals (Boolean.FALSE, result);
  }

  @Test
  public void complainsAsExpectedForBadlyFormattedValue () throws Exception {
    try {
      subject.convert ("Booga");
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("Can't convert 'Booga' to Boolean value", e.getMessage ());
    }
  }

  @Test
  public void complainsAsExpectedForEmptyValue () throws Exception {
    try {
      subject.convert ("");
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("Can't convert '' to Boolean value", e.getMessage ());
    }
  }
}
