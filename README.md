#Orienteer
_An Opinionated, Consistent Multilayer Configuration Library_
_For JVM Languages_

Current version: 0.1

###The Need for Configuration
Any application of significant size needs to be configured,
before it can run, with various values; perhaps a thread pool
size, or the base URL of a web service, or information about
whether various feature toggles should be on or off.

These configuration values will need to be different on a
developer's workstation than they will be in the testing or
QA region, and certainly different from what they'll be in
production.

Not only do the values themselves need to be different in
different environments; but they may also come from different
sources in different environments.

Perhaps you want the
application to use a particular stub web service whenever
it's run by a developer, unless the developer sets an
environment variable designating a different service for
testing purposes; and when the application is in production
it will expect to draw most of its configuration from a
properties file.

###Orienteer Can Help
Orienteer is designed for this situation.

With Orienteer, you can define a Java interface (or a Scala
trait, if you prefer) that is full of methods that provide
configuration values of various types.  For example:

```java
public interface ConfigurationSingleton {
  Integer threadPoolSize ();
  String weatherServiceBaseUrl ();
  Boolean tagDirectoryFeatureOn ();
  Boolean userProfileFeatureOn ();
}
```

Orienteer will provide you with an implementation of that
interface that pulls configuration information from wherever
you tell it to, according to whatever priorities you set.

For example, suppose you have a few defaults in a Map, but
you'd like to be able to override those with a properties file,
but you'd want to be able to specify environment variables to
override those, but system properties specified at execution
time should trump everything.

Finally, suppose you wanted to make sure that your application
crashed at bootstrap (with an appropriate message, of course)
if any of the configuration values you needed weren't
available from any of the sources you specified.

You'd do that like this:

```java
ConfigurationSingleton singleton = new Orienteer ().make (ConfigurationSingleton.class,
  new SystemPropsLookup (),
  new EnvironmentLookup (),
  new PropertiesLookup ("config/props/config.properties")),
  new MapLookup (defaults),
  new FailingLookup ()
);
```

Now you can use your ```singleton``` object to get the thread
pool size, the weather service base URL, and the feature toggle
states whenever you want, and each query will consult the
various possible configuration sources in the order they were
specified. If the search arrives at the FailingLookup, an
exception with an appropriate message will be thrown.

The moment the ```singleton``` is constructed, before you get
a chance to see it, Orienteer will automatically try to 
retrieve every field on it; if any of them can't be found
in any source, you'll get that exception immediately.

###Orienteer Is Opinionated
Inconsistent naming is one of the bugaboos of configuration
that both developers and operations personnel have had to
put up with for years.  For example, maybe in the properties
file the base URL was called ```weather.service.base.URL```
but if you wanted to put the value in an environment variable
it had to be ```WEATH_SVC_URL```.

Orienteer makes that sort of inconsistency much more difficult
(although it can be done, if you insist, which we hope you
won't).  Each configuration source (or _Lookup_) that Orienteer 
knows about defines its own naming conventions, and applies
them based on the names of the configuration properties in
the singleton interface for which Orienteer provides the
implementation.

For example, the method name above is ```weatherServiceBaseUrl```.
That means that the property name will have to be ```weather.service.base.url``` 
and the environment variable
will have to be ```WEATHER_SERVICE_BASE_URL``` or else their
values won't be used.  (In case you're curious, in the map
of defaults, the key would have to be ```WeatherServiceBaseUrl```.)

###Orienteer Is Extensible
Orienteer has two major limitations.

First, it can only
provide configuration values in a certain small number of
types: String, Integer, Long, Double, and Boolean. (Note
that these are all boxed object types, not primitive types,
because if a value can't be found it needs to be returned
as ```null```.)

Second, it can only retrieve configuration values from a
certain small number of sources (default maps, environment
variables, property files, and so on).

However, it is quite simple to provide both your own
configuration types and your own Lookups.  See the ```CustomDataTypeExample.java```
and the ```CustomLookupExample.java``` files in 
the ```com.dnwiebe.orienteer.examples``` package for example
code.
