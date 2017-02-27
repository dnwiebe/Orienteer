package org.dnwiebe.orienteer.lookups;

import org.junit.Test;

import java.util.List;

import static org.dnwiebe.orienteer.helpers.Joiner.capitalize;
import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class LookupTest {

  private static class TameLookup extends Lookup {

    public String nameFromFragments(List<String> fragments) {
      throw new UnsupportedOperationException();
    }

    public String valueFromName(String name, Class singletonType) {
      throw new UnsupportedOperationException();
    }
  }

  @Test
  public void defaultNameIsNameOfClass () {
    Lookup subject = new TameLookup ();

    String result = subject.getName ();

    assertEquals (TameLookup.class.getName (), result);
  }

  @Test
  public void capitalizeWorksOnRegularString () {
    TameLookup subject = new TameLookup ();

    String result = capitalize ("wIBbLE");

    assertEquals ("WIBbLE", result);
  }

  @Test
  public void capitalizeWorksOnAlreadyCapitalizedString () {
    TameLookup subject = new TameLookup ();

    String result = capitalize ("Wibble");

    assertEquals ("Wibble", result);
  }

  @Test
  public void capitalizeWorksOnEmptyString () {
    TameLookup subject = new TameLookup ();

    String result = capitalize ("");

    assertEquals ("", result);
  }

  @Test
  public void capitalizeWorksOnNullString () {
    TameLookup subject = new TameLookup ();

    String result = capitalize (null);

    assertEquals (null, result);
  }
}
