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

import org.apache.commons.collections.primitives.decorators.UnmodifiableBooleanIterator;
import org.apache.commons.collections.primitives.decorators.UnmodifiableBooleanList;
import org.apache.commons.collections.primitives.decorators.UnmodifiableBooleanListIterator;

/**
 * This class consists exclusively of static methods that operate on or return
 * BooleanCollections.
 * <p>
 * The methods of this class all throw a NullPointerException if the provided
 * collection is null.
 *
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public final class BooleanCollections
{
  private BooleanCollections ()
  {}

  /**
   * Returns an unmodifiable BooleanList containing only the specified element.
   *
   * @param value
   *        the single value
   * @return an unmodifiable BooleanList containing only the specified element.
   */
  @Nonnull
  public static BooleanList singletonBooleanList (final boolean value)
  {
    // hint: a specialized implementation of BooleanList may be more performant
    final BooleanList list = new ArrayBooleanList (1);
    list.add (value);
    return UnmodifiableBooleanList.wrap (list);
  }

  /**
   * Returns an unmodifiable BooleanIterator containing only the specified element.
   *
   * @param value
   *        the single value
   * @return an unmodifiable BooleanIterator containing only the specified element.
   */
  @Nonnull
  public static BooleanIterator singletonBooleanIterator (final boolean value)
  {
    return singletonBooleanList (value).iterator ();
  }

  /**
   * Returns an unmodifiable BooleanListIterator containing only the specified
   * element.
   *
   * @param value
   *        the single value
   * @return an unmodifiable BooleanListIterator containing only the specified
   *         element.
   */
  @Nonnull
  public static BooleanListIterator singletonBooleanListIterator (final boolean value)
  {
    return singletonBooleanList (value).listIterator ();
  }

  /**
   * Returns an unmodifiable version of the given non-null BooleanList.
   *
   * @param list
   *        the non-null BooleanList to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null BooleanList
   * @throws NullPointerException
   *         if the given BooleanList is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableBooleanList#wrap
   */
  @Nonnull
  public static BooleanList unmodifiableBooleanList (@Nonnull final BooleanList list) throws NullPointerException
  {
    if (null == list)
      throw new NullPointerException ();
    return UnmodifiableBooleanList.wrap (list);
  }

  /**
   * Returns an unmodifiable version of the given non-null BooleanIterator.
   *
   * @param iter
   *        the non-null BooleanIterator to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null BooleanIterator
   * @throws NullPointerException
   *         if the given BooleanIterator is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableBooleanIterator#wrap
   */
  @Nonnull
  public static BooleanIterator unmodifiableBooleanIterator (@Nonnull final BooleanIterator iter)
  {
    if (null == iter)
      throw new NullPointerException ();
    return UnmodifiableBooleanIterator.wrap (iter);
  }

  /**
   * Returns an unmodifiable version of the given non-null BooleanListIterator.
   *
   * @param iter
   *        the non-null BooleanListIterator to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null BooleanListIterator
   * @throws NullPointerException
   *         if the given BooleanListIterator is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableBooleanListIterator#wrap
   */
  @Nonnull
  public static BooleanListIterator unmodifiableBooleanListIterator (@Nonnull final BooleanListIterator iter)
  {
    if (null == iter)
      throw new NullPointerException ();
    return UnmodifiableBooleanListIterator.wrap (iter);
  }

  /**
   * Returns an unmodifiable, empty BooleanList.
   *
   * @return an unmodifiable, empty BooleanList.
   * @see #EMPTY_BOOLEAN_LIST
   */
  @Nonnull
  public static BooleanList getEmptyBooleanList ()
  {
    return EMPTY_BOOLEAN_LIST;
  }

  /**
   * Returns an unmodifiable, empty BooleanIterator
   *
   * @return an unmodifiable, empty BooleanIterator.
   * @see #EMPTY_BOOLEAN_ITERATOR
   */
  @Nonnull
  public static BooleanIterator getEmptyBooleanIterator ()
  {
    return EMPTY_BOOLEAN_ITERATOR;
  }

  /**
   * Returns an unmodifiable, empty BooleanListIterator
   *
   * @return an unmodifiable, empty BooleanListIterator.
   * @see #EMPTY_BOOLEAN_LIST_ITERATOR
   */
  @Nonnull
  public static BooleanListIterator getEmptyBooleanListIterator ()
  {
    return EMPTY_BOOLEAN_LIST_ITERATOR;
  }

  /**
   * An unmodifiable, empty BooleanList
   *
   * @see #getEmptyBooleanList
   */
  public static final BooleanList EMPTY_BOOLEAN_LIST = unmodifiableBooleanList (new ArrayBooleanList (0));

  /**
   * An unmodifiable, empty BooleanIterator
   *
   * @see #getEmptyBooleanIterator
   */
  public static final BooleanIterator EMPTY_BOOLEAN_ITERATOR = unmodifiableBooleanIterator (EMPTY_BOOLEAN_LIST.iterator ());

  /**
   * An unmodifiable, empty BooleanListIterator
   *
   * @see #getEmptyBooleanListIterator
   */
  public static final BooleanListIterator EMPTY_BOOLEAN_LIST_ITERATOR = unmodifiableBooleanListIterator (EMPTY_BOOLEAN_LIST.listIterator ());
}
