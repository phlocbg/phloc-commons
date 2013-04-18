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
package com.phloc.commons.aggregate;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Implementation if {@link IAggregatorFactory} with a constant instance
 * @author philip
 *
 * @param <SRCTYPE> Aggregator source type
 * @param <DSTTYPE> Aggregator destination type
 */
@NotThreadSafe
public final class AggregatorFactoryConst <SRCTYPE, DSTTYPE> implements IAggregatorFactory <SRCTYPE, DSTTYPE>
{
  private final IAggregator <SRCTYPE, DSTTYPE> m_aAggregator;

  public AggregatorFactoryConst (@Nonnull final IAggregator <SRCTYPE, DSTTYPE> aAggregator)
  {
    if (aAggregator == null)
      throw new NullPointerException ("inst");

    m_aAggregator = aAggregator;
  }

  @Nonnull
  public IAggregator <SRCTYPE, DSTTYPE> create ()
  {
    return m_aAggregator;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof AggregatorFactoryConst <?, ?>))
      return false;
    final AggregatorFactoryConst <?, ?> rhs = (AggregatorFactoryConst <?, ?>) o;
    return m_aAggregator.equals (rhs.m_aAggregator);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aAggregator).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("aggregator", m_aAggregator).toString ();
  }
}
