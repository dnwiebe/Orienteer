package com.dnwiebe.orienteer;

import com.dnwiebe.orienteer.converters.Converter;
import com.dnwiebe.orienteer.converters.Converters;
import com.dnwiebe.orienteer.lookups.Lookup;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class Orienteer {
  Converters converters = new Converters ();
  Fragmenter fragmenter = new Fragmenter ();
  Logger logger = Logger.getLogger (Orienteer.class.getName ());

  public <T> T make (Class<T> singletonInterface, Lookup... lookups) {
    validateInterface (singletonInterface);
    return (T)Proxy.newProxyInstance (
        Orienteer.class.getClassLoader(),
        new Class[] {singletonInterface},
        new Handler (lookups)
    );
  }

  private void validateInterface (Class singletonInterface) {
    validateClassObject (singletonInterface);
    Method[] methods = singletonInterface.getMethods ();
    for (Method method : methods) {
      validateReturnType (method);
      validateParameters (method);
    }
  }

  private void validateClassObject (Class singletonInterface) {
    if (!singletonInterface.isInterface ()) {
      throw new IllegalArgumentException ("Configuration singletons must be built on interfaces, not classes like " +
          singletonInterface.getName () + ".");
    }
  }

  private void validateReturnType (Method method) {
    if (!converters.getTargetTypes ().contains (method.getReturnType())) {
      StringBuilder buf = new StringBuilder();
      for (Class type : converters.getTargetTypes()) {
        if (buf.length() > 0) {
          buf.append(", ");
        }
        buf.append(type.getName());
      }
      throw new IllegalArgumentException("The " + method.getName() + " method of the " +
          method.getDeclaringClass().getName() +
          " interface returns type " + method.getReturnType ().getName () +
          ". Methods on configuration interfaces must return one of the following types: " + buf.toString ()
      );
    }
  }

  private void validateParameters (Method method) {
    if (method.getParameterTypes ().length > 0) {
      throw new IllegalArgumentException("The " + method.getName() + " method of the " +
          method.getDeclaringClass().getName() +
          " interface takes a parameter. Methods on configuration interfaces must be parameterless."
      );
    }
  }

  private class Handler implements InvocationHandler {
    private Lookup[] lookups;

    Handler (Lookup[] lookups) {
      this.lookups = new Lookup[lookups.length];
      for (int i = 0; i < lookups.length; i++) {this.lookups[i] = lookups[i];}
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      logger.info ("Seeking configuration value for '" + method.getName () + "'");
      List<String> fragments = fragmenter.fragment (method.getName ());
      for (Lookup lookup : lookups) {
        String name = lookup.nameFromFragments (fragments);
        String value = lookup.valueFromName (name, method.getDeclaringClass ());
        Object result = processValue (method, lookup, name, value);
        if (result != null) {return result;}
      }
      logger.warning ("NOT CONFIGURED: " + method.getDeclaringClass ().getName () + "." + method.getName () + "()");
      return null;
    }

    private Object processValue (Method method, Lookup lookup, String name, String value) {
      String logPreamble = "  Consulting " + lookup.getName () + " for '" + name + "': ";
      if (value == null) {
        logger.info (logPreamble + "not found");
        return null;
      }
      else {
        Converter<?> converter = converters.find (method.getReturnType (), lookup.getClass ());
        Object result = converter.convert (value);
        logger.info (logPreamble + "found '" + result + "'");
        logger.info ("Configured: " + method.getDeclaringClass().getName () + "." + method.getName () +
            "() -> " + result);
        return result;
      }
    }
  }

  static class Fragmenter {

    List<String> fragment (String name) {
      FragmentationState state = new FragmentationState();
      for (int i = 0; i < name.length (); i++) {
        state.acceptCharacter(name.charAt (i));
      }
      state.finish();
      return state.getResult ();
    }
  }

  static private class FragmentationState {
    private int accumulatedUppers = 1;
    private StringBuilder wordSoFar = new StringBuilder ();
    private List<String> result = new ArrayList<String> ();

    public void acceptCharacter(char c) {
      if (Character.isUpperCase (c)) {
        processUpperCaseCharacter(c);
      }
      else {
        processLowerCaseCharacter(c);
      }
    }

    public void finish() {
      result.add (wordSoFar.toString ());
    }

    public List<String> getResult () {
      return result;
    }

    private void processLowerCaseCharacter(char c) {
      if (accumulatedUppers > 1) {
        char firstOfThisWord = wordSoFar.charAt (wordSoFar.length () - 1);
        wordSoFar.setLength (wordSoFar.length () - 1);
        result.add (wordSoFar.toString ());
        wordSoFar = new StringBuilder ();
        wordSoFar.append (firstOfThisWord);
      }
      wordSoFar.append(c);
      accumulatedUppers = 0;
    }

    private void processUpperCaseCharacter(char c) {
      if (accumulatedUppers == 0) {
        result.add (wordSoFar.toString ());
        wordSoFar = new StringBuilder ();
      }
      wordSoFar.append (c);
      accumulatedUppers++;
    }
  }
}
