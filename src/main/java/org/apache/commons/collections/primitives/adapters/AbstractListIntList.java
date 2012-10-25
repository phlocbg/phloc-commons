/**
 * Copyright (C) 2006-2012 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import org.apache.commons.collections.primitives.IntCollection;
import org.apache.commons.collections.primitives.IntIterator;
import org.apache.commons.collections.primitives.IntList;
import org.apache.commons.collections.primitives.IntListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListIntList extends AbstractCollectionIntCollection implements IntList
{
  @Override
  @Nonnull
  protected final Collection <Integer> getCollection ()
  {
    return getList ();
  }

  @Nonnull
  protected abstract List <Integer> getList ();

  public void add (final int nIndex, final int element)
  {
    getList ().add (nIndex, Integer.valueOf (element));
  }

  public boolean addAll (final int index, final IntCollection collection)
  {
    return getList ().addAll (index, IntCollectionCollection.wrap (collection));
  }

  public int get (final int index)
  {
    return getList ().get (index).intValue ();
  }

  public int indexOf (final int element)
  {
    return getList ().indexOf (Integer.valueOf (element));
  }

  public int lastIndexOf (final int aElement)
  {
    return getList ().lastIndexOf (Integer.valueOf (aElement));
  }

  /**
   * {@link ListIteratorIntListIterator#wrap wraps} the {@link IntList
   * IntList} returned by my underlying {@link IntListIterator
   * IntListIterator}, if any.
   */
  @Nonnull
  public IntListIterator listIterator ()
  {
    return ListIteratorIntListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorIntListIterator#wrap wraps} the {@link IntList
   * IntList} returned by my underlying {@link IntListIterator
   * IntListIterator}, if any.
   */
  @Nonnull
  public IntListIterator listIterator (final int nIndex)
  {
    return ListIteratorIntListIterator.wrap (getList ().listIterator (nIndex));
  }

  public int removeElementAt (final int index)
  {
    return getList ().remove (index).intValue ();
  }

  public int set (final int index, final int element)
  {
    return getList ().set (index, new Integer (element)).intValue ();
  }

  public IntList subList (final int fromIndex, final int toIndex)
  {
    return ListIntList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (@Nullable final Object obj)
  {
    if (obj == this)
      return true;
    if (!(obj instanceof IntList))
      return false;

    final IntList that = (IntList) obj;
    if (size () != that.size ())
      return false;
    final IntIterator thisiter = iterator ();
    final IntIterator thatiter = that.iterator ();
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
