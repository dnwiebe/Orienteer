package org.dnwiebe.orienteer.helpers;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/24/17.
 */
public class FragmenterTest {

  private Fragmenter subject;

  @Before
  public void setup () {
    subject = new Fragmenter();
  }

  @Test
  public void fragmenterCanHandleNoCaps () {

    List<String> result = subject.fragment ("stringwithnocapitals");

    assertEquals (Arrays.asList ("stringwithnocapitals"), result);
  }

  @Test
  public void fragmenterCanHandleAllCaps () {

    List<String> result = subject.fragment ("STRINGWITHALLCAPITALS");

    assertEquals (Arrays.asList ("STRINGWITHALLCAPITALS"), result);
  }

  @Test
  public void fragmenterBreaksOnLowerToUpperTransitions () {

    List<String> result = subject.fragment ("oneTwoTHREE");

    assertEquals (Arrays.asList ("one", "Two", "THREE"), result);
  }

  @Test
  public void fragmenterHandlesAllCapsSubstrings () {

    List<String> result = subject.fragment ("oneTWOThree");

    assertEquals (Arrays.asList ("one", "TWO", "Three"), result);
  }

  @Test
  public void fragmenterSeparatesOutDigits () {

    List<String> result = subject.fragment ("one1two2three3");

    assertEquals (Arrays.asList ("one", "1", "two", "2", "three", "3"), result);
  }

  @Test
  public void fragmenterHandlesDigitsAndAllCaps () {

    List<String> result = subject.fragment ("ABCDennisXY123");

    assertEquals (Arrays.asList ("ABC", "Dennis", "XY", "123"), result);
  }

  @Test
  public void forever21Website () {

    List<String> result = subject.fragment ("forever21Website");

    assertEquals (Arrays.asList ("forever", "21", "Website"), result);
  }
}
