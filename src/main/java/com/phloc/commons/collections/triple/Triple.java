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
package com.phloc.commons.collections.triple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A generic writable triple class. It wraps three objects of arbitrary type. If
 * you only want two objects, look at the class
 * {@link com.phloc.commons.collections.pair.Pair}. If you just want to wrap a
 * single object, look at class {@link com.phloc.commons.mutable.Wrapper}.
 * 
 * @author Philip Helger
 * @param <DATA1TYPE>
 *        First type.
 * @param <DATA2TYPE>
 *        Second type.
 * @param <DATA3TYPE>
 *        Third type.
 */
public final class Triple <DATA1TYPE, DATA2TYPE, DATA3TYPE> implements ITriple <DATA1TYPE, DATA2TYPE, DATA3TYPE>
{
  private DATA1TYPE m_aFirst;
  private DATA2TYPE m_aSecond;
  private DATA3TYPE m_aThird;

  public <T2 extends DATA1TYPE, U2 extends DATA2TYPE, V2 extends DATA3TYPE> Triple (@Nullable final T2 aFirst,
                                                                                    @Nullable final U2 aSecond,
                                                                                    @Nullable final V2 aThird)
  {
    this.m_aFirst = aFirst;
    this.m_aSecond = aSecond;
    this.m_aThird = aThird;
  }

  public Triple (@Nonnull final IReadonlyTriple <? extends DATA1TYPE, ? extends DATA2TYPE, ? extends DATA3TYPE> rhs)
  {
    ValueEnforcer.notNull (rhs, "Triple"); //$NON-NLS-1$
    this.m_aFirst = rhs.getFirst ();
    this.m_aSecond = rhs.getSecond ();
    this.m_aThird = rhs.getThird ();
  }

  @Override
  @Nullable
  public DATA1TYPE getFirst ()
  {
    return this.m_aFirst;
  }

  @Override
  @Nonnull
  public EChange setFirst (@Nullable final DATA1TYPE aFirst)
  {
    if (EqualsUtils.equals (aFirst, this.m_aFirst))
      return EChange.UNCHANGED;
    this.m_aFirst = aFirst;
    return EChange.CHANGED;
  }

  @Override
  @Nullable
  public DATA2TYPE getSecond ()
  {
    return this.m_aSecond;
  }

  @Override
  @Nonnull
  public EChange setSecond (@Nullable final DATA2TYPE aSecond)
  {
    if (EqualsUtils.equals (aSecond, this.m_aSecond))
      return EChange.UNCHANGED;
    this.m_aSecond = aSecond;
    return EChange.CHANGED;
  }

  @Override
  @Nullable
  public DATA3TYPE getThird ()
  {
    return this.m_aThird;
  }

  @Override
  @Nonnull
  public EChange setThird (@Nullable final DATA3TYPE aThird)
  {
    if (EqualsUtils.equals (aThird, this.m_aThird))
      return EChange.UNCHANGED;
    this.m_aThird = aThird;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof Triple <?, ?, ?>))
      return false;
    final Triple <?, ?, ?> rhs = (Triple <?, ?, ?>) o;
    return EqualsUtils.equals (this.m_aFirst, rhs.m_aFirst) &&
           EqualsUtils.equals (this.m_aSecond, rhs.m_aSecond) &&
           EqualsUtils.equals (this.m_aThird, rhs.m_aThird);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_aFirst)
                                       .append (this.m_aSecond)
                                       .append (this.m_aThird)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("first", this.m_aFirst) //$NON-NLS-1$
                                       .append ("second", this.m_aSecond) //$NON-NLS-1$
                                       .append ("third", this.m_aThird) //$NON-NLS-1$
                                       .toString ();
  }

  @Nonnull
  public static <T, U, V> ITriple <T, U, V> create (@Nullable final T aFirst,
                                                    @Nullable final U aSecond,
                                                    @Nullable final V aThird)
  {
    return new Triple <T, U, V> (aFirst, aSecond, aThird);
  }
}
