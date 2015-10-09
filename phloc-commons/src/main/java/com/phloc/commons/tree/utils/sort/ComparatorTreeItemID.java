/**
 * Copyright (C) 2006-2015 phloc systems
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

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.id.ComparatorHasID;
import com.phloc.commons.tree.withid.ITreeItemWithID;

/**
 * Comparator for sorting {@link com.phloc.commons.tree.withid.ITreeItemWithID}
 * items by their ID using an explicit {@link Comparator}.
 * 
 * @author Philip Helger
 * @param <KEYTYPE>
 *        tree item key type
 * @param <DATATYPE>
 *        tree item value type
 * @param <ITEMTYPE>
 *        tree item implementation type
 */
public class ComparatorTreeItemID <KEYTYPE, DATATYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, DATATYPE, ITEMTYPE>> extends ComparatorHasID <KEYTYPE, ITEMTYPE>
{
  /**
   * Comparator with default sort order and no nested comparator.
   * 
   * @param aIDComparator
   *        The comparator for comparing the IDs. May not be <code>null</code>.
   */
  public ComparatorTreeItemID (@Nonnull final Comparator <? super KEYTYPE> aIDComparator)
  {
    super (aIDComparator);
  }

  /**
   * Constructor with sort order and no nested comparator.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   * @param aIDComparator
   *        The comparator for comparing the IDs. May not be <code>null</code>.
   */
  public ComparatorTreeItemID (@Nonnull final ESortOrder eSortOrder,
                               @Nonnull final Comparator <? super KEYTYPE> aIDComparator)
  {
    super (eSortOrder, aIDComparator);
  }

  /**
   * Comparator with default sort order and a nested comparator.
   * 
   * @param aNestedComparator
   *        The nested comparator to be invoked, when the main comparison
   *        resulted in 0.
   * @param aIDComparator
   *        The comparator for comparing the IDs. May not be <code>null</code>.
   */
  public ComparatorTreeItemID (@Nullable final Comparator <? super ITEMTYPE> aNestedComparator,
                               @Nonnull final Comparator <? super KEYTYPE> aIDComparator)
  {
    super (aNestedComparator, aIDComparator);
  }

  /**
   * Constructor with sort order and a nested comparator.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   * @param aNestedComparator
   *        The nested comparator to be invoked, when the main comparison
   *        resulted in 0.
   * @param aIDComparator
   *        The comparator for comparing the IDs. May not be <code>null</code>.
   */
  public ComparatorTreeItemID (@Nonnull final ESortOrder eSortOrder,
                               @Nullable final Comparator <? super ITEMTYPE> aNestedComparator,
                               @Nonnull final Comparator <? super KEYTYPE> aIDComparator)
  {
    super (eSortOrder, aNestedComparator, aIDComparator);
  }
}
