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

/**
 * Abstract base class for {@link ShortList}s backed by random access structures
 * like arrays.
 * <p />
 * Read-only subclasses must override {@link #get} and {@link #size}. Mutable
 * subclasses should also override {@link #set}. Variably-sized subclasses
 * should also override {@link #add(short)} and {@link #removeElementAt}. All
 * other methods have at least some base implementation derived from these.
 * Subclasses may choose to override these methods to provide a more efficient
 * implementation.
 * 
 * @since Commons Primitives 0.1
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class RandomAccessShortList extends AbstractShortCollection implements ShortList
{

  // constructors
  // -------------------------------------------------------------------------

  /** Constructs an empty list. */
  protected RandomAccessShortList ()
  {}

  // fully abstract methods
  // -------------------------------------------------------------------------

  public abstract short get (int index);

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
  public short removeElementAt (final int index)
  {
    throw new UnsupportedOperationException ();
  }

  /**
   * Unsupported in this implementation.
   * 
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public short set (final int index, final short element)
  {
    throw new UnsupportedOperationException ();
  }

  /**
   * Unsupported in this implementation.
   * 
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public void add (final int index, final short element)
  {
    throw new UnsupportedOperationException ();
  }

  // -------------------------------------------------------------------------

  // javadocs here are inherited

  @Override
  public boolean add (final short element)
  {
    add (size (), element);
    return true;
  }

  public boolean addAll (int index, final ShortCollection collection)
  {
    boolean modified = false;
    for (final ShortIterator iter = collection.iterator (); iter.hasNext ();)
    {
      add (index++, iter.next ());
      modified = true;
    }
    return modified;
  }

  public int indexOf (final short element)
  {
    int i = 0;
    for (final ShortIterator iter = iterator (); iter.hasNext ();)
    {
      if (iter.next () == element)
      {
        return i;
      }
      else
      {
        i++;
      }
    }
    return -1;
  }

  public int lastIndexOf (final short element)
  {
    for (final ShortListIterator iter = listIterator (size ()); iter.hasPrevious ();)
    {
      if (iter.previous () == element)
      {
        return iter.nextIndex ();
      }
    }
    return -1;
  }

  @Override
  public ShortIterator iterator ()
  {
    return listIterator ();
  }

  public ShortListIterator listIterator ()
  {
    return listIterator (0);
  }

  public ShortListIterator listIterator (final int index)
  {
    return new RandomAccessShortListIterator (this, index);
  }

  public ShortList subList (final int fromIndex, final int toIndex)
  {
    return new RandomAccessShortSubList (this, fromIndex, toIndex);
  }

  @Override
  public boolean equals (final Object that)
  {
    if (this == that)
    {
      return true;
    }
    else
      if (that instanceof ShortList)
      {
        final ShortList thatList = (ShortList) that;
        if (size () != thatList.size ())
        {
          return false;
        }
        for (ShortIterator thatIter = thatList.iterator (), thisIter = iterator (); thisIter.hasNext ();)
        {
          if (thisIter.next () != thatIter.next ())
          {
            return false;
          }
        }
        return true;
      }
      else
      {
        return false;
      }
  }

  @Override
  public int hashCode ()
  {
    int hash = 1;
    for (final ShortIterator iter = iterator (); iter.hasNext ();)
    {
      hash = 31 * hash + iter.next ();
    }
    return hash;
  }

  @Override
  public String toString ()
  {
    final StringBuffer buf = new StringBuffer ();
    buf.append ("[");
    for (final ShortIterator iter = iterator (); iter.hasNext ();)
    {
      buf.append (iter.next ());
      if (iter.hasNext ())
      {
        buf.append (", ");
      }
    }
    buf.append ("]");
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
    ComodChecker (final RandomAccessShortList source)
    {
      _source = source;
      resyncModCount ();
    }

    protected RandomAccessShortList getList ()
    {
      return _source;
    }

    protected void assertNotComodified () throws ConcurrentModificationException
    {
      if (_expectedModCount != getList ().getModCount ())
      {
        throw new ConcurrentModificationException ();
      }
    }

    protected void resyncModCount ()
    {
      _expectedModCount = getList ().getModCount ();
    }

    private RandomAccessShortList _source = null;
    private int _expectedModCount = -1;
  }

  protected static class RandomAccessShortListIterator extends ComodChecker implements ShortListIterator
  {
    RandomAccessShortListIterator (final RandomAccessShortList list, final int index)
    {
      super (list);
      if (index < 0 || index > getList ().size ())
      {
        throw new IndexOutOfBoundsException ("Index " + index + " not in [0," + getList ().size () + ")");
      }
      else
      {
        _nextIndex = index;
        resyncModCount ();
      }
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

    public short next ()
    {
      assertNotComodified ();
      if (!hasNext ())
      {
        throw new NoSuchElementException ();
      }
      else
      {
        final short val = getList ().get (_nextIndex);
        _lastReturnedIndex = _nextIndex;
        _nextIndex++;
        return val;
      }
    }

    public short previous ()
    {
      assertNotComodified ();
      if (!hasPrevious ())
      {
        throw new NoSuchElementException ();
      }
      else
      {
        final short val = getList ().get (_nextIndex - 1);
        _lastReturnedIndex = _nextIndex - 1;
        _nextIndex--;
        return val;
      }
    }

    public void add (final short value)
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

    public void set (final short value)
    {
      assertNotComodified ();
      if (-1 == _lastReturnedIndex)
      {
        throw new IllegalStateException ();
      }
      else
      {
        getList ().set (_lastReturnedIndex, value);
        resyncModCount ();
      }
    }

    private int _nextIndex = 0;
    private int _lastReturnedIndex = -1;
  }

  protected static class RandomAccessShortSubList extends RandomAccessShortList implements ShortList
  {
    RandomAccessShortSubList (final RandomAccessShortList list, final int fromIndex, final int toIndex)
    {
      if (fromIndex < 0 || toIndex > list.size ())
      {
        throw new IndexOutOfBoundsException ();
      }
      else
        if (fromIndex > toIndex)
        {
          throw new IllegalArgumentException ();
        }
        else
        {
          _list = list;
          _offset = fromIndex;
          _limit = toIndex - fromIndex;
          _comod = new ComodChecker (list);
          _comod.resyncModCount ();
        }
    }

    @Override
    public short get (final int index)
    {
      checkRange (index);
      _comod.assertNotComodified ();
      return _list.get (toUnderlyingIndex (index));
    }

    @Override
    public short removeElementAt (final int index)
    {
      checkRange (index);
      _comod.assertNotComodified ();
      final short val = _list.removeElementAt (toUnderlyingIndex (index));
      _limit--;
      _comod.resyncModCount ();
      incrModCount ();
      return val;
    }

    @Override
    public short set (final int index, final short element)
    {
      checkRange (index);
      _comod.assertNotComodified ();
      final short val = _list.set (toUnderlyingIndex (index), element);
      incrModCount ();
      _comod.resyncModCount ();
      return val;
    }

    @Override
    public void add (final int index, final short element)
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
      {
        throw new IndexOutOfBoundsException ("index " + index + " not in [0," + size () + ")");
      }
    }

    private void checkRangeIncludingEndpoint (final int index)
    {
      if (index < 0 || index > size ())
      {
        throw new IndexOutOfBoundsException ("index " + index + " not in [0," + size () + "]");
      }
    }

    private int toUnderlyingIndex (final int index)
    {
      return (index + _offset);
    }

    private int _offset = 0;
    private int _limit = 0;
    private RandomAccessShortList _list = null;
    private ComodChecker _comod = null;

  }
}
