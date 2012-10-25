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

import org.apache.commons.collections.primitives.IntListIterator;

/**
 * Adapts a {@link Number}-valued {@link ListIterator ListIterator} to the
 * {@link IntListIterator IntListIterator} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link IntListIterator IntListIterator} implementation in the "obvious"
 * way.
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class ListIteratorIntListIterator implements IntListIterator
{
  private final ListIterator <Integer> m_aIterator;

  /**
   * Creates an {@link IntListIterator IntListIterator} wrapping the specified
   * {@link ListIterator ListIterator}.
   *
   * @see #wrap
   */
  public ListIteratorIntListIterator (@Nonnull final ListIterator <Integer> iterator)
  {
    m_aIterator = iterator;
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

  public int next ()
  {
    return m_aIterator.next ().intValue ();
  }

  public int previous ()
  {
    return m_aIterator.previous ().intValue ();
  }

  public void add (final int element)
  {
    m_aIterator.add (Integer.valueOf (element));
  }

  public void set (final int element)
  {
    m_aIterator.set (Integer.valueOf (element));
  }

  public void remove ()
  {
    m_aIterator.remove ();
  }

  /**
   * Create an {@link IntListIterator IntListIterator} wrapping the specified
   * {@link ListIterator ListIterator}. When the given <i>iterator</i> is
   * <code>null</code>, returns <code>null</code>.
   *
   * @param iterator
   *        the (possibly <code>null</code>) {@link ListIterator ListIterator}
   *        to wrap
   * @return an {@link IntListIterator IntListIterator} wrapping the given
   *         <i>iterator</i>, or <code>null</code> when <i>iterator</i> is
   *         <code>null</code>.
   */
  @Nullable
  public static IntListIterator wrap (@Nullable final ListIterator <Integer> iterator)
  {
    return null == iterator ? null : new ListIteratorIntListIterator (iterator);
  }
}
