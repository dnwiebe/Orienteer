package org.dnwiebe.orienteer.lookups;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/18/17.
 */
public class TestLookupTest {

  @Test
  public void justPastesNameFragmentsBackTogether () {
    TestLookup subject = new TestLookup ();

    String result = subject.nameFromFragments (Arrays.asList("Fourscore", "and", "SEVEN"));

    assertEquals ("FourscoreandSEVEN", result);
  }

  @Test
  public void retrievesValues () {
    TestLookup subject = new TestLookup ("firstKey", "firstValue", "secondKey", "secondValue");

    String result1 = subject.valueFromName ("firstKey", TestLookupTest.class);
    String result2 = subject.valueFromName ("secondKey", TestLookupTest.class);

    assertEquals ("firstValue", result1);
    assertEquals ("secondValue", result2);
  }
}
