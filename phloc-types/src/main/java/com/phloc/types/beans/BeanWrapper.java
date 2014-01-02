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

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.state.ESuccess;

/**
 * Default implementation of the {@link IBeanWrapper} interface.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class BeanWrapper implements IBeanWrapper
{
  public static final boolean DEFAULT_USE_TYPE_CONVERSION = false;

  private final Object m_aSrcObj;
  private final boolean m_bUseTypeConversion;

  /**
   * Constructor not using type conversion.
   * 
   * @param aSrcObj
   *        The source object to wrap. May not be <code>null</code> .
   */
  public BeanWrapper (@Nonnull final Object aSrcObj)
  {
    this (aSrcObj, DEFAULT_USE_TYPE_CONVERSION);
  }

  /**
   * Constructor.
   * 
   * @param aSrcObj
   *        The source object to wrap. May not be <code>null</code>.
   * @param bUseTypeConversion
   *        <code>true</code> to enable automatic type conversion,
   *        <code>false</code> to disable automatic type conversion.
   */
  public BeanWrapper (@Nonnull final Object aSrcObj, final boolean bUseTypeConversion)
  {
    if (aSrcObj == null)
      throw new NullPointerException ("srcObj");
    m_aSrcObj = aSrcObj;
    m_bUseTypeConversion = bUseTypeConversion;
  }

  @Nullable
  public Object getPropertyValue (@Nonnull final String sFieldName)
  {
    return BeanHelper.getPropertyValue (m_aSrcObj, sFieldName);
  }

  @Nullable
  public <DSTTYPE> DSTTYPE getTypedPropertyValue (@Nonnull final String sFieldName, @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return BeanHelper.getTypedPropertyValue (m_aSrcObj, sFieldName, aDstClass, m_bUseTypeConversion);
  }

  public boolean getPropertyValueBoolean (@Nonnull final String sFieldName)
  {
    return getTypedPropertyValue (sFieldName, Boolean.class).booleanValue ();
  }

  public byte getPropertyValueByte (@Nonnull final String sFieldName)
  {
    return getTypedPropertyValue (sFieldName, Number.class).byteValue ();
  }

  public char getPropertyValueChar (@Nonnull final String sFieldName)
  {
    return getTypedPropertyValue (sFieldName, Character.class).charValue ();
  }

  public double getPropertyValueDouble (@Nonnull final String sFieldName)
  {
    return getTypedPropertyValue (sFieldName, Number.class).doubleValue ();
  }

  public float getPropertyValueFloat (@Nonnull final String sFieldName)
  {
    return getTypedPropertyValue (sFieldName, Number.class).floatValue ();
  }

  public int getPropertyValueInt (@Nonnull final String sFieldName)
  {
    return getTypedPropertyValue (sFieldName, Number.class).intValue ();
  }

  public long getPropertyValueLong (@Nonnull final String sFieldName)
  {
    return getTypedPropertyValue (sFieldName, Number.class).longValue ();
  }

  public short getPropertyValueShort (@Nonnull final String sFieldName)
  {
    return getTypedPropertyValue (sFieldName, Number.class).shortValue ();
  }

  @Nonnull
  public ESuccess setPropertyValue (@Nonnull final String sFieldName, @Nullable final Object aValue)
  {
    return BeanHelper.setPropertyValueExt (m_aSrcObj, sFieldName, aValue, m_bUseTypeConversion);
  }

  @Nonnull
  public ESuccess setPropertyValue (@Nonnull final String sFieldName, final boolean aValue)
  {
    return setPropertyValue (sFieldName, Boolean.valueOf (aValue));
  }

  @Nonnull
  public ESuccess setPropertyValue (@Nonnull final String sFieldName, final byte aValue)
  {
    return setPropertyValue (sFieldName, Byte.valueOf (aValue));
  }

  @Nonnull
  public ESuccess setPropertyValue (@Nonnull final String sFieldName, final char aValue)
  {
    return setPropertyValue (sFieldName, Character.valueOf (aValue));
  }

  @Nonnull
  public ESuccess setPropertyValue (@Nonnull final String sFieldName, final double aValue)
  {
    return setPropertyValue (sFieldName, Double.valueOf (aValue));
  }

  @Nonnull
  public ESuccess setPropertyValue (@Nonnull final String sFieldName, final float aValue)
  {
    return setPropertyValue (sFieldName, Float.valueOf (aValue));
  }

  @Nonnull
  public ESuccess setPropertyValue (@Nonnull final String sFieldName, final int aValue)
  {
    return setPropertyValue (sFieldName, Integer.valueOf (aValue));
  }

  @Nonnull
  public ESuccess setPropertyValue (@Nonnull final String sFieldName, final long aValue)
  {
    return setPropertyValue (sFieldName, Long.valueOf (aValue));
  }

  @Nonnull
  public ESuccess setPropertyValue (@Nonnull final String sFieldName, final short aValue)
  {
    return setPropertyValue (sFieldName, Short.valueOf (aValue));
  }

  @Nonnull
  public ESuccess setPropertyValues (@Nullable final Map <String, ?> aProperties)
  {
    ESuccess eSuccess = ESuccess.SUCCESS;
    if (aProperties != null)
      for (final Map.Entry <String, ?> aEntry : aProperties.entrySet ())
        eSuccess = eSuccess.and (setPropertyValue (aEntry.getKey (), aEntry.getValue ()));
    return eSuccess;
  }
}
