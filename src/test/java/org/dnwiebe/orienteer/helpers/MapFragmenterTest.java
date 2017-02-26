package org.dnwiebe.orienteer.helpers;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/25/17.
 */
public class MapFragmenterTest {

  private MapFragmenter subject;

  @Before
  public void setup () {
    subject = new MapFragmenter (
      "firstNameToFragment", Arrays.asList ("first", "second", "third"),
      "secondNameToFragment", Arrays.asList ("fourth", "fifth", "sixth")
    );
  }

  @Test
  public void fragmentsNamesAsExpected () {
    assertEquals (Arrays.asList ("first", "second", "third"), subject.fragment ("firstNameToFragment"));
    assertEquals (Arrays.asList ("fourth", "fifth", "sixth"), subject.fragment ("secondNameToFragment"));
  }

  @Test
  public void returnsNullForUnknownNames () {
    assertEquals (null, subject.fragment ("thirdNameToFragment"));
    assertEquals (null, subject.fragment ("fourthNameToFragment"));
  }
}
