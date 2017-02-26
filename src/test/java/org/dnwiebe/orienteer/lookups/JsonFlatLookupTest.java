package org.dnwiebe.orienteer.lookups;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dnwiebe on 2/26/17.
 */
public class JsonFlatLookupTest {
  private JsonFlatLookup subject;

  @Before
  public void setup () {
    InputStream istr = getClass ().getClassLoader ().getResourceAsStream ("json/lookup.json");
    subject = new JsonFlatLookup (istr, "configFlat");
  }

  @Test
  public void translatesNamesCorrectly () {
    assertEquals ("first.array.0", subject.nameFromFragments (Arrays.asList ("first", "Array", "0")));
    assertEquals ("first.array.1.second", subject.nameFromFragments (Arrays.asList ("first", "Array", "1", "Second")));
    assertEquals ("first.array.2", subject.nameFromFragments (Arrays.asList ("first", "Array", "2")));
    assertEquals ("first.object.first", subject.nameFromFragments (Arrays.asList("first", "Object", "First")));
    assertEquals ("second", subject.nameFromFragments (Arrays.asList("second")));
  }

  @Test
  public void locatesValuesCorrectly () {
    assertEquals("first", subject.valueFromName("first.array.0", JsonNestingLookupTest.class));
    assertEquals("secondValue", subject.valueFromName("first.array.1.second", JsonNestingLookupTest.class));
    assertEquals("3", subject.valueFromName("first.array.2", JsonNestingLookupTest.class));
    assertEquals("4.75", subject.valueFromName("first.object.first", JsonNestingLookupTest.class));
    assertEquals("true", subject.valueFromName("second", JsonNestingLookupTest.class));
  }

  @Test
  public void returnsNullWhenNamesAreWrongOrValuesNonexistent () {
    assertEquals (null, subject.valueFromName ("first.array.3", JsonNestingLookupTest.class));
    assertEquals (null, subject.valueFromName ("]}.gorb", JsonNestingLookupTest.class));
  }

  @Test
  public void worksWithReader () {
    InputStream istr = getClass ().getClassLoader ().getResourceAsStream ("json/lookup.json");
    Reader rdr = new InputStreamReader (istr);
    JsonFlatLookup subject = new JsonFlatLookup (rdr, "configFlat");

    assertEquals ("first", subject.valueFromName("first.array.0",
      JsonFlatLookupTest.class));
  }

  @Test
  public void worksWithResourceString () {
    JsonFlatLookup subject = new JsonFlatLookup ("json/lookup.json", "configFlat");

    assertEquals ("first", subject.valueFromName("first.array.0",
      JsonFlatLookupTest.class));
  }

  @Test
  public void worksWithoutConfigRoot () {
    JsonFlatLookup subject = new JsonFlatLookup ("json/lookup.json", null);

    assertEquals ("goober", subject.valueFromName("glorf",
      JsonFlatLookupTest.class));
  }

  @Test
  public void throwsExceptionWhenConfigRootCantBeFound () {
    InputStream istr = getClass ().getClassLoader ().getResourceAsStream ("json/lookup.json");
    try {
      new JsonFlatLookup (istr, "flintstone");
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("Could not find config root 'flintstone' in JSON structure", e.getMessage ());
    }
  }
}
