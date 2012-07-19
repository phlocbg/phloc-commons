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
package com.phloc.commons.typeconvert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.typeconvert.TypeConverterException.EReason;

/**
 * Helper class for converting base types likes "boolean" to object types like
 * "Boolean".<br>
 * Uses {@link TypeConverterRegistry#getFuzzyConverter(Class, Class)} for
 * retrieving a registered converter. If no converter is found, it is checked
 * whether a mapping from a primitive type to an object type exists.
 * 
 * @author philip
 */
@Immutable
public final class TypeConverter
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final TypeConverter s_aInstance = new TypeConverter ();

  private TypeConverter ()
  {}

  /**
   * Get the class to use. In case the passed class is a primitive type, the
   * corresponding wrapper class is used.
   * 
   * @param aClass
   *        The class to check. Can be <code>null</code> but should not be
   *        <code>null</code>.
   * @return <code>null</code> if the parameter is <code>null</code>.
   */
  @Nullable
  private static Class <?> _getUsableClass (@Nullable final Class <?> aClass)
  {
    final Class <?> aPrimitiveWrapperType = ClassHelper.getPrimitiveWrapperClass (aClass);
    return aPrimitiveWrapperType != null ? aPrimitiveWrapperType : aClass;
  }

  /**
   * Check if the passed classes are convertible. Includes conversion checks
   * between primitive types and primitive wrapper types.
   * 
   * @param aSrcClass
   *        First class. May not be <code>null</code>.
   * @param aDstClass
   *        Second class. May not be <code>null</code>.
   * @return <code>true</code> if the classes are directly convertible.
   */
  private static boolean _areConvertibleClasses (@Nonnull final Class <?> aSrcClass, @Nonnull final Class <?> aDstClass)
  {
    // Default assignable
    if (aDstClass.isAssignableFrom (aSrcClass))
      return true;

    // Special handling for "int.class" == "Integer.class" etc.
    if (aDstClass == ClassHelper.getPrimitiveWrapperClass (aSrcClass))
      return true;
    if (aDstClass == ClassHelper.getPrimitiveClass (aSrcClass))
      return true;

    // Not convertible
    return false;
  }

  @Nullable
  public static <DSTTYPE> DSTTYPE convert (final boolean aSrcValue, @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return convertIfNecessary (Boolean.valueOf (aSrcValue), aDstClass);
  }

  @Nullable
  public static <DSTTYPE> DSTTYPE convert (final byte aSrcValue, @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return convertIfNecessary (Byte.valueOf (aSrcValue), aDstClass);
  }

  @Nullable
  public static <DSTTYPE> DSTTYPE convert (final char aSrcValue, @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return convertIfNecessary (Character.valueOf (aSrcValue), aDstClass);
  }

  @Nullable
  public static <DSTTYPE> DSTTYPE convert (final double aSrcValue, @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return convertIfNecessary (Double.valueOf (aSrcValue), aDstClass);
  }

  @Nullable
  public static <DSTTYPE> DSTTYPE convert (final float aSrcValue, @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return convertIfNecessary (Float.valueOf (aSrcValue), aDstClass);
  }

  @Nullable
  public static <DSTTYPE> DSTTYPE convert (final int aSrcValue, @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return convertIfNecessary (Integer.valueOf (aSrcValue), aDstClass);
  }

  @Nullable
  public static <DSTTYPE> DSTTYPE convert (final long aSrcValue, @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return convertIfNecessary (Long.valueOf (aSrcValue), aDstClass);
  }

  @Nullable
  public static <DSTTYPE> DSTTYPE convert (final short aSrcValue, @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return convertIfNecessary (Short.valueOf (aSrcValue), aDstClass);
  }

  /**
   * Convert the passed source value to the destination class, if a conversion
   * is necessary. By default the fuzzy type converter provider is used.
   * 
   * @param <DSTTYPE>
   *        The destination type.
   * @param aSrcValue
   *        The source value. May be <code>null</code>.
   * @param aDstClass
   *        The destination class to use.
   * @return <code>null</code> if the source value was <code>null</code>.
   * @throws IllegalArgumentException
   *         if the conversion process fails because either the conversion
   *         failed, or no converter was found.
   * @see TypeConverterProviderExactBeforeFuzzy
   */
  @Nullable
  public static <DSTTYPE> DSTTYPE convertIfNecessary (@Nullable final Object aSrcValue,
                                                      @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return convertIfNecessary (TypeConverterProviderExactBeforeFuzzy.getInstance (), aSrcValue, aDstClass);
  }

  /**
   * Convert the passed source value to the destination class, if a conversion
   * is necessary.
   * 
   * @param <DSTTYPE>
   *        The destination type.
   * @param aSrcValue
   *        The source value. May be <code>null</code>.
   * @param aDstClass
   *        The destination class to use.
   * @return <code>null</code> if the source value was <code>null</code>.
   * @throws IllegalArgumentException
   *         if the conversion process fails because either the conversion
   *         failed, or no converter was found.
   * @throws TypeConverterException
   *         If either no converter could be found, or if the conversion failed!
   */
  @Nullable
  public static <DSTTYPE> DSTTYPE convertIfNecessary (@Nonnull final ITypeConverterProvider aTypeConverterProvider,
                                                      @Nullable final Object aSrcValue,
                                                      @Nonnull final Class <DSTTYPE> aDstClass) throws TypeConverterException
  {
    if (aTypeConverterProvider == null)
      throw new NullPointerException ("typeConverterProvider");
    if (aDstClass == null)
      throw new NullPointerException ("destClass");

    // Nothing to convert for null
    if (aSrcValue == null)
      return null;

    final Class <?> aSrcClass = aSrcValue.getClass ();
    final Class <?> aMappedDstClass = _getUsableClass (aDstClass);
    Object aRetVal = aSrcValue;

    // are source and destination class identical?
    if (!aSrcClass.equals (aMappedDstClass))
    {
      // First check if a direct cast is possible
      if (!_areConvertibleClasses (aSrcClass, aMappedDstClass))
      {
        // try to find matching converter
        final ITypeConverter aConverter = aTypeConverterProvider.getTypeConverter (aSrcClass, aMappedDstClass);
        if (aConverter == null)
          throw new TypeConverterException (aSrcClass, aMappedDstClass, EReason.NO_CONVERTER_FOUND);

        // Okay, converter was found -> invoke it
        aRetVal = aConverter.convert (aSrcValue);
        if (aRetVal == null)
          throw new TypeConverterException (aSrcClass, aMappedDstClass, EReason.CONVERSION_FAILED);
      }
    }

    // Done :)
    // Note: aMappedDstClass.cast (aRetValue) does not work on conversion from
    // "boolean" to "Boolean" whereas casting works
    return GenericReflection.<Object, DSTTYPE> uncheckedCast (aRetVal);
  }
}
