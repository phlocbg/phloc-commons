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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.hash.HashCodeGenerator;

/**
 * Abstract base class for {@link FloatList}s backed by random access structures
 * like arrays.
 * <p />
 * Read-only subclasses must override {@link #get} and {@link #size}. Mutable
 * subclasses should also override {@link #set}. Variably-sized subclasses
 * should also override {@link #add(float)} and {@link #removeElementAt}. All
 * other methods have at least some base implementation derived from these.
 * Subclasses may choose to override these methods to provide a more efficient
 * implementation.
 *
 * @since Commons Primitives 1.2
 * @version $Revision: 480460 $
 */
public abstract class RandomAccessFloatList extends AbstractFloatCollection implements FloatList
{

  // constructors
  // -------------------------------------------------------------------------

  /** Constructs an empty list. */
  protected RandomAccessFloatList ()
  {}

  // fully abstract methods
  // -------------------------------------------------------------------------

  public abstract float get (int nIndex);

  @Override
  public abstract int size ();

  // unsupported in base
  // -------------------------------------------------------------------------

  /**
   * Unsupported in this implementation.
   *
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public float removeElementAt (final int index)
  {
    throw new UnsupportedOperationException ();
  }

  /**
   * Unsupported in this implementation.
   *
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public float set (final int index, final float aElement)
  {
    throw new UnsupportedOperationException ();
  }

  /**
   * Unsupported in this implementation.
   *
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public void add (final int index, final float aElement)
  {
    throw new UnsupportedOperationException ();
  }

  // -------------------------------------------------------------------------

  // javadocs here are inherited

  @Override
  public boolean add (final float aElement)
  {
    add (size (), aElement);
    return true;
  }

  public boolean addAll (final int nIndex, @Nonnull final FloatCollection aCollection)
  {
    int index = nIndex;
    boolean bModified = false;
    for (final FloatIterator iter = aCollection.iterator (); iter.hasNext ();)
    {
      add (index++, iter.next ());
      bModified = true;
    }
    return bModified;
  }

  public int indexOf (final float aElement)
  {
    int i = 0;
    for (final FloatIterator iter = iterator (); iter.hasNext ();)
    {
      if (iter.next () == aElement)
        return i;
      i++;
    }
    return -1;
  }

  public int lastIndexOf (final float aElement)
  {
    for (final FloatListIterator iter = listIterator (size ()); iter.hasPrevious ();)
      if (iter.previous () == aElement)
        return iter.nextIndex ();
    return -1;
  }

  @Override
  public FloatIterator iterator ()
  {
    return listIterator ();
  }

  public FloatListIterator listIterator ()
  {
    return listIterator (0);
  }

  public FloatListIterator listIterator (final int nIndex)
  {
    return new RandomAccessFloatListIterator (this, nIndex);
  }

  public FloatList subList (final int nFromIndex, final int nToIndex)
  {
    return new RandomAccessFloatSubList (this, nFromIndex, nToIndex);
  }

  @Override
  public boolean equals (@Nullable final Object that)
  {
    if (this == that)
      return true;
    if (!(that instanceof FloatList))
      return false;
    final FloatList thatList = (FloatList) that;
    if (size () != thatList.size ())
      return false;
    for (FloatIterator thatIter = thatList.iterator (), thisIter = iterator (); thisIter.hasNext ();)
      if (thisIter.next () != thatIter.next ())
        return false;
    return true;
  }

  @Override
  public int hashCode ()
  {
    final HashCodeGenerator aHC = new HashCodeGenerator (this);
    for (final FloatIterator iter = iterator (); iter.hasNext ();)
      aHC.append (iter.next ());
    return aHC.getHashCode ();
  }

  @Override
  public String toString ()
  {
    final StringBuilder buf = new StringBuilder ();
    buf.append ('[');
    for (final FloatIterator iter = iterator (); iter.hasNext ();)
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

  /** Get my count of structural modifications. */
  protected int getModCount ()
  {
    return _modCount;
  }

  /** Increment my count of structural modifications. */
  protected void incrModCount ()
  {
    _modCount++;
  }

  // attributes
  // -------------------------------------------------------------------------

  private int _modCount = 0;

  // inner classes
  // -------------------------------------------------------------------------

  private static class ComodChecker
  {
    private RandomAccessFloatList _source = null;
    private int _expectedModCount = -1;

    ComodChecker (final RandomAccessFloatList source)
    {
      _source = source;
      resyncModCount ();
    }

    protected RandomAccessFloatList getList ()
    {
      return _source;
    }

