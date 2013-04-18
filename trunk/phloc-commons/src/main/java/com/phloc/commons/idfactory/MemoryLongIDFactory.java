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
package com.phloc.commons.idfactory;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A default implementation for non-negative in-memory IDs.
 * 
 * @author philip
 */
@ThreadSafe
public final class MemoryLongIDFactory implements ILongIDFactory
{
  /** The default start ID to use. */
  @Nonnegative
  public static final long DEFAULT_START_ID = 10000L;

  @Nonnegative
  private final AtomicLong m_aID;

  public MemoryLongIDFactory ()
  {
    // new IDs start at 10000
    this (DEFAULT_START_ID);
  }

  public MemoryLongIDFactory (@Nonnegative final long nStartID)
  {
    if (nStartID < 0)
      throw new IllegalArgumentException ("Passed start ID is invalid: " + nStartID);
    m_aID = new AtomicLong (nStartID);
  }

  @Nonnegative
  public long getNewID ()
  {
    return m_aID.getAndIncrement ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MemoryLongIDFactory))
      return false;
    // AtomicInteger does not implement equals and hashCode!
    final MemoryLongIDFactory rhs = (MemoryLongIDFactory) o;
    return m_aID.get () == rhs.m_aID.get ();
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aID.get ()).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_aID).toString ();
  }
}
