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
package com.phloc.commons.typeconvert;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
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

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.multimap.IMultiMapListBased;
import com.phloc.commons.collections.multimap.MultiTreeMapArrayListBased;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.ClassHierarchyCache;
import com.phloc.commons.lang.ServiceLoaderUtils;
import com.phloc.commons.mutable.Wrapper;
import com.phloc.commons.state.EContinue;

/**
 * This class contains all the default type converters for the default types
 * that are required. The {@link TypeConverter} class uses this factory for
 * converting objects.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class TypeConverterRegistry implements ITypeConverterRegistry
{
  private static final TypeConverterRegistry s_aInstance = new TypeConverterRegistry ();
  private static final Logger s_aLogger = LoggerFactory.getLogger (TypeConverterRegistry.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // Use a weak hash map, because the key is a class
  private static final Map <Class <?>, Map <Class <?>, ITypeConverter>> s_aConverter = new WeakHashMap <Class <?>, Map <Class <?>, ITypeConverter>> ();
  // ESCA-JAVA0261:
  private static final IMultiMapListBased <ITypeConverterRule.ESubType, ITypeConverterRule> s_aRules = new MultiTreeMapArrayListBased <ITypeConverterRule.ESubType, ITypeConverterRule> ();

  static
  {
    // Register all custom type converter
    for (final ITypeConverterRegistrarSPI aSPI : ServiceLoaderUtils.getAllSPIImplementations (ITypeConverterRegistrarSPI.class))
      aSPI.registerTypeConverter (s_aInstance);
    s_aLogger.info (getRegisteredTypeConverterCount () +
                    " type converters and " +
                    getRegisteredTypeConverterRuleCount () +
                    " rules registered");
  }

  private TypeConverterRegistry ()
  {}

  @Nonnull
  private static Map <Class <?>, ITypeConverter> _getOrCreateConverterMap (@Nonnull final Class <?> aClass)
  {
    Map <Class <?>, ITypeConverter> ret;
    s_aRWLock.readLock ().lock ();
    try
    {
      ret = s_aConverter.get (aClass);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }

    if (ret == null)
    {
      s_aRWLock.writeLock ().lock ();
      try
      {
        // Try again in write lock
        ret = s_aConverter.get (aClass);
        if (ret == null)
        {
          // Weak hash map because key is a class
          ret = new WeakHashMap <Class <?>, ITypeConverter> ();
          s_aConverter.put (aClass, ret);
        }
      }
      finally
      {
        s_aRWLock.writeLock ().unlock ();
      }
    }
    return ret;
  }

  /**
   * Register a default type converter.
   * 
   * @param aSrcClass
   *        A non-<code>null</code> source class to convert from. Must be an
   *        instancable class.
   * @param aDstClass
   *        A non-<code>null</code> destination class to convert to. Must be an
   *        instancable class. May not equal the source class.
   * @param aConverter
   *        The convert to use. May not be <code>null</code>.
   */
  private static void _registerTypeConverter (@Nonnull final Class <?> aSrcClass,
                                              @Nonnull final Class <?> aDstClass,
                                              @Nonnull final ITypeConverter aConverter)
  {
    if (aSrcClass == null)
      throw new NullPointerException ("srcClass");
    if (!ClassHelper.isPublic (aSrcClass))
      throw new IllegalArgumentException ("Source " + aSrcClass + " is no public class!");
    if (aDstClass == null)
      throw new NullPointerException ("dstClass");
    if (!ClassHelper.isPublic (aDstClass))
      throw new IllegalArgumentException ("Destination " + aDstClass + " is no public class!");
    if (aSrcClass.equals (aDstClass))
      throw new IllegalArgumentException ("Source and destination class are equal and therefore no converter is required.");
    if (aConverter == null)
      throw new NullPointerException ("converter");
    if (aConverter instanceof ITypeConverterRule)
      throw new IllegalArgumentException ("Type converter rules must be registered via registerTypeConverterRule");
    if (ClassHelper.areConvertibleClasses (aSrcClass, aDstClass))
      s_aLogger.warn ("No type converter needed between " +
                      aSrcClass +
                      " and " +
                      aDstClass +
                      " because types are convertible!");

    // The main class should not already be registered
    final Map <Class <?>, ITypeConverter> aSrcMap = _getOrCreateConverterMap (aSrcClass);
    if (aSrcMap.containsKey (aDstClass))
      throw new IllegalArgumentException ("A mapping from " + aSrcClass + " to " + aDstClass + " is already defined!");

    s_aRWLock.writeLock ().lock ();
    try
    {
      // Automatically register the destination class, and all parent
      // classes/interfaces
      for (final WeakReference <Class <?>> aCurWRDstClass : ClassHierarchyCache.getClassHierarchyIterator (aDstClass))
      {
        final Class <?> aCurDstClass = aCurWRDstClass.get ();
        if (aCurDstClass != null)
          if (!aSrcMap.containsKey (aCurDstClass))
            if (aSrcMap.put (aCurDstClass, aConverter) != null)
              s_aLogger.warn ("Overwriting converter from " + aSrcClass + " to " + aCurDstClass);
      }
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public void registerTypeConverter (@Nonnull final Class <?> aSrcClass,
                                     @Nonnull final Class <?> aDstClass,
                                     @Nonnull final ITypeConverter aConverter)
  {
    _registerTypeConverter (aSrcClass, aDstClass, aConverter);
  }

  public void registerTypeConverter (@Nonnull final Class <?> [] aSrcClasses,
                                     @Nonnull final Class <?> aDstClass,
                                     @Nonnull final ITypeConverter aConverter)
  {
    for (final Class <?> aSrcClass : aSrcClasses)
      _registerTypeConverter (aSrcClass, aDstClass, aConverter);
  }

  /**
   * Get the converter that can convert objects from aSrcClass to aDstClass.
   * Thereby no fuzzy logic is applied.
   * 
   * @param aSrcClass
   *        Source class. May not be <code>null</code>.
   * @param aDstClass
   *        Destination class. May not be <code>null</code>.
   * @return <code>null</code> if no such type converter exists, the converter
   *         object otherwise.
   */
  @Nullable
  static ITypeConverter getExactConverter (@Nullable final Class <?> aSrcClass, @Nullable final Class <?> aDstClass)
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      final Map <Class <?>, ITypeConverter> aConverterMap = s_aConverter.get (aSrcClass);
      return aConverterMap == null ? null : aConverterMap.get (aDstClass);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Get the converter that can convert objects from aSrcClass to aDstClass
   * using the registered rules. The first match is returned.
   * 
   * @param aSrcClass
   *        Source class. May not be <code>null</code>.
   * @param aDstClass
   *        Destination class. May not be <code>null</code>.
   * @return <code>null</code> if no such type converter exists, the converter
   *         object otherwise.
   */
  @Nullable
  static ITypeConverter getRuleBasedConverter (@Nullable final Class <?> aSrcClass, @Nullable final Class <?> aDstClass)
  {
    if (aSrcClass == null || aDstClass == null)
      return null;

    s_aRWLock.readLock ().lock ();
    try
    {
      // Check all rules in the correct order
      for (final Map.Entry <ITypeConverterRule.ESubType, List <ITypeConverterRule>> aEntry : s_aRules.entrySet ())
        for (final ITypeConverterRule aRule : aEntry.getValue ())
          if (aRule.canConvert (aSrcClass, aDstClass))
            return aRule;

      return null;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Iterate all possible fuzzy converters from source class to destination
   * class.
   * 
   * @param aSrcClass
   *        Source class.
   * @param aDstClass
   *        Destination class.
   * @param aCallback
   *        The callback to be invoked once a converter was found. Must return
   *        either {@link EContinue#CONTINUE} to continue iteration or
   *        {@link EContinue#BREAK} to break iteration at the current position.
   */
  private static void _iterateFuzzyConverters (@Nonnull final Class <?> aSrcClass,
                                               @Nonnull final Class <?> aDstClass,
                                               @Nonnull final ITypeConverterCallback aCallback)
  {
    // For all possible source classes
    for (final WeakReference <Class <?>> aCurWRSrcClass : ClassHierarchyCache.getClassHierarchyIterator (aSrcClass))
    {
      final Class <?> aCurSrcClass = aCurWRSrcClass.get ();
      if (aCurSrcClass != null)
      {
        // Do we have a source converter?
        final Map <Class <?>, ITypeConverter> aConverterMap = s_aConverter.get (aCurSrcClass);
        if (aConverterMap != null)
        {
          // Check explicit destination classes
          final ITypeConverter aConverter = aConverterMap.get (aDstClass);
          if (aConverter != null)
          {
            // We found a match -> invoke the callback!
            if (aCallback.call (aCurSrcClass, aDstClass, aConverter).isBreak ())
              break;
          }
        }
      }
    }
  }

  /**
   * Get the converter that can convert objects from aSrcClass to aDstClass. If
   * no exact match is found, the super-classes and interface of source and
   * destination class are searched for matching type converters. The first
   * match is returned.
   * 
   * @param aSrcClass
   *        Source class. May not be <code>null</code>.
   * @param aDstClass
   *        Destination class. May not be <code>null</code>.
   * @return <code>null</code> if no such type converter exists, the converter
   *         object otherwise.
   */
  @Nullable
  static ITypeConverter getFuzzyConverter (@Nullable final Class <?> aSrcClass, @Nullable final Class <?> aDstClass)
  {
    if (aSrcClass == null || aDstClass == null)
      return null;

    s_aRWLock.readLock ().lock ();
    try
    {
      if (GlobalDebug.isDebugMode ())
      {
        // Perform a check, whether there is more than one potential converter
        // present!
        final List <String> aAllConverters = new ArrayList <String> ();
        _iterateFuzzyConverters (aSrcClass, aDstClass, new ITypeConverterCallback ()
        {
          @Nonnull
          public EContinue call (@Nonnull final Class <?> aCurSrcClass,
                                 @Nonnull final Class <?> aCurDstClass,
                                 @Nonnull final ITypeConverter aConverter)
          {
            final boolean bExact = aSrcClass.equals (aCurSrcClass) && aDstClass.equals (aCurDstClass);
            aAllConverters.add ("[" + aCurSrcClass.getName () + "->" + aCurDstClass.getName () + "]");
            return bExact ? EContinue.BREAK : EContinue.CONTINUE;
          }
        });
        if (aAllConverters.size () > 1)
          s_aLogger.warn ("The fuzzy type converter resolver returned more than 1 match for the conversion from " +
                          aSrcClass +
                          " to " +
                          aDstClass +
                          ": " +
                          aAllConverters);
      }

      // Iterate and find the first matching type converter
      final Wrapper <ITypeConverter> ret = new Wrapper <ITypeConverter> ();
      _iterateFuzzyConverters (aSrcClass, aDstClass, new ITypeConverterCallback ()
      {
        @Nonnull
        public EContinue call (@Nonnull final Class <?> aCurSrcClass,
                               @Nonnull final Class <?> aCurDstClass,
                               @Nonnull final ITypeConverter aConverter)
        {
          ret.set (aConverter);
          return EContinue.BREAK;
        }
      });
      return ret.get ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Iterate all registered type converters. For informational purposes only.
   * 
   * @param aCallback
   *        The callback invoked for all iterations.
   */
  public static void iterateAllRegisteredTypeConverters (@Nonnull final ITypeConverterCallback aCallback)
  {
    // Create a copy of the map
    Map <Class <?>, Map <Class <?>, ITypeConverter>> aCopy;
    s_aRWLock.readLock ().lock ();
    try
    {
      aCopy = ContainerHelper.newMap (s_aConverter);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }

    // And iterate the copy
    outer: for (final Map.Entry <Class <?>, Map <Class <?>, ITypeConverter>> aSrcEntry : aCopy.entrySet ())
    {
      final Class <?> aSrcClass = aSrcEntry.getKey ();
      for (final Map.Entry <Class <?>, ITypeConverter> aDstEntry : aSrcEntry.getValue ().entrySet ())
        if (aCallback.call (aSrcClass, aDstEntry.getKey (), aDstEntry.getValue ()).isBreak ())
          break outer;
    }
  }

  @Nonnegative
  public static int getRegisteredTypeConverterCount ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      int ret = 0;
      for (final Map <Class <?>, ITypeConverter> aMap : s_aConverter.values ())
        ret += aMap.size ();
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  public void registerTypeConverterRule (@Nonnull final ITypeConverterRule aTypeConverterRule)
  {
    if (aTypeConverterRule == null)
      throw new NullPointerException ("typeConverterRule");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aRules.putSingle (aTypeConverterRule.getSubType (), aTypeConverterRule);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnegative
  public static long getRegisteredTypeConverterRuleCount ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aRules.getTotalValueCount ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }
}
