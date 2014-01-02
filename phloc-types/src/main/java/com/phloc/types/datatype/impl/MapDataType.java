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
import com.phloc.types.datatype.IDataType;
import com.phloc.types.datatype.IMapDataType;

/**
 * Implementation of a map data type.
 * 
 * @author Philip Helger
 */
@Immutable
public final class MapDataType extends AbstractComplexDataType implements IMapDataType
{
  private final IDataType m_aKeyDataType;
  private final IDataType m_aValueDataType;

  public MapDataType (@Nonnull final IDataType aKeyDataType, @Nonnull final IDataType aValueDataType)
  {
    super (CDataType.ID_MAP_PREFIX +
           CDataType.ID_TYPE_SEPARATOR +
           aKeyDataType.getID () +
           CDataType.ID_TYPE_SEPARATOR +
           aValueDataType.getID ());
    m_aKeyDataType = aKeyDataType;
    m_aValueDataType = aValueDataType;
  }

  @Nonnull
  public IDataType getKeyDataType ()
  {
    return m_aKeyDataType;
  }

  @Nonnull
  public IDataType getValueDataType ()
  {
    return m_aValueDataType;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final MapDataType rhs = (MapDataType) o;
    return m_aKeyDataType.equals (rhs.m_aKeyDataType) && m_aValueDataType.equals (rhs.m_aValueDataType);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_aKeyDataType)
                            .append (m_aValueDataType)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("keyDataType", m_aKeyDataType)
                            .append ("valueDataType", m_aValueDataType)
                            .toString ();
  }
}
