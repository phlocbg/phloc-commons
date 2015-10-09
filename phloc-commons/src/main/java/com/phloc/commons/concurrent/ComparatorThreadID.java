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
package com.phloc.commons.concurrent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.CGlobal;
import com.phloc.commons.compare.AbstractIntegerComparator;
import com.phloc.commons.compare.ESortOrder;

/**
 * {@link java.util.Comparator} for ordering {@link Thread} objects by their ID.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class ComparatorThreadID extends AbstractIntegerComparator <Thread>
{
  public ComparatorThreadID ()
  {
    super ();
  }

  public ComparatorThreadID (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  @Override
  protected long asLong (@Nullable final Thread aThread)
  {
    return aThread == null ? CGlobal.ILLEGAL_ULONG : aThread.getId ();
  }
}
