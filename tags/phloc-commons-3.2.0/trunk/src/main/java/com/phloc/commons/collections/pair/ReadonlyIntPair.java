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
package com.phloc.commons.collections.pair;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A readable pair of ints.
 * 
 * @author philip
 */
@Immutable
public final class ReadonlyIntPair
{
  private final int m_aFirst;
  private final int m_aSecond;

  public ReadonlyIntPair (final int aFirst, final int aSecond)
  {
    m_aFirst = aFirst;
    m_aSecond = aSecond;
  }

  public ReadonlyIntPair (@Nonnull final ReadonlyIntPair rhs)
  {
    m_aFirst = rhs.getFirst ();
    m_aSecond = rhs.getSecond ();
  }

  public int getFirst ()
  {
    return m_aFirst;
  }

  public int getSecond ()
  {
    return m_aSecond;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ReadonlyIntPair))
      return false;
    final ReadonlyIntPair rhs = (ReadonlyIntPair) o;
    return m_aFirst == rhs.m_aFirst && m_aSecond == rhs.m_aSecond;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFirst).append (m_aSecond).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("first", m_aFirst).append ("second", m_aSecond).toString ();
  }
}
