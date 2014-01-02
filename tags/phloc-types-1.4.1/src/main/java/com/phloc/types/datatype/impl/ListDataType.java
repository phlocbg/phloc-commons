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
package com.phloc.types.datatype.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.types.datatype.CDataType;
import com.phloc.types.datatype.ICollectionDataType;
import com.phloc.types.datatype.IDataType;

/**
 * Implementation of a list data type
 * 
 * @author Philip Helger
 */
@Immutable
public final class ListDataType extends AbstractComplexDataType implements ICollectionDataType
{
  private final IDataType m_aNestedDataType;

  public ListDataType (@Nonnull final IDataType aNestedDataType)
  {
    super (CDataType.ID_LIST_PREFIX + CDataType.ID_TYPE_SEPARATOR + aNestedDataType.getID ());
    m_aNestedDataType = aNestedDataType;
  }

  public boolean isList ()
  {
    return true;
  }

  public boolean isSet ()
  {
    return false;
  }

  @Nonnull
  public IDataType getNestedDataType ()
  {
    return m_aNestedDataType;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ListDataType rhs = (ListDataType) o;
    return m_aNestedDataType.equals (rhs.m_aNestedDataType);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aNestedDataType).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("nestedDataType", m_aNestedDataType).toString ();
  }
}
