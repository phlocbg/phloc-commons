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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.state.EChange;

/**
 * This class should not be static since it may have an impact if this class is
 * used by different projects which have a separate IntID factory.
 * 
 * @author philip
 */
@ThreadSafe
public final class GlobalIDFactory
{
  /** The default prefix to use for creating IDs */
  public static final String DEFAULT_PREFIX = "id";

  private static final Logger s_aLogger = LoggerFactory.getLogger (GlobalIDFactory.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  private static IIntIDFactory s_aIntIDFactory = new MemoryIntIDFactory ();
  private static IIntIDFactory s_aPersistentIntIDFactory;
  private static ILongIDFactory s_aLongIDFactory = new MemoryLongIDFactory ();
  private static ILongIDFactory s_aPersistentLongIDFactory;
  private static IStringIDFactory s_aStringIDFactory = new StringIDFromGlobalIntIDFactory ();
  private static IStringIDFactory s_aPersistentStringIDFactory = new StringIDFromGlobalPersistentIntIDFactory ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final GlobalIDFactory s_aInstance = new GlobalIDFactory ();

  private GlobalIDFactory ()
  {}

  public static boolean hasIntIDFactory ()
  {
    return getIntIDFactory () != null;
  }

  @Nullable
  public static IIntIDFactory getIntIDFactory ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aIntIDFactory;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public static EChange setIntIDFactory (@Nullable final IIntIDFactory aFactory)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (EqualsUtils.equals (s_aIntIDFactory, aFactory))
        return EChange.UNCHANGED;
      if (s_aLogger.isInfoEnabled ())
        s_aLogger.info ("Setting in-memory int ID factory " + aFactory);
      s_aIntIDFactory = aFactory;
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public static boolean hasPersistentIntIDFactory ()
  {
    return getPersistentIntIDFactory () != null;
  }

  @Nullable
  public static IIntIDFactory getPersistentIntIDFactory ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aPersistentIntIDFactory;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public static EChange setPersistentIntIDFactory (@Nullable final IIntIDFactory aFactory)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (EqualsUtils.equals (s_aPersistentIntIDFactory, aFactory))
        return EChange.UNCHANGED;
      if (s_aLogger.isInfoEnabled ())
        s_aLogger.info ("Setting persistent int ID factory " + aFactory);
      s_aPersistentIntIDFactory = aFactory;
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public static boolean hasLongIDFactory ()
  {
    return getLongIDFactory () != null;
  }

  @Nullable
  public static ILongIDFactory getLongIDFactory ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aLongIDFactory;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public static EChange setLongIDFactory (@Nullable final ILongIDFactory aFactory)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (EqualsUtils.equals (s_aLongIDFactory, aFactory))
        return EChange.UNCHANGED;
      if (s_aLogger.isInfoEnabled ())
        s_aLogger.info ("Setting in-memory long ID factory " + aFactory);
      s_aLongIDFactory = aFactory;
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public static boolean hasPersistentLongIDFactory ()
  {
    return getPersistentLongIDFactory () != null;
  }

  @Nullable
  public static ILongIDFactory getPersistentLongIDFactory ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aPersistentLongIDFactory;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public static EChange setPersistentLongIDFactory (@Nullable final ILongIDFactory aFactory)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (EqualsUtils.equals (s_aPersistentLongIDFactory, aFactory))
        return EChange.UNCHANGED;
      if (s_aLogger.isInfoEnabled ())
        s_aLogger.info ("Setting persistent long ID factory " + aFactory);
      s_aPersistentLongIDFactory = aFactory;
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public static boolean hasStringIDFactory ()
  {
    return getStringIDFactory () != null;
  }

  @Nullable
  public static IStringIDFactory getStringIDFactory ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aStringIDFactory;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public static EChange setStringIDFactory (@Nullable final IStringIDFactory aFactory)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (EqualsUtils.equals (s_aStringIDFactory, aFactory))
        return EChange.UNCHANGED;
      if (s_aLogger.isInfoEnabled ())
        s_aLogger.info ("Setting in-memory string ID factory " + aFactory);
      s_aStringIDFactory = aFactory;
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public static boolean hasPersistentStringIDFactory ()
  {
    return getPersistentStringIDFactory () != null;
  }

  @Nullable
  public static IStringIDFactory getPersistentStringIDFactory ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aPersistentStringIDFactory;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public static EChange setPersistentStringIDFactory (@Nullable final IStringIDFactory aFactory)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (EqualsUtils.equals (s_aPersistentStringIDFactory, aFactory))
        return EChange.UNCHANGED;
      if (s_aLogger.isInfoEnabled ())
        s_aLogger.info ("Setting persistent string ID factory " + aFactory);
      s_aPersistentStringIDFactory = aFactory;
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return A new int ID
   */
  public static int getNewIntID ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aIntIDFactory == null)
        throw new IllegalStateException ("No in-memory int ID factory has been supplied!");
      return s_aIntIDFactory.getNewID ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A new persistent int ID
   */
  public static int getNewPersistentIntID ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aPersistentIntIDFactory == null)
        throw new IllegalStateException ("No persistent int ID factory has been supplied. Don't know how to create persistent IDs!");
      return s_aPersistentIntIDFactory.getNewID ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A new long ID
   */
  public static long getNewLongID ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aLongIDFactory == null)
        throw new IllegalStateException ("No in-memory long ID factory has been supplied!");
      return s_aLongIDFactory.getNewID ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A new persistent long ID
   */
  public static long getNewPersistentLongID ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aPersistentLongIDFactory == null)
        throw new IllegalStateException ("No persistent long ID factory has been supplied. Don't know how to create persistent IDs!");
      return s_aPersistentLongIDFactory.getNewID ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A new String ID
   */
  @Nonnull
  public static String getNewStringID ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aStringIDFactory == null)
        throw new IllegalStateException ("No in-memory string ID factory has been supplied!");
      return s_aStringIDFactory.getNewID ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A new persistent String ID
   */
  @Nonnull
  public static String getNewPersistentStringID ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aPersistentStringIDFactory == null)
        throw new IllegalStateException ("No persistent string ID factory has been supplied!");
      return s_aPersistentStringIDFactory.getNewID ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @param nCount
   *        The number of IDs to retrieve. Must be &gt; 0.
   * @return An array of new int IDs
   */
  public static int [] getBulkNewIntIDs (@Nonnegative final int nCount)
  {
    if (nCount <= 0)
      throw new IllegalArgumentException ("At least 1 ID must be created!");

    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aIntIDFactory == null)
        throw new IllegalStateException ("No in-memory int ID factory has been supplied!");
      final int [] ret = new int [nCount];
      for (int i = 0; i < nCount; ++i)
        ret[i] = s_aIntIDFactory.getNewID ();
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @param nCount
   *        The number of IDs to retrieve. Must be &gt; 0.
   * @return An array of new persistent int IDs
   */
  public static int [] getBulkNewPersistentIntIDs (@Nonnegative final int nCount)
  {
    if (nCount <= 0)
      throw new IllegalArgumentException ("At least 1 ID must be created!");

    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aPersistentIntIDFactory == null)
        throw new IllegalStateException ("No persistent int ID factory has been supplied. Don't know how to create persistent IDs!");
      final int [] ret = new int [nCount];
      for (int i = 0; i < nCount; ++i)
        ret[i] = s_aPersistentIntIDFactory.getNewID ();
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @param nCount
   *        The number of IDs to retrieve. Must be &gt; 0.
   * @return An array of new long IDs
   */
  public static long [] getBulkNewLongIDs (@Nonnegative final int nCount)
  {
    if (nCount <= 0)
      throw new IllegalArgumentException ("At least 1 ID must be created!");

    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aLongIDFactory == null)
        throw new IllegalStateException ("No in-memory long ID factory has been supplied!");
      final long [] ret = new long [nCount];
      for (int i = 0; i < nCount; ++i)
        ret[i] = s_aLongIDFactory.getNewID ();
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @param nCount
   *        The number of IDs to retrieve. Must be &gt; 0.
   * @return An array of new persistent long IDs
   */
  public static long [] getBulkNewPersistentLongIDs (@Nonnegative final int nCount)
  {
    if (nCount <= 0)
      throw new IllegalArgumentException ("At least 1 ID must be created!");

    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aPersistentLongIDFactory == null)
        throw new IllegalStateException ("No persistent long ID factory has been supplied. Don't know how to create persistent IDs!");
      final long [] ret = new long [nCount];
      for (int i = 0; i < nCount; ++i)
        ret[i] = s_aPersistentLongIDFactory.getNewID ();
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @param nCount
   *        The number of IDs to retrieve
   * @return An array of new String IDs
   */
  @Nonnull
  public static String [] getBulkNewStringIDs (@Nonnegative final int nCount)
  {
    if (nCount <= 0)
      throw new IllegalArgumentException ("At least 1 ID must be created!");

    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aStringIDFactory == null)
        throw new IllegalStateException ("No in-memory string ID factory has been supplied!");
      final String [] ret = new String [nCount];
      for (int i = 0; i < nCount; ++i)
        ret[i] = s_aStringIDFactory.getNewID ();
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @param nCount
   *        The number of IDs to retrieve. Must be &gt; 0.
   * @return An array of new persistent String IDs
   */
  @Nonnull
  public static String [] getBulkNewPersistentStringIDs (@Nonnegative final int nCount)
  {
    if (nCount <= 0)
      throw new IllegalArgumentException ("At least 1 ID must be created!");

    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aPersistentStringIDFactory == null)
        throw new IllegalStateException ("No persistent string ID factory has been supplied!");
      final String [] ret = new String [nCount];
      for (int i = 0; i < nCount; ++i)
        ret[i] = s_aPersistentStringIDFactory.getNewID ();
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }
}
