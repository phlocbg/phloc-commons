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
package com.phloc.types.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.typeconvert.TypeConverter;

/**
 * Static bean access methods for getting and setting values.
 * 
 * @author Philip Helger
 */
@Immutable
public final class BeanHelper
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (BeanHelper.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final BeanHelper s_aInstance = new BeanHelper ();

  private BeanHelper ()
  {}

  /**
   * Get the property value of a given object
   * 
   * @param aObject
   *        The base Java object
   * @param sField
   *        The field name, who's getter is to be invoked
   * @return The object value or <code>null</code> in case an error occurred.
   */
  @Nullable
  public static Object getPropertyValue (@Nonnull final Object aObject, @Nonnull final String sField)
  {
    try
    {
      final String sGetterName = BeanNameHelper.getGetterName (sField);
      final PropertyDescriptor aPD = PropertyDescriptorCache.getPropertyDescriptor (aObject.getClass (),
                                                                                    sField,
                                                                                    sGetterName);
      return aPD.getReadMethod ().invoke (aObject);
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Failed to get property value of field '" + sField + "' for object " + aObject, ex);
      return null;
    }
  }

  /**
   * Get the property value of a given object using the specified type
   * 
   * @param aObject
   *        The base Java object
   * @param sField
   *        The field name, who's getter is to be invoked
   * @param aDstClass
   *        The destination class to be used.
   * @param bUseTypeConversion
   *        if <code>true</code> automatic type conversion to the desired
   *        destination type is performed (if necessary)
   * @return The object value or <code>null</code> in case an error occurred.
   */
  @Nullable
  public static <T> T getTypedPropertyValue (@Nonnull final Object aObject,
                                             @Nonnull final String sField,
                                             @Nonnull final Class <T> aDstClass,
                                             final boolean bUseTypeConversion)
  {
    final Object aPropertyValue = getPropertyValue (aObject, sField);
    return bUseTypeConversion ? TypeConverter.convertIfNecessary (aPropertyValue, aDstClass)
                             : aDstClass.cast (aPropertyValue);
  }

  /**
   * Set the property value of an object.
   * 
   * @param aObject
   *        The Java object where the value should be changed
   * @param sField
   *        The field name who's setter is to be invoked
   * @param aValue
   *        The new value to be set
   * @param bUseTypeConversion
   *        if <code>true</code> automatic conversion is performed for the
   *        parameter value (if necessary)
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess setPropertyValueExt (@Nonnull final Object aObject,
                                              @Nonnull final String sField,
                                              @Nullable final Object aValue,
                                              final boolean bUseTypeConversion)
  {
    try
    {
      final Class <?> aClass = aObject.getClass ();
      final String sSetterName = BeanNameHelper.getSetterName (sField);
      final PropertyDescriptor aPD = PropertyDescriptorCache.getPropertyDescriptor (aClass, sField, null, sSetterName);
      final Method aWriteMethod = aPD.getWriteMethod ();
      final Class <?> [] aParams = aWriteMethod.getParameterTypes ();
      if (ArrayHelper.getSize (aParams) != 1)
      {
        s_aLogger.error ("Method " +
                         sSetterName +
                         " of class " +
                         aClass.getName () +
                         " does not have exactly 1 parameter!");
      }
      else
      {
        final Object aParamValue = bUseTypeConversion ? TypeConverter.convertIfNecessary (aValue, aParams[0]) : aValue;
        aWriteMethod.invoke (aObject, aParamValue);
        return ESuccess.SUCCESS;
      }
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Failed to set property '" +
                       sField +
                       "' to '" +
                       aValue +
                       "' (" +
                       (aValue == null ? "null" : aValue.getClass ()) +
                       ")", t);
    }
    return ESuccess.FAILURE;
  }
}
