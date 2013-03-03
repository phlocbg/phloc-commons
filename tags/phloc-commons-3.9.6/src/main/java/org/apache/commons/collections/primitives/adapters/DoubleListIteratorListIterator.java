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
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.primitives.adapters;

import java.util.ListIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.DoubleListIterator;

/**
 * Adapts an {@link DoubleListIterator DoubleListIterator} to the {@link ListIterator
 * ListIterator} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link DoubleListIterator DoubleListIterator} implementation in the "obvious" way.
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class DoubleListIteratorListIterator implements ListIterator <Double>
{
  private final DoubleListIterator m_aIterator;

  /**
   * Creates an {@link ListIterator ListIterator} wrapping the specified
   * {@link DoubleListIterator DoubleListIterator}.
   *
   * @param aIterator
   *        The iterator to be wrapped. May not be <code>null</code>.
   * @see #wrap
   */
  public DoubleListIteratorListIterator (@Nonnull final DoubleListIterator aIterator)
  {
    m_aIterator = aIterator;
  }

  public int nextIndex ()
  {
    return m_aIterator.nextIndex ();
  }

  public int previousIndex ()
  {
    return m_aIterator.previousIndex ();
  }

  public boolean hasNext ()
  {
    return m_aIterator.hasNext ();
  }

  public boolean hasPrevious ()
  {
    return m_aIterator.hasPrevious ();
  }

  @Nonnull
  public Double next ()
  {
    return Double.valueOf (m_aIterator.next ());
  }

  @Nonnull
  public Double previous ()
  {
    return Double.valueOf (m_aIterator.previous ());
  }

  public void add (@Nonnull final Double obj)
  {
    m_aIterator.add (obj.doubleValue ());
  }

  public void set (@Nonnull final Double obj)
  {
    m_aIterator.set (obj.doubleValue ());
  }

  public void remove ()
  {
    m_aIterator.remove ();
  }

  /**
   * Create a {@link ListIterator ListIterator} wrapping the specified
   * {@link DoubleListIterator DoubleListIterator}. When the given <i>iterator</i> is
   * <code>null</code>, returns <code>null</code>.
   *
   * @param iterator
   *        the (possibly <code>null</code>) {@link DoubleListIterator
   *        DoubleListIterator} to wrap
   * @return a {@link ListIterator ListIterator} wrapping the given
   *         <i>iterator</i>, or <code>null</code> when <i>iterator</i> is
   *         <code>null</code>.
   */
  @Nullable
  public static ListIterator <Double> wrap (@Nullable final DoubleListIterator iterator)
  {
    return null == iterator ? null : new DoubleListIteratorListIterator (iterator);
  }
}