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
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.string.ToStringGenerator;

public final class ReverseListIterator <ELEMENTTYPE> implements Iterator <ELEMENTTYPE>
{
  private final List <ELEMENTTYPE> m_aList;
  private int m_nIndex;

  public ReverseListIterator (@Nonnull final List <ELEMENTTYPE> aList)
  {
    m_aList = aList;
    m_nIndex = aList.size () - 1;
  }

  public boolean hasNext ()
  {
    return m_nIndex >= 0;
  }

  @Nullable
  public ELEMENTTYPE next ()
  {
    if (m_nIndex < 0)
      throw new NoSuchElementException ();
    final ELEMENTTYPE ret = m_aList.get (m_nIndex);
    --m_nIndex;
    return ret;
  }

  @UnsupportedOperation
  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("list", m_aList).append ("index", m_nIndex).toString ();
  }
}
