package com.phloc.commons.state.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.mutable.IReadonlyWrapper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.IChangeIndicator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Wraps a change indicator and an arbitrary value.
 * 
 * @author philip
 * @param <DATATYPE>
 *        The data type that is wrapped together with the change indicator
 */
@Immutable
public final class ChangeWithValue <DATATYPE> implements IChangeIndicator, IReadonlyWrapper <DATATYPE>
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
    if (aChangeIndicator == null)
      throw new NullPointerException ("changeIndicator");

    // Wrap in EChange so that equals works for sure
    m_eChange = EChange.valueOf (aChangeIndicator);
    m_aObj = aObj;
  }

  public boolean isChanged ()
  {
    return m_eChange.isChanged ();
  }

  public boolean isUnchanged ()
  {
    return m_eChange.isUnchanged ();
  }

  @Nullable
  public DATATYPE get ()
  {
    return m_aObj;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ChangeWithValue <?>))
      return false;
    final ChangeWithValue <?> rhs = (ChangeWithValue <?>) o;
    return m_eChange.equals (rhs.m_eChange) && EqualsUtils.nullSafeEquals (m_aObj, rhs.m_aObj);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eChange).append (m_aObj).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("change", m_eChange).append ("obj", m_aObj).toString ();
  }

  /**
   * Create a new changed object with the given value.
   * 
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
