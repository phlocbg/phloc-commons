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
package com.phloc.commons.filter;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * An implementation of {@link ISerializableFilter} that chains multiple
 * instances of {@link ISerializableFilter} with an <b>OR</b> operator.
 *
 * @author Philip Helger
 * @param <DATATYPE>
 *        The type to be filtered.
 */
@Immutable
@Deprecated
public final class SerializableFilterChainOR <DATATYPE> implements ISerializableFilter <DATATYPE>
{
  private final List <? extends ISerializableFilter <? super DATATYPE>> m_aFilters;

  @SafeVarargs
  public SerializableFilterChainOR (@Nullable final ISerializableFilter <? super DATATYPE>... aFilters)
  {
    m_aFilters = ContainerHelper.newList (aFilters);
  }

  public SerializableFilterChainOR (@Nullable final Iterable <? extends ISerializableFilter <? super DATATYPE>> aFilters)
  {
    m_aFilters = ContainerHelper.newList (aFilters);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <? extends ISerializableFilter <? super DATATYPE>> getContainedFilters ()
  {
    return ContainerHelper.newList (m_aFilters);
  }

  public boolean matchesFilter (@Nullable final DATATYPE aValue)
  {
    for (final ISerializableFilter <? super DATATYPE> aFilter : m_aFilters)
      if (aFilter.matchesFilter (aValue))
        return true;
    return false;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SerializableFilterChainOR <?>))
      return false;
    final SerializableFilterChainOR <?> rhs = (SerializableFilterChainOR <?>) o;
    return m_aFilters.equals (rhs.m_aFilters);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFilters).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("filters", m_aFilters).toString ();
  }

  @SafeVarargs
  @Nonnull
  public static <DATATYPE> SerializableFilterChainOR <DATATYPE> create (@Nullable final ISerializableFilter <? super DATATYPE>... aFilters)
  {
    return new SerializableFilterChainOR <DATATYPE> (aFilters);
  }

  @Nonnull
  public static <DATATYPE> SerializableFilterChainOR <DATATYPE> create (@Nullable final Iterable <? extends ISerializableFilter <? super DATATYPE>> aFilters)
  {
    return new SerializableFilterChainOR <DATATYPE> (aFilters);
  }
}
