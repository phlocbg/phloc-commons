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

import org.apache.commons.collections.primitives.LongListIterator;

/**
 * Adapts an {@link LongListIterator LongListIterator} to the {@link ListIterator
 * ListIterator} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link LongListIterator LongListIterator} implementation in the "obvious" way.
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class LongListIteratorListIterator implements ListIterator <Long>
{
  private final LongListIterator _iterator;

  /**
   * Creates an {@link ListIterator ListIterator} wrapping the specified
   * {@link LongListIterator LongListIterator}.
   *
   * @see #wrap
   */
  public LongListIteratorListIterator (@Nonnull final LongListIterator iterator)
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
  public Long next ()
  {
    return Long.valueOf (_iterator.next ());
  }

  @Nonnull
  public Long previous ()
  {
    return Long.valueOf (_iterator.previous ());
  }

  public void add (@Nonnull final Long obj)
  {
    _iterator.add (obj.longValue ());
  }

  public void set (@Nonnull final Long obj)
  {
    _iterator.set (obj.longValue ());
  }

  public void remove ()
  {
    _iterator.remove ();
  }

  /**
   * Create a {@link ListIterator ListIterator} wrapping the specified
   * {@link LongListIterator LongListIterator}. When the given <i>iterator</i> is
   * <code>null</code>, returns <code>null</code>.
   *
   * @param iterator
   *        the (possibly <code>null</code>) {@link LongListIterator
   *        LongListIterator} to wrap
   * @return a {@link ListIterator ListIterator} wrapping the given
   *         <i>iterator</i>, or <code>null</code> when <i>iterator</i> is
   *         <code>null</code>.
   */
  @Nullable
  public static ListIterator <Long> wrap (@Nullable final LongListIterator iterator)
  {
    return null == iterator ? null : new LongListIteratorListIterator (iterator);
  }
}