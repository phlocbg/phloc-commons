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
package com.phloc.commons.stats;

import java.math.BigInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;

@ThreadSafe
abstract class AbstractStatisticsHandlerNumeric implements IStatisticsHandlerNumeric
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private int m_nInvocationCount = 0;
  private long m_nMin = CGlobal.ILLEGAL_UINT;
  private long m_nMax = CGlobal.ILLEGAL_UINT;
  private BigInteger m_aSum = BigInteger.ZERO;

  @Nonnegative
  public final int getInvocationCount ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_nInvocationCount;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  protected final void addValue (final long nValue)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_nInvocationCount++;
      if (m_nMin == CGlobal.ILLEGAL_UINT || nValue < m_nMin)
        m_nMin = nValue;
      if (m_nMax == CGlobal.ILLEGAL_UINT || nValue > m_nMax)
        m_nMax = nValue;
      m_aSum = m_aSum.add (BigInteger.valueOf (nValue));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public final BigInteger getSum ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aSum;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public final long getMin ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_nMin;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public final long getAverage ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      if (m_nInvocationCount == 0)
        return CGlobal.ILLEGAL_UINT;
      return m_aSum.divide (BigInteger.valueOf (m_nInvocationCount)).longValue ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public long getMax ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_nMax;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @Nonempty
  public String getAsString ()
  {
    return "invocations=" +
           getInvocationCount () +
           ";sum=" +
           getSum () +
           ";min=" +
           getMin () +
           ";avg=" +
           getAverage () +
           ";max=" +
           getMax ();
  }
}
