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
package com.phloc.commons.compare;

import java.io.Serializable;
import java.util.Comparator;

import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;

/**
 * A special comparator that reverses another comparator
 * 
 * @author philip
 */
public class ReverseComparator <T> implements Comparator <T>, Serializable
{
  private final Comparator <T> m_aComparator;

  public ReverseComparator (@Nonnull final Comparator <T> aComparator)
  {
    if (aComparator == null)
      throw new NullPointerException ("comparator");
    m_aComparator = aComparator;
  }

  public int compare (final T aObj1, final T aObj2)
  {
    return -m_aComparator.compare (aObj1, aObj2);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("comparator", m_aComparator).toString ();
  }

  @Nonnull
  public static <U> ReverseComparator <U> create (@Nonnull final Comparator <U> aComparator)
  {
    return new ReverseComparator <U> (aComparator);
  }
}
