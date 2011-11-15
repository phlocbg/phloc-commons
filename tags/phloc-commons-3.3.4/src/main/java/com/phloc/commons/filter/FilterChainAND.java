/**
 * Copyright (C) 2006-2011 phloc systems
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.collections.ContainerHelper;

@Immutable
public final class FilterChainAND <DATATYPE> implements IFilter <DATATYPE>
{
  private final Iterable <? extends IFilter <? super DATATYPE>> m_aFilters;

  public FilterChainAND (@Nullable final IFilter <? super DATATYPE>... aFilters)
  {
    m_aFilters = ContainerHelper.newList (aFilters);
  }

  public FilterChainAND (@Nullable final Iterable <? extends IFilter <? super DATATYPE>> aFilters)
  {
    m_aFilters = ContainerHelper.newList (aFilters);
  }

  public boolean matchesFilter (@Nullable final DATATYPE aValue)
  {
    for (final IFilter <? super DATATYPE> aFilter : m_aFilters)
      if (!aFilter.matchesFilter (aValue))
        return false;
    return true;
  }

  @Nonnull
  public static <DATATYPE> FilterChainAND <DATATYPE> create (@Nullable final IFilter <? super DATATYPE>... aFilters)
  {
    return new FilterChainAND <DATATYPE> (aFilters);
  }

  @Nonnull
  public static <DATATYPE> FilterChainAND <DATATYPE> create (@Nullable final Iterable <? extends IFilter <? super DATATYPE>> aFilters)
  {
    return new FilterChainAND <DATATYPE> (aFilters);
  }
}
