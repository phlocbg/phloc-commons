/**
 * Copyright (C) 2006-2013 phloc systems
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

import com.phloc.commons.tree.simple.DefaultTreeItem;

/**
 * Comparator for sorting {@link DefaultTreeItem} items by their value using an
 * explicit {@link Comparator}.
 * 
 * @author philip
 * @param <DATATYPE>
 *        tree item value type
 */
public final class ComparatorDefaultTreeItem <DATATYPE> extends
                                                        ComparatorTreeItemValue <DATATYPE, DefaultTreeItem <DATATYPE>>
{
  public ComparatorDefaultTreeItem (@Nonnull final Comparator <? super DATATYPE> aValueComparator)
  {
    super (aValueComparator);
  }
}
