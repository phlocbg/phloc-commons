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
package com.phloc.types.datatype.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.types.datatype.CDataType;
import com.phloc.types.datatype.ISimpleDataType;

/**
 * Implementation of a simple data type
 * 
 * @author Philip Helger
 */
@Immutable
public final class SimpleDataType <DATATYPE> implements ISimpleDataType <DATATYPE>
{
  private final String m_sID;
  private final Class <DATATYPE> m_aImplClass;
  private final boolean m_bIsPrimitiveWrapperType;
  private final boolean m_bIsArrayType;

  public SimpleDataType (@Nonnull final String sID, @Nonnull final Class <DATATYPE> aImplClass)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    if (sID.indexOf (CDataType.ID_TYPE_SEPARATOR) != -1)
      throw new IllegalArgumentException ("ID contains a forbidden character: " + sID);
    if (aImplClass == null)
      throw new NullPointerException ("implClass");
    if (!ClassHelper.isPublic (aImplClass))
      throw new IllegalArgumentException ("The " + aImplClass + " is not public!");
    m_sID = sID;
    m_aImplClass = aImplClass;
    m_bIsPrimitiveWrapperType = ClassHelper.isPrimitiveWrapperType (aImplClass);
    m_bIsArrayType = ClassHelper.isArrayClass (aImplClass);
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public Class <DATATYPE> getImplClass ()
  {
    return m_aImplClass;
  }

  public boolean isSimple ()
  {
    return true;
  }

  public boolean isComplex ()
  {
    return false;
  }

  public boolean isPrimitiveWrapperType ()
  {
    return m_bIsPrimitiveWrapperType;
  }

  public boolean isArrayType ()
  {
    return m_bIsArrayType;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SimpleDataType))
      return false;
    final SimpleDataType <?> rhs = (SimpleDataType <?>) o;
    // Don't use defaultValue because we cannot ensure that the class overrides
    // equals and hashCode
    return m_sID.equals (rhs.m_sID) && m_aImplClass.equals (rhs.m_aImplClass);
  }

  @Override
  public int hashCode ()
  {
    // Don't use defaultValue because we cannot ensure that the class overrides
    // equals and hashCode
    return new HashCodeGenerator (this).append (m_sID).append (m_aImplClass).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("id", m_sID)
                                       .append ("implClass", m_aImplClass)
                                       .append ("isPrimitiveWrapperType", m_bIsPrimitiveWrapperType)
                                       .append ("isArrayType", m_bIsArrayType)
                                       .toString ();
  }

  @Nonnull
  public static <DATATYPE> ISimpleDataType <DATATYPE> create (@Nonnull final String sID, @Nonnull final Class <DATATYPE> aImplClass)
  {
    return new SimpleDataType <DATATYPE> (sID, aImplClass);
  }
}
