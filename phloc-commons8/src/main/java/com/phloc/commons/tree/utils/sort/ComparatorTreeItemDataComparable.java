/**
 * Copyright (C) 2006-2014 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.commons.tree.utils.sort;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.AbstractPartComparatorComparable;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.tree.IBasicTreeItem;

/**
 * Comparator for sorting {@link IBasicTreeItem} items by their value using an
 * comparable value types.<br>
 * Works for {@link com.phloc.commons.tree.simple.ITreeItem} and
 * {@link com.phloc.commons.tree.withid.ITreeItemWithID}.
 *
 * @author Philip Helger
 * @param <DATATYPE>
 *        tree item value type
 * @param <ITEMTYPE>
 *        tree item implementation type
 */
@Deprecated
public class ComparatorTreeItemDataComparable <DATATYPE extends Comparable <? super DATATYPE>, ITEMTYPE extends IBasicTreeItem <DATATYPE, ITEMTYPE>> extends AbstractPartComparatorComparable <ITEMTYPE, DATATYPE>
{
  /**
   * Comparator with default sort order.
   */
  public ComparatorTreeItemDataComparable ()
  {
    super ();
  }

  /**
   * Constructor with sort order.
   *
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public ComparatorTreeItemDataComparable (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  @Override
  @Nullable
  protected DATATYPE getPart (@Nonnull final ITEMTYPE aTreeItem)
  {
    return aTreeItem.getData ();
  }
}
