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

import org.apache.commons.collections.primitives.decorators.UnmodifiableByteIterator;
import org.apache.commons.collections.primitives.decorators.UnmodifiableByteList;
import org.apache.commons.collections.primitives.decorators.UnmodifiableByteListIterator;

/**
 * This class consists exclusively of static methods that operate on or return
 * ByteCollections.
 * <p>
 * The methods of this class all throw a NullPoByteerException if the provided
 * collection is null.
 * 
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public final class ByteCollections
{

  /**
   * Returns an unmodifiable ByteList containing only the specified element.
   * 
   * @param value
   *        the single value
   * @return an unmodifiable ByteList containing only the specified element.
   */
  public static ByteList singletonByteList (final byte value)
  {
    // TODO: a specialized implementation of ByteList may be more performant
    final ByteList list = new ArrayByteList (1);
    list.add (value);
    return UnmodifiableByteList.wrap (list);
  }

  /**
   * Returns an unmodifiable ByteIterator containing only the specified element.
   * 
   * @param value
   *        the single value
   * @return an unmodifiable ByteIterator containing only the specified element.
   */
  public static ByteIterator singletonByteIterator (final byte value)
  {
    return singletonByteList (value).iterator ();
  }

  /**
   * Returns an unmodifiable ByteListIterator containing only the specified
   * element.
   * 
   * @param value
   *        the single value
   * @return an unmodifiable ByteListIterator containing only the specified
   *         element.
   */
  public static ByteListIterator singletonByteListIterator (final byte value)
  {
    return singletonByteList (value).listIterator ();
  }

  /**
   * Returns an unmodifiable version of the given non-null ByteList.
   * 
   * @param list
   *        the non-null ByteList to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null ByteList
   * @throws NullPointerException
   *         if the given ByteList is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableByteList#wrap
   */
  public static ByteList unmodifiableByteList (final ByteList list) throws NullPointerException
  {
    if (null == list)
    {
      throw new NullPointerException ();
    }
    return UnmodifiableByteList.wrap (list);
  }

  /**
   * Returns an unmodifiable version of the given non-null ByteIterator.
   * 
   * @param iter
   *        the non-null ByteIterator to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null ByteIterator
   * @throws NullPointerException
   *         if the given ByteIterator is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableByteIterator#wrap
   */
  public static ByteIterator unmodifiableByteIterator (final ByteIterator iter)
  {
    if (null == iter)
    {
      throw new NullPointerException ();
    }
    return UnmodifiableByteIterator.wrap (iter);
  }

  /**
   * Returns an unmodifiable version of the given non-null ByteListIterator.
   * 
   * @param iter
   *        the non-null ByteListIterator to wrap in an unmodifiable decorator
   * @return an unmodifiable version of the given non-null ByteListIterator
   * @throws NullPointerException
   *         if the given ByteListIterator is null
   * @see org.apache.commons.collections.primitives.decorators.UnmodifiableByteListIterator#wrap
   */
  public static ByteListIterator unmodifiableByteListIterator (final ByteListIterator iter)
  {
    if (null == iter)
    {
      throw new NullPointerException ();
    }
    return UnmodifiableByteListIterator.wrap (iter);
  }

  /**
   * Returns an unmodifiable, empty ByteList.
   * 
   * @return an unmodifiable, empty ByteList.
   * @see #EMPTY_BYTE_LIST
   */
  public static ByteList getEmptyByteList ()
  {
    return EMPTY_BYTE_LIST;
  }

  /**
   * Returns an unmodifiable, empty ByteIterator
   * 
   * @return an unmodifiable, empty ByteIterator.
   * @see #EMPTY_BYTE_ITERATOR
   */
  public static ByteIterator getEmptyByteIterator ()
  {
    return EMPTY_BYTE_ITERATOR;
  }

  /**
   * Returns an unmodifiable, empty ByteListIterator
   * 
   * @return an unmodifiable, empty ByteListIterator.
   * @see #EMPTY_BYTE_LIST_ITERATOR
   */
  public static ByteListIterator getEmptyByteListIterator ()
  {
    return EMPTY_BYTE_LIST_ITERATOR;
  }

  /**
   * An unmodifiable, empty ByteList
   * 
   * @see #getEmptyByteList
   */
  public static final ByteList EMPTY_BYTE_LIST = unmodifiableByteList (new ArrayByteList (0));

  /**
   * An unmodifiable, empty ByteIterator
   * 
   * @see #getEmptyByteIterator
   */
  public static final ByteIterator EMPTY_BYTE_ITERATOR = unmodifiableByteIterator (EMPTY_BYTE_LIST.iterator ());

  /**
   * An unmodifiable, empty ByteListIterator
   * 
   * @see #getEmptyByteListIterator
   */
  public static final ByteListIterator EMPTY_BYTE_LIST_ITERATOR = unmodifiableByteListIterator (EMPTY_BYTE_LIST.listIterator ());
}
