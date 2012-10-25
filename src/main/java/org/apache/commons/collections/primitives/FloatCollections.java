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
package org.apache.commons.collections.primitives;

import javax.annotation.Nonnull;

import org.apache.commons.collections.primitives.decorators.UnmodifiableFloatIterator;
import org.apache.commons.collections.primitives.decorators.UnmodifiableFloatList;
import org.apache.commons.collections.primitives.decorators.UnmodifiableFloatListIterator;

/**
 * This class consists exclusively of static methods that operate on or return
 * FloatCollections.
 * <p>
 * The methods of this class all throw a NullPointerException if the provided
 * collection is null.
 *
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public final class FloatCollections
{
  private FloatCollections ()
  {}

  /**
   * Returns an unmodifiable FloatList containing only the specified element.
   *
   * @param value
   *        the single value
   * @return an unmodifiable FloatList containing only the specified element.
   */
  @Nonnull
  public static FloatList singletonFloatList (final float value)
  {
    // hint: a specialized implementation of FloatList may be more performant
    final FloatList list = new ArrayFloatList (1);
    list.add (value);
    return UnmodifiableFloatList.wrap (list);
  }

  /**
   * Returns an unmodifiable FloatIterator containing only the specified element.
   *
   * @param value
   *        the single value
   * @return an unmodifiable FloatIterator containing only the specified element.
   */
  @Nonnull
  public static FloatIterator singletonFloatIterator (final float value)
  {
    return singletonFloatList (value).iterator ();
  }

  /**
   * Returns an unmodifiable FloatListIterator containing only the specified
   * element.
   *
   * @param value
   *        the single value
   * @return an unmodifiable FloatListIterator containing only the specified
   *         element.
   */
  @Nonnull
  public static FloatListIterator singletonFloatListIterator (final float value)
  {
    return singletonFloatList (value).listIterator ();
  }

  /**
   * Returns an unmodifiable version of the given non-null FloatList.
   *
   * @param list
   *        the non-null FloatList to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null FloatList
   * @throws NullPointerException
   *         if the given FloatList is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableFloatList#wrap
   */
  @Nonnull
  public static FloatList unmodifiableFloatList (@Nonnull final FloatList list) throws NullPointerException
  {
    if (null == list)
      throw new NullPointerException ();
    return UnmodifiableFloatList.wrap (list);
  }

  /**
   * Returns an unmodifiable version of the given non-null FloatIterator.
   *
   * @param iter
   *        the non-null FloatIterator to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null FloatIterator
   * @throws NullPointerException
   *         if the given FloatIterator is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableFloatIterator#wrap
   */
  @Nonnull
  public static FloatIterator unmodifiableFloatIterator (@Nonnull final FloatIterator iter)
  {
    if (null == iter)
      throw new NullPointerException ();
    return UnmodifiableFloatIterator.wrap (iter);
  }

  /**
   * Returns an unmodifiable version of the given non-null FloatListIterator.
   *
   * @param iter
   *        the non-null FloatListIterator to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null FloatListIterator
   * @throws NullPointerException
   *         if the given FloatListIterator is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableFloatListIterator#wrap
   */
  @Nonnull
  public static FloatListIterator unmodifiableFloatListIterator (@Nonnull final FloatListIterator iter)
  {
    if (null == iter)
      throw new NullPointerException ();
    return UnmodifiableFloatListIterator.wrap (iter);
  }

  /**
   * Returns an unmodifiable, empty FloatList.
   *
   * @return an unmodifiable, empty FloatList.
   * @see #EMPTY_FLOAT_LIST
   */
  @Nonnull
  public static FloatList getEmptyFloatList ()
  {
    return EMPTY_FLOAT_LIST;
  }

  /**
   * Returns an unmodifiable, empty FloatIterator
   *
   * @return an unmodifiable, empty FloatIterator.
   * @see #EMPTY_FLOAT_ITERATOR
   */
  @Nonnull
  public static FloatIterator getEmptyFloatIterator ()
  {
    return EMPTY_FLOAT_ITERATOR;
  }

  /**
   * Returns an unmodifiable, empty FloatListIterator
   *
   * @return an unmodifiable, empty FloatListIterator.
   * @see #EMPTY_FLOAT_LIST_ITERATOR
   */
  @Nonnull
  public static FloatListIterator getEmptyFloatListIterator ()
  {
    return EMPTY_FLOAT_LIST_ITERATOR;
  }

  /**
   * An unmodifiable, empty FloatList
   *
   * @see #getEmptyFloatList
   */
  public static final FloatList EMPTY_FLOAT_LIST = unmodifiableFloatList (new ArrayFloatList (0));

  /**
   * An unmodifiable, empty FloatIterator
   *
   * @see #getEmptyFloatIterator
   */
  public static final FloatIterator EMPTY_FLOAT_ITERATOR = unmodifiableFloatIterator (EMPTY_FLOAT_LIST.iterator ());

  /**
   * An unmodifiable, empty FloatListIterator
   *
   * @see #getEmptyFloatListIterator
   */
  public static final FloatListIterator EMPTY_FLOAT_LIST_ITERATOR = unmodifiableFloatListIterator (EMPTY_FLOAT_LIST.listIterator ());
}
