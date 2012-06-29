package com.phloc.commons.tree.utils.sort;

import com.phloc.commons.tree.withid.DefaultTreeItemWithID;

/**
 * Comparator for sorting {@link DefaultTreeItemWithID} items by their value
 * using an comparable value types.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        tree item value type
 */
public final class ComparatorDefaultTreeItemWithIDValueComparable <VALUETYPE extends Comparable <? super VALUETYPE>> extends
                                                                                                                     ComparatorTreeItemValueComparable <VALUETYPE, DefaultTreeItemWithID <?, VALUETYPE>>
{
  public ComparatorDefaultTreeItemWithIDValueComparable ()
  {
    super ();
  }
}
