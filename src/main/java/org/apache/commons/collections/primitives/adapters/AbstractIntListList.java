/**
 * Copyright (C) 2006-2013 phloc systems
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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.IntCollection;
import org.apache.commons.collections.primitives.IntList;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractIntListList extends AbstractIntCollectionCollection implements List <Integer>
{
  @Nonnull
  protected abstract IntList getIntList ();

  @Override
  @Nonnull
  protected final IntCollection getIntCollection ()
  {
    return getIntList ();
  }

  public void add (final int index, @Nonnull final Integer aElement)
  {
    getIntList ().add (index, aElement.intValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final int index, final Collection <? extends Integer> c)
  {
    return getIntList ().addAll (index, CollectionIntCollection.wrap ((Collection <Integer>) c));
  }

  @Nonnull
  public Integer get (final int nIndex)
  {
    return Integer.valueOf (getIntList ().get (nIndex));
  }

  public int indexOf (final Object element)
  {
    return getIntList ().indexOf (((Integer) element).intValue ());
  }

  public int lastIndexOf (final Object element)
  {
    return getIntList ().lastIndexOf (((Integer) element).intValue ());
  }

  /**
   * @return {@link IntListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.IntListIterator
   * IntListIterator} returned by my underlying {@link IntList IntList}, if
   * any.
   */
  public ListIterator <Integer> listIterator ()
  {
    return IntListIteratorListIterator.wrap (getIntList ().listIterator ());
  }

  /**
   * @param index
   *        The index where iterator should start
   * @return {@link IntListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.IntListIterator
   * IntListIterator} returned by my underlying {@link IntList IntList}, if
   * any.
   */
  public ListIterator <Integer> listIterator (final int index)
  {
    return IntListIteratorListIterator.wrap (getIntList ().listIterator (index));
  }

  @Nonnull
  public Integer remove (final int index)
  {
    return Integer.valueOf (getIntList ().removeElementAt (index));
  }

  @Nonnull
  public Integer set (final int index, final Integer element)
  {
    return Integer.valueOf (getIntList ().set (index, element.intValue ()));
  }

  public List <Integer> subList (final int nFromIndex, final int nToIndex)
  {
    return IntListList.wrap (getIntList ().subList (nFromIndex, nToIndex));
  }

  @Override
  public boolean equals (@Nullable final Object obj)
  {
    if (obj == this)
      return true;
    if (!(obj instanceof List <?>))
      return false;
    final List <?> that = (List <?>) obj;
    if (size () != that.size ())
      return false;
    final Iterator <?> thisiter = iterator ();
    final Iterator <?> thatiter = that.iterator ();
    while (thisiter.hasNext ())
    {
      final Object thiselt = thisiter.next ();
      final Object thatelt = thatiter.next ();
      if (null == thiselt ? null != thatelt : !(thiselt.equals (thatelt)))
        return false;
    }
    return true;
  }

  @Override
  public int hashCode ()
  {
    return getIntList ().hashCode ();
  }
}
