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
package com.phloc.commons.compare;

import java.io.Serializable;
import java.util.Comparator;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A special comparator that reverses another comparator
 * 
 * @author Boris Gregorcic
 * @author Philip Helger
 */
public class ReverseComparator <T> implements Comparator <T>, Serializable
{
  private static final long serialVersionUID = 2818334029541125294L;
  private final Comparator <T> m_aComparator;

  public ReverseComparator (@Nonnull final Comparator <T> aComparator)
  {
    m_aComparator = ValueEnforcer.notNull (aComparator, "Comparator");
  }

  @Nonnull
  public Comparator <T> getOriginalComparator ()
  {
    return m_aComparator;
  }

  public int compare (final T aObj1, final T aObj2)
  {
    // Reverse result, by reversing the object order :)
    return m_aComparator.compare (aObj2, aObj1);
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
