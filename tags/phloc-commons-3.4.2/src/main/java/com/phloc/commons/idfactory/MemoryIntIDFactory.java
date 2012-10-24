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

import javax.annotation.Nonnegative;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A default implementation for non-negative in-memory IDs.
 * 
 * @author philip
 */
public final class MemoryIntIDFactory implements IIntIDFactory
{
  @Nonnegative
  public static final int DEFAULT_START_ID = 10000;

  @Nonnegative
  private int m_nID;

  public MemoryIntIDFactory ()
  {
    // new IDs start at 10000
    this (DEFAULT_START_ID);
  }

  public MemoryIntIDFactory (@Nonnegative final int nStartID)
  {
    if (nStartID < 0)
      throw new IllegalArgumentException ("Passed start ID is invalid: " + nStartID);
    m_nID = nStartID;
  }

  @Nonnegative
  public int getNewID ()
  {
    return m_nID++;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MemoryIntIDFactory))
      return false;
    final MemoryIntIDFactory rhs = (MemoryIntIDFactory) o;
    return m_nID == rhs.m_nID;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_nID).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_nID).toString ();
  }
}