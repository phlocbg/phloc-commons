package com.phloc.commons.tree.utils.sort;

import com.phloc.commons.tree.simple.DefaultTreeItem;

/**
 * Comparator for sorting {@link DefaultTreeItem} items by their value using an
 * comparable value types.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        tree item value type
 */
public final class ComparatorDefaultTreeItemComparable <VALUETYPE extends Comparable <? super VALUETYPE>> extends
                                                                                                          ComparatorTreeItemValueComparable <VALUETYPE, DefaultTreeItem <VALUETYPE>>
{
  public ComparatorDefaultTreeItemComparable ()
  {
    super ();
  }
}
