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
package com.phloc.commons.microdom.convert;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.ClassHierarchyCache;
import com.phloc.commons.lang.ServiceLoaderUtils;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.typeconvert.TypeConverterRegistry;

/**
 * A utility class for converting objects from and to {@link IMicroElement}.<br>
 * The functionality is a special case of the {@link TypeConverterRegistry} as
 * we need a parameter for conversion in this case.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class MicroTypeConverterRegistry implements IMicroTypeConverterRegistry
{
  private static final MicroTypeConverterRegistry s_aInstance = new MicroTypeConverterRegistry ();
  private static final Logger s_aLogger = LoggerFactory.getLogger (MicroTypeConverterRegistry.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // WeakHashMap because key is a class
  private static final Map <Class <?>, IMicroTypeConverter> s_aMap = new WeakHashMap <Class <?>, IMicroTypeConverter> ();

  static
  {
    // Register all custom micro type converter
    for (final IMicroTypeConverterRegistrarSPI aSPI : ServiceLoaderUtils.getAllSPIImplementations (IMicroTypeConverterRegistrarSPI.class))
      aSPI.registerMicroTypeConverter (s_aInstance);
    s_aLogger.info (getRegisteredMicroTypeConverterCount () + " micro type converters registered");
  }

  private MicroTypeConverterRegistry ()
  {}

  /**
   * @return The singleton instance of this class. Never <code>null</code>.
   */
  @Nonnull
  public static MicroTypeConverterRegistry getInstance ()
  {
    return s_aInstance;
  }

  public void registerMicroElementTypeConverter (@Nonnull final Class <?> aClass,
                                                 @Nonnull final IMicroTypeConverter aConverter)
  {
    _registerMicroElementTypeConverter (aClass, aConverter);
  }

  /**
   * Register type converters from and to XML (IMicroElement). This method is
   * private to avoid later modification of the available type converters,
   * because this may lead to unexpected results.
   *
   * @param aClass
   *        The class to be registered.
   * @param aConverter
   *        The type converter from and to XML
   */
  private static void _registerMicroElementTypeConverter (@Nonnull final Class <?> aClass,
                                                          @Nonnull final IMicroTypeConverter aConverter)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    if (aConverter == null)
      throw new NullPointerException ("converter");

    s_aRWLock.writeLock ().lock ();
    try
    {
      // The main class should not already be registered
      if (s_aMap.containsKey (aClass))
        throw new IllegalArgumentException ("A microtype convert for class " + aClass + " is already registered!");

      // Automatically register the class, and all parent classes/interfaces
      for (final WeakReference <Class <?>> aCurWRSrcClass : ClassHierarchyCache.getClassHierarchyIterator (aClass))
      {
        final Class <?> aCurSrcClass = aCurWRSrcClass.get ();
        if (aCurSrcClass != null)
          if (!s_aMap.containsKey (aCurSrcClass))
            s_aMap.put (aCurSrcClass, aConverter);
      }
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public static IMicroTypeConverter getConverterToMicroElement (@Nullable final Class <?> aSrcClass)
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aMap.get (aSrcClass);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public static IMicroTypeConverter getConverterToNative (@Nonnull final Class <?> aDstClass)
  {
    if (aDstClass == null)
      throw new NullPointerException ("dstClass");

    s_aRWLock.readLock ().lock ();
    try
    {
      // Check for an exact match first
      IMicroTypeConverter ret = s_aMap.get (aDstClass);
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
                s_aLogger.debug ("Using micro type converter " +
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
   * Iterate all registered micro type converters. For informational purposes
   * only.
   *
   * @param aCallback
   *        The callback invoked for all iterations.
   */
  public static void iterateAllRegisteredMicroTypeConverters (@Nonnull final IMicroTypeConverterCallback aCallback)
  {
    // Create a copy of the map
    Map <Class <?>, IMicroTypeConverter> aCopy;
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
    for (final Map.Entry <Class <?>, IMicroTypeConverter> aEntry : aCopy.entrySet ())
      if (aCallback.call (aEntry.getKey (), aEntry.getValue ()).isBreak ())
        break;
  }

  @Nonnegative
  public static int getRegisteredMicroTypeConverterCount ()
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
