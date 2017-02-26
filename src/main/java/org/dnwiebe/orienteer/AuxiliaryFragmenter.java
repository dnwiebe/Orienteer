package org.dnwiebe.orienteer;

import java.util.List;

/**
 * Created by dnwiebe on 2/25/17.
 */

/**
 * If you don't like the way Orienteer's standard Fragmenter fragments method names, write an implementation of this
 * interface that does just what you want (first check org.dnwiebe.orienteer.helpers.MapFragmenter to make sure it
 * won't serve) and use Orienteer.addFragmenter() to deploy it.
 */
public interface AuxiliaryFragmenter {

  /**
   * Break a method name into a list of fragments for use by Lookups.
   * @param name Name to fragment
   * @return List of fragments, or null to transfer responsibility to another Fragmenter.
   */
  List<String> fragment(String name);
}
