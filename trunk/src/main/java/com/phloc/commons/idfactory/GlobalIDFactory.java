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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
  public static final String DEFAULT_PREFIX = "id";

  private static final Logger s_aLogger = LoggerFactory.getLogger (GlobalIDFactory.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  private static IIntIDFactory s_aIntIDFactory = new MemoryIntIDFactory ();
  private static IIntIDFactory s_aPersistentIntIDFactory;
  private static IIDFactory <String> s_aStringIDFactory = new StringIDFromGlobalIntIDFactory ();
  private static IIDFactory <String> s_aPersistentStringIDFactory = new StringIDFromGlobalPersistentIntIDFactory ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final GlobalIDFactory s_aInstance = new GlobalIDFactory ();

  private GlobalIDFactory ()
  {}

  public static boolean hasPersistentIntIDFactory ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aPersistentIntIDFactory != null;
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

  @Nonnull
  public static EChange setStringIDFactory (@Nullable final IIDFactory <String> aFactory)
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

  @Nonnull
  public static EChange setPersistentStringIDFactory (@Nullable final IIDFactory <String> aFactory)
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
}
