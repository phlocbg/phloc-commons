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

import org.apache.commons.collections.primitives.ByteListIterator;

/**
 * Adapts an {@link ByteListIterator ByteListIterator} to the {@link ListIterator
 * ListIterator} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link ByteListIterator ByteListIterator} implementation in the "obvious" way.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class ByteListIteratorListIterator implements ListIterator <Byte>
{
  private final ByteListIterator _iterator;

  /**
   * Creates an {@link ListIterator ListIterator} wrapping the specified
   * {@link ByteListIterator ByteListIterator}.
   * 
   * @see #wrap
   */
  public ByteListIteratorListIterator (@Nonnull final ByteListIterator iterator)
  {
    _iterator = iterator;
  }

  public int nextIndex ()
  {
    return _iterator.nextIndex ();
  }

  public int previousIndex ()
  {
    return _iterator.previousIndex ();
  }

  public boolean hasNext ()
  {
    return _iterator.hasNext ();
  }

  public boolean hasPrevious ()
  {
    return _iterator.hasPrevious ();
  }

  @Nonnull
  public Byte next ()
  {
    return Byte.valueOf (_iterator.next ());
  }

  @Nonnull
  public Byte previous ()
  {
    return Byte.valueOf (_iterator.previous ());
  }

  public void add (@Nonnull final Byte obj)
  {
    _iterator.add (obj.byteValue ());
  }

  public void set (@Nonnull final Byte obj)
  {
    _iterator.set (obj.byteValue ());
  }

  public void remove ()
  {
    _iterator.remove ();
  }

  /**
   * Create a {@link ListIterator ListIterator} wrapping the specified
   * {@link ByteListIterator ByteListIterator}. When the given <i>iterator</i> is
   * <code>null</code>, returns <code>null</code>.
   * 
   * @param iterator
   *        the (possibly <code>null</code>) {@link ByteListIterator
   *        ByteListIterator} to wrap
   * @return a {@link ListIterator ListIterator} wrapping the given
   *         <i>iterator</i>, or <code>null</code> when <i>iterator</i> is
   *         <code>null</code>.
   */
  @Nullable
  public static ListIterator <Byte> wrap (@Nullable final ByteListIterator iterator)
  {
    return null == iterator ? null : new ByteListIteratorListIterator (iterator);
  }
}
