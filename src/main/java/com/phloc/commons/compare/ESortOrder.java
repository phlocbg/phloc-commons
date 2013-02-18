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
package com.phloc.commons.compare;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Defines possible sort orders. The default is {@link #ASCENDING} sorting.
 * 
 * @author philip
 */
public enum ESortOrder implements ISortOrderIndicator
{
  ASCENDING (1),
  DESCENDING (0);

  /** The default sort order is {@link #ASCENDING} */
  public static final ESortOrder DEFAULT = ASCENDING;

  private final int m_nValue;

  private ESortOrder (@Nonnegative final int nValue)
  {
    m_nValue = nValue;
  }

  /**
   * @return The associated value for this sort order. The value can directly be
   *         used in compare methods to multiply the initial return value with
   *         this value to have the correct ordering.
   */
  @Nonnegative
  public int getValue ()
  {
    return m_nValue;
  }

  @Nonnull
  public String getValueAsString ()
  {
    return Integer.toString (m_nValue);
  }

  @Override
  public boolean isAscending ()
  {
    return this == ASCENDING;
  }

  @Override
  public boolean isDescending ()
  {
    return this == DESCENDING;
  }

  /**
   * Get the alternate sort order. If the current sort order is ascending, the
   * result will be descending and if this is descending, the return will be
   * ascending.
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  public ESortOrder getAlternate ()
  {
    return isAscending () ? DESCENDING : ASCENDING;
  }

  @Nonnull
  public static ESortOrder getFromValueOrDefault (final int nValue)
  {
    return getFromValue (nValue, DEFAULT);
  }

  @Nullable
  public static ESortOrder getFromValue (final int nValue, @Nullable final ESortOrder eDefault)
  {
    if (nValue == ASCENDING.m_nValue)
      return ASCENDING;
    if (nValue == DESCENDING.m_nValue)
      return DESCENDING;
    return eDefault;
  }
}
