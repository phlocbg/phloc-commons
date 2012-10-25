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

import org.apache.commons.collections.primitives.ShortCollection;
import org.apache.commons.collections.primitives.ShortIterator;
import org.apache.commons.collections.primitives.ShortList;
import org.apache.commons.collections.primitives.ShortListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListShortList extends AbstractCollectionShortCollection implements ShortList
{

  public void add (final int index, final short element)
  {
    getList ().add (index, new Short (element));
  }

  public boolean addAll (final int index, final ShortCollection collection)
  {
    return getList ().addAll (index, ShortCollectionCollection.wrap (collection));
  }

  public short get (final int index)
  {
    return ((Number) getList ().get (index)).shortValue ();
  }

  public int indexOf (final short element)
  {
    return getList ().indexOf (new Short (element));
  }

  public int lastIndexOf (final short element)
  {
    return getList ().lastIndexOf (new Short (element));
  }

  /**
   * {@link ListIteratorShortListIterator#wrap wraps} the {@link ShortList
   * ShortList} returned by my underlying {@link ShortListIterator
   * ShortListIterator}, if any.
   */
  public ShortListIterator listIterator ()
  {
    return ListIteratorShortListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorShortListIterator#wrap wraps} the {@link ShortList
   * ShortList} returned by my underlying {@link ShortListIterator
   * ShortListIterator}, if any.
   */
  public ShortListIterator listIterator (final int index)
  {
    return ListIteratorShortListIterator.wrap (getList ().listIterator (index));
  }

  public short removeElementAt (final int index)
  {
    return ((Number) getList ().remove (index)).shortValue ();
  }

  public short set (final int index, final short element)
  {
    return ((Number) getList ().set (index, new Short (element))).shortValue ();
  }

  public ShortList subList (final int fromIndex, final int toIndex)
  {
    return ListShortList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (obj instanceof ShortList)
    {
      final ShortList that = (ShortList) obj;
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
          final ShortIterator thisiter = iterator ();
          final ShortIterator thatiter = that.iterator ();
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
