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
package com.phloc.commons.filter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A filter implementation that always returns <code>true</code>.
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The data type to filter
 */
@Immutable
public final class FilterTrue <DATATYPE> implements IFilter <DATATYPE>
{
  private static final FilterTrue <Object> s_aInstance = new FilterTrue <Object> ();

  private FilterTrue ()
  {}

  public boolean matchesFilter (@Nullable final DATATYPE aValue)
  {
    return true;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FilterTrue <?>))
      return false;
    return true;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }

  @Nonnull
  public static <DATATYPE> FilterTrue <DATATYPE> getInstance ()
  {
    return GenericReflection.<FilterTrue <Object>, FilterTrue <DATATYPE>> uncheckedCast (s_aInstance);
  }
}
