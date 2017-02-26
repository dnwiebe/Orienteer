package org.dnwiebe.orienteer.helpers;

import org.dnwiebe.orienteer.AuxiliaryFragmenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dnwiebe on 2/25/17.
 */
public class MapFragmenter implements AuxiliaryFragmenter {

  private Map<String, List<String>> map;

  public MapFragmenter (Object... namesAndLists) {
    Map<String, List> untyped = MapLiteral.convert (String.class, List.class, namesAndLists);
    map = new HashMap<String, List<String>> ();
    for (Map.Entry<String, List> entry: untyped.entrySet ()) {
      map.put (entry.getKey (), entry.getValue ());
    }
  }

  public List<String> fragment (String name) {
    return map.get (name);
  }
}
