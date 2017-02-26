package org.dnwiebe.orienteer.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dnwiebe on 2/25/17.
 */
public class MapLiteral {
  public static <K, V> Map<K, V> convert (Class<K> keyClass, Class<V> valueClass, Object... keysAndValues) {
    checkParameterCount (keysAndValues);
    StringBuilder keyErrors = new StringBuilder ();
    StringBuilder valueErrors = new StringBuilder ();
    Map<K, V> result = new HashMap<K, V> ();
    for (int i = 0; i < keysAndValues.length; i += 2) {
      boolean error = false;
      Object k = keysAndValues[i];
      if (!keyClass.isAssignableFrom (k.getClass ())) {
        appendDelimited (keyErrors, k.toString () + " (" + k.getClass ().getName () + ")");
        error = true;
      }
      Object v = keysAndValues[i + 1];
      if (!valueClass.isAssignableFrom (v.getClass ())) {
        appendDelimited (valueErrors, k.toString () + " -> " + v.toString () + " (" + v.getClass ().getName () + ")");
        error = true;
      }
      if (!error) {
        result.put ((K)k, (V)v);
      }
    }
    checkErrorBuffers (keyClass, keyErrors, valueClass, valueErrors);
    return result;
  }

  private static void appendDelimited (StringBuilder buf, String s) {
    if (buf.length () > 0) {buf.append (", ");}
    buf.append (s);
  }

  private static void checkParameterCount (Object[] keysAndValues) {
    if ((keysAndValues.length & 1) != 0) {
      throw new IllegalArgumentException ("Need value for every key, but found odd number (" + keysAndValues.length +
        ") of key-and-value parameters");
    }
  }

  private static void checkErrorBuffers (Class<?> keyClass, StringBuilder keyErrors, Class<?> valueClass, StringBuilder valueErrors) {
    StringBuilder buf = new StringBuilder ();
    if (keyErrors.length () > 0) {
      buf.append ("Cannot convert keys to type ").append (keyClass.getName ()).append (": ").append (keyErrors.toString ());
    }
    if (valueErrors.length () > 0) {
      if (buf.length () > 0) {buf.append ("\n");}
      buf.append ("Cannot convert values to type ").append (valueClass.getName ()).append (": ").append (valueErrors.toString ());
    }
    if (buf.length () > 0) {throw new IllegalArgumentException (buf.toString ());}
  }
}
