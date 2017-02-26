package org.dnwiebe.orienteer.examples;

import org.dnwiebe.orienteer.Orienteer;
import org.dnwiebe.orienteer.helpers.Fragments;
import org.dnwiebe.orienteer.lookups.JsonNestingLookup;
import org.dnwiebe.orienteer.lookups.Lookup;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/25/17.
 */
public class CustomFragmenterExample {

  private static final String CONFIG_JSON =
    "{\n" +
    "  \"configTree\": {\n" +
    "    \"kohls\": {\n" +
    "      \"website\": \"https://www.kohls.com\"\n" +
    "    },\n" +
    "    \"forever21\": {\n" +
    "      \"website\": \"http://www.forever21.com\"\n" +
    "    }\n" +
    "  }\n" +
    "}\n";

  public interface ConfigurationSingletonWithoutAnnotation {
    String kohlsWebsite ();
    String forever21Website ();
  }

  @Test
  public void standardFragmentationDoesntAlwaysProduceWhatYouWant () {
    Reader rdr = new StringReader (CONFIG_JSON);
    Lookup lookup = new JsonNestingLookup (rdr, "configTree");
    ConfigurationSingletonWithoutAnnotation singleton = new Orienteer ()
      .make (ConfigurationSingletonWithoutAnnotation.class, lookup);

    String kohls = singleton.kohlsWebsite ();
    String forever21 = singleton.forever21Website ();

    assertEquals ("https://www.kohls.com", kohls);
    assertEquals (null, forever21);
    assertEquals ("forever[21].website", lookup.nameFromFragments (Arrays.asList ("forever", "21", "Website")));
  }

  public interface ConfigurationSingletonWithAnnotation {
    String kohlsWebsite ();
    @Fragments ({"forever21", "Website"})
    String forever21Website ();
  }

  @Test
  public void usingFragmentsAnnotationInConfigurationSingletonOverridesStandardFragmenter () {
    Reader rdr = new StringReader (CONFIG_JSON);
    Lookup lookup = new JsonNestingLookup (rdr, "configTree");
    ConfigurationSingletonWithAnnotation singleton = new Orienteer ()
      .make (ConfigurationSingletonWithAnnotation.class, lookup);

    String kohls = singleton.kohlsWebsite ();
    String forever21 = singleton.forever21Website ();

    assertEquals ("https://www.kohls.com", kohls);
    assertEquals ("http://www.forever21.com", forever21);
    assertEquals ("forever21.website", lookup.nameFromFragments (Arrays.asList ("forever21", "Website")));
  }
}
