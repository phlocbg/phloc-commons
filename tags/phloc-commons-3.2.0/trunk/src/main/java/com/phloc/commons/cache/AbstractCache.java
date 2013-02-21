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
package com.phloc.commons.cache;

import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.stats.IStatisticsHandlerCache;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;

@NotThreadSafe
public abstract class AbstractCache <K, V> implements ISimpleCache <K, V>
{
  private final String m_sCacheName;
  private final IStatisticsHandlerCache m_aCacheAccessStats;
  private final IStatisticsHandlerCounter m_aCacheRemoveStats;
  private final IStatisticsHandlerCounter m_aCacheClearStats;
  private volatile Map <K, V> m_aCache;

  public AbstractCache (@Nonnull @Nonempty final String sCacheName)
  {
    if (StringHelper.hasNoText (sCacheName))
      throw new IllegalArgumentException ("cacheName");

    m_sCacheName = sCacheName;
    m_aCacheAccessStats = StatisticsManager.getCacheHandler ("cache:" + sCacheName + "$access");
    m_aCacheRemoveStats = StatisticsManager.getCounterHandler ("cache:" + sCacheName + "$remove");
    m_aCacheClearStats = StatisticsManager.getCounterHandler ("cache:" + sCacheName + "$clear");
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
  protected Map <K, V> createCache ()
  {
    return new WeakHashMap <K, V> ();
  }

  /**
   * Put a new value into the cache.
   * 
   * @param aKey
   *        The cache key. May not be <code>null</code>.
   * @param aValue
   *        The cache value. May not be <code>null</code>.
   */
  protected final void putInCache (@Nonnull final K aKey, @Nonnull final V aValue)
  {
    if (aKey == null)
      throw new NullPointerException ("cacheKey");
    if (aValue == null)
      throw new NullPointerException ("cacheValue");

    if (m_aCache == null)
    {
      // Create a new map to cache the objects
      m_aCache = createCache ();
      if (m_aCache == null)
        throw new IllegalStateException ("No cache created!");
    }
    m_aCache.put (aKey, aValue);
  }

  @Nullable
  @OverridingMethodsMustInvokeSuper
  public V getFromCache (@Nullable final K aKey)
  {
    // Since null is not allowed as value, we don't need to check with
    // containsKey before get!
    final V aValue = m_aCache == null ? null : m_aCache.get (aKey);
    if (aValue == null)
      m_aCacheAccessStats.cacheMiss ();
    else
      m_aCacheAccessStats.cacheHit ();
    return aValue;
  }

  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public EChange removeFromCache (@Nullable final K aKey)
  {
    if (m_aCache == null || m_aCache.remove (aKey) == null)
      return EChange.UNCHANGED;
    m_aCacheRemoveStats.increment ();
    return EChange.CHANGED;
  }

  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public EChange clearCache ()
  {
    if (isEmpty ())
      return EChange.UNCHANGED;

    m_aCache.clear ();
    m_aCacheClearStats.increment ();
    return EChange.CHANGED;
  }

  @Nonnegative
  public int size ()
  {
    return ContainerHelper.getSize (m_aCache);
  }

  public boolean isEmpty ()
  {
    return ContainerHelper.isEmpty (m_aCache);
  }
}