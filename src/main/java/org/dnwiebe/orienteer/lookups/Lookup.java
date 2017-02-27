package org.dnwiebe.orienteer.lookups;

import java.util.List;

/**
 * Created by dnwiebe on 2/17/17.
 */

/**
 * Looks up configuration data from a particular source.
 */
public abstract class Lookup {

  private String name;

  protected Lookup () {
    this.name = getClass ().getName ();
  }

  protected Lookup (String name) {
    this.name = name;
  }

  /**
   * Provides a name for this Lookup, mostly for use in system logs. Defaults to the fully-qualified name of the class.
   * @return Name for Lookup.
   */
  public String getName () {
    return name;
  }

  /**
   * Assemble an attribute name that conforms to this Lookup's naming scheme from the provided list of name fragments.
   * This is where the naming scheme for a Lookup is defined.
   * @param fragments This will be the camel-cased name of the method from your configuration interface, broken apart
   *                  into fragments. For example, "threadPoolSize" will become {"thread", "Pool", "Size"} and
   *                  "weatherServiceBaseURL" will become {"weather", "Service", "Base", "URL"}.
   * @return Schemed name for this Lookup. For example, "thread.pool.size" or "THREAD_POOL_SIZE" or "ThreadPoolSize"
   *          or whatever you decide is appropriate.
   */
  public abstract String nameFromFragments (List<String> fragments);

  /**
   * Perform the actual lookup characteristic of this Lookup.
   * @param name The name of the value to look up, in this Lookup's naming scheme.  For example, if the configuration
   *             interface's method name is "threadPoolSize" and nameFromFragments converts that to "thread.pool.size,"
   *             then this method will receive "thread.pool.size" as the name to look up.
   * @param singletonType This is the configuration interface that's being satisfied.  Mostly you'll ignore this
   *                      parameter, but you might find it useful for composing error messages or logging.
   * @return The String value (always a String value) of the attribute being sought, or null if not available.
   */
  public abstract String valueFromName(String name, Class singletonType);
//
//  /**
//   * Capitalizes the first character of a String, leaves the rest alone. Perhaps useful as a utility.
//   * @param string String to capitalize
//   * @return Capitalized String
//   */
//  protected static String capitalize (String string) {
//    if ((string == null) || (string.length () == 0)) {return string;}
//    return Character.toUpperCase (string.charAt (0)) + string.substring (1);
//  }
//
//  protected interface Mapper {
//    String apply (String i);
//  }
//
//  protected static final Mapper NULL_MAPPER = new Mapper () {public String apply (String s) {return s;}};
//  protected static final Mapper LC_MAPPER = new Mapper () {public String apply (String s) {return s.toLowerCase ();}};
//  protected static final Mapper UC_MAPPER = new Mapper () {public String apply (String s) {return s.toUpperCase ();}};
//  protected static final Mapper CAP_MAPPER = new Mapper () {public String apply (String s) {
//    return capitalize (s.toLowerCase ());}
//  };
//
//  protected static String join (List<String> fragments, String delimiter, Mapper mapper) {
//    StringBuilder buf = new StringBuilder ();
//    for (String fragment: fragments) {
//      if (buf.length () > 0) {buf.append (delimiter);}
//      buf.append (mapper.apply (fragment));
//    }
//    return buf.toString ();
//  }
}
