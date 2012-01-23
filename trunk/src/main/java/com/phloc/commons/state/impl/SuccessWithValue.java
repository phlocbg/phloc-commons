package com.phloc.commons.state.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.mutable.IReadonlyWrapper;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Wraps a success indicator and an arbitrary value.
 * 
 * @author philip
 * @param <DATATYPE>
 *        The data type that is wrapped together with the success indicator
 */
@Immutable
public final class SuccessWithValue <DATATYPE> implements ISuccessIndicator, IReadonlyWrapper <DATATYPE>
{
  private final ESuccess m_eSuccess;
  private final DATATYPE m_aObj;

  /**
   * Constructor
   * 
   * @param aSuccessIndicator
   *        The success indicator. May not be <code>null</code>.
   * @param aObj
   *        The assigned value. May be <code>null</code>.
   */
  public SuccessWithValue (@Nonnull final ISuccessIndicator aSuccessIndicator, @Nullable final DATATYPE aObj)
  {
    if (aSuccessIndicator == null)
      throw new NullPointerException ("successIndicator");

    // Wrap in ESuccess so that equals works for sure
    m_eSuccess = ESuccess.valueOf (aSuccessIndicator);
    m_aObj = aObj;
  }

  public boolean isSuccess ()
  {
    return m_eSuccess.isSuccess ();
  }

  public boolean isFailure ()
  {
    return m_eSuccess.isFailure ();
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
    if (!(o instanceof SuccessWithValue <?>))
      return false;
    final SuccessWithValue <?> rhs = (SuccessWithValue <?>) o;
    return m_eSuccess.equals (rhs.m_eSuccess) && EqualsUtils.nullSafeEquals (m_aObj, rhs.m_aObj);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eSuccess).append (m_aObj).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("success", m_eSuccess).append ("obj", m_aObj).toString ();
  }

  /**
   * Create a new success object with the given value.
   * 
   * @param aValue
   *        The value to be used. May be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <DATATYPE> SuccessWithValue <DATATYPE> createSuccess (@Nullable final DATATYPE aValue)
  {
    return new SuccessWithValue <DATATYPE> (ESuccess.SUCCESS, aValue);
  }

  /**
   * Create a new failure object with the given value.
   * 
   * @param aValue
   *        The value to be used. May be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <DATATYPE> SuccessWithValue <DATATYPE> createFailure (@Nullable final DATATYPE aValue)
  {
    return new SuccessWithValue <DATATYPE> (ESuccess.FAILURE, aValue);
  }
}
