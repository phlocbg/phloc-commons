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

import com.phloc.commons.ValueEnforcer;
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
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstract base implementation of {@link ISimpleCache}
 * 
 * @author Philip Helger
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

  /**
   * lock
   */
  protected final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final String m_sCacheName;
  /**
   * access statistics
   */
  protected final IStatisticsHandlerCache m_aCacheAccessStats;
  private final IStatisticsHandlerCounter m_aCacheRemoveStats;
  private final IStatisticsHandlerCounter m_aCacheClearStats;
  private volatile Map <KEYTYPE, VALUETYPE> m_aCache;

  /**
   * Ctor
   * 
   * @param sCacheName
   *        Name of the cache
   */
  public AbstractCache (@Nonnull @Nonempty final String sCacheName)
  {
    this.m_sCacheName = ValueEnforcer.notEmpty (sCacheName, "cacheName");
    this.m_aCacheAccessStats = StatisticsManager.getCacheHandler (STATISTICS_PREFIX + sCacheName + "$access");
    this.m_aCacheRemoveStats = StatisticsManager.getCounterHandler (STATISTICS_PREFIX + sCacheName + "$remove");
    this.m_aCacheClearStats = StatisticsManager.getCounterHandler (STATISTICS_PREFIX + sCacheName + "$clear");
    if (isJMXEnabled ())
      JMXUtils.exposeMBeanWithAutoName (new SimpleCache (this), sCacheName);
  }

  /**
   * @return Whether or not JMX is enabled
   */
  public static boolean isJMXEnabled ()
  {
    return s_aJMXEnabled.get ();
  }

  /**
   * @param bEnabled
   *        Whether or not JMX should be enabled
   */
  public static void setJMXEnabled (final boolean bEnabled)
  {
    s_aJMXEnabled.set (bEnabled);
  }

  @Override
  @Nonnull
  @Nonempty
  public final String getName ()
  {
    return this.m_sCacheName;
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
    ValueEnforcer.notNull (aKey, "cacheKey");
    ValueEnforcer.notNull (aValue, "cacheValue");

    // try again in write lock
    if (this.m_aCache == null)
    {
      // Create a new map to cache the objects
      this.m_aCache = createCache ();
      if (this.m_aCache == null)
        throw new IllegalStateException ("No cache created!");
    }
    this.m_aCache.put (aKey, aValue);
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
    ValueEnforcer.notNull (aKey, "cacheKey");
    ValueEnforcer.notNull (aValue, "cacheValue");

    this.m_aRWLock.writeLock ().lock ();
    try
    {
      putInCacheNotLocked (aKey, aValue);
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @param aKey
   *        The cache key
   * @return The corresponding value from the cache, or <code>null</code>
   */
  @MustBeLocked (ELockType.READ)
  protected final VALUETYPE getFromCacheNoStatsNotLocked (@Nullable final KEYTYPE aKey)
  {
    // Since null is not allowed as value, we don't need to check with
    // containsKey before get!
    return this.m_aCache == null ? null : this.m_aCache.get (aKey);
  }

  /**
   * @param aKey
   *        The cache key
   * @return The corresponding value from the cache, or <code>null</code>
   */
  @Nullable
  @OverridingMethodsMustInvokeSuper
  protected final VALUETYPE getFromCacheNoStats (@Nullable final KEYTYPE aKey)
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return getFromCacheNoStatsNotLocked (aKey);
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  private void _updateStats (final boolean bMiss)
  {
    if (bMiss)
      this.m_aCacheAccessStats.cacheMiss ();
    else
      this.m_aCacheAccessStats.cacheHit ();
  }

  /**
   * @param aKey
   *        The cache key
   * @return The corresponding value from the cache, or <code>null</code>
   */
  @Nullable
  protected final VALUETYPE getFromCacheNotLocked (@Nullable final KEYTYPE aKey)
  {
    final VALUETYPE aValue = getFromCacheNoStatsNotLocked (aKey);
    _updateStats (aValue == null);
    return aValue;
  }

  @Override
  @Nullable
  @OverridingMethodsMustInvokeSuper
  public VALUETYPE getFromCache (final KEYTYPE aKey)
  {
    final VALUETYPE aValue = getFromCacheNoStats (aKey);
    _updateStats (aValue == null);
    return aValue;
  }

  @Override
  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public EChange removeFromCache (final KEYTYPE aKey)
  {
    this.m_aRWLock.writeLock ().lock ();
    try
    {
      if (this.m_aCache == null || this.m_aCache.remove (aKey) == null)
        return EChange.UNCHANGED;
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
    this.m_aCacheRemoveStats.increment ();
    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public EChange clearCache ()
  {
    this.m_aRWLock.writeLock ().lock ();
    try
    {
      if (this.m_aCache == null || this.m_aCache.isEmpty ())
        return EChange.UNCHANGED;

      this.m_aCache.clear ();
      this.m_aCacheClearStats.increment ();
      return EChange.CHANGED;
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  @Nonnegative
  public int size ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.getSize (this.m_aCache);
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public boolean isEmpty ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.isEmpty (this.m_aCache);
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return Whether or not the cache has any content
   */
  public boolean isNotEmpty ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.isNotEmpty (this.m_aCache);
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("cacheName", this.m_sCacheName)
                                       .append ("content", this.m_aCache)
                                       .toString ();
  }
}
