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
package com.phloc.commons.state.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.mutable.IReadonlyWrapper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.IChangeIndicator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Wraps a change indicator and an arbitrary value.
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The data type that is wrapped together with the change indicator
 */
@Immutable
public class ChangeWithValue <DATATYPE> implements IChangeIndicator, IReadonlyWrapper <DATATYPE>
{
  private final EChange m_eChange;
  private final DATATYPE m_aObj;

  /**
   * Constructor
   * 
   * @param aChangeIndicator
   *        The change indicator. May not be <code>null</code>.
   * @param aObj
   *        The assigned value. May be <code>null</code>.
   */
  public ChangeWithValue (@Nonnull final IChangeIndicator aChangeIndicator, @Nullable final DATATYPE aObj)
  {
    ValueEnforcer.notNull (aChangeIndicator, "ChangeIndicator");

    // Wrap in EChange so that equals works for sure
    this.m_eChange = EChange.valueOf (aChangeIndicator);
    this.m_aObj = aObj;
  }

  @Override
  public boolean isChanged ()
  {
    return this.m_eChange.isChanged ();
  }

  @Override
  public boolean isUnchanged ()
  {
    return this.m_eChange.isUnchanged ();
  }

  @Override
  @Nullable
  public DATATYPE get ()
  {
    return this.m_aObj;
  }

  @Override
  public boolean isSet ()
  {
    return this.m_aObj != null;
  }

  /**
   * Get the store value if this is a change. Otherwise the passed unchanged
   * value is returned.
   * 
   * @param aUnchangedValue
   *        The unchanged value to be used. May be <code>null</code>.
   * @return Either the stored value or the unchanged value. May be
   *         <code>null</code>.
   */
  @Nullable
  public DATATYPE getIfChanged (@Nullable final DATATYPE aUnchangedValue)
  {
    return this.m_eChange.isChanged () ? this.m_aObj : aUnchangedValue;
  }

  /**
   * Get the store value if this is a changed. Otherwise <code>null</code> is
   * returned.
   * 
   * @return Either the stored value or <code>null</code>.
   */
  @Nullable
  public DATATYPE getIfChangedOrNull ()
  {
    return getIfChanged (null);
  }

  /**
   * Get the store value if this is unchanged. Otherwise the passed changed
   * value is returned.
   * 
   * @param aChangedValue
   *        The changed value to be used. May be <code>null</code>.
   * @return Either the stored value or the changed value. May be
   *         <code>null</code>.
   */
  @Nullable
  public DATATYPE getIfUnchanged (@Nullable final DATATYPE aChangedValue)
  {
    return this.m_eChange.isUnchanged () ? this.m_aObj : aChangedValue;
  }

  /**
   * Get the store value if this is unchanged. Otherwise <code>null</code> is
   * returned.
   * 
   * @return Either the stored value or <code>null</code>.
   */
  @Nullable
  public DATATYPE getIfUnchangedOrNull ()
  {
    return getIfUnchanged (null);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final ChangeWithValue <?> rhs = (ChangeWithValue <?>) o;
    return this.m_eChange.equals (rhs.m_eChange) && EqualsUtils.equals (this.m_aObj, rhs.m_aObj);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_eChange).append (this.m_aObj).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("change", this.m_eChange).append ("obj", this.m_aObj).toString ();
  }

  /**
   * Create a new changed object with the given value.
   * 
   * @param <DATATYPE>
   *        data type
   * @param aValue
   *        The value to be used. May be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <DATATYPE> ChangeWithValue <DATATYPE> createChanged (@Nullable final DATATYPE aValue)
  {
    return new ChangeWithValue <DATATYPE> (EChange.CHANGED, aValue);
  }

  /**
   * Create a new unchanged object with the given value.
   * 
   * @param <DATATYPE>
   *        data type
   * @param aValue
   *        The value to be used. May be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <DATATYPE> ChangeWithValue <DATATYPE> createUnchanged (@Nullable final DATATYPE aValue)
  {
    return new ChangeWithValue <DATATYPE> (EChange.UNCHANGED, aValue);
  }
}
