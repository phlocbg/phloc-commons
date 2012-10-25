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

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.FloatIterator;

/**
 * Adapts a {@link java.lang.Number Number}-valued {@link Iterator Iterator} to
 * the {@link FloatIterator FloatIterator} interface.
 * <p />
 * This implementation delegates most methods to the provided {@link Iterator
 * Iterator} implementation in the "obvious" way.
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class IteratorFloatIterator implements FloatIterator
{
  private final Iterator <Float> _iterator;

  /**
   * Creates an {@link FloatIterator FloatIterator} wrapping the specified
   * {@link Iterator Iterator}.
   *
   * @see #wrap
   */
  public IteratorFloatIterator (@Nonnull final Iterator <Float> iterator)
  {
    _iterator = iterator;
  }

  public boolean hasNext ()
  {
    return _iterator.hasNext ();
  }

  public float next ()
  {
    return _iterator.next ().floatValue ();
  }

  public void remove ()
  {
    _iterator.remove ();
  }

  /**
   * Create an {@link FloatIterator FloatIterator} wrapping the specified
   * {@link Iterator Iterator}. When the given <i>iterator</i> is
   * <code>null</code>, returns <code>null</code>.
   *
   * @param iterator
   *        the (possibly <code>null</code>) {@link Iterator Iterator} to wrap
   * @return an {@link FloatIterator FloatIterator} wrapping the given
   *         <i>iterator</i>, or <code>null</code> when <i>iterator</i> is
   *         <code>null</code>.
   */
  @Nullable
  public static FloatIterator wrap (@Nullable final Iterator <Float> iterator)
  {
    return null == iterator ? null : new IteratorFloatIterator (iterator);
  }
}
