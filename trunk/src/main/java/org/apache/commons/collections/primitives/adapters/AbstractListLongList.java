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

import org.apache.commons.collections.primitives.LongCollection;
import org.apache.commons.collections.primitives.LongIterator;
import org.apache.commons.collections.primitives.LongList;
import org.apache.commons.collections.primitives.LongListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListLongList extends AbstractCollectionLongCollection implements LongList
{

  public void add (final int index, final long element)
  {
    getList ().add (index, new Long (element));
  }

  public boolean addAll (final int index, final LongCollection collection)
  {
    return getList ().addAll (index, LongCollectionCollection.wrap (collection));
  }

  public long get (final int index)
  {
    return ((Number) getList ().get (index)).longValue ();
  }

  public int indexOf (final long element)
  {
    return getList ().indexOf (new Long (element));
  }

  public int lastIndexOf (final long element)
  {
    return getList ().lastIndexOf (new Long (element));
  }

  /**
   * {@link ListIteratorLongListIterator#wrap wraps} the {@link LongList
   * LongList} returned by my underlying {@link LongListIterator
   * LongListIterator}, if any.
   */
  public LongListIterator listIterator ()
  {
    return ListIteratorLongListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorLongListIterator#wrap wraps} the {@link LongList
   * LongList} returned by my underlying {@link LongListIterator
   * LongListIterator}, if any.
   */
  public LongListIterator listIterator (final int index)
  {
    return ListIteratorLongListIterator.wrap (getList ().listIterator (index));
  }

  public long removeElementAt (final int index)
  {
    return ((Number) getList ().remove (index)).longValue ();
  }

  public long set (final int index, final long element)
  {
    return ((Number) getList ().set (index, new Long (element))).longValue ();
  }

  public LongList subList (final int fromIndex, final int toIndex)
  {
    return ListLongList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (obj instanceof LongList)
    {
      final LongList that = (LongList) obj;
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
          final LongIterator thisiter = iterator ();
          final LongIterator thatiter = that.iterator ();
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
