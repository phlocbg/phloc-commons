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

import org.apache.commons.collections.primitives.BooleanCollection;
import org.apache.commons.collections.primitives.BooleanIterator;
import org.apache.commons.collections.primitives.BooleanList;
import org.apache.commons.collections.primitives.BooleanListIterator;

/**
 * @since Commons Primitives 1.1
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListBooleanList extends AbstractCollectionBooleanCollection implements BooleanList
{

  public void add (final int index, final boolean element)
  {
    getList ().add (index, new Boolean (element));
  }

  public boolean addAll (final int index, final BooleanCollection collection)
  {
    return getList ().addAll (index, BooleanCollectionCollection.wrap (collection));
  }

  public boolean get (final int index)
  {
    return ((Boolean) getList ().get (index)).booleanValue ();
  }

  public int indexOf (final boolean element)
  {
    return getList ().indexOf (new Boolean (element));
  }

  public int lastIndexOf (final boolean element)
  {
    return getList ().lastIndexOf (new Boolean (element));
  }

  /**
   * {@link ListIteratorBooleanListIterator#wrap wraps} the {@link BooleanList
   * BooleanList} returned by my underlying {@link BooleanListIterator
   * BooleanListIterator}, if any.
   */
  public BooleanListIterator listIterator ()
  {
    return ListIteratorBooleanListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorBooleanListIterator#wrap wraps} the {@link BooleanList
   * BooleanList} returned by my underlying {@link BooleanListIterator
   * BooleanListIterator}, if any.
   */
  public BooleanListIterator listIterator (final int index)
  {
    return ListIteratorBooleanListIterator.wrap (getList ().listIterator (index));
  }

  public boolean removeElementAt (final int index)
  {
    return ((Boolean) getList ().remove (index)).booleanValue ();
  }

  public boolean set (final int index, final boolean element)
  {
    return ((Boolean) getList ().set (index, new Boolean (element))).booleanValue ();
  }

  public BooleanList subList (final int fromIndex, final int toIndex)
  {
    return ListBooleanList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (obj instanceof BooleanList)
    {
      final BooleanList that = (BooleanList) obj;
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
          final BooleanIterator thisiter = iterator ();
          final BooleanIterator thatiter = that.iterator ();
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
