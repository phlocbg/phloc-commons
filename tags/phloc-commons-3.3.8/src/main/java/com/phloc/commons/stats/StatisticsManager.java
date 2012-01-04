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
package com.phloc.commons.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;

/**
 * Provides a central manager for the internal statistics.
 * 
 * @author philip
 */
@ThreadSafe
public final class StatisticsManager
{
  /*
   * No static logger for this class -> used in XML reader -> used by logging
   * initialization
   */
  private static final ReadWriteLock s_aRWLockCache = new ReentrantReadWriteLock ();
  private static final ReadWriteLock s_aRWLockTimer = new ReentrantReadWriteLock ();
  private static final ReadWriteLock s_aRWLockSize = new ReentrantReadWriteLock ();
  private static final ReadWriteLock s_aRWLockCounter = new ReentrantReadWriteLock ();
  private static final ReadWriteLock s_aRWLockKeyedCounter = new ReentrantReadWriteLock ();
  private static final Map <String, StatisticsHandlerCache> s_aHdlCache = new HashMap <String, StatisticsHandlerCache> ();
  private static final Map <String, StatisticsHandlerTimer> s_aHdlTimer = new HashMap <String, StatisticsHandlerTimer> ();
  private static final Map <String, StatisticsHandlerSize> s_aHdlSize = new HashMap <String, StatisticsHandlerSize> ();
  private static final Map <String, StatisticsHandlerCounter> s_aHdlCounter = new HashMap <String, StatisticsHandlerCounter> ();
  private static final Map <String, StatisticsHandlerKeyedCounter> s_aHdlKeyedCounter = new HashMap <String, StatisticsHandlerKeyedCounter> ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final StatisticsManager s_aInstance = new StatisticsManager ();

  private StatisticsManager ()
  {}

  @Nonnull
  public static IStatisticsHandlerCache getCacheHandler (@Nonnull final Class <?> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    return getCacheHandler (aClass.getName ());
  }

  @Nonnull
  public static IStatisticsHandlerCache getCacheHandler (@Nonnull @Nonempty final String sName)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");

    s_aRWLockCache.writeLock ().lock ();
    try
    {
      StatisticsHandlerCache aHdl = s_aHdlCache.get (sName);
      if (aHdl == null)
      {
        aHdl = new StatisticsHandlerCache ();
        s_aHdlCache.put (sName, aHdl);
      }
      return aHdl;
    }
    finally
    {
      s_aRWLockCache.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <String> getAllCacheHandler ()
  {
    s_aRWLockCache.readLock ().lock ();
    try
    {
      return ContainerHelper.makeUnmodifiable (s_aHdlCache.keySet ());
    }
    finally
    {
      s_aRWLockCache.readLock ().unlock ();
    }
  }

  @Nonnull
  public static IStatisticsHandlerTimer getTimerHandler (@Nonnull final Class <?> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    return getTimerHandler (aClass.getName ());
  }

  @Nonnull
  public static IStatisticsHandlerTimer getTimerHandler (@Nonnull @Nonempty final String sName)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");

    s_aRWLockTimer.writeLock ().lock ();
    try
    {
      StatisticsHandlerTimer aHdl = s_aHdlTimer.get (sName);
      if (aHdl == null)
      {
        aHdl = new StatisticsHandlerTimer ();
        s_aHdlTimer.put (sName, aHdl);
      }
      return aHdl;
    }
    finally
    {
      s_aRWLockTimer.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <String> getAllTimerHandler ()
  {
    s_aRWLockTimer.readLock ().lock ();
    try
    {
      return ContainerHelper.makeUnmodifiable (s_aHdlTimer.keySet ());
    }
    finally
    {
      s_aRWLockTimer.readLock ().unlock ();
    }
  }

  @Nonnull
  public static IStatisticsHandlerSize getSizeHandler (@Nonnull final Class <?> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    return getSizeHandler (aClass.getName ());
  }

  @Nonnull
  public static IStatisticsHandlerSize getSizeHandler (@Nonnull @Nonempty final String sName)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");

    s_aRWLockSize.writeLock ().lock ();
    try
    {
      StatisticsHandlerSize aHdl = s_aHdlSize.get (sName);
      if (aHdl == null)
      {
        aHdl = new StatisticsHandlerSize ();
        s_aHdlSize.put (sName, aHdl);
      }
      return aHdl;
    }
    finally
    {
      s_aRWLockSize.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <String> getAllSizeHandler ()
  {
    s_aRWLockSize.readLock ().lock ();
    try
    {
      return ContainerHelper.makeUnmodifiable (s_aHdlSize.keySet ());
    }
    finally
    {
      s_aRWLockSize.readLock ().unlock ();
    }
  }

  @Nonnull
  public static IStatisticsHandlerCounter getCounterHandler (@Nonnull final Class <?> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    return getCounterHandler (aClass.getName ());
  }

  @Nonnull
  public static IStatisticsHandlerCounter getCounterHandler (@Nonnull @Nonempty final String sName)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");

    s_aRWLockCounter.writeLock ().lock ();
    try
    {
      StatisticsHandlerCounter aHdl = s_aHdlCounter.get (sName);
      if (aHdl == null)
      {
        aHdl = new StatisticsHandlerCounter ();
        s_aHdlCounter.put (sName, aHdl);
      }
      return aHdl;
    }
    finally
    {
      s_aRWLockCounter.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <String> getAllCounterHandler ()
  {
    s_aRWLockCounter.readLock ().lock ();
    try
    {
      return ContainerHelper.makeUnmodifiable (s_aHdlCounter.keySet ());
    }
    finally
    {
      s_aRWLockCounter.readLock ().unlock ();
    }
  }

  @Nonnull
  public static IStatisticsHandlerKeyedCounter getKeyedCounterHandler (@Nonnull final Class <?> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    return getKeyedCounterHandler (aClass.getName ());
  }

  @Nonnull
  public static IStatisticsHandlerKeyedCounter getKeyedCounterHandler (@Nonnull @Nonempty final String sName)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");

    s_aRWLockKeyedCounter.writeLock ().lock ();
    try
    {
      StatisticsHandlerKeyedCounter aHdl = s_aHdlKeyedCounter.get (sName);
      if (aHdl == null)
      {
        aHdl = new StatisticsHandlerKeyedCounter ();
        s_aHdlKeyedCounter.put (sName, aHdl);
      }
      return aHdl;
    }
    finally
    {
      s_aRWLockKeyedCounter.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <String> getAllKeyedCounterHandler ()
  {
    s_aRWLockKeyedCounter.readLock ().lock ();
    try
    {
      return ContainerHelper.makeUnmodifiable (s_aHdlKeyedCounter.keySet ());
    }
    finally
    {
      s_aRWLockKeyedCounter.readLock ().unlock ();
    }
  }
}
