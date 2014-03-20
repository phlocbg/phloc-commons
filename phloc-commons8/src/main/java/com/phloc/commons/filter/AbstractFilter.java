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
package com.phloc.commons.filter;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * An abstract implementation of {@link IFilter} that has an optional nested
 * filter.
 *
 * @author Philip Helger
 * @param <DATATYPE>
 *        The type of object to filter.
 */
@NotThreadSafe
@Deprecated
public abstract class AbstractFilter <DATATYPE> implements IFilter <DATATYPE>
{
  private final IFilter <DATATYPE> m_aNestedFilter;

  public AbstractFilter ()
  {
    this (null);
  }

  public AbstractFilter (@Nullable final IFilter <DATATYPE> aCustomFilter)
  {
    m_aNestedFilter = aCustomFilter;
  }

  /**
   * This is the method to be implemented to match this filter.
   *
   * @param aValue
   *        The value to be matched
   * @return <code>true</code> if the value matches the filter
   */
  protected abstract boolean matchesThisFilter (final DATATYPE aValue);

  public final boolean matchesFilter (final DATATYPE aValue)
  {
    if (matchesThisFilter (aValue))
      return true;

    // Check nested filter
    return m_aNestedFilter == null || m_aNestedFilter.matchesFilter (aValue);
  }

  /**
   * @return The nested filter. May be <code>null</code>.
   */
  @Nullable
  public IFilter <DATATYPE> getNestedFilter ()
  {
    return m_aNestedFilter;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractFilter <?> rhs = (AbstractFilter <?>) o;
    return m_aNestedFilter.equals (rhs.m_aNestedFilter);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aNestedFilter).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("nestedFilter", m_aNestedFilter).toString ();
  }
}
