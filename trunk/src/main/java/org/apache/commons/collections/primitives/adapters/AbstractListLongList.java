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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
  @Override
  @Nonnull
  protected final Collection <Long> getCollection ()
  {
    return getList ();
  }

  @Nonnull
  protected abstract List <Long> getList ();

  public void add (final int nIndex, final long element)
  {
    getList ().add (nIndex, Long.valueOf (element));
  }

  public boolean addAll (final int index, final LongCollection collection)
  {
    return getList ().addAll (index, LongCollectionCollection.wrap (collection));
  }

  public long get (final int index)
  {
    return getList ().get (index).longValue ();
  }

  public int indexOf (final long element)
  {
    return getList ().indexOf (Long.valueOf (element));
  }

  public int lastIndexOf (final long aElement)
  {
    return getList ().lastIndexOf (Long.valueOf (aElement));
  }

  /**
   * {@link ListIteratorLongListIterator#wrap wraps} the {@link LongList
   * LongList} returned by my underlying {@link LongListIterator
   * LongListIterator}, if any.
   */
  @Nonnull
  public LongListIterator listIterator ()
  {
    return ListIteratorLongListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorLongListIterator#wrap wraps} the {@link LongList
   * LongList} returned by my underlying {@link LongListIterator
   * LongListIterator}, if any.
   */
  @Nonnull
  public LongListIterator listIterator (final int nIndex)
  {
    return ListIteratorLongListIterator.wrap (getList ().listIterator (nIndex));
  }

  public long removeElementAt (final int index)
  {
    return getList ().remove (index).longValue ();
  }

  public long set (final int index, final long element)
  {
    return getList ().set (index, new Long (element)).longValue ();
  }

  public LongList subList (final int fromIndex, final int toIndex)
  {
    return ListLongList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (@Nullable final Object obj)
  {
    if (obj == this)
      return true;
    if (!(obj instanceof LongList))
      return false;

    final LongList that = (LongList) obj;
    if (size () != that.size ())
      return false;
    final LongIterator thisiter = iterator ();
    final LongIterator thatiter = that.iterator ();
    while (thisiter.hasNext ())
      if (thisiter.next () != thatiter.next ())
        return false;
    return true;
  }

  @Override
  public int hashCode ()
  {
    return getList ().hashCode ();
  }
}
