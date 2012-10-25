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
package org.apache.commons.collections.primitives.adapters;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.primitives.ByteCollection;
import org.apache.commons.collections.primitives.ByteList;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractByteListList extends AbstractByteCollectionCollection implements List
{

  public void add (final int index, final Object element)
  {
    getByteList ().add (index, ((Number) element).byteValue ());
  }

  public boolean addAll (final int index, final Collection c)
  {
    return getByteList ().addAll (index, CollectionByteCollection.wrap (c));
  }

  public Object get (final int index)
  {
    return new Byte (getByteList ().get (index));
  }

  public int indexOf (final Object element)
  {
    return getByteList ().indexOf (((Number) element).byteValue ());
  }

  public int lastIndexOf (final Object element)
  {
    return getByteList ().lastIndexOf (((Number) element).byteValue ());
  }

  /**
   * {@link ByteListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ByteListIterator
   * ByteListIterator} returned by my underlying {@link ByteList ByteList}, if
   * any.
   */
  public ListIterator listIterator ()
  {
    return ByteListIteratorListIterator.wrap (getByteList ().listIterator ());
  }

  /**
   * {@link ByteListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ByteListIterator
   * ByteListIterator} returned by my underlying {@link ByteList ByteList}, if
   * any.
   */
  public ListIterator listIterator (final int index)
  {
    return ByteListIteratorListIterator.wrap (getByteList ().listIterator (index));
  }

  public Object remove (final int index)
  {
    return new Byte (getByteList ().removeElementAt (index));
  }

  public Object set (final int index, final Object element)
  {
    return new Byte (getByteList ().set (index, ((Number) element).byteValue ()));
  }

  public List subList (final int fromIndex, final int toIndex)
  {
    return ByteListList.wrap (getByteList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (obj instanceof List)
    {
      final List that = (List) obj;
      if (this == that)
      {
        return true;
      }
      else
        if (this.size () != that.size ())
        {
          return false;
        }
        else
        {
          final Iterator thisiter = iterator ();
          final Iterator thatiter = that.iterator ();
          while (thisiter.hasNext ())
          {
            final Object thiselt = thisiter.next ();
            final Object thatelt = thatiter.next ();
            if (null == thiselt ? null != thatelt : !(thiselt.equals (thatelt)))
            {
              return false;
            }
          }
          return true;
        }
    }
    else
    {
      return false;
    }
  }

  @Override
  public int hashCode ()
  {
    return getByteList ().hashCode ();
  }

  @Override
  protected final ByteCollection getByteCollection ()
  {
    return getByteList ();
  }

  protected abstract ByteList getByteList ();

}
