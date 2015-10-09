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
package com.phloc.types.dyntypes.impl;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.ClassHierarchyCache;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.lang.ServiceLoaderUtils;
import com.phloc.commons.state.EChange;
import com.phloc.types.dyntypes.IDynamicTypeRegistrarSPI;
import com.phloc.types.dyntypes.IDynamicValue;

/**
 * This class manages the available dynamic types.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class DynamicTypeRegistry
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DynamicTypeRegistry.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  // Weak hash map, because key is a class
  private static final Map <Class <?>, Class <? extends IDynamicValue>> s_aMap = new WeakHashMap <Class <?>, Class <? extends IDynamicValue>> ();

  static
  {
    // Register all custom dynamic values
    for (final IDynamicTypeRegistrarSPI aSPI : ServiceLoaderUtils.getAllSPIImplementations (IDynamicTypeRegistrarSPI.class))
      aSPI.registerDynamicTypes ();
    s_aLogger.info (s_aMap.size () + " dynamic types have been registered");
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final DynamicTypeRegistry s_aInstance = new DynamicTypeRegistry ();

  private DynamicTypeRegistry ()
  {}

  private static void _registerDynamicType (@Nonnull final Class <?> aClass,
                                            @Nonnull final Class <? extends IDynamicValue> aDynamicValueClass,
                                            final boolean bAllowOverwrite)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    if (aDynamicValueClass == null)
      throw new NullPointerException ("dynamicTypeClass");
    if (!ClassHelper.isInstancableClass (aDynamicValueClass))
      throw new IllegalArgumentException ("The passed dynamic type class must be public, instancable and needs a no-argument constructor: " +
                                          aDynamicValueClass);

    s_aRWLock.writeLock ().lock ();
    try
    {
      if (!bAllowOverwrite && s_aMap.containsKey (aClass))
        throw new IllegalArgumentException ("A dynamic value class is already registered for " + aClass);
      if (GlobalDebug.isDebugMode ())
      {
        // Small consistency check whether the "native class" of the dynamic
        // value class is equal (or a primitive wrapper) of the passed class
        final IDynamicValue aDynamicValue = GenericReflection.newInstance (aDynamicValueClass);
        if (aDynamicValue == null)
          throw new IllegalArgumentException (aDynamicValueClass + " is not instancable!");
        final Class <?> aNativeClass = aDynamicValue.getNativeClass ();
        if (!aClass.equals (aNativeClass) && !ClassHelper.getPrimitiveWrapperClass (aClass).equals (aNativeClass))
          throw new IllegalArgumentException (aClass + " is different from native class " + aNativeClass);
      }
      s_aMap.put (aClass, aDynamicValueClass);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Register a new dynamic type. If the dynamic type is already registered, an
   * exception is thrown.
   * 
   * @param aClass
   *        The class for which the dynamic type is to be registered. May not be
   *        <code>null</code>.
   * @param aDynamicValueClass
   *        The dynamic value class to be instantiated. May not be
   *        <code>null</code>.
   * @throws IllegalArgumentException
   *         If a dynamic type is already registered.
   */
  public static void registerDynamicType (@Nonnull final Class <?> aClass,
                                          @Nonnull final Class <? extends IDynamicValue> aDynamicValueClass)
  {
    _registerDynamicType (aClass, aDynamicValueClass, false);
  }

  /**
   * Overwrite an existing dynamic type mapping.
   * 
   * @param aClass
   *        The class for which the dynamic type is to be registered. May not be
   *        <code>null</code>.
   * @param aDynamicValueClass
   *        The dynamic value class to be instantiated. May not be
   *        <code>null</code>.
   */
  public static void overwriteDynamicType (@Nonnull final Class <?> aClass,
                                           @Nonnull final Class <? extends IDynamicValue> aDynamicValueClass)
  {
    _registerDynamicType (aClass, aDynamicValueClass, true);
  }

  /**
   * Change all dynamic type mappings that point to the old dynamic value class
   * with ones that point to the new dynamic value class.
   * 
   * @param aOldDynamicValueClass
   *        The old dynamic value class to be replaced. May not be
   *        <code>null</code>.
   * @param aNewDynamicValueClass
   *        The new dynamic value class to be set. May not be <code>null</code>.
   * @return EChange
   */
  @Nonnull
  public static EChange replaceDynamicType (@Nonnull final Class <? extends IDynamicValue> aOldDynamicValueClass,
                                            @Nonnull final Class <? extends IDynamicValue> aNewDynamicValueClass)
  {
    if (aOldDynamicValueClass == null)
      throw new NullPointerException ("oldDynamicValueClass");
    if (aNewDynamicValueClass == null)
      throw new NullPointerException ("newDynamicValueClass");

    // Is anything to change?
    if (aOldDynamicValueClass.equals (aNewDynamicValueClass))
      return EChange.UNCHANGED;

    // find all classes that are mapped to the old dynamic value class and
    // replace them
    boolean bMatchFound = false;
    for (final Map.Entry <Class <?>, Class <? extends IDynamicValue>> aEntry : getAllRegisteredTypes ().entrySet ())
      if (aEntry.getValue ().equals (aOldDynamicValueClass))
      {
        overwriteDynamicType (aEntry.getKey (), aNewDynamicValueClass);
        bMatchFound = true;
      }
    return EChange.valueOf (bMatchFound);
  }

  /**
   * Find the dynamic value class matching the passed class.
   * 
   * @param aClass
   *        The class for which the dynamic value class is searched. May be
   *        <code>null</code>.
   * @return <code>null</code> if no dynamic value class is present.
   */
  @Nullable
  public static Class <? extends IDynamicValue> getDynamicValueClass (@Nullable final Class <?> aClass)
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aMap.get (aClass);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Create a new dynamic type based on the given class. The initial value of
   * the dynamic type depends on the {@link IDynamicValue} implementation.
   * 
   * @param aClass
   *        The class to resolve. May not be <code>null</code>.
   * @return <code>null</code> if no dynamic type is registered for the passed
   *         class.
   */
  @Nullable
  public static IDynamicValue createNewDynamicValue (@Nonnull final Class <?> aClass)
  {
    // Try direct hit first
    Class <? extends IDynamicValue> aDynamicValueClass = getDynamicValueClass (aClass);
    if (aDynamicValueClass == null)
    {
      // Check fuzzy over the whole hierarchy
      for (final Class <?> aPossibleClass : ClassHierarchyCache.getClassHierarchy (aClass))
      {
        aDynamicValueClass = getDynamicValueClass (aPossibleClass);
        if (aDynamicValueClass != null)
          break;
      }

      if (aDynamicValueClass == null)
      {
        // No match was found
        return null;
      }
    }

    return GenericReflection.newInstance (aDynamicValueClass);
  }

  /**
   * Create a new dynamic value for the passed value.
   * 
   * @param aValue
   *        The value for which the dynamic type is requested. May not be
   *        <code>null</code> because the class of the object must be
   *        resolvable.
   * @return <code>null</code> if no dynamic type is available for the given
   *         value.
   */
  @Nullable
  public static IDynamicValue createDynamicValue (@Nonnull final Object aValue)
  {
    if (aValue == null)
      throw new NullPointerException ("value");

    final IDynamicValue aDynamicValue = createNewDynamicValue (aValue.getClass ());
    if (aDynamicValue != null)
      aDynamicValue.setValue (aValue);
    return aDynamicValue;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Map <Class <?>, Class <? extends IDynamicValue>> getAllRegisteredTypes ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newMap (s_aMap);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }
}
