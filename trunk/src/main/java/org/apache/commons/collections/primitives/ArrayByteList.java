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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.annotation.Nonnull;

/**
 * An {@link ByteList} backed by an array of <code>byte</code>s. This
 * implementation supports all optional methods.
 *
 * @since Commons Primitives 1.1
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 */
public class ArrayByteList extends RandomAccessByteList implements Serializable
{
  private transient byte [] _data = null;
  private int _size = 0;

  // constructors
  // -------------------------------------------------------------------------

  /**
   * Construct an empty list with the default initial capacity.
   */
  public ArrayByteList ()
  {
    this (8);
  }

  /**
   * Construct an empty list with the given initial capacity.
   *
   * @throws IllegalArgumentException
   *         when <i>initialCapacity</i> is negative
   */
  public ArrayByteList (final int initialCapacity)
  {
    if (initialCapacity < 0)
      throw new IllegalArgumentException ("capacity " + initialCapacity);
    _data = new byte [initialCapacity];
    _size = 0;
  }

  /**
   * Constructs a list containing the elements of the given collection, in the
   * order they are returned by that collection's iterator.
   *
   * @see ArrayByteList#addAll(ByteCollection)
   * @param that
   *        the non-<code>null</code> collection of <code>byte</code>s to add
   * @throws NullPointerException
   *         if <i>that</i> is <code>null</code>
   */
  public ArrayByteList (final ByteCollection that)
  {
    this (that.size ());
    addAll (that);
  }

  /**
   * Constructs a list by copying the specified array.
   *
   * @param array
   *        the array to initialize the collection with
   * @throws NullPointerException
   *         if the array is <code>null</code>
   */
  public ArrayByteList (final byte [] array)
  {
    this (array.length);
    System.arraycopy (array, 0, _data, 0, array.length);
    _size = array.length;
  }

  // ByteList methods
  // -------------------------------------------------------------------------

  @Override
  public byte get (final int index)
  {
    _checkRange (index);
    return _data[index];
  }

  @Override
  public int size ()
  {
    return _size;
  }

  /**
   * Removes the element at the specified position in (optional operation). Any
   * subsequent elements are shifted to the left, subtracting one from their
   * indices. Returns the element that was removed.
   *
   * @param index
   *        the index of the element to remove
   * @return the value of the element that was removed
   * @throws UnsupportedOperationException
   *         when this operation is not supported
   * @throws IndexOutOfBoundsException
   *         if the specified index is out of range
   */
  @Override
  public byte removeElementAt (final int index)
  {
    _checkRange (index);
    incrModCount ();
    final byte oldval = _data[index];
    final int numtomove = _size - index - 1;
    if (numtomove > 0)
    {
      System.arraycopy (_data, index + 1, _data, index, numtomove);
    }
    _size--;
    return oldval;
  }

  /**
   * Replaces the element at the specified position in me with the specified
   * element (optional operation).
   *
   * @param index
   *        the index of the element to change
   * @param element
   *        the value to be stored at the specified position
   * @return the value previously stored at the specified position
   * @throws UnsupportedOperationException
   *         when this operation is not supported
   * @throws IndexOutOfBoundsException
   *         if the specified index is out of range
   */
  @Override
  public byte set (final int index, final byte element)
  {
    _checkRange (index);
    incrModCount ();
    final byte oldval = _data[index];
    _data[index] = element;
    return oldval;
  }

  /**
   * Inserts the specified element at the specified position (optional
   * operation). Shifts the element currently at that position (if any) and any
   * subsequent elements to the right, increasing their indices.
   *
   * @param index
   *        the index at which to insert the element
   * @param element
   *        the value to insert
   * @throws UnsupportedOperationException
   *         when this operation is not supported
   * @throws IllegalArgumentException
   *         if some aspect of the specified element prevents it from being
   *         added to me
   * @throws IndexOutOfBoundsException
   *         if the specified index is out of range
   */
  @Override
  public void add (final int index, final byte element)
  {
    _checkRangeIncludingEndpoint (index);
    incrModCount ();
    ensureCapacity (_size + 1);
    final int numtomove = _size - index;
    System.arraycopy (_data, index, _data, index + 1, numtomove);
    _data[index] = element;
    _size++;
  }

  @Override
  public void clear ()
  {
    incrModCount ();
    _size = 0;
  }

  @Override
  public boolean addAll (@Nonnull final ByteCollection collection)
  {
    return addAll (size (), collection);
  }

  @Override
  public boolean addAll (final int nIndex, @Nonnull final ByteCollection collection)
  {
    if (collection.size () == 0)
      return false;

    int index = nIndex;
    _checkRangeIncludingEndpoint (index);
    incrModCount ();
    ensureCapacity (_size + collection.size ());
    if (index != _size)
    {
      // Need to move some elements
      System.arraycopy (_data, index, _data, index + collection.size (), _size - index);
    }
    for (final ByteIterator it = collection.iterator (); it.hasNext ();)
    {
      _data[index] = it.next ();
      index++;
    }
    _size += collection.size ();
    return true;
  }

  // capacity methods
  // -------------------------------------------------------------------------

  /**
   * Increases my capacity, if necessary, to ensure that I can hold at least the
   * number of elements specified by the minimum capacity argument without
   * growing.
   */
  public void ensureCapacity (final int mincap)
  {
    incrModCount ();
    if (mincap > _data.length)
    {
      final int newcap = (_data.length * 3) / 2 + 1;
      final byte [] olddata = _data;
      _data = new byte [newcap < mincap ? mincap : newcap];
      System.arraycopy (olddata, 0, _data, 0, _size);
    }
  }

  /**
   * Reduce my capacity, if necessary, to match my current {@link #size size}.
   */
  public void trimToSize ()
  {
    incrModCount ();
    if (_size < _data.length)
    {
      final byte [] olddata = _data;
      _data = new byte [_size];
      System.arraycopy (olddata, 0, _data, 0, _size);
    }
  }

  // private methods
  // -------------------------------------------------------------------------

  private void writeObject (final ObjectOutputStream out) throws IOException
  {
    out.defaultWriteObject ();
    out.writeInt (_data.length);
    for (int i = 0; i < _size; i++)
    {
      out.writeByte (_data[i]);
    }
  }

  private void readObject (final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject ();
    _data = new byte [in.readInt ()];
    for (int i = 0; i < _size; i++)
      _data[i] = in.readByte ();
  }

  private final void _checkRange (final int index)
  {
    if (index < 0 || index >= _size)
      throw new IndexOutOfBoundsException ("Should be at least 0 and less than " + _size + ", found " + index);
  }

  private final void _checkRangeIncludingEndpoint (final int index)
  {
    if (index < 0 || index > _size)
      throw new IndexOutOfBoundsException ("Should be at least 0 and at most " + _size + ", found " + index);
  }
}
