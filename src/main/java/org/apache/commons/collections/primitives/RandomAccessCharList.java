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
 * @since Commons Primitives 1.0
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class RandomAccessCharList extends AbstractCharCollection implements CharList
{

  // constructors
  // -------------------------------------------------------------------------

  /** Constructs an empty list. */
  protected RandomAccessCharList ()
  {}

  // fully abstract methods
  // -------------------------------------------------------------------------

  public abstract char get (int index);

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
  public char removeElementAt (final int index)
  {
    throw new UnsupportedOperationException ();
  }

  /**
   * Unsupported in this implementation.
   * 
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public char set (final int index, final char element)
  {
    throw new UnsupportedOperationException ();
  }

  /**
   * Unsupported in this implementation.
   * 
   * @throws UnsupportedOperationException
   *         since this method is not supported
   */
  public void add (final int index, final char element)
  {
    throw new UnsupportedOperationException ();
  }

  // -------------------------------------------------------------------------

  // javadocs here are inherited

  @Override
  public boolean add (final char element)
  {
    add (size (), element);
    return true;
  }

  public boolean addAll (int index, final CharCollection collection)
  {
    boolean modified = false;
    for (final CharIterator iter = collection.iterator (); iter.hasNext ();)
    {
      add (index++, iter.next ());
      modified = true;
    }
    return modified;
  }

  public int indexOf (final char element)
  {
    int i = 0;
    for (final CharIterator iter = iterator (); iter.hasNext ();)
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

  public int lastIndexOf (final char element)
  {
    for (final CharListIterator iter = listIterator (size ()); iter.hasPrevious ();)
    {
      if (iter.previous () == element)
      {
        return iter.nextIndex ();
      }
    }
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

  public CharListIterator listIterator (final int index)
  {
    return new RandomAccessCharListIterator (this, index);
  }

  public CharList subList (final int fromIndex, final int toIndex)
  {
    return new RandomAccessCharSubList (this, fromIndex, toIndex);
  }

  @Override
  public boolean equals (final Object that)
  {
    if (this == that)
    {
      return true;
    }
    else
      if (that instanceof CharList)
      {
        final CharList thatList = (CharList) that;
        if (size () != thatList.size ())
        {
          return false;
        }
        for (CharIterator thatIter = thatList.iterator (), thisIter = iterator (); thisIter.hasNext ();)
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
    for (final CharIterator iter = iterator (); iter.hasNext ();)
    {
      hash = 31 * hash + iter.next ();
    }
    return hash;
  }

  @Override
  public String toString ()
  {
    // could cache these like StringBuffer does
    return new String (toArray ());
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
    ComodChecker (final RandomAccessCharList source)
    {
      _source = source;
      resyncModCount ();
    }

    protected RandomAccessCharList getList ()
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

    private RandomAccessCharList _source = null;
    private int _expectedModCount = -1;
  }

  protected static class RandomAccessCharListIterator extends ComodChecker implements CharListIterator
  {
    RandomAccessCharListIterator (final RandomAccessCharList list, final int index)
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

    public char next ()
    {
      assertNotComodified ();
      if (!hasNext ())
      {
        throw new NoSuchElementException ();
      }
      else
      {
        final char val = getList ().get (_nextIndex);
        _lastReturnedIndex = _nextIndex;
        _nextIndex++;
        return val;
      }
    }

    public char previous ()
    {
      assertNotComodified ();
      if (!hasPrevious ())
      {
        throw new NoSuchElementException ();
      }
      else
      {
        final char val = getList ().get (_nextIndex - 1);
        _lastReturnedIndex = _nextIndex - 1;
        _nextIndex--;
        return val;
      }
    }

    public void add (final char value)
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

    public void set (final char value)
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

  protected static class RandomAccessCharSubList extends RandomAccessCharList implements CharList
  {
    RandomAccessCharSubList (final RandomAccessCharList list, final int fromIndex, final int toIndex)
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
    public char get (final int index)
    {
      checkRange (index);
      _comod.assertNotComodified ();
      return _list.get (toUnderlyingIndex (index));
    }

    @Override
    public char removeElementAt (final int index)
    {
      checkRange (index);
      _comod.assertNotComodified ();
      final char val = _list.removeElementAt (toUnderlyingIndex (index));
      _limit--;
      _comod.resyncModCount ();
      incrModCount ();
      return val;
    }

    @Override
    public char set (final int index, final char element)
    {
      checkRange (index);
      _comod.assertNotComodified ();
      final char val = _list.set (toUnderlyingIndex (index), element);
      incrModCount ();
      _comod.resyncModCount ();
      return val;
    }

    @Override
    public void add (final int index, final char element)
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
    private RandomAccessCharList _list = null;
    private ComodChecker _comod = null;

  }
}
