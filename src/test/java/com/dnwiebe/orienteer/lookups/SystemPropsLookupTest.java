package com.dnwiebe.orienteer.lookups;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class SystemPropsLookupTest {

  private SystemPropsLookup subject;

  @Before
  public void setup () {
    subject = new SystemPropsLookup ();
  }

  @Test
  public void handlesNonexistentVariable () {

    String result = subject.valueFromName ("nonexistent variable", null);

    assertEquals (null, result);
  }

  @Test
  public void handlesExistingVariable () {
    String name = System.getProperties ().propertyNames ().nextElement ().toString ();
    String expectedResult = System.getProperty (name);

    String result = subject.valueFromName (name, null);

    assertEquals (expectedResult, result);
  }
}
