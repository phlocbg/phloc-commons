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
package com.phloc.commons.microdom.convert;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.ServiceLoaderBackport;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.typeconvert.TypeConverterException;
import com.phloc.commons.typeconvert.TypeConverterException.EReason;
import com.phloc.commons.typeconvert.TypeConverterRegistry;

/**
 * A utility class for converting objects from and to {@link IMicroElement}.<br>
 * The functionality is a special case of the {@link TypeConverterRegistry} as
 * we need a parameter for conversion in this case.
 * 
 * @author philip
 */
@ThreadSafe
public final class MicroTypeConverterRegistry
{
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // WeakHashMap because key is a class
  private static final Map <Class <?>, IMicroTypeConverter> s_aMap = new WeakHashMap <Class <?>, IMicroTypeConverter> ();

  static
  {
    // Register all custom micro type converter
    for (final IMicroTypeConverterRegistrarSPI aSPI : ServiceLoaderBackport.load (IMicroTypeConverterRegistrarSPI.class))
      aSPI.registerMicroTypeConverter ();
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final MicroTypeConverterRegistry s_aInstance = new MicroTypeConverterRegistry ();

  private MicroTypeConverterRegistry ()
  {}

  /**
   * Register type converters from and to XML (IMicroElement).
   * 
   * @param aClass
   *        The class to be registered.
   * @param aConverter
   *        The type converter from and to XML
   */
  public static void registerMicroElementTypeConverter (@Nonnull final Class <?> aClass,
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
      for (final Class <?> aCurSrcClass : ClassHelper.getClassHierarchy (aClass))
        if (!s_aMap.containsKey (aCurSrcClass))
          s_aMap.put (aCurSrcClass, aConverter);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  private static IMicroTypeConverter _getSrcConverter (final Class <?> aSrcClass)
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
  public static IMicroElement convertToMicroElement (@Nullable final Object aObject,
                                                     @Nonnull @Nonempty final String sTagName)
  {
    // Use a null namespace
    return convertToMicroElement (aObject, null, sTagName);
  }

  @Nullable
  public static IMicroElement convertToMicroElement (@Nullable final Object aObject,
                                                     @Nullable final String sNamespaceURI,
                                                     @Nonnull @Nonempty final String sTagName) throws TypeConverterException
  {
    if (StringHelper.hasNoText (sTagName))
      throw new IllegalArgumentException ("tagName is empty");

    if (aObject == null)
      return null;

    // Lookup converter
    final Class <?> aSrcClass = aObject.getClass ();
    final IMicroTypeConverter aConverter = _getSrcConverter (aSrcClass);
    if (aConverter == null)
      throw new TypeConverterException (aSrcClass, IMicroElement.class, EReason.NO_CONVERTER_FOUND);

    // Perform conversion
    final IMicroElement ret = aConverter.convertToMicroElement (aObject, sNamespaceURI, sTagName);
    if (ret == null)
      throw new TypeConverterException (aSrcClass, IMicroElement.class, EReason.CONVERSION_FAILED);
    return ret;
  }

  @Nullable
  private static IMicroTypeConverter _getDstConverter (final Class <?> aDstClass)
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      IMicroTypeConverter ret = null;
      for (final Class <?> aCurDstClass : ClassHelper.getClassHierarchy (aDstClass))
        if ((ret = s_aMap.get (aCurDstClass)) != null)
          break;
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public static <DSTTYPE> DSTTYPE convertToNative (@Nullable final IMicroElement aElement,
                                                   @Nonnull final Class <DSTTYPE> aDstClass) throws TypeConverterException
  {
    if (aDstClass == null)
      throw new NullPointerException ("destinationClass");

    if (aElement == null)
      return null;

    // Lookup converter
    final IMicroTypeConverter aConverter = _getDstConverter (aDstClass);
    if (aConverter == null)
      throw new TypeConverterException (IMicroElement.class, aDstClass, EReason.NO_CONVERTER_FOUND);

    // Perform conversion
    final DSTTYPE ret = aDstClass.cast (aConverter.convertToNative (aElement));
    if (ret == null)
      throw new TypeConverterException (IMicroElement.class, aDstClass, EReason.CONVERSION_FAILED);
    return ret;
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
    for (final Map.Entry <Class <?>, IMicroTypeConverter> aEntry : s_aMap.entrySet ())
      if (aCallback.call (aEntry.getKey (), aEntry.getValue ()).isBreak ())
        break;
  }
}