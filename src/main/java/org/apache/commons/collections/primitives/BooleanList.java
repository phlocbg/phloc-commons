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
import javax.annotation.Nullable;

/**
 * An ordered collection of <code>byte</code> values.
 *
 * @see org.apache.commons.collections.primitives.adapters.BooleanListList
 * @see org.apache.commons.collections.primitives.adapters.ListBooleanList
 * @since Commons Primitives 1.1
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 */
public interface BooleanList extends BooleanCollection
{
  /**
   * Appends the specified element to the end of me (optional operation).
   * Returns <code>true</code> if I changed as a result of this call.
   * <p/>
   * If a collection refuses to add the specified element for any reason other
   * than that it already contains the element, it <i>must</i> throw an
   * exception (rather than simply returning <tt>false</tt>). This preserves the
   * invariant that a collection always contains the specified element after
   * this call returns.
   *
   * @param aElement
   *        the value whose presence within me is to be ensured
   * @return <code>true</code> if I changed as a result of this call
   * @throws UnsupportedOperationException
   *         when this operation is not supported
   * @throws IllegalArgumentException
   *         may be thrown if some aspect of the specified element prevents it
   *         from being added to me
   */
  boolean add (boolean aElement);

  /**
   * Inserts the specified element at the specified position (optional
   * operation). Shifts the element currently at that position (if any) and any
   * subsequent elements to the right, increasing their indices.
   *
   * @param nIndex
   *        the index at which to insert the element
   * @param aElement
   *        the value to insert
   * @throws UnsupportedOperationException
   *         when this operation is not supported
   * @throws IllegalArgumentException
   *         if some aspect of the specified element prevents it from being
   *         added to me
   * @throws IndexOutOfBoundsException
   *         if the specified index is out of range
   */
  void add (int nIndex, boolean aElement);

  /**
   * Inserts all of the elements in the specified collection into me, at the
   * specified position (optional operation). Shifts the element currently at
   * that position (if any) and any subsequent elements to the right, increasing
   * their indices. The new elements will appear in the order that they are
   * returned by the given collection's {@link BooleanCollection#iterator iterator}.
   *
   * @param index
   *        the index at which to insert the first element from the specified
   *        collection
   * @param aCollection
   *        the {@link BooleanCollection ByteCollection} of elements to add
   * @return <code>true</code> if I changed as a result of this call
   * @throws UnsupportedOperationException
   *         when this operation is not supported
   * @throws IndexOutOfBoundsException
   *         if the specified index is out of range
   */
  boolean addAll (int index, @Nonnull BooleanCollection aCollection);

  /**
   * Returns <code>true</code> if <i>that</i> is an <code>BooleanList</code>
   * that contains the same elements in the same order as me. In other words,
   * returns <code>true</code> if <i>that</i> is a <code>BooleanList</code> that
   * has the same {@link #size() size} as me, and for which the elements
   * returned by its {@link BooleanList#iterator iterator} are equal (
   * <code>==</code>) to the corresponding elements within me. (This contract
   * ensures that this method works properly across different implementations of
   * the <code>BooleanList</code> interface.)
   *
   * @param that
   *        the object to compare to me
   * @return <code>true</code> if <i>that</i> is an <code>BooleanList</code>
   *         that contains the same elements in the same order as me
   */
  boolean equals (@Nullable Object that);

  /**
   * Returns the value of the element at the specified position within me.
   *
   * @param index
   *        the index of the element to return
   * @return the value of the element at the specified position
   * @throws IndexOutOfBoundsException
   *         if the specified index is out of range
   */
  boolean get (int index);

  /**
   * The hash code of an {@link BooleanList} is defined to be the result of the
   * following calculation:
   *
   * <pre>
   * int hash = 1;
   * for (BooleanIterator iter = iterator (); iter.hasNext ();)
   * {
   *   boolean value = iter.next ();
   *   hash = 31 * hash + (int) (value &circ; (value &gt;&gt;&gt; 32));
   * }
   * </pre>
   * <p />
   * This contract ensures that this method is consistent with {@link #equals
   * equals} and with the {@link java.util.List#hashCode hashCode} method of a
   * {@link java.util.List List} of {@link Boolean} s.
   *
   * @return my hash code
   */
  int hashCode ();

