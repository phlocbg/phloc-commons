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

import org.apache.commons.collections.primitives.CharCollection;
import org.apache.commons.collections.primitives.CharIterator;
import org.apache.commons.collections.primitives.CharList;
import org.apache.commons.collections.primitives.CharListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListCharList extends AbstractCollectionCharCollection implements CharList
{

  public void add (final int index, final char element)
  {
    getList ().add (index, new Character (element));
  }

  public boolean addAll (final int index, final CharCollection collection)
  {
    return getList ().addAll (index, CharCollectionCollection.wrap (collection));
  }

  public char get (final int index)
  {
    return ((Character) getList ().get (index)).charValue ();
  }

  public int indexOf (final char element)
  {
    return getList ().indexOf (new Character (element));
  }

  public int lastIndexOf (final char element)
  {
    return getList ().lastIndexOf (new Character (element));
  }

  /**
   * {@link ListIteratorCharListIterator#wrap wraps} the {@link CharList
   * CharList} returned by my underlying {@link CharListIterator
   * CharListIterator}, if any.
   */
  public CharListIterator listIterator ()
  {
    return ListIteratorCharListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorCharListIterator#wrap wraps} the {@link CharList
   * CharList} returned by my underlying {@link CharListIterator
   * CharListIterator}, if any.
   */
  public CharListIterator listIterator (final int index)
  {
    return ListIteratorCharListIterator.wrap (getList ().listIterator (index));
  }

  public char removeElementAt (final int index)
  {
    return ((Character) getList ().remove (index)).charValue ();
  }

  public char set (final int index, final char element)
  {
    return ((Character) getList ().set (index, new Character (element))).charValue ();
  }

  public CharList subList (final int fromIndex, final int toIndex)
  {
    return ListCharList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (obj instanceof CharList)
    {
      final CharList that = (CharList) obj;
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
          final CharIterator thisiter = iterator ();
          final CharIterator thatiter = that.iterator ();
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
