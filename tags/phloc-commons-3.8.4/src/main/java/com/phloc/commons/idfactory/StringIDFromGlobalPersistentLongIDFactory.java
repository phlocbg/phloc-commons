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
package com.phloc.commons.idfactory;

import javax.annotation.Nonnull;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * An {@link IStringIDFactory} implementation that uses a constant prefix and a
 * long supplied from {@link GlobalIDFactory#getNewPersistentLongID()} to create
 * unique IDs.
 * 
 * @author philip
 */
public final class StringIDFromGlobalPersistentLongIDFactory implements IStringIDFactory
{
  private final String m_sPrefix;

  public StringIDFromGlobalPersistentLongIDFactory ()
  {
    this (GlobalIDFactory.DEFAULT_PREFIX);
  }

  public StringIDFromGlobalPersistentLongIDFactory (@Nonnull final String sPrefix)
  {
    if (sPrefix == null)
      throw new NullPointerException ("prefix");
    m_sPrefix = sPrefix;
  }

  @Nonnull
  public String getPrefix ()
  {
    return m_sPrefix;
  }

  @Nonnull
  public String getNewID ()
  {
    return m_sPrefix + Long.toString (GlobalIDFactory.getNewPersistentLongID ());
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof StringIDFromGlobalPersistentLongIDFactory))
      return false;
    final StringIDFromGlobalPersistentLongIDFactory rhs = (StringIDFromGlobalPersistentLongIDFactory) o;
    return m_sPrefix.equals (rhs.m_sPrefix);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sPrefix).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("prefix", m_sPrefix).toString ();
  }
}
