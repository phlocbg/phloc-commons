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

import java.util.function.Predicate;

import com.phloc.commons.annotations.DevelopersNote;

/**
 * A generic filter interface for simple object selection. If you need an
 * additional parameter for determining whether to filter an object or not, you
 * may use the {@link IFilterWithParameter} instead.
 *
 * @author Philip
 * @param <DATATYPE>
 *        The type of object to filter.
 */
@FunctionalInterface
public interface IFilter <DATATYPE> extends Predicate <DATATYPE>
{
  /**
   * Check if the given value matches the filter or not.
   *
   * @param aValue
   *        The object to filter. May be <code>null</code> depending on the
   *        implementation.
   * @return <code>true</code> if the object match the filter,
   *         <code>false</code> otherwise.
   */
  @DevelopersNote ("No @Nullable annotation as we can make no assumptions on the state")
  boolean matchesFilter (DATATYPE aValue);

  default boolean test (final DATATYPE aValue)
  {
    return matchesFilter (aValue);
  }
}
