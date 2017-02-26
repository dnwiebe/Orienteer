package org.dnwiebe.orienteer.lookups;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class FailingLookupTest {

  private FailingLookup subject;

  @Before
  public void setup () {
    subject = new FailingLookup ();
  }

  @Test
  public void justPastesNameFragmentsTogether () {
    String result = subject.nameFromFragments (Arrays.asList("Fourscore", "and", "SEVEN"));

    assertEquals ("FourscoreandSEVEN", result);
  }

  @Test
  public void throwsExceptionsAboutEverything () {
    try {
      subject.valueFromName("blah", FailingLookupTest.class);
      fail ();
    }
    catch (IllegalStateException e) {
      assertEquals ("No configuration value found anywhere for property 'blah' of " + FailingLookupTest.class.getName (),
          e.getMessage ());
    }
  }

  @Test
  public void nameViaConstructors () {
    assertEquals (FailingLookup.class.getName (), new FailingLookup ().getName ());
    assertEquals ("booga", new FailingLookup ("booga").getName ());
  }
}
