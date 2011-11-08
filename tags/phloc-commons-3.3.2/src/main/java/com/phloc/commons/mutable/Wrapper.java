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
package com.phloc.commons.mutable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This is a stupid wrapper around any object. Its original purpose was the
 * encapsulation of return values from within an anonymous class.
 * 
 * @author Philip
 * @param <DATATYPE>
 *        The type of object to be wrapped.
 */
@NotThreadSafe
public final class Wrapper <DATATYPE> implements IReadonlyWrapper <DATATYPE>
{
  private DATATYPE m_aObj;

  /**
   * Default constructor.
   */
  public Wrapper ()
  {}

  /**
   * Constructor with an existing object.
   * 
   * @param aObj
   *        The existing object. May be <code>null</code>.
   */
  public Wrapper (@Nullable final DATATYPE aObj)
  {
    m_aObj = aObj;
  }

  /**
   * Copy constructor. Only takes wrappers of the same type.
   * 
   * @param rhs
   *        The other wrapper to use. May not be <code>null</code>.
   */
  public Wrapper (@Nonnull final IReadonlyWrapper <DATATYPE> rhs)
  {
    if (rhs == null)
      throw new NullPointerException ("rhs");
    m_aObj = rhs.get ();
  }

  /**
   * @return The currently wrapped object. May be <code>null</code>.
   */
  @Nullable
  public DATATYPE get ()
  {
    return m_aObj;
  }

  /**
   * Change the wrapped object.
   * 
   * @param aObj
   *        The new object to be wrapped. May be <code>null</code>.
   * @return {@link EChange}
   */
  @Nonnull
  public EChange set (@Nullable final DATATYPE aObj)
  {
    if (CompareUtils.nullSafeEquals (m_aObj, aObj))
      return EChange.UNCHANGED;
    m_aObj = aObj;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof Wrapper <?>))
      return false;
    final Wrapper <?> rhs = (Wrapper <?>) o;
    return CompareUtils.nullSafeEquals (m_aObj, rhs.m_aObj);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aObj).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("obj", m_aObj).toString ();
  }

  /**
   * Static factory method with automatic type deduction.
   * 
   * @param <DATATYPE>
   *        The type to be wrapped.
   * @param aObj
   *        The object to be wrapped.
   * @return The wrapped object.
   */
  @Nonnull
  public static <DATATYPE> Wrapper <DATATYPE> create (@Nullable final DATATYPE aObj)
  {
    return new Wrapper <DATATYPE> (aObj);
  }
}
