package org.dnwiebe.orienteer.lookups;

import org.junit.Test;

import java.util.List;

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

    @Override
    public String capitalize (String string) {
      return super.capitalize (string);
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

    String result = subject.capitalize ("wIBbLE");

    assertEquals ("WIBbLE", result);
  }

  @Test
  public void capitalizeWorksOnAlreadyCapitalizedString () {
    TameLookup subject = new TameLookup ();

    String result = subject.capitalize ("Wibble");

    assertEquals ("Wibble", result);
  }

  @Test
  public void capitalizeWorksOnEmptyString () {
    TameLookup subject = new TameLookup ();

    String result = subject.capitalize ("");

    assertEquals ("", result);
  }

  @Test
  public void capitalizeWorksOnNullString () {
    TameLookup subject = new TameLookup ();

    String result = subject.capitalize (null);

    assertEquals (null, result);
  }
}
