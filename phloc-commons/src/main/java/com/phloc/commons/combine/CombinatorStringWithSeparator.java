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
package com.phloc.commons.combine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A simple combinator that concatenates 2 strings using a common separator.
 * 
 * @author Philip Helger
 */
@Immutable
public final class CombinatorStringWithSeparator implements ICombinator <String>
{
  private final String m_sSep;

  public CombinatorStringWithSeparator (@Nonnull final String sSep)
  {
    m_sSep = ValueEnforcer.notNull (sSep, "Separator");
  }

  @Nonnull
  public String getSeparator ()
  {
    return m_sSep;
  }

  public String combine (@Nullable final String sFirst, @Nullable final String sSecond)
  {
    return sFirst + m_sSep + sSecond;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof CombinatorStringWithSeparator))
      return false;
    final CombinatorStringWithSeparator rhs = (CombinatorStringWithSeparator) o;
    return m_sSep.equals (rhs.m_sSep);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sSep).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("sep", m_sSep).toString ();
  }
}
