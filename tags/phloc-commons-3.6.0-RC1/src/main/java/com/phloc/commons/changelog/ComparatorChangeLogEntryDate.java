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
package com.phloc.commons.changelog;

import javax.annotation.Nonnull;

import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.ESortOrder;

/**
 * Special comparator to sort change log entries by their date and in case of
 * equality by the parent change logs component name.
 * 
 * @author philip
 */
public final class ComparatorChangeLogEntryDate extends AbstractComparator <ChangeLogEntry>
{
  public ComparatorChangeLogEntryDate ()
  {
    this (ESortOrder.DEFAULT);
  }

  public ComparatorChangeLogEntryDate (@Nonnull final ESortOrder eSortOder)
  {
    super (eSortOder);
  }

  @Override
  protected int mainCompare (@Nonnull final ChangeLogEntry aEntry1, @Nonnull final ChangeLogEntry aEntry2)
  {
    final long n1 = aEntry1.getDate ().getTime ();
    final long n2 = aEntry2.getDate ().getTime ();
    int i = n1 < n2 ? -1 : n1 == n2 ? 0 : +1;
    if (i == 0)
      i = aEntry1.getChangeLog ().getComponent ().compareTo (aEntry2.getChangeLog ().getComponent ());
    return i;
  }
}