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
import java.util.List;

import org.apache.commons.collections.primitives.ByteCollection;
import org.apache.commons.collections.primitives.ByteIterator;
import org.apache.commons.collections.primitives.ByteList;
import org.apache.commons.collections.primitives.ByteListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListByteList extends AbstractCollectionByteCollection implements ByteList
{

  public void add (final int index, final byte element)
  {
    getList ().add (index, new Byte (element));
  }

  public boolean addAll (final int index, final ByteCollection collection)
  {
    return getList ().addAll (index, ByteCollectionCollection.wrap (collection));
  }

  public byte get (final int index)
  {
    return ((Number) getList ().get (index)).byteValue ();
  }

  public int indexOf (final byte element)
  {
    return getList ().indexOf (new Byte (element));
  }

  public int lastIndexOf (final byte element)
  {
    return getList ().lastIndexOf (new Byte (element));
  }

  /**
   * {@link ListIteratorByteListIterator#wrap wraps} the {@link ByteList
   * ByteList} returned by my underlying {@link ByteListIterator
   * ByteListIterator}, if any.
   */
  public ByteListIterator listIterator ()
  {
    return ListIteratorByteListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorByteListIterator#wrap wraps} the {@link ByteList
   * ByteList} returned by my underlying {@link ByteListIterator
   * ByteListIterator}, if any.
   */
  public ByteListIterator listIterator (final int index)
  {
    return ListIteratorByteListIterator.wrap (getList ().listIterator (index));
  }

  public byte removeElementAt (final int index)
  {
    return ((Number) getList ().remove (index)).byteValue ();
  }

  public byte set (final int index, final byte element)
  {
    return ((Number) getList ().set (index, new Byte (element))).byteValue ();
  }

  public ByteList subList (final int fromIndex, final int toIndex)
  {
    return ListByteList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (obj instanceof ByteList)
    {
      final ByteList that = (ByteList) obj;
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
          final ByteIterator thisiter = iterator ();
          final ByteIterator thatiter = that.iterator ();
          while (thisiter.hasNext ())
          {
            if (thisiter.next () != thatiter.next ())
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
    return getList ().hashCode ();
  }

  @Override
  final protected Collection getCollection ()
  {
    return getList ();
  }

  abstract protected List getList ();
}
