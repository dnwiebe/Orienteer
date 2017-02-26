package org.dnwiebe.orienteer.examples;

import org.dnwiebe.orienteer.Orienteer;
import org.dnwiebe.orienteer.helpers.MapFragmenter;
import org.dnwiebe.orienteer.lookups.JsonNestingLookup;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by dnwiebe on 2/25/17.
 */
public class CustomFragmenterExample {

  public interface ConfigurationSingleton {
    String kohlsWebsite ();
    String forever21Website ();
  }

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

  @Test
  public void standardFragmenterWontWork () {
    Reader rdr = new StringReader (CONFIG_JSON);
    ConfigurationSingleton singleton = new Orienteer ()
      .make (ConfigurationSingleton.class, new JsonNestingLookup (rdr, "configTree"));

    String kohls = singleton.kohlsWebsite ();
    String forever21 = singleton.forever21Website ();

    assertEquals ("https://www.kohls.com", kohls);
    assertEquals (null, forever21);
  }

  @Test
  public void soUseCustomFragmenterWhereNecessary () {
    Reader rdr = new StringReader (CONFIG_JSON);
    ConfigurationSingleton singleton = new Orienteer ()
      .addFragmenter (new MapFragmenter ("forever21Website", Arrays.asList ("forever21", "Website")))
      .make (ConfigurationSingleton.class, new JsonNestingLookup (rdr, "configTree"));

    String kohls = singleton.kohlsWebsite ();
    String forever21 = singleton.forever21Website ();

    assertEquals ("https://www.kohls.com", kohls);
    assertEquals ("http://www.forever21.com", forever21);
  }
}
