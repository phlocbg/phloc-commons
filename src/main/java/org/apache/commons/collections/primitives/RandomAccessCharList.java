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

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.hash.HashCodeGenerator;

/**
 * Abstract base class for {@link CharList}s backed by random access structures
 * like arrays.
 * <p />
 * Read-only subclasses must override {@link #get} and {@link #size}. Mutable
 * subclasses should also override {@link #set}. Variably-sized subclasses
 * should also override {@link #add(char)} and {@link #removeElementAt}. All
 * other methods have at least some base implementation derived from these.
 * Subclasses may choose to override these methods to provide a more efficient
 * implementation.
 *
 * @since Commons Primitives 1.2
 * @version $Revision: 480460 $
 */
public abstract class RandomAccessCharList extends AbstractCharCollection implements CharList
{
  private int m_nModCount = 0;

  // constructors
  // -------------------------------------------------------------------------

  /** Constructs an empty list. */
  protected RandomAccessCharList ()
  {}

  // fully abstract methods
  // -------------------------------------------------------------------------

  public abstract char get (int nIndex);

  @Override
  public abstract int size ();

  // unsupported in base
  // -------------------------------------------------------------------------

  /**
   * Unsupported in this implementation.
   *
   * @return never
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public char removeElementAt (final int index)
  {
    throw new UnsupportedOperationException ();
  }

  /**
   * Unsupported in this implementation.
   *
   * @return never
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public char set (final int index, final char aElement)
  {
    throw new UnsupportedOperationException ();
  }

  /**
   * Unsupported in this implementation.
   *
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public void add (final int index, final char aElement)
  {
    throw new UnsupportedOperationException ();
  }

  // -------------------------------------------------------------------------

  // javadocs here are inherited

  @Override
  public boolean add (final char aElement)
  {
    add (size (), aElement);
    return true;
  }

  public boolean addAll (final int nIndex, @Nonnull final CharCollection aCollection)
  {
    int index = nIndex;
    boolean bModified = false;
    for (final CharIterator iter = aCollection.iterator (); iter.hasNext ();)
    {
      add (index++, iter.next ());
      bModified = true;
    }
    return bModified;
  }

  public int indexOf (final char aElement)
  {
    int i = 0;
    for (final CharIterator iter = iterator (); iter.hasNext ();)
    {
      if (iter.next () == aElement)
        return i;
      i++;
    }
    return -1;
  }

  public int lastIndexOf (final char aElement)
  {
    for (final CharListIterator iter = listIterator (size ()); iter.hasPrevious ();)
      if (iter.previous () == aElement)
        return iter.nextIndex ();
    return -1;
  }

  @Override
  public CharIterator iterator ()
  {
    return listIterator ();
  }

  public CharListIterator listIterator ()
  {
    return listIterator (0);
  }

  public CharListIterator listIterator (final int nIndex)
  {
    return new RandomAccessCharacterListIterator (this, nIndex);
  }

  public CharList subList (final int nFromIndex, final int nToIndex)
  {
    return new RandomAccessCharacterSubList (this, nFromIndex, nToIndex);
  }

  @Override
  public boolean equals (@Nullable final Object that)
  {
    if (this == that)
      return true;
    if (!(that instanceof CharList))
      return false;
    final CharList thatList = (CharList) that;
    if (size () != thatList.size ())
      return false;
    for (CharIterator thatIter = thatList.iterator (), thisIter = iterator (); thisIter.hasNext ();)
      if (thisIter.next () != thatIter.next ())
        return false;
    return true;
  }

  @Override
  public int hashCode ()
  {
    final HashCodeGenerator aHC = new HashCodeGenerator (this);
    for (final CharIterator iter = iterator (); iter.hasNext ();)
      aHC.append (iter.next ());
    return aHC.getHashCode ();
  }

  @Override
  public String toString ()
  {
    final StringBuilder buf = new StringBuilder ();
    buf.append ('[');
    for (final CharIterator iter = iterator (); iter.hasNext ();)
    {
      buf.append (iter.next ());
      if (iter.hasNext ())
      {
        buf.append (", ");
      }
    }
    buf.append (']');
    return buf.toString ();
  }

  // protected utilities
  // -------------------------------------------------------------------------

  /** 
   * @return my count of structural modifications.
   */
  protected int getModCount ()
  {
    return m_nModCount;
  }

  /** Increment my count of structural modifications. */
  protected void incrModCount ()
  {
    m_nModCount++;
  }

  // inner classes
  // -------------------------------------------------------------------------

  private static class ComodChecker
  {
    private RandomAccessCharList m_aSource;
    private int m_nExpectedModCount = -1;

    ComodChecker (@Nonnull final RandomAccessCharList source)
    {
      m_aSource = source;
      resyncModCount ();
    }

    @Nonnull
    protected RandomAccessCharList getList ()
    {
      return m_aSource;
    }

    protected void assertNotComodified () throws ConcurrentModificationException
    {
      if (m_nExpectedModCount != getList ().getModCount ())
        throw new ConcurrentModificationException ();
    }

