/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.factory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Implementation of {@link IFactory} that returns a constant value
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The return type of the factory
 */
public final class FactoryConstantValue <DATATYPE> implements IFactory <DATATYPE>
{
  private final DATATYPE m_aConstantValue;

  public FactoryConstantValue (@Nullable final DATATYPE aConstantValue)
  {
    m_aConstantValue = aConstantValue;
  }

  @Nullable
  public DATATYPE create ()
  {
    return m_aConstantValue;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FactoryConstantValue))
      return false;
    final FactoryConstantValue <?> rhs = (FactoryConstantValue <?>) o;
    return EqualsUtils.equals (m_aConstantValue, rhs.m_aConstantValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aConstantValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("constant", m_aConstantValue).toString ();
  }

  @Nonnull
  public static <DATATYPE> FactoryConstantValue <DATATYPE> create (@Nullable final DATATYPE aConstantValue)
  {
    return new FactoryConstantValue <DATATYPE> (aConstantValue);
  }
}