    protected void assertNotComodified () throws ConcurrentModificationException
    {
      if (_expectedModCount != getList ().getModCount ())
        throw new ConcurrentModificationException ();
    }

    protected void resyncModCount ()
    {
      _expectedModCount = getList ().getModCount ();
    }
  }

  protected static class RandomAccessFloatListIterator extends ComodChecker implements FloatListIterator
  {
    private int _nextIndex = 0;
    private int _lastReturnedIndex = -1;

    RandomAccessFloatListIterator (final RandomAccessFloatList list, final int index)
    {
      super (list);
      if (index < 0 || index > getList ().size ())
        throw new IndexOutOfBoundsException ("Index " + index + " not in [0," + getList ().size () + ")");
      _nextIndex = index;
      resyncModCount ();
    }

    public boolean hasNext ()
    {
      assertNotComodified ();
      return _nextIndex < getList ().size ();
    }

    public boolean hasPrevious ()
    {
      assertNotComodified ();
      return _nextIndex > 0;
    }

    public int nextIndex ()
    {
      assertNotComodified ();
      return _nextIndex;
    }

    public int previousIndex ()
    {
      assertNotComodified ();
      return _nextIndex - 1;
    }

    public float next ()
    {
      assertNotComodified ();
      if (!hasNext ())
        throw new NoSuchElementException ();
      final float val = getList ().get (_nextIndex);
      _lastReturnedIndex = _nextIndex;
      _nextIndex++;
      return val;
    }

    public float previous ()
    {
      assertNotComodified ();
      if (!hasPrevious ())
        throw new NoSuchElementException ();
      final float val = getList ().get (_nextIndex - 1);
      _lastReturnedIndex = _nextIndex - 1;
      _nextIndex--;
      return val;
    }

    public void add (final float value)
    {
      assertNotComodified ();
      getList ().add (_nextIndex, value);
      _nextIndex++;
      _lastReturnedIndex = -1;
      resyncModCount ();
    }

    public void remove ()
    {
      assertNotComodified ();
      if (_lastReturnedIndex == -1)
      {
        throw new IllegalStateException ();
      }
      if (_lastReturnedIndex == _nextIndex)
      {
        // remove() following previous()
        getList ().removeElementAt (_lastReturnedIndex);
      }
      else
      {
        // remove() following next()
        getList ().removeElementAt (_lastReturnedIndex);
        _nextIndex--;
      }
      _lastReturnedIndex = -1;
      resyncModCount ();
    }

    public void set (final float value)
    {
      assertNotComodified ();
      if (-1 == _lastReturnedIndex)
        throw new IllegalStateException ();
      getList ().set (_lastReturnedIndex, value);
      resyncModCount ();
    }
  }

  protected static class RandomAccessFloatSubList extends RandomAccessFloatList
  {
    private int _offset = 0;
    private int _limit = 0;
    private RandomAccessFloatList _list = null;
    private ComodChecker _comod = null;

    RandomAccessFloatSubList (final RandomAccessFloatList list, final int fromIndex, final int toIndex)
    {
      if (fromIndex < 0 || toIndex > list.size ())
        throw new IndexOutOfBoundsException ();
      if (fromIndex > toIndex)
        throw new IllegalArgumentException ();
      _list = list;
      _offset = fromIndex;
      _limit = toIndex - fromIndex;
      _comod = new ComodChecker (list);
      _comod.resyncModCount ();
    }

    @Override
    public float get (final int index)
    {
      checkRange (index);
      _comod.assertNotComodified ();
      return _list.get (toUnderlyingIndex (index));
    }

    @Override
    public float removeElementAt (final int index)
    {
      checkRange (index);
      _comod.assertNotComodified ();
      final float val = _list.removeElementAt (toUnderlyingIndex (index));
      _limit--;
      _comod.resyncModCount ();
      incrModCount ();
      return val;
    }

    @Override
    public float set (final int index, final float element)
    {
      checkRange (index);
      _comod.assertNotComodified ();
      final float val = _list.set (toUnderlyingIndex (index), element);
      incrModCount ();
      _comod.resyncModCount ();
      return val;
    }

    @Override
    public void add (final int index, final float element)
    {
      checkRangeIncludingEndpoint (index);
      _comod.assertNotComodified ();
      _list.add (toUnderlyingIndex (index), element);
      _limit++;
      _comod.resyncModCount ();
      incrModCount ();
    }

    @Override
    public int size ()
    {
      _comod.assertNotComodified ();
      return _limit;
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
      return (index + _offset);
    }
  }
}
