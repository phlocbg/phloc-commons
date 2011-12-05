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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mutable.MutableInt;

@ThreadSafe
final class StatisticsHandlerKeyedCounter implements IStatisticsHandlerKeyedCounter
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final AtomicInteger m_aCount = new AtomicInteger ();
  private final Map <String, MutableInt> m_aKeyedCount = new HashMap <String, MutableInt> ();

  public int getInvocationCount ()
  {
    return m_aCount.intValue ();
  }

  public void increment (@Nullable final String sKey)
  {
    m_aCount.incrementAndGet ();
    m_aRWLock.writeLock ().lock ();
    try
    {
      final MutableInt aPerKey = m_aKeyedCount.get (sKey);
      if (aPerKey == null)
        m_aKeyedCount.put (sKey, new MutableInt (1));
      else
        aPerKey.inc (1);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @ReturnsImmutableObject
  @Nonnull
  public Set <String> getAllKeys ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.makeUnmodifiable (m_aKeyedCount.keySet ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public int getKeyCount (@Nullable final String sKey)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final MutableInt aCount = m_aKeyedCount.get (sKey);
      return aCount == null ? CGlobal.ILLEGAL_UINT : aCount.intValue ();
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
    return "invocations=" + getInvocationCount () + "; keyed=" + m_aKeyedCount.entrySet ();
  }
}
