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

import org.apache.commons.collections.primitives.BooleanCollection;
import org.apache.commons.collections.primitives.BooleanList;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractBooleanListList extends AbstractBooleanCollectionCollection implements List <Boolean>
{
  @Nonnull
  protected abstract BooleanList getBooleanList ();

  @Override
  @Nonnull
  protected final BooleanCollection getBooleanCollection ()
  {
    return getBooleanList ();
  }

  public void add (final int index, @Nonnull final Boolean aElement)
  {
    getBooleanList ().add (index, aElement.booleanValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final int index, final Collection <? extends Boolean> c)
  {
    return getBooleanList ().addAll (index, CollectionBooleanCollection.wrap ((Collection <Boolean>) c));
  }

  @Nonnull
  public Boolean get (final int nIndex)
  {
    return Boolean.valueOf (getBooleanList ().get (nIndex));
  }

  public int indexOf (final Object element)
  {
    return getBooleanList ().indexOf (((Boolean) element).booleanValue ());
  }

  public int lastIndexOf (final Object element)
  {
    return getBooleanList ().lastIndexOf (((Boolean) element).booleanValue ());
  }

  /**
   * {@link BooleanListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.BooleanListIterator
   * BooleanListIterator} returned by my underlying {@link BooleanList BooleanList}, if
   * any.
   */
  public ListIterator <Boolean> listIterator ()
  {
    return BooleanListIteratorListIterator.wrap (getBooleanList ().listIterator ());
  }

  /**
   * {@link BooleanListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.BooleanListIterator
   * BooleanListIterator} returned by my underlying {@link BooleanList BooleanList}, if
   * any.
   */
  public ListIterator <Boolean> listIterator (final int index)
  {
    return BooleanListIteratorListIterator.wrap (getBooleanList ().listIterator (index));
  }

  @Nonnull
  public Boolean remove (final int index)
  {
    return Boolean.valueOf (getBooleanList ().removeElementAt (index));
  }

  @Nonnull
  public Boolean set (final int index, final Boolean element)
  {
    return Boolean.valueOf (getBooleanList ().set (index, element.booleanValue ()));
  }

  public List <Boolean> subList (final int nFromIndex, final int nToIndex)
  {
    return BooleanListList.wrap (getBooleanList ().subList (nFromIndex, nToIndex));
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
    return getBooleanList ().hashCode ();
  }
}
