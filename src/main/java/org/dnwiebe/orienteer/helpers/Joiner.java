package org.dnwiebe.orienteer.helpers;

import java.util.List;

/**
 * Created by dnwiebe on 2/27/17.
 */
public class Joiner {

  public static String capitalize (String string) {
    if ((string == null) || (string.length () == 0)) {return string;}
    return Character.toUpperCase (string.charAt (0)) + string.substring (1);
  }

  public interface Mapper<T> {
    String apply (T i);
  }

  public static final Mapper NULL_MAPPER = new Mapper<String> () {public String apply (String s) {return s;}};
  public static final Mapper LC_MAPPER = new Mapper<String> () {public String apply (String s) {return s.toLowerCase ();}};
  public static final Mapper UC_MAPPER = new Mapper<String> () {public String apply (String s) {return s.toUpperCase ();}};
  public static final Mapper CAP_MAPPER = new Mapper<String> () {public String apply (String s) {
    return capitalize (s.toLowerCase ());}
  };
  public static final Mapper CLASS_MAPPER = new Mapper<Class> () {public String apply (Class cls) {return cls.getName ();}};

  public static<T> String join (Iterable<T> elements, String delimiter, Mapper<T> mapper) {
    StringBuilder buf = new StringBuilder ();
    for (T element: elements) {
      if (buf.length () > 0) {buf.append (delimiter);}
      buf.append (mapper.apply (element));
    }
    return buf.toString ();
  }

  public static String join (Iterable<String> elements, String delimiter) {
    return join (elements, delimiter, NULL_MAPPER);
  }
}
