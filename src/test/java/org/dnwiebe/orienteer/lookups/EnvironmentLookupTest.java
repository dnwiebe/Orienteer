package org.dnwiebe.orienteer.lookups;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class EnvironmentLookupTest {

  private EnvironmentLookup subject;

  @Before
  public void setup () {
    subject = new EnvironmentLookup ();
  }

  @Test
  public void formatsFragmentsCorrectly () {

    String result = subject.nameFromFragments (Arrays.asList("super", "Duper", "HOOPEY"));

    assertEquals ("SUPER_DUPER_HOOPEY", result);
  }

  @Test
  public void handlesNonexistentVariable () {

    String result = subject.valueFromName ("nonexistent variable", null);

    assertEquals (null, result);
  }

  @Test
  public void handlesExistingVariable () {
    String name = System.getenv ().keySet ().iterator ().next ();
    String expectedResult = System.getenv (name);

    String result = subject.valueFromName (name, null);

    assertEquals (expectedResult, result);
  }
}
