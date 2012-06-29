package com.phloc.commons.tree.utils.sort;

import java.util.Comparator;

import javax.annotation.Nonnull;

import com.phloc.commons.tree.withid.DefaultTreeItemWithID;

/**
 * Comparator for sorting {@link DefaultTreeItemWithID} items by their value
 * using an explicit {@link Comparator}.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        tree item value type
 */
public final class ComparatorDefaultTreeItemWithIDValue <VALUETYPE> extends
                                                                    ComparatorTreeItemValue <VALUETYPE, DefaultTreeItemWithID <?, VALUETYPE>>
{
  public ComparatorDefaultTreeItemWithIDValue (@Nonnull final Comparator <? super VALUETYPE> aValueComparator)
  {
    super (aValueComparator);
  }
}
