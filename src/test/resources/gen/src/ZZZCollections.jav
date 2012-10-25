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

import org.apache.commons.collections.primitives.decorators.UnmodifiableZZZIterator;
import org.apache.commons.collections.primitives.decorators.UnmodifiableZZZList;
import org.apache.commons.collections.primitives.decorators.UnmodifiableZZZListIterator;

/**
 * This class consists exclusively of static methods that operate on or return
 * ZZZCollections.
 * <p>
 * The methods of this class all throw a NullPointerException if the provided
 * collection is null.
 * 
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public final class ZZZCollections
{
  private ZZZCollections ()
  {}

  /**
   * Returns an unmodifiable ZZZList containing only the specified element.
   * 
   * @param value
   *        the single value
   * @return an unmodifiable ZZZList containing only the specified element.
   */
  @Nonnull 
  public static ZZZList singletonZZZList (final YYY value)
  {
    // hint: a specialized implementation of ZZZList may be more performant
    final ZZZList list = new ArrayZZZList (1);
    list.add (value);
    return UnmodifiableZZZList.wrap (list);
  }

  /**
   * Returns an unmodifiable ZZZIterator containing only the specified element.
   * 
   * @param value
   *        the single value
   * @return an unmodifiable ZZZIterator containing only the specified element.
   */
  @Nonnull 
  public static ZZZIterator singletonZZZIterator (final YYY value)
  {
    return singletonZZZList (value).iterator ();
  }

  /**
   * Returns an unmodifiable ZZZListIterator containing only the specified
   * element.
   * 
   * @param value
   *        the single value
   * @return an unmodifiable ZZZListIterator containing only the specified
   *         element.
   */
  @Nonnull 
  public static ZZZListIterator singletonZZZListIterator (final YYY value)
  {
    return singletonZZZList (value).listIterator ();
  }

  /**
   * Returns an unmodifiable version of the given non-null ZZZList.
   * 
   * @param list
   *        the non-null ZZZList to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null ZZZList
   * @throws NullPointerException
   *         if the given ZZZList is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableZZZList#wrap
   */
  @Nonnull 
  public static ZZZList unmodifiableZZZList (@Nonnull final ZZZList list) throws NullPointerException
  {
    if (null == list)
      throw new NullPointerException ();
    return UnmodifiableZZZList.wrap (list);
  }

  /**
   * Returns an unmodifiable version of the given non-null ZZZIterator.
   * 
   * @param iter
   *        the non-null ZZZIterator to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null ZZZIterator
   * @throws NullPointerException
   *         if the given ZZZIterator is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableZZZIterator#wrap
   */
  @Nonnull
  public static ZZZIterator unmodifiableZZZIterator (@Nonnull final ZZZIterator iter)
  {
    if (null == iter)
      throw new NullPointerException ();
    return UnmodifiableZZZIterator.wrap (iter);
  }

  /**
   * Returns an unmodifiable version of the given non-null ZZZListIterator.
   * 
   * @param iter
   *        the non-null ZZZListIterator to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null ZZZListIterator
   * @throws NullPointerException
   *         if the given ZZZListIterator is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableZZZListIterator#wrap
   */
  @Nonnull
  public static ZZZListIterator unmodifiableZZZListIterator (@Nonnull final ZZZListIterator iter)
  {
    if (null == iter)
      throw new NullPointerException ();
    return UnmodifiableZZZListIterator.wrap (iter);
  }

  /**
   * Returns an unmodifiable, empty ZZZList.
   * 
   * @return an unmodifiable, empty ZZZList.
   * @see #EMPTY_YYY$UC$_LIST
   */
  @Nonnull
  public static ZZZList getEmptyZZZList ()
  {
    return EMPTY_YYY$UC$_LIST;
  }

  /**
   * Returns an unmodifiable, empty ZZZIterator
   * 
   * @return an unmodifiable, empty ZZZIterator.
   * @see #EMPTY_YYY$UC$_ITERATOR
   */
  @Nonnull
  public static ZZZIterator getEmptyZZZIterator ()
  {
    return EMPTY_YYY$UC$_ITERATOR;
  }

  /**
   * Returns an unmodifiable, empty ZZZListIterator
   * 
   * @return an unmodifiable, empty ZZZListIterator.
   * @see #EMPTY_YYY$UC$_LIST_ITERATOR
   */
  @Nonnull
  public static ZZZListIterator getEmptyZZZListIterator ()
  {
    return EMPTY_YYY$UC$_LIST_ITERATOR;
  }

  /**
   * An unmodifiable, empty ZZZList
   * 
   * @see #getEmptyZZZList
   */
  public static final ZZZList EMPTY_YYY$UC$_LIST = unmodifiableZZZList (new ArrayZZZList (0));

  /**
   * An unmodifiable, empty ZZZIterator
   * 
   * @see #getEmptyZZZIterator
   */
  public static final ZZZIterator EMPTY_YYY$UC$_ITERATOR = unmodifiableZZZIterator (EMPTY_YYY$UC$_LIST.iterator ());

  /**
   * An unmodifiable, empty ZZZListIterator
   * 
   * @see #getEmptyZZZListIterator
   */
  public static final ZZZListIterator EMPTY_YYY$UC$_LIST_ITERATOR = unmodifiableZZZListIterator (EMPTY_YYY$UC$_LIST.listIterator ());
}
