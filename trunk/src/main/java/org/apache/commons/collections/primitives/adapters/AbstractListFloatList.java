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

import org.apache.commons.collections.primitives.FloatCollection;
import org.apache.commons.collections.primitives.FloatIterator;
import org.apache.commons.collections.primitives.FloatList;
import org.apache.commons.collections.primitives.FloatListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListFloatList extends AbstractCollectionFloatCollection implements FloatList
{

  public void add (final int index, final float element)
  {
    getList ().add (index, new Float (element));
  }

  public boolean addAll (final int index, final FloatCollection collection)
  {
    return getList ().addAll (index, FloatCollectionCollection.wrap (collection));
  }

  public float get (final int index)
  {
    return ((Number) getList ().get (index)).floatValue ();
  }

  public int indexOf (final float element)
  {
    return getList ().indexOf (new Float (element));
  }

  public int lastIndexOf (final float element)
  {
    return getList ().lastIndexOf (new Float (element));
  }

  /**
   * {@link ListIteratorFloatListIterator#wrap wraps} the {@link FloatList
   * FloatList} returned by my underlying {@link FloatListIterator
   * FloatListIterator}, if any.
   */
  public FloatListIterator listIterator ()
  {
    return ListIteratorFloatListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorFloatListIterator#wrap wraps} the {@link FloatList
   * FloatList} returned by my underlying {@link FloatListIterator
   * FloatListIterator}, if any.
   */
  public FloatListIterator listIterator (final int index)
  {
    return ListIteratorFloatListIterator.wrap (getList ().listIterator (index));
  }

  public float removeElementAt (final int index)
  {
    return ((Number) getList ().remove (index)).floatValue ();
  }

  public float set (final int index, final float element)
  {
    return ((Number) getList ().set (index, new Float (element))).floatValue ();
  }

  public FloatList subList (final int fromIndex, final int toIndex)
  {
    return ListFloatList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (obj instanceof FloatList)
    {
      final FloatList that = (FloatList) obj;
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
          final FloatIterator thisiter = iterator ();
          final FloatIterator thatiter = that.iterator ();
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
