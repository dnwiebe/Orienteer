package org.dnwiebe.orienteer.lookups;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dnwiebe on 2/24/17.
 */
public class JsonNestingLookupTest {
  private JsonNestingLookup subject;

  @Before
  public void setup () {
    subject = new JsonNestingLookup (istr (), "configNesting");
  }

  @Test
  public void translatesNamesCorrectly () {
    assertEquals ("first.array[0]", subject.nameFromFragments (Arrays.asList ("first", "Array", "0")));
    assertEquals ("first.array[1].second", subject.nameFromFragments (Arrays.asList ("first", "Array", "1", "Second")));
    assertEquals ("first.array[2]", subject.nameFromFragments (Arrays.asList ("first", "Array", "2")));
    assertEquals ("first.object.first", subject.nameFromFragments (Arrays.asList("first", "Object", "First")));
    assertEquals ("second", subject.nameFromFragments (Arrays.asList("second")));
  }

  @Test
  public void locatesValuesCorrectly () {
    assertEquals("first", subject.valueFromName("first.array[0]", JsonNestingLookupTest.class));
    assertEquals("secondValue", subject.valueFromName("first.array[1].second", JsonNestingLookupTest.class));
    assertEquals("3", subject.valueFromName("first.array[2]", JsonNestingLookupTest.class));
    assertEquals("4.75", subject.valueFromName("first.object.first", JsonNestingLookupTest.class));
    assertEquals("true", subject.valueFromName("second", JsonNestingLookupTest.class));
  }

  @Test
  public void returnsNullWhenNamesAreWrongOrValuesNonexistent () {
    assertEquals (null, subject.valueFromName ("first.array[3]", JsonNestingLookupTest.class));
    assertEquals (null, subject.valueFromName ("first.array[booga]", JsonNestingLookupTest.class));
    assertEquals (null, subject.valueFromName ("first.array[]", JsonNestingLookupTest.class));
    assertEquals (null, subject.valueFromName (".array[booga]", JsonNestingLookupTest.class));
    assertEquals (null, subject.valueFromName ("[4]", JsonNestingLookupTest.class));
    assertEquals (null, subject.valueFromName ("]}.gorb", JsonNestingLookupTest.class));
  }

  @Test
  public void worksWithReader () {
    Reader rdr = new InputStreamReader (istr ());
    JsonNestingLookup subject = new JsonNestingLookup (rdr, null);

    assertEquals ("first", subject.valueFromName("configNesting.first.array[0]",
      JsonNestingLookupTest.class));
  }

  @Test
  public void worksWithResourceString () {
    JsonNestingLookup subject = new JsonNestingLookup ("json/lookup.json", null);

    assertEquals ("first", subject.valueFromName("configNesting.first.array[0]",
      JsonNestingLookupTest.class));
  }

  @Test
  public void throwsExceptionWhenConfigRootCantBeFound () {
    try {
      new JsonNestingLookup (istr (), "flintstone");
      fail ();
    }
    catch (IllegalArgumentException e) {
      assertEquals ("Could not find config root 'flintstone' in JSON structure", e.getMessage ());
    }
  }

  @Test
  public void nameViaConstructors () {
    assertEquals (JsonNestingLookup.class.getName (), new JsonNestingLookup (rdr (), null).getName ());
    assertEquals (JsonNestingLookup.class.getName (), new JsonNestingLookup (istr (), null).getName ());
    assertEquals (JsonNestingLookup.class.getName (), new JsonNestingLookup ("json/lookup.json", null).getName ());
    assertEquals ("booga", new JsonNestingLookup ("booga", rdr (), null).getName ());
    assertEquals ("booga", new JsonNestingLookup ("booga", istr (), null).getName ());
    assertEquals ("booga", new JsonNestingLookup ("booga", "json/lookup.json", null).getName ());
  }

  private InputStream istr () {
    return getClass ().getClassLoader ().getResourceAsStream ("json/lookup.json");
  }

  private Reader rdr () {
    return new InputStreamReader (istr ());
  }
}
