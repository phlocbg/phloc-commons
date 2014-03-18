/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.serialize.convert;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.ClassHierarchyCache;
import com.phloc.commons.lang.ServiceLoaderUtils;

/**
 * The registry that keeps the mappings for serialization converters.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class SerializationConverterRegistry implements ISerializationConverterRegistry
{
  private static final SerializationConverterRegistry s_aInstance = new SerializationConverterRegistry ();
  private static final Logger s_aLogger = LoggerFactory.getLogger (SerializationConverterRegistry.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // WeakHashMap because key is a class
  @GuardedBy ("s_aRWLock")
  private static final Map <Class <?>, ISerializationConverter> s_aMap = new WeakHashMap <Class <?>, ISerializationConverter> ();

  static
  {
    // Register all custom micro type converter
    for (final ISerializationConverterRegistrarSPI aSPI : ServiceLoaderUtils.getAllSPIImplementations (ISerializationConverterRegistrarSPI.class))
      aSPI.registerSerializationConverter (s_aInstance);
    s_aLogger.info (getRegisteredSerializationConverterCount () + " serialization converters registered");
  }

  private SerializationConverterRegistry ()
  {}

  /**
   * @return The singleton instance of this class. Never <code>null</code>.
   */
  @Nonnull
  public static SerializationConverterRegistry getInstance ()
  {
    return s_aInstance;
  }

  public void registerSerializationConverter (@Nonnull final Class <?> aClass,
                                              @Nonnull final ISerializationConverter aConverter)
  {
    _registerSerializationConverter (aClass, aConverter);
  }

  /**
   * Register type converters from and to XML (IMicroElement). This method is
   * private to avoid later modification of the available type converters,
   * because this may lead to unexpected results.
   * 
   * @param aClass
   *        The class to be registered.
   * @param aConverter
   *        The type converter
   */
  private static void _registerSerializationConverter (@Nonnull final Class <?> aClass,
                                                       @Nonnull final ISerializationConverter aConverter)
  {
    ValueEnforcer.notNull (aClass, "Class");
    ValueEnforcer.notNull (aConverter, "Converter");
    if (Serializable.class.isAssignableFrom (aClass))
      throw new IllegalArgumentException ("The provided " + aClass.toString () + " is already Serializable!");

    s_aRWLock.writeLock ().lock ();
    try
    {
      // The main class should not already be registered
      if (s_aMap.containsKey (aClass))
        throw new IllegalArgumentException ("A micro type converter for class " + aClass + " is already registered!");

      // Automatically register the class, and all parent classes/interfaces
      for (final WeakReference <Class <?>> aCurWRSrcClass : ClassHierarchyCache.getClassHierarchyIterator (aClass))
      {
        final Class <?> aCurSrcClass = aCurWRSrcClass.get ();
        if (aCurSrcClass != null)
          if (!s_aMap.containsKey (aCurSrcClass))
          {
            s_aMap.put (aCurSrcClass, aConverter);
            if (s_aLogger.isDebugEnabled ())
              s_aLogger.debug ("Registered serialization converter for '" + aCurSrcClass.toString () + "'");
          }
      }
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public static ISerializationConverter getConverter (@Nullable final Class <?> aDstClass)
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      // Check for an exact match first
      ISerializationConverter ret = s_aMap.get (aDstClass);
      if (ret == null)
      {
        // No exact match found - try fuzzy
        for (final WeakReference <Class <?>> aCurWRDstClass : ClassHierarchyCache.getClassHierarchyIterator (aDstClass))
        {
          final Class <?> aCurDstClass = aCurWRDstClass.get ();
          if (aCurDstClass != null)
          {
            ret = s_aMap.get (aCurDstClass);
            if (ret != null)
            {
              if (s_aLogger.isDebugEnabled ())
                s_aLogger.debug ("Using serialization converter " +
                                 ret +
                                 " for class " +
                                 aDstClass +
                                 " based on " +
                                 aCurDstClass);
              break;
            }
          }
        }
      }
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Iterate all registered serialization converters. For informational purposes
   * only.
   * 
   * @param aCallback
   *        The callback invoked for all iterations.
   */
  public static void iterateAllRegisteredSerializationConverters (@Nonnull final ISerializationConverterCallback aCallback)
  {
    // Create a copy of the map
    Map <Class <?>, ISerializationConverter> aCopy;
    s_aRWLock.readLock ().lock ();
    try
    {
      aCopy = ContainerHelper.newMap (s_aMap);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }

    // And iterate the copy
    for (final Map.Entry <Class <?>, ISerializationConverter> aEntry : aCopy.entrySet ())
      if (aCallback.call (aEntry.getKey (), aEntry.getValue ()).isBreak ())
        break;
  }

  @Nonnegative
  public static int getRegisteredSerializationConverterCount ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aMap.size ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }
}
