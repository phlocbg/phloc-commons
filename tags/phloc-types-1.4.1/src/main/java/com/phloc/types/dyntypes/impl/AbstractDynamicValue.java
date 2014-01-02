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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.typeconvert.ITypeConverter;
import com.phloc.commons.typeconvert.ITypeConverterProvider;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.commons.typeconvert.TypeConverterException;
import com.phloc.commons.typeconvert.TypeConverterProviderExact;
import com.phloc.commons.typeconvert.TypeConverterProviderRuleBased;
import com.phloc.types.datatype.ISimpleDataType;
import com.phloc.types.dyntypes.IDynamicValue;

/**
 * Abstract base class for dynamic types for built-in types.
 * 
 * @author Philip Helger
 * @param <T>
 *        The contained data type
 */
public abstract class AbstractDynamicValue <T> implements IDynamicValue
{
  private static final class DynamicValueTypeConverter implements ITypeConverterProvider
  {
    @Nullable
    public ITypeConverter getTypeConverter (@Nonnull final Class <?> aSrcClass, @Nonnull final Class <?> aDstClass)
    {
      // Try to find an explicit type converter
      final TypeConverterProviderExact aExact = TypeConverterProviderExact.getInstance ();
      ITypeConverter aConverter = aExact.getTypeConverter (aSrcClass, aDstClass);
      if (aConverter != null)
        return aConverter;

      // If no explicit type converter was found try to use a rule based one,
      // but only if a converter in the other direction also exists (exact or
      // rule based)
      final TypeConverterProviderRuleBased aRuleBased = TypeConverterProviderRuleBased.getInstance ();
      aConverter = aRuleBased.getTypeConverter (aSrcClass, aDstClass);
      if (aConverter != null)
        if (aExact.getTypeConverter (aDstClass, aSrcClass) != null ||
            aRuleBased.getTypeConverter (aDstClass, aSrcClass) != null)
          return aConverter;

      return null;
    }
  }

  private final ISimpleDataType <T> m_aDataType;
  private T m_aValue;

  public AbstractDynamicValue (@Nonnull final ISimpleDataType <T> aDataType, @Nullable final T aValue)
  {
    if (aDataType == null)
      throw new NullPointerException ("dataType");
    m_aDataType = aDataType;
    setValue (aValue);
  }

  @Nonnull
  public final ISimpleDataType <T> getDataType ()
  {
    return m_aDataType;
  }

  @Nonnull
  public final Class <T> getNativeClass ()
  {
    return m_aDataType.getImplClass ();
  }

  @Nullable
  public final T getValue ()
  {
    return m_aValue;
  }

  @Nullable
  public final <V> V getCastedValue (@Nonnull final Class <V> aDstClass)
  {
    return TypeConverter.convertIfNecessary (new DynamicValueTypeConverter (), m_aValue, aDstClass);
  }

  @Nonnull
  public final EChange setValue (@Nullable final Object aValue) throws TypeConverterException
  {
    // determine the value to be set
    T aValueToSet;
    if (aValue == null)
    {
      // null value
      aValueToSet = null;
    }
    else
    {
      // Use the type converter
      aValueToSet = TypeConverter.convertIfNecessary (new DynamicValueTypeConverter (), aValue, getNativeClass ());
    }

    // Is the value to set equal to the existing value?
    if (EqualsUtils.equals (m_aValue, aValueToSet))
      return EChange.UNCHANGED;

    // Finally set the new value
    m_aValue = aValueToSet;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractDynamicValue <?> rhs = (AbstractDynamicValue <?>) o;
    return m_aDataType.equals (rhs.m_aDataType) && EqualsUtils.equals (m_aValue, rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aDataType).append (m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("dataType", m_aDataType).append ("value", m_aValue).toString ();
  }
}
