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
 * Abstract base class for {@link ZZZList}s backed by random access structures
 * like arrays.
 * <p />
 * Read-only subclasses must override {@link #get} and {@link #size}. Mutable
 * subclasses should also override {@link #set}. Variably-sized subclasses
 * should also override {@link #add(YYY)} and {@link #removeElementAt}. All
 * other methods have at least some base implementation derived from these.
 * Subclasses may choose to override these methods to provide a more efficient
 * implementation.
 *
 * @since Commons Primitives 1.2
 * @version $Revision: 480460 $
 */
public abstract class RandomAccessZZZList extends AbstractZZZCollection implements ZZZList
{
  private int m_nModCount = 0;

  // constructors
  // -------------------------------------------------------------------------

  /** Constructs an empty list. */
  protected RandomAccessZZZList ()
  {}

  // fully abstract methods
  // -------------------------------------------------------------------------

  public abstract YYY get (int nIndex);

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
  public YYY removeElementAt (final int index)
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
  public YYY set (final int index, final YYY aElement)
  {
    throw new UnsupportedOperationException ();
  }

  /**
   * Unsupported in this implementation.
   *
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public void add (final int index, final YYY aElement)
  {
    throw new UnsupportedOperationException ();
  }

  // -------------------------------------------------------------------------

  // javadocs here are inherited

  @Override
  public boolean add (final YYY aElement)
  {
    add (size (), aElement);
    return true;
  }

  public boolean addAll (final int nIndex, @Nonnull final ZZZCollection aCollection)
  {
    int index = nIndex;
    boolean bModified = false;
    for (final ZZZIterator iter = aCollection.iterator (); iter.hasNext ();)
    {
      add (index++, iter.next ());
      bModified = true;
    }
    return bModified;
  }

  public int indexOf (final YYY aElement)
  {
    int i = 0;
    for (final ZZZIterator iter = iterator (); iter.hasNext ();)
    {
      if (iter.next () == aElement)
        return i;
      i++;
    }
    return -1;
  }

  public int lastIndexOf (final YYY aElement)
  {
    for (final ZZZListIterator iter = listIterator (size ()); iter.hasPrevious ();)
      if (iter.previous () == aElement)
        return iter.nextIndex ();
    return -1;
  }

  @Override
  public ZZZIterator iterator ()
  {
    return listIterator ();
  }

  public ZZZListIterator listIterator ()
  {
    return listIterator (0);
  }

  public ZZZListIterator listIterator (final int nIndex)
  {
    return new RandomAccessXXXListIterator (this, nIndex);
  }

  public ZZZList subList (final int nFromIndex, final int nToIndex)
  {
    return new RandomAccessXXXSubList (this, nFromIndex, nToIndex);
  }

  @Override
  public boolean equals (@Nullable final Object that)
  {
    if (this == that)
      return true;
    if (!(that instanceof ZZZList))
      return false;
    final ZZZList thatList = (ZZZList) that;
    if (size () != thatList.size ())
      return false;
    for (ZZZIterator thatIter = thatList.iterator (), thisIter = iterator (); thisIter.hasNext ();)
      if (thisIter.next () != thatIter.next ())
        return false;
    return true;
  }

  @Override
  public int hashCode ()
  {
    final HashCodeGenerator aHC = new HashCodeGenerator (this);
    for (final ZZZIterator iter = iterator (); iter.hasNext ();)
      aHC.append (iter.next ());
    return aHC.getHashCode ();
  }

  @Override
  public String toString ()
  {
    final StringBuilder buf = new StringBuilder ();
    buf.append ('[');
    for (final ZZZIterator iter = iterator (); iter.hasNext ();)
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
    private RandomAccessZZZList m_aSource;
    private int m_nExpectedModCount = -1;

    ComodChecker (@Nonnull final RandomAccessZZZList source)
    {
      m_aSource = source;
      resyncModCount ();
    }

    @Nonnull
    protected RandomAccessZZZList getList ()
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

  protected static class RandomAccessXXXListIterator extends ComodChecker implements ZZZListIterator
  {
    private int m_nNextIndex = 0;
    private int m_nLastReturnedIndex = -1;

    RandomAccessXXXListIterator (final RandomAccessZZZList list, final int index)
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

    public YYY next ()
    {
      assertNotComodified ();
      if (!hasNext ())
        throw new NoSuchElementException ();
      final YYY val = getList ().get (m_nNextIndex);
      m_nLastReturnedIndex = m_nNextIndex;
      m_nNextIndex++;
      return val;
    }

    public YYY previous ()
    {
      assertNotComodified ();
      if (!hasPrevious ())
        throw new NoSuchElementException ();
      final YYY val = getList ().get (m_nNextIndex - 1);
      m_nLastReturnedIndex = m_nNextIndex - 1;
      m_nNextIndex--;
      return val;
    }

    public void add (final YYY value)
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

    public void set (final YYY value)
    {
      assertNotComodified ();
      if (-1 == m_nLastReturnedIndex)
        throw new IllegalStateException ();
      getList ().set (m_nLastReturnedIndex, value);
      resyncModCount ();
    }
  }

  protected static class RandomAccessXXXSubList extends RandomAccessZZZList
  {
    private int m_nOfs = 0;
    private int m_nLimit = 0;
    private RandomAccessZZZList m_aList;
    private ComodChecker m_aComod;

    RandomAccessXXXSubList (@Nonnull final RandomAccessZZZList list, @Nonnegative final int fromIndex, @Nonnegative  final int toIndex)
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
    public YYY get (final int index)
    {
      checkRange (index);
      m_aComod.assertNotComodified ();
      return m_aList.get (toUnderlyingIndex (index));
    }

    @Override
    public YYY removeElementAt (final int index)
    {
      checkRange (index);
      m_aComod.assertNotComodified ();
      final YYY val = m_aList.removeElementAt (toUnderlyingIndex (index));
      m_nLimit--;
      m_aComod.resyncModCount ();
      incrModCount ();
      return val;
    }

    @Override
    public YYY set (final int index, final YYY element)
    {
      checkRange (index);
      m_aComod.assertNotComodified ();
      final YYY val = m_aList.set (toUnderlyingIndex (index), element);
      incrModCount ();
      m_aComod.resyncModCount ();
      return val;
    }

    @Override
    public void add (final int index, final YYY element)
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
