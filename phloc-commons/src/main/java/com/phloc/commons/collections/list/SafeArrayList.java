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
package com.phloc.commons.collections.list;

import java.util.ArrayList;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.IHasSize;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.factory.FactoryNull;
import com.phloc.commons.factory.IFactory;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This is a specialized {@link ArrayList} that can handle read accesses on list
 * items that are not yet in the container. If {@link #get(int)} is called with
 * an index that would normally throw an {@link ArrayIndexOutOfBoundsException}
 * this class will fill all indices between the current {@link #size()} and the
 * desired index with values provided by an {@link IFactory}. If you don't pass
 * an {@link IFactory} in the constructor a default factory returning null
 * values is used.
 * 
 * @author Philip
 * @param <ELEMENTTYPE>
 *        The type of the elements in the list
 */
@NotThreadSafe
public class SafeArrayList <ELEMENTTYPE> extends ArrayList <ELEMENTTYPE> implements IHasSize
{
  @MustImplementEqualsAndHashcode
  private final IFactory <ELEMENTTYPE> m_aFactory;

  public SafeArrayList ()
  {
    this (FactoryNull.<ELEMENTTYPE> getInstance ());
  }

  public SafeArrayList (@Nonnull final IFactory <ELEMENTTYPE> aFactory)
  {
    m_aFactory = ValueEnforcer.notNull (aFactory, "Factory");
  }

  private void _ensureSize (@Nonnegative final int nIndex)
  {
    // fill the gap
    while (size () <= nIndex)
      add (m_aFactory.create ());
  }

  @Override
  public ELEMENTTYPE get (@Nonnegative final int nIndex)
  {
    _ensureSize (nIndex);
    return super.get (nIndex);
  }

  @Override
  public ELEMENTTYPE set (@Nonnegative final int nIndex, @Nonnull final ELEMENTTYPE aElement)
  {
    _ensureSize (nIndex);
    return super.set (nIndex, aElement);
  }

  @Override
  public boolean equals (final Object o) // NOPMD
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    if (!super.equals (o))
      return false;
    final SafeArrayList <?> rhs = (SafeArrayList <?>) o;
    return m_aFactory.equals (rhs.m_aFactory);
  }

  @Override
  public int hashCode () // NOPMD
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aFactory).getHashCode ();
  }

  @Override
  public String toString () // NOPMD
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("factory", m_aFactory).toString ();
  }
}
