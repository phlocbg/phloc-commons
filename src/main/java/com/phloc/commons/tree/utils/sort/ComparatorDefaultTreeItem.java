package com.phloc.commons.tree.utils.sort;

import java.util.Comparator;

import javax.annotation.Nonnull;

import com.phloc.commons.tree.simple.DefaultTreeItem;

/**
 * Comparator for sorting {@link DefaultTreeItem} items by their value using an
 * explicit {@link Comparator}.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        tree item value type
 */
public final class ComparatorDefaultTreeItem <VALUETYPE> extends
                                                         ComparatorTreeItemValue <VALUETYPE, DefaultTreeItem <VALUETYPE>>
{
  public ComparatorDefaultTreeItem (@Nonnull final Comparator <? super VALUETYPE> aValueComparator)
  {
    super (aValueComparator);
  }
}