  /**
   * Returns the index of the first occurrence of the specified element within
   * me, or <code>-1</code> if I do not contain the element.
   *
   * @param aElement
   *        the element to search for
   * @return the smallest index of an element matching the specified value, or
   *         <code>-1</code> if no such matching element can be found
   */
  int indexOf (boolean aElement);

  /**
   * Returns an {@link BooleanIterator iterator} over all my elements, in the
   * appropriate sequence.
   *
   * @return an {@link BooleanIterator iterator} over all my elements.
   */
  @Nonnull
  BooleanIterator iterator ();

  /**
   * Returns the index of the last occurrence of the specified element within
   * me, or -1 if I do not contain the element.
   *
   * @param aElement
   *        the element to search for
   * @return the largest index of an element matching the specified value, or
   *         <code>-1</code> if no such matching element can be found
   */
  int lastIndexOf (boolean aElement);

  /**
   * @return a {@link BooleanListIterator bidirectional iterator} over all my
   * elements, in the appropriate sequence.
   */
  @Nonnull
  BooleanListIterator listIterator ();

  /**
   * @param nIndex
   *          Starting index to return the iterator from  
   * @return a {@link BooleanListIterator bidirectional iterator} over all my
   * elements, in the appropriate sequence, starting at the specified position.
   * The specified <i>index</i> indicates the first element that would be
   * returned by an initial call to the {@link BooleanListIterator#next next}
   * method. An initial call to the {@link BooleanListIterator#previous previous}
   * method would return the element with the specified <i>index</i> minus one.
   *
   * @throws IndexOutOfBoundsException
   *         if the specified index is out of range
   */
  @Nonnull
  BooleanListIterator listIterator (int nIndex);

  /**
   * Removes the element at the specified position in (optional operation). Any
   * subsequent elements are shifted to the left, subtracting one from their
   * indices. Returns the element that was removed.
   *
   * @param nIndex
   *        the index of the element to remove
   * @return the value of the element that was removed
   * @throws UnsupportedOperationException
   *         when this operation is not supported
   * @throws IndexOutOfBoundsException
   *         if the specified index is out of range
   */
  boolean removeElementAt (int nIndex);

  /**
   * Replaces the element at the specified position in me with the specified
   * element (optional operation).
   *
   * @param nIndex
   *        the index of the element to change
   * @param aElement
   *        the value to be stored at the specified position
   * @return the value previously stored at the specified position
   * @throws UnsupportedOperationException
   *         when this operation is not supported
   * @throws IndexOutOfBoundsException
   *         if the specified index is out of range
   */
  boolean set (int nIndex, boolean aElement);

  /**
   * Returns a view of the elements within me between the specified
   * <i>fromIndex</i>, inclusive, and <i>toIndex</i>, exclusive. The returned
   * <code>BooleanList</code> is backed by me, so that any changes in the
   * returned list are reflected in me, and vice-versa. The returned list
   * supports all of the optional operations that I support.
   * <p/>
   * Note that when <code><i>fromIndex</i> == <i>toIndex</i></code>, the
   * returned list is initially empty, and when <code><i>fromIndex</i> == 0
   * && <i>toIndex</i> == {@link #size() size()}</code> the returned list is my
   * "improper" sublist, containing all my elements.
   * <p/>
   * The semantics of the returned list become undefined if I am structurally
   * modified in any way other than via the returned list.
   *
   * @param nFromIndex
   *        the smallest index (inclusive) in me that appears in the returned
   *        list
   * @param nToIndex
   *        the largest index (exclusive) in me that appears in the returned
   *        list
   * @return a view of this list from <i>fromIndex</i> (inclusive) to
   *         <i>toIndex</i> (exclusive)
   * @throws IndexOutOfBoundsException
   *         if either specified index is out of range
   */
  @Nonnull
  BooleanList subList (int nFromIndex, int nToIndex);
}
