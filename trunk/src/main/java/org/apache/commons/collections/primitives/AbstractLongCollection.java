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

/**
 * Abstract base class for {@link LongCollection}s.
 * <p />
 * Read-only subclasses must override {@link #iterator} and {@link #size}.
 * Mutable subclasses should also override {@link #add} and
 * {@link LongIterator#remove LongIterator.remove}. All other methods have at
 * least some base implementation derived from these. Subclasses may choose to
 * override these methods to provide a more efficient implementation.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class AbstractLongCollection implements LongCollection
{
  public abstract LongIterator iterator ();

  public abstract int size ();

  protected AbstractLongCollection ()
  {}

  /** Unsupported in this base implementation. */
  public boolean add (final long element)
  {
    throw new UnsupportedOperationException ("add(long) is not supported.");
  }

  public boolean addAll (final LongCollection c)
  {
    boolean modified = false;
    for (final LongIterator iter = c.iterator (); iter.hasNext ();)
    {
      modified |= add (iter.next ());
    }
    return modified;
  }

  public void clear ()
  {
    for (final LongIterator iter = iterator (); iter.hasNext ();)
    {
      iter.next ();
      iter.remove ();
    }
  }

  public boolean contains (final long element)
  {
    for (final LongIterator iter = iterator (); iter.hasNext ();)
    {
      if (iter.next () == element)
      {
        return true;
      }
    }
    return false;
  }

  public boolean containsAll (final LongCollection c)
  {
    for (final LongIterator iter = c.iterator (); iter.hasNext ();)
    {
      if (!contains (iter.next ()))
      {
        return false;
      }
    }
    return true;
  }

  public boolean isEmpty ()
  {
    return (0 == size ());
  }

  public boolean removeElement (final long element)
  {
    for (final LongIterator iter = iterator (); iter.hasNext ();)
    {
      if (iter.next () == element)
      {
        iter.remove ();
        return true;
      }
    }
    return false;
  }

  public boolean removeAll (final LongCollection c)
  {
    boolean modified = false;
    for (final LongIterator iter = c.iterator (); iter.hasNext ();)
    {
      modified |= removeElement (iter.next ());
    }
    return modified;
  }

  public boolean retainAll (final LongCollection c)
  {
    boolean modified = false;
    for (final LongIterator iter = iterator (); iter.hasNext ();)
    {
      if (!c.contains (iter.next ()))
      {
        iter.remove ();
        modified = true;
      }
    }
    return modified;
  }

  public long [] toArray ()
  {
    final long [] array = new long [size ()];
    int i = 0;
    for (final LongIterator iter = iterator (); iter.hasNext ();)
    {
      array[i] = iter.next ();
      i++;
    }
    return array;
  }

  public long [] toArray (final long [] a)
  {
    if (a.length < size ())
      return toArray ();
    int i = 0;
    for (final LongIterator iter = iterator (); iter.hasNext ();)
    {
      a[i] = iter.next ();
      i++;
    }
    return a;
  }
}
