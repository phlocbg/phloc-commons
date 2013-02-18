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
package org.apache.commons.collections.primitives;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * An {@link IntList} backed by an array of unsigned <code>short</code> values.
 * This list stores <code>int</code> values in the range [{@link #MIN_VALUE
 * <code>0</code>}, {@link #MAX_VALUE <code>65535</code>}] in 16-bits per
 * element. Attempts to use elements outside this range may cause an
 * {@link IllegalArgumentException IllegalArgumentException} to be thrown.
 * <p />
 * This implementation supports all optional methods.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
@SuppressFBWarnings ("SE_NO_SERIALVERSIONID")
public class ArrayUnsignedShortList extends RandomAccessIntList implements Serializable
{
  /** The maximum possible unsigned 16-bit value (<code>0xFFFF</code>). */
  public static final int MAX_VALUE = 0xFFFF;

  /** The minimum possible unsigned 16-bit value (<code>0x0000</code>). */
  public static final int MIN_VALUE = 0;

  private transient short [] m_aData;
  private int m_nSize;

  /**
   * Construct an empty list with the default initial capacity.
   */
  public ArrayUnsignedShortList ()
  {
    this (8);
  }

  /**
   * Construct an empty list with the given initial capacity.
   * 
   * @param nInitialCapacity
   *        The initial capacity to allocate. Must be &gt; 0.
   * @throws IllegalArgumentException
   *         when <i>initialCapacity</i> is negative
   */
  public ArrayUnsignedShortList (final int nInitialCapacity)
  {
    if (nInitialCapacity < 0)
      throw new IllegalArgumentException ("capacity " + nInitialCapacity);
    m_aData = new short [nInitialCapacity];
    m_nSize = 0;
  }

  /**
   * Constructs a list containing the elements of the given collection, in the
   * order they are returned by that collection's iterator.
   * 
   * @see ArrayIntList#addAll(org.apache.commons.collections.primitives.IntCollection)
   * @param that
   *        the non-<code>null</code> collection of <code>int</code>s to add
   * @throws NullPointerException
   *         if <i>that</i> is <code>null</code>
   */
  public ArrayUnsignedShortList (final IntCollection that)
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
  public ArrayUnsignedShortList (final int [] array)
  {
    this (array.length);
    for (int i = 0; i < array.length; i++)
    {
      m_aData[i] = _fromInt (array[i]);
    }
    m_nSize = array.length;
  }

  // IntList methods
  // -------------------------------------------------------------------------

  /**
   * Returns the element at the specified position within me. By construction,
   * the returned value will be between {@link #MIN_VALUE} and
   * {@link #MAX_VALUE}, inclusive.
   * 
   * @param index
   *        the index of the element to return
   * @return the value of the element at the specified position
   * @throws IndexOutOfBoundsException
   *         if the specified index is out of range
   */
  @Override
  public int get (final int index)
  {
    _checkRange (index);
    return _toInt (m_aData[index]);
  }

  @Override
  public int size ()
  {
    return m_nSize;
  }

  /**
   * Removes the element at the specified position in (optional operation). Any
   * subsequent elements are shifted to the left, subtracting one from their
   * indices. Returns the element that was removed. By construction, the
   * returned value will be between {@link #MIN_VALUE} and {@link #MAX_VALUE},
   * inclusive.
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
  public int removeElementAt (final int index)
  {
    _checkRange (index);
    incrModCount ();
    final int oldval = _toInt (m_aData[index]);
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
   * element (optional operation). Throws {@link IllegalArgumentException} if
   * <i>element</i> is less than {@link #MIN_VALUE} or greater than
   * {@link #MAX_VALUE}.
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
  public int set (final int index, final int element)
  {
    _assertValidUnsignedShort (element);
    _checkRange (index);
    incrModCount ();
    final int oldval = _toInt (m_aData[index]);
    m_aData[index] = _fromInt (element);
    return oldval;
  }

  /**
   * Inserts the specified element at the specified position (optional
   * operation). Shifts the element currently at that position (if any) and any
   * subsequent elements to the right, increasing their indices. Throws
   * {@link IllegalArgumentException} if <i>element</i> is less than
   * {@link #MIN_VALUE} or greater than {@link #MAX_VALUE}.
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
  public void add (final int index, final int element)
  {
    _assertValidUnsignedShort (element);
    _checkRangeIncludingEndpoint (index);
    incrModCount ();
    ensureCapacity (m_nSize + 1);
    final int numtomove = m_nSize - index;
    System.arraycopy (m_aData, index, m_aData, index + 1, numtomove);
    m_aData[index] = _fromInt (element);
    m_nSize++;
  }

  @Override
  public void clear ()
  {
    incrModCount ();
    m_nSize = 0;
  }

  // capacity methods
  // -------------------------------------------------------------------------

  /**
   * Increases my capacity, if necessary, to ensure that I can hold at least the
   * number of elements specified by the minimum capacity argument without
   * growing.
   * 
   * @param nMinCap
   *        minimum capacity
   */
  public void ensureCapacity (final int nMinCap)
  {
    incrModCount ();
    if (nMinCap > m_aData.length)
    {
      final int newcap = (m_aData.length * 3) / 2 + 1;
      final short [] olddata = m_aData;
      m_aData = new short [newcap < nMinCap ? nMinCap : newcap];
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
      final short [] olddata = m_aData;
      m_aData = new short [m_nSize];
      System.arraycopy (olddata, 0, m_aData, 0, m_nSize);
    }
  }

  // private methods
  // -------------------------------------------------------------------------

  private static final int _toInt (final short value)
  {
    return value & MAX_VALUE;
  }

  private static final short _fromInt (final int value)
  {
    return (short) (value & MAX_VALUE);
  }

  private static final void _assertValidUnsignedShort (final int value) throws IllegalArgumentException
  {
    if (value > MAX_VALUE)
    {
      throw new IllegalArgumentException (value + " > " + MAX_VALUE);
    }
    if (value < MIN_VALUE)
    {
      throw new IllegalArgumentException (value + " < " + MIN_VALUE);
    }
  }

  private void writeObject (final ObjectOutputStream out) throws IOException
  {
    out.defaultWriteObject ();
    out.writeInt (m_aData.length);
    for (int i = 0; i < m_nSize; i++)
      out.writeShort (m_aData[i]);
  }

  private void readObject (final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject ();
    m_aData = new short [in.readInt ()];
    for (int i = 0; i < m_nSize; i++)
      m_aData[i] = in.readShort ();
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
