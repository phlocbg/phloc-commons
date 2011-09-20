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

import javax.annotation.Nonnull;

import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.tree.withid.ITreeItemWithID;

public class ComparatorTreeItemKeyComparable <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> extends
                                                                                                                                                                        AbstractComparator <ITEMTYPE>
{
  @Override
  protected int mainCompare (@Nonnull final ITEMTYPE aItem1, @Nonnull final ITEMTYPE aItem2)
  {
    return CompareUtils.nullSafeCompare (aItem1.getID (), aItem2.getID ());
  }
}
