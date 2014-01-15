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
package com.phloc.commons.aggregate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Factory for new aggregator objects. Holds a hard reference to the class.
 * 
 * @author Philip Helger
 * @param <SRCTYPE>
 *        Aggregator in type
 * @param <DSTTYPE>
 *        Aggregator out type
 */
@NotThreadSafe
public final class AggregatorFactoryNewInstance <SRCTYPE, DSTTYPE> implements IAggregatorFactory <SRCTYPE, DSTTYPE>
{
  private final Class <? extends IAggregator <?, ?>> m_aClass;

  public <DATATYPE extends IAggregator <?, ?>> AggregatorFactoryNewInstance (@Nonnull final Class <DATATYPE> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    if (!ClassHelper.isInstancableClass (aClass))
      throw new IllegalArgumentException ("Class " + aClass + " is not instancable!");

    m_aClass = aClass;
  }

  @Nullable
  public IAggregator <SRCTYPE, DSTTYPE> create ()
  {
    // Can never return null (hopefully)
    return GenericReflection.uncheckedCast (GenericReflection.newInstance (m_aClass));
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof AggregatorFactoryNewInstance <?, ?>))
      return false;
    final AggregatorFactoryNewInstance <?, ?> rhs = (AggregatorFactoryNewInstance <?, ?>) o;
    return m_aClass.equals (rhs.m_aClass);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aClass).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("class", m_aClass).toString ();
  }
}
