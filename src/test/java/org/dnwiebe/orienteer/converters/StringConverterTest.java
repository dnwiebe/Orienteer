package org.dnwiebe.orienteer.converters;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class StringConverterTest {
  @Test
  public void convertsNonNullString () {
    StringConverter subject = new StringConverter();

    String result = subject.convert ("Booga");

    assertEquals ("Booga", result);
  }

  @Test
  public void convertsNullString () {
    StringConverter subject = new StringConverter();

    String result = subject.convert (null);

    assertEquals (null, result);
  }
}
