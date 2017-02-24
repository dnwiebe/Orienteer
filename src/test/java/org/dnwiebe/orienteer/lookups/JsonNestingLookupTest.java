package org.dnwiebe.orienteer.lookups;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/24/17.
 */
public class JsonNestingLookupTest {
  private JsonNestingLookup subject;

  @Before
  public void setup () {
    String json =
        "{\n" +
        "	\"first\": {\n" +
        "		\"array\": [\n" +
        "			\"first\",\n" +
        "			{\n" +
        "				\"second\": \"secondValue\"\n" +
        "			},\n" +
        "			3\n" +
        "		],\n" +
        "		\"object\": {\n" +
        "			\"first\": 4.75\n" +
        "		}\n" +
        "	},\n" +
        "	\"second\": true\n" +
        "}\n";

    subject = new JsonNestingLookup (new ByteArrayInputStream(json.getBytes ()));
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
}
