/**
 * Copyright (C) 2006-2012 phloc systems
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

import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.tree.IBasicTreeItem;
import com.phloc.commons.tree.simple.ITreeItem;
import com.phloc.commons.tree.withid.ITreeItemWithID;

/**
 * Comparator for sorting {@link IBasicTreeItem} items by their value using an
 * explicit {@link Comparator}.<br>
 * Works for {@link ITreeItem} and {@link ITreeItemWithID}.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        tree item value type
 * @param <ITEMTYPE>
 *        tree item implementation type
 */
public class ComparatorTreeItemValue <VALUETYPE, ITEMTYPE extends IBasicTreeItem <VALUETYPE, ITEMTYPE>> extends
                                                                                                        AbstractComparator <ITEMTYPE>
{
  private final Comparator <? super VALUETYPE> m_aValueComparator;

  public ComparatorTreeItemValue (@Nonnull final Comparator <? super VALUETYPE> aValueComparator)
  {
    if (aValueComparator == null)
      throw new NullPointerException ("valueComparator");
    m_aValueComparator = aValueComparator;
  }

  @Override
  protected final int mainCompare (@Nonnull final ITEMTYPE aTreeItem1, @Nonnull final ITEMTYPE aTreeItem2)
  {
    return CompareUtils.nullSafeCompare (aTreeItem1.getData (), aTreeItem1.getData (), m_aValueComparator);
  }
}
