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
package com.phloc.commons.changelog;

import javax.annotation.Nonnull;

import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.ESortOrder;

/**
 * Special comparator to sort change logs by their component.
 * 
 * @author philip
 */
public final class ComparatorChangeLogComponent extends AbstractComparator <ChangeLog>
{
  public ComparatorChangeLogComponent ()
  {
    this (ESortOrder.DEFAULT);
  }

  public ComparatorChangeLogComponent (@Nonnull final ESortOrder eSortOder)
  {
    super (eSortOder);
  }

  @Override
  protected int mainCompare (final ChangeLog aChangeLog1, final ChangeLog aChangeLog2)
  {
    return aChangeLog1.getComponent ().compareTo (aChangeLog2.getComponent ());
  }
}