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
package com.phloc.commons.factory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Implementation of {@link IFactory} that always creates a new instance via
 * reflection
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The return type of the factory
 */
public final class FactoryNewInstance <DATATYPE> implements IFactory <DATATYPE>
{
  private final Class <? extends DATATYPE> m_aClass;

  public FactoryNewInstance (@Nullable final Class <? extends DATATYPE> aClass, final boolean bCheckInstancable)
  {
    if (bCheckInstancable && !ClassHelper.isInstancableClass (aClass))
      throw new IllegalArgumentException ("The passed class '" +
                                          aClass +
                                          "' is not instancable or doesn't have a no-argument constructor!");
    m_aClass = aClass;
  }

  @Nullable
  public DATATYPE create ()
  {
    return GenericReflection.newInstance (m_aClass);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FactoryNewInstance))
      return false;
    final FactoryNewInstance <?> rhs = (FactoryNewInstance <?>) o;
    return EqualsUtils.equals (m_aClass, rhs.m_aClass);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aClass).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("class", m_aClass).toString ();
  }

  @Nonnull
  public static <DATATYPE> FactoryNewInstance <DATATYPE> create (@Nullable final Class <DATATYPE> aClass)
  {
    return create (aClass, false);
  }

  @Nonnull
  public static <DATATYPE> FactoryNewInstance <DATATYPE> create (@Nullable final Class <DATATYPE> aClass,
                                                                 final boolean bCheckInstancable)
  {
    return new FactoryNewInstance <DATATYPE> (aClass, bCheckInstancable);
  }
}