    protected void resyncModCount ()
    {
      m_nExpectedModCount = getList ().getModCount ();
    }
  }

  protected static class RandomAccessCharacterListIterator extends ComodChecker implements CharListIterator
  {
    private int m_nNextIndex = 0;
    private int m_nLastReturnedIndex = -1;

    RandomAccessCharacterListIterator (final RandomAccessCharList list, final int index)
    {
      super (list);
      if (index < 0 || index > getList ().size ())
        throw new IndexOutOfBoundsException ("Index " + index + " not in [0," + getList ().size () + ")");
      m_nNextIndex = index;
      resyncModCount ();
    }

    public boolean hasNext ()
    {
      assertNotComodified ();
      return m_nNextIndex < getList ().size ();
    }

    public boolean hasPrevious ()
    {
      assertNotComodified ();
      return m_nNextIndex > 0;
    }

    public int nextIndex ()
    {
      assertNotComodified ();
      return m_nNextIndex;
    }

    public int previousIndex ()
    {
      assertNotComodified ();
      return m_nNextIndex - 1;
    }

    public char next ()
    {
      assertNotComodified ();
      if (!hasNext ())
        throw new NoSuchElementException ();
      final char val = getList ().get (m_nNextIndex);
      m_nLastReturnedIndex = m_nNextIndex;
      m_nNextIndex++;
      return val;
    }

    public char previous ()
    {
      assertNotComodified ();
      if (!hasPrevious ())
        throw new NoSuchElementException ();
      final char val = getList ().get (m_nNextIndex - 1);
      m_nLastReturnedIndex = m_nNextIndex - 1;
      m_nNextIndex--;
      return val;
    }

    public void add (final char value)
    {
      assertNotComodified ();
      getList ().add (m_nNextIndex, value);
      m_nNextIndex++;
      m_nLastReturnedIndex = -1;
      resyncModCount ();
    }

    public void remove ()
    {
      assertNotComodified ();
      if (m_nLastReturnedIndex == -1)
      {
        throw new IllegalStateException ();
      }
      if (m_nLastReturnedIndex == m_nNextIndex)
      {
        // remove() following previous()
        getList ().removeElementAt (m_nLastReturnedIndex);
      }
      else
      {
        // remove() following next()
        getList ().removeElementAt (m_nLastReturnedIndex);
        m_nNextIndex--;
      }
      m_nLastReturnedIndex = -1;
      resyncModCount ();
    }

    public void set (final char value)
    {
      assertNotComodified ();
      if (-1 == m_nLastReturnedIndex)
        throw new IllegalStateException ();
      getList ().set (m_nLastReturnedIndex, value);
      resyncModCount ();
    }
  }

  protected static class RandomAccessCharacterSubList extends RandomAccessCharList
  {
    private int m_nOfs = 0;
    private int m_nLimit = 0;
    private RandomAccessCharList m_aList;
    private ComodChecker m_aComod;

    RandomAccessCharacterSubList (@Nonnull final RandomAccessCharList list, @Nonnegative final int fromIndex, @Nonnegative  final int toIndex)
    {
      if (fromIndex < 0 || toIndex > list.size ())
        throw new IndexOutOfBoundsException ();
      if (fromIndex > toIndex)
        throw new IllegalArgumentException ();
      m_aList = list;
      m_nOfs = fromIndex;
      m_nLimit = toIndex - fromIndex;
      m_aComod = new ComodChecker (list);
      m_aComod.resyncModCount ();
    }

    @Override
    public char get (final int index)
    {
      checkRange (index);
      m_aComod.assertNotComodified ();
      return m_aList.get (toUnderlyingIndex (index));
    }

    @Override
    public char removeElementAt (final int index)
    {
      checkRange (index);
      m_aComod.assertNotComodified ();
      final char val = m_aList.removeElementAt (toUnderlyingIndex (index));
      m_nLimit--;
      m_aComod.resyncModCount ();
      incrModCount ();
      return val;
    }

    @Override
    public char set (final int index, final char element)
    {
      checkRange (index);
      m_aComod.assertNotComodified ();
      final char val = m_aList.set (toUnderlyingIndex (index), element);
      incrModCount ();
      m_aComod.resyncModCount ();
      return val;
    }

    @Override
    public void add (final int index, final char element)
    {
      checkRangeIncludingEndpoint (index);
      m_aComod.assertNotComodified ();
      m_aList.add (toUnderlyingIndex (index), element);
      m_nLimit++;
      m_aComod.resyncModCount ();
      incrModCount ();
    }

    @Override
    public int size ()
    {
      m_aComod.assertNotComodified ();
      return m_nLimit;
    }

    private void checkRange (final int index)
    {
      if (index < 0 || index >= size ())
        throw new IndexOutOfBoundsException ("index " + index + " not in [0," + size () + ")");
    }

    private void checkRangeIncludingEndpoint (final int index)
    {
      if (index < 0 || index > size ())
        throw new IndexOutOfBoundsException ("index " + index + " not in [0," + size () + "]");
    }

    private int toUnderlyingIndex (final int index)
    {
      return (index + m_nOfs);
    }
  }
}