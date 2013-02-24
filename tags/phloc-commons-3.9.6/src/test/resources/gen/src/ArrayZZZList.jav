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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * An {@link ZZZList} backed by an array of <code>YYY</code>s. This
 * implementation supports all optional methods.
 *
 * @since Commons Primitives 1.1
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 */
@SuppressFBWarnings ("SE_NO_SERIALVERSIONID")
public class ArrayZZZList extends RandomAccessZZZList implements Serializable
{
  private transient YYY [] m_aData;
  private int m_nSize;

  // constructors
  // -------------------------------------------------------------------------

  /**
   * Construct an empty list with the default initial capacity.
   */
  public ArrayZZZList ()
  {
    this (8);
  }

  /**
   * Construct an empty list with the given initial capacity.
   *
   * @param nInitialCapacity
   *          The initial capacity to allocate. Must be &gt; 0.
   * @throws IllegalArgumentException
   *         when <i>initialCapacity</i> is negative
   */
  public ArrayZZZList (final int nInitialCapacity)
  {
    if (nInitialCapacity < 0)
      throw new IllegalArgumentException ("capacity " + nInitialCapacity);
    m_aData = new YYY [nInitialCapacity];
    m_nSize = 0;
  }

  /**
   * Constructs a list containing the elements of the given collection, in the
   * order they are returned by that collection's iterator.
   *
   * @see ArrayZZZList#addAll(ZZZCollection)
   * @param that
   *        the non-<code>null</code> collection of <code>YYY</code>s to add
   * @throws NullPointerException
   *         if <i>that</i> is <code>null</code>
   */
  public ArrayZZZList (@Nonnull final ZZZCollection that)
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
  public ArrayZZZList (@Nonnull final YYY [] array)
  {
    this (array.length);
    System.arraycopy (array, 0, m_aData, 0, array.length);
    m_nSize = array.length;
  }

  // ZZZList methods
  // -------------------------------------------------------------------------

  @Override
  public YYY get (final int index)
  {
    _checkRange (index);
    return m_aData[index];
  }

  @Override
  public int size ()
  {
    return m_nSize;
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
  public YYY removeElementAt (final int index)
  {
    _checkRange (index);
    incrModCount ();
    final YYY oldval = m_aData[index];
    final int numtomove = m_nSize - index - 1;
    if (numtomove > 0)
    {
      System.arraycopy (m_aData, index + 1, m_aData, index, numtomove);
    }
    m_nSize--;
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
  public YYY set (final int index, final YYY element)
  {
    _checkRange (index);
    incrModCount ();
    final YYY oldval = m_aData[index];
    m_aData[index] = element;
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
  public void add (final int index, final YYY element)
  {
    _checkRangeIncludingEndpoint (index);
    incrModCount ();
    ensureCapacity (m_nSize + 1);
    final int numtomove = m_nSize - index;
    System.arraycopy (m_aData, index, m_aData, index + 1, numtomove);
    m_aData[index] = element;
    m_nSize++;
  }

  @Override
  public void clear ()
  {
    incrModCount ();
    m_nSize = 0;
  }

  @Override
  public boolean addAll (@Nonnull final ZZZCollection collection)
  {
    return addAll (size (), collection);
  }

  @Override
  public boolean addAll (final int nIndex, @Nonnull final ZZZCollection collection)
  {
    if (collection.size () == 0)
      return false;

    int index = nIndex;
    _checkRangeIncludingEndpoint (index);
    incrModCount ();
    ensureCapacity (m_nSize + collection.size ());
    if (index != m_nSize)
    {
      // Need to move some elements
      System.arraycopy (m_aData, index, m_aData, index + collection.size (), m_nSize - index);
    }
    for (final ZZZIterator it = collection.iterator (); it.hasNext ();)
    {
      m_aData[index] = it.next ();
      index++;
    }
    m_nSize += collection.size ();
    return true;
  }

  // capacity methods
  // -------------------------------------------------------------------------

  /**
   * Increases my capacity, if necessary, to ensure that I can hold at least the
   * number of elements specified by the minimum capacity argument without
   * growing.
   *
   * @param nMinCap
   *          The new minimum capacity
   */
  public void ensureCapacity (final int nMinCap)
  {
    incrModCount ();
    if (nMinCap > m_aData.length)
    {
      final int newcap = (m_aData.length * 3) / 2 + 1;
      final YYY [] olddata = m_aData;
      m_aData = new YYY [newcap < nMinCap ? nMinCap : newcap];
      System.arraycopy (olddata, 0, m_aData, 0, m_nSize);
    }
  }

  /**
   * Reduce my capacity, if necessary, to match my current {@link #size size}.
   */
  public void trimToSize ()
  {
    incrModCount ();
    if (m_nSize < m_aData.length)
    {
      final YYY [] olddata = m_aData;
      m_aData = new YYY [m_nSize];
      System.arraycopy (olddata, 0, m_aData, 0, m_nSize);
    }
  }

  // private methods
  // -------------------------------------------------------------------------

  private void writeObject (@Nonnull final ObjectOutputStream out) throws IOException
  {
    out.defaultWriteObject ();
    out.writeInt (m_aData.length);
    for (int i = 0; i < m_nSize; i++)
      out.writeZZZ (m_aData[i]);
  }

  private void readObject (@Nonnull final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject ();
    m_aData = new YYY [in.readInt ()];
    for (int i = 0; i < m_nSize; i++)
      m_aData[i] = in.readZZZ ();
  }

  private final void _checkRange (final int index)
  {
    if (index < 0 || index >= m_nSize)
      throw new IndexOutOfBoundsException ("Should be at least 0 and less than " + m_nSize + ", found " + index);
  }

  private final void _checkRangeIncludingEndpoint (final int index)
  {
    if (index < 0 || index > m_nSize)
      throw new IndexOutOfBoundsException ("Should be at least 0 and at most " + m_nSize + ", found " + index);
  }
}
