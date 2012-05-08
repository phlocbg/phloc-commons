/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.commons.collections.iterate;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Specialized iterator for iterating exactly one element.
 * 
 * @author philip
 * @param <ELEMENTTYPE>
 *        The type of object to iterate.
 */
@NotThreadSafe
public final class SingleElementIterator <ELEMENTTYPE> implements Iterator <ELEMENTTYPE>
{
  private boolean m_bHasNext;
  private final ELEMENTTYPE m_aElement;

  public SingleElementIterator (@Nullable final ELEMENTTYPE aElement)
  {
    m_bHasNext = true;
    m_aElement = aElement;
  }

  public boolean hasNext ()
  {
    return m_bHasNext;
  }

  @Nullable
  public ELEMENTTYPE next ()
  {
    if (m_bHasNext)
    {
      m_bHasNext = false;
      return m_aElement;
    }
    throw new NoSuchElementException ();
  }

  @UnsupportedOperation
  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SingleElementIterator <?>))
      return false;
    final SingleElementIterator <?> rhs = (SingleElementIterator <?>) o;
    return m_bHasNext == rhs.m_bHasNext && EqualsUtils.equals (m_aElement, rhs.m_aElement);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_bHasNext).append (m_aElement).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("hasNext", m_bHasNext).append ("element", m_aElement).toString ();
  }

  @Nonnull
  public static <T> Iterator <T> create (@Nullable final T aElement)
  {
    return new SingleElementIterator <T> (aElement);
  }
}
