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
 * Abstract base class for {@link ByteCollection}s.
 * <p />
 * Read-only subclasses must override {@link #iterator} and {@link #size}.
 * Mutable subclasses should also override {@link #add} and
 * {@link ByteIterator#remove ByteIterator.remove}. All other methods have at
 * least some base implementation derived from these. Subclasses may choose to
 * override these methods to provide a more efficient implementation.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class AbstractByteCollection implements ByteCollection
{
  public abstract ByteIterator iterator ();

  public abstract int size ();

  protected AbstractByteCollection ()
  {}

  /** Unsupported in this base implementation. */
  public boolean add (final byte element)
  {
    throw new UnsupportedOperationException ("add(byte) is not supported.");
  }

  public boolean addAll (final ByteCollection c)
  {
    boolean modified = false;
    for (final ByteIterator iter = c.iterator (); iter.hasNext ();)
    {
      modified |= add (iter.next ());
    }
    return modified;
  }

  public void clear ()
  {
    for (final ByteIterator iter = iterator (); iter.hasNext ();)
    {
      iter.next ();
      iter.remove ();
    }
  }

  public boolean contains (final byte element)
  {
    for (final ByteIterator iter = iterator (); iter.hasNext ();)
    {
      if (iter.next () == element)
      {
        return true;
      }
    }
    return false;
  }

  public boolean containsAll (final ByteCollection c)
  {
    for (final ByteIterator iter = c.iterator (); iter.hasNext ();)
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

  public boolean removeElement (final byte element)
  {
    for (final ByteIterator iter = iterator (); iter.hasNext ();)
    {
      if (iter.next () == element)
      {
        iter.remove ();
        return true;
      }
    }
    return false;
  }

  public boolean removeAll (final ByteCollection c)
  {
    boolean modified = false;
    for (final ByteIterator iter = c.iterator (); iter.hasNext ();)
    {
      modified |= removeElement (iter.next ());
    }
    return modified;
  }

  public boolean retainAll (final ByteCollection c)
  {
    boolean modified = false;
    for (final ByteIterator iter = iterator (); iter.hasNext ();)
    {
      if (!c.contains (iter.next ()))
      {
        iter.remove ();
        modified = true;
      }
    }
    return modified;
  }

  public byte [] toArray ()
  {
    final byte [] array = new byte [size ()];
    int i = 0;
    for (final ByteIterator iter = iterator (); iter.hasNext ();)
    {
      array[i] = iter.next ();
      i++;
    }
    return array;
  }

  public byte [] toArray (final byte [] a)
  {
    if (a.length < size ())
      return toArray ();
    int i = 0;
    for (final ByteIterator iter = iterator (); iter.hasNext ();)
    {
      a[i] = iter.next ();
      i++;
    }
    return a;
  }
}
