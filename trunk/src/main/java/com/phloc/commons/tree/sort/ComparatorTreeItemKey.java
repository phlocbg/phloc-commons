/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.tree.sort;

import java.util.Comparator;

import javax.annotation.Nonnull;

import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.tree.withid.ITreeItemWithID;

public final class ComparatorTreeItemKey <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> extends
                                                                                                                               AbstractComparator <ITEMTYPE>
{
  private final Comparator <? super KEYTYPE> m_aKeyComparator;

  public ComparatorTreeItemKey (@Nonnull final Comparator <? super KEYTYPE> aKeyComparator)
  {
    if (aKeyComparator == null)
      throw new NullPointerException ("keyComparator");
    m_aKeyComparator = aKeyComparator;
  }

  @Override
  protected int mainCompare (@Nonnull final ITEMTYPE aTreeItem1, @Nonnull final ITEMTYPE aTreeItem2)
  {
    return m_aKeyComparator.compare (aTreeItem1.getID (), aTreeItem2.getID ());
  }
}
