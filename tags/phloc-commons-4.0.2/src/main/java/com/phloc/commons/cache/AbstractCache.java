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
package com.phloc.commons.cache;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.MustBeLocked;
import com.phloc.commons.annotations.MustBeLocked.ELockType;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.jmx.JMXUtils;
import com.phloc.commons.state.EChange;
import com.phloc.commons.stats.IStatisticsHandlerCache;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstract base implementation of {@link ISimpleCache}
 * 
 * @author philip
 * @param <KEYTYPE>
 *        The cache key type
 * @param <VALUETYPE>
 *        The cache value type
 */
@ThreadSafe
public abstract class AbstractCache <KEYTYPE, VALUETYPE> implements ISimpleCache <KEYTYPE, VALUETYPE>
{
  /** By default JMS is disabled */
  public static final boolean DEFAULT_JMX_ENABLED = false;
  /** The prefix to be used for statistics elements */
  public static final String STATISTICS_PREFIX = "cache:";

  private static final AtomicBoolean s_aJMXEnabled = new AtomicBoolean (DEFAULT_JMX_ENABLED);

  protected final IStatisticsHandlerCache m_aCacheAccessStats;
  private final IStatisticsHandlerCounter m_aCacheRemoveStats;
  private final IStatisticsHandlerCounter m_aCacheClearStats;

  protected final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final String m_sCacheName;
  private volatile Map <KEYTYPE, VALUETYPE> m_aCache;

  public AbstractCache (@Nonnull @Nonempty final String sCacheName)
  {
    if (StringHelper.hasNoText (sCacheName))
      throw new IllegalArgumentException ("cacheName");

    m_aCacheAccessStats = StatisticsManager.getCacheHandler (STATISTICS_PREFIX + sCacheName + "$access");
    m_aCacheRemoveStats = StatisticsManager.getCounterHandler (STATISTICS_PREFIX + sCacheName + "$remove");
    m_aCacheClearStats = StatisticsManager.getCounterHandler (STATISTICS_PREFIX + sCacheName + "$clear");
    m_sCacheName = sCacheName;
    if (isJMXEnabled ())
      JMXUtils.exposeMBeanWithAutoName (new SimpleCache (this), sCacheName);
  }

  public static boolean isJMXEnabled ()
  {
    return s_aJMXEnabled.get ();
  }

  public static void setJMXEnabled (final boolean bEnabled)
  {
    s_aJMXEnabled.set (bEnabled);
  }

  @Nonnull
  @Nonempty
  public final String getName ()
  {
    return m_sCacheName;
  }

  /**
   * Create a new cache map.
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected Map <KEYTYPE, VALUETYPE> createCache ()
  {
    return new WeakHashMap <KEYTYPE, VALUETYPE> ();
  }

  /**
   * Put a new value into the cache.
   * 
   * @param aKey
   *        The cache key. May not be <code>null</code>.
   * @param aValue
   *        The cache value. May not be <code>null</code>.
   */
  @MustBeLocked (ELockType.WRITE)
  protected final void putInCacheNotLocked (@Nonnull final KEYTYPE aKey, @Nonnull final VALUETYPE aValue)
  {
    if (aKey == null)
      throw new NullPointerException ("cacheKey");
    if (aValue == null)
      throw new NullPointerException ("cacheValue");

    // try again in write lock
    if (m_aCache == null)
    {
      // Create a new map to cache the objects
      m_aCache = createCache ();
      if (m_aCache == null)
        throw new IllegalStateException ("No cache created!");
    }
    m_aCache.put (aKey, aValue);
  }

  /**
   * Put a new value into the cache.
   * 
   * @param aKey
   *        The cache key. May not be <code>null</code>.
   * @param aValue
   *        The cache value. May not be <code>null</code>.
   */
  protected final void putInCache (@Nonnull final KEYTYPE aKey, @Nonnull final VALUETYPE aValue)
  {
    if (aKey == null)
      throw new NullPointerException ("cacheKey");
    if (aValue == null)
      throw new NullPointerException ("cacheValue");

    m_aRWLock.writeLock ().lock ();
    try
    {
      putInCacheNotLocked (aKey, aValue);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @MustBeLocked (ELockType.READ)
  protected final VALUETYPE getFromCacheNoStatsNotLocked (@Nullable final KEYTYPE aKey)
  {
    // Since null is not allowed as value, we don't need to check with
    // containsKey before get!
    return m_aCache == null ? null : m_aCache.get (aKey);
  }

  @Nullable
  @OverridingMethodsMustInvokeSuper
  protected final VALUETYPE getFromCacheNoStats (@Nullable final KEYTYPE aKey)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return getFromCacheNoStatsNotLocked (aKey);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  private void _updateStats (final boolean bMiss)
  {
    if (bMiss)
      m_aCacheAccessStats.cacheMiss ();
    else
      m_aCacheAccessStats.cacheHit ();
  }

  @Nullable
  protected final VALUETYPE getFromCacheNotLocked (@Nullable final KEYTYPE aKey)
  {
    final VALUETYPE aValue = getFromCacheNoStatsNotLocked (aKey);
    _updateStats (aValue == null);
    return aValue;
  }

  @Nullable
  @OverridingMethodsMustInvokeSuper
  public VALUETYPE getFromCache (@Nullable final KEYTYPE aKey)
  {
    final VALUETYPE aValue = getFromCacheNoStats (aKey);
    _updateStats (aValue == null);
    return aValue;
  }

  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public EChange removeFromCache (@Nullable final KEYTYPE aKey)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aCache == null || m_aCache.remove (aKey) == null)
        return EChange.UNCHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    m_aCacheRemoveStats.increment ();
    return EChange.CHANGED;
  }

  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public EChange clearCache ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aCache == null || m_aCache.isEmpty ())
        return EChange.UNCHANGED;

      m_aCache.clear ();
      m_aCacheClearStats.increment ();
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnegative
  public int size ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.getSize (m_aCache);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean isEmpty ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.isEmpty (m_aCache);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean isNotEmpty ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.isNotEmpty (m_aCache);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("cacheName", m_sCacheName).append ("content", m_aCache).toString ();
  }
}
