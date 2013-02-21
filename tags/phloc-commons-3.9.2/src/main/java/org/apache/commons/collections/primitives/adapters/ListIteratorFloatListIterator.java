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

import org.apache.commons.collections.primitives.FloatListIterator;

/**
 * Adapts a {@link Number}-valued {@link ListIterator ListIterator} to the
 * {@link FloatListIterator FloatListIterator} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link FloatListIterator FloatListIterator} implementation in the "obvious"
 * way.
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class ListIteratorFloatListIterator implements FloatListIterator
{
  private final ListIterator <Float> m_aIterator;

  /**
   * Creates an {@link FloatListIterator FloatListIterator} wrapping the specified
   * {@link ListIterator ListIterator}.
   *
   * @see #wrap
   */
  public ListIteratorFloatListIterator (@Nonnull final ListIterator <Float> iterator)
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

  public float next ()
  {
    return m_aIterator.next ().floatValue ();
  }

  public float previous ()
  {
    return m_aIterator.previous ().floatValue ();
  }

  public void add (final float element)
  {
    m_aIterator.add (Float.valueOf (element));
  }

  public void set (final float element)
  {
    m_aIterator.set (Float.valueOf (element));
  }

  public void remove ()
  {
    m_aIterator.remove ();
  }

  /**
   * Create an {@link FloatListIterator FloatListIterator} wrapping the specified
   * {@link ListIterator ListIterator}. When the given <i>iterator</i> is
   * <code>null</code>, returns <code>null</code>.
   *
   * @param iterator
   *        the (possibly <code>null</code>) {@link ListIterator ListIterator}
   *        to wrap
   * @return an {@link FloatListIterator FloatListIterator} wrapping the given
   *         <i>iterator</i>, or <code>null</code> when <i>iterator</i> is
   *         <code>null</code>.
   */
  @Nullable
  public static FloatListIterator wrap (@Nullable final ListIterator <Float> iterator)
  {
    return null == iterator ? null : new ListIteratorFloatListIterator (iterator);
  }
}