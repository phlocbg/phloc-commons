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
package org.apache.commons.collections.primitives;

import javax.annotation.Nonnull;

import org.apache.commons.collections.primitives.decorators.UnmodifiableLongIterator;
import org.apache.commons.collections.primitives.decorators.UnmodifiableLongList;
import org.apache.commons.collections.primitives.decorators.UnmodifiableLongListIterator;

/**
 * This class consists exclusively of static methods that operate on or return
 * LongCollections.
 * <p>
 * The methods of this class all throw a NullPointerException if the provided
 * collection is null.
 *
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public final class LongCollections
{
  private LongCollections ()
  {}

  /**
   * Returns an unmodifiable LongList containing only the specified element.
   *
   * @param value
   *        the single value
   * @return an unmodifiable LongList containing only the specified element.
   */
  @Nonnull
  public static LongList singletonLongList (final long value)
  {
    // hint: a specialized implementation of LongList may be more performant
    final LongList list = new ArrayLongList (1);
    list.add (value);
    return UnmodifiableLongList.wrap (list);
  }

  /**
   * Returns an unmodifiable LongIterator containing only the specified element.
   *
   * @param value
   *        the single value
   * @return an unmodifiable LongIterator containing only the specified element.
   */
  @Nonnull
  public static LongIterator singletonLongIterator (final long value)
  {
    return singletonLongList (value).iterator ();
  }

  /**
   * Returns an unmodifiable LongListIterator containing only the specified
   * element.
   *
   * @param value
   *        the single value
   * @return an unmodifiable LongListIterator containing only the specified
   *         element.
   */
  @Nonnull
  public static LongListIterator singletonLongListIterator (final long value)
  {
    return singletonLongList (value).listIterator ();
  }

  /**
   * Returns an unmodifiable version of the given non-null LongList.
   *
   * @param list
   *        the non-null LongList to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null LongList
   * @throws NullPointerException
   *         if the given LongList is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableLongList#wrap
   */
  @Nonnull
  public static LongList unmodifiableLongList (@Nonnull final LongList list) throws NullPointerException
  {
    if (null == list)
      throw new NullPointerException ();
    return UnmodifiableLongList.wrap (list);
  }

  /**
   * Returns an unmodifiable version of the given non-null LongIterator.
   *
   * @param iter
   *        the non-null LongIterator to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null LongIterator
   * @throws NullPointerException
   *         if the given LongIterator is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableLongIterator#wrap
   */
  @Nonnull
  public static LongIterator unmodifiableLongIterator (@Nonnull final LongIterator iter)
  {
    if (null == iter)
      throw new NullPointerException ();
    return UnmodifiableLongIterator.wrap (iter);
  }

  /**
   * Returns an unmodifiable version of the given non-null LongListIterator.
   *
   * @param iter
   *        the non-null LongListIterator to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null LongListIterator
   * @throws NullPointerException
   *         if the given LongListIterator is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableLongListIterator#wrap
   */
  @Nonnull
  public static LongListIterator unmodifiableLongListIterator (@Nonnull final LongListIterator iter)
  {
    if (null == iter)
      throw new NullPointerException ();
    return UnmodifiableLongListIterator.wrap (iter);
  }

  /**
   * Returns an unmodifiable, empty LongList.
   *
   * @return an unmodifiable, empty LongList.
   * @see #EMPTY_LONG_LIST
   */
  @Nonnull
  public static LongList getEmptyLongList ()
  {
    return EMPTY_LONG_LIST;
  }

  /**
   * Returns an unmodifiable, empty LongIterator
   *
   * @return an unmodifiable, empty LongIterator.
   * @see #EMPTY_LONG_ITERATOR
   */
  @Nonnull
  public static LongIterator getEmptyLongIterator ()
  {
    return EMPTY_LONG_ITERATOR;
  }

  /**
   * Returns an unmodifiable, empty LongListIterator
   *
   * @return an unmodifiable, empty LongListIterator.
   * @see #EMPTY_LONG_LIST_ITERATOR
   */
  @Nonnull
  public static LongListIterator getEmptyLongListIterator ()
  {
    return EMPTY_LONG_LIST_ITERATOR;
  }

  /**
   * An unmodifiable, empty LongList
   *
   * @see #getEmptyLongList
   */
  public static final LongList EMPTY_LONG_LIST = unmodifiableLongList (new ArrayLongList (0));

  /**
   * An unmodifiable, empty LongIterator
   *
   * @see #getEmptyLongIterator
   */
  public static final LongIterator EMPTY_LONG_ITERATOR = unmodifiableLongIterator (EMPTY_LONG_LIST.iterator ());

  /**
   * An unmodifiable, empty LongListIterator
   *
   * @see #getEmptyLongListIterator
   */
  public static final LongListIterator EMPTY_LONG_LIST_ITERATOR = unmodifiableLongListIterator (EMPTY_LONG_LIST.listIterator ());
}