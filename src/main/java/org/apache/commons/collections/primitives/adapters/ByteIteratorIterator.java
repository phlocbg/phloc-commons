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

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.ByteIterator;

/**
 * Adapts an {@link ByteIterator ByteIterator} to the {@link java.util.Iterator
 * Iterator} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link ByteIterator ByteIterator} implementation in the "obvious" way.
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class ByteIteratorIterator implements Iterator <Byte>
{
  private final ByteIterator m_aIterator;

  /**
   * Creates an {@link Iterator Iterator} wrapping the specified
   * {@link ByteIterator ByteIterator}.
   *
   * @param aIterator
   *        The iterator to be wrapped. May not be <code>null</code>.
   * @see #wrap
   */
  public ByteIteratorIterator (@Nonnull final ByteIterator aIterator)
  {
    m_aIterator = aIterator;
  }

  public boolean hasNext ()
  {
    return m_aIterator.hasNext ();
  }

  @Nonnull
  public Byte next ()
  {
    return Byte.valueOf (m_aIterator.next ());
  }

  public void remove ()
  {
    m_aIterator.remove ();
  }

  /**
   * Create an {@link Iterator Iterator} wrapping the specified
   * {@link ByteIterator ByteIterator}. When the given <i>iterator</i> is
   * <code>null</code>, returns <code>null</code>.
   *
   * @param iterator
   *        the (possibly <code>null</code>) {@link ByteIterator ByteIterator} to
   *        wrap
   * @return an {@link Iterator Iterator} wrapping the given <i>iterator</i>, or
   *         <code>null</code> when <i>iterator</i> is <code>null</code>.
   */
  @Nullable
  public static Iterator <Byte> wrap (@Nullable final ByteIterator iterator)
  {
    return null == iterator ? null : new ByteIteratorIterator (iterator);
  }
}