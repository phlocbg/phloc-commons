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
package com.phloc.commons.format;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class represents a single object with an additional formatter.
 * 
 * @author philip
 */
public class FormattedObject implements IFormattedObject
{
  /** The current value. Maybe null. */
  private final Object m_aValue;

  /** The optional formatter to use. */
  private final IFormatter m_aFormatter;

  /**
   * Init the field with a value.
   * 
   * @param aValue
   *        The value to be used. May be <code>null</code>.
   * @param aFormatter
   *        The optional formatter to use. May be <code>null</code>.
   */
  public FormattedObject (@Nullable final Object aValue, @Nullable final IFormatter aFormatter)
  {
    m_aValue = aValue;
    m_aFormatter = aFormatter;
  }

  /**
   * Init the field with a value.
   * 
   * @param aValue
   *        The value to be used. May be <code>null</code>.
   * @param aFormatterProvider
   *        The optional formatter to use. May not be <code>null</code>.
   */
  public FormattedObject (@Nullable final Object aValue, @Nonnull final IFormatterProvider aFormatterProvider)
  {
    if (aFormatterProvider == null)
      throw new NullPointerException ("formatterProvider");
    m_aValue = aValue;
    m_aFormatter = aFormatterProvider.getFormatter ();
  }

  @Nullable
  public Object getValue ()
  {
    return m_aValue;
  }

  @Nonnull
  public IFormatter getFormatter ()
  {
    return m_aFormatter;
  }

  @Nullable
  public String getAsString ()
  {
    return m_aFormatter != null ? m_aFormatter.getFormattedValue (m_aValue) : String.valueOf (m_aValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final FormattedObject rhs = (FormattedObject) o;
    return CompareUtils.nullSafeEquals (m_aValue, rhs.m_aValue) &&
           CompareUtils.nullSafeEquals (m_aFormatter, rhs.m_aFormatter);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aValue).append (m_aFormatter).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_aValue)
                                       .appendIfNotNull ("formatter", m_aFormatter)
                                       .toString ();
  }
}
