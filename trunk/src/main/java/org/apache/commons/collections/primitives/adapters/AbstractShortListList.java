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

import org.apache.commons.collections.primitives.ShortCollection;
import org.apache.commons.collections.primitives.ShortList;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractShortListList extends AbstractShortCollectionCollection implements List <Short>
{
  @Nonnull
  protected abstract ShortList getShortList ();

  @Override
  @Nonnull
  protected final ShortCollection getShortCollection ()
  {
    return getShortList ();
  }

  public void add (final int index, @Nonnull final Short aElement)
  {
    getShortList ().add (index, aElement.shortValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final int index, final Collection <? extends Short> c)
  {
    return getShortList ().addAll (index, CollectionShortCollection.wrap ((Collection <Short>) c));
  }

  @Nonnull
  public Short get (final int nIndex)
  {
    return Short.valueOf (getShortList ().get (nIndex));
  }

  public int indexOf (final Object element)
  {
    return getShortList ().indexOf (((Short) element).shortValue ());
  }

  public int lastIndexOf (final Object element)
  {
    return getShortList ().lastIndexOf (((Short) element).shortValue ());
  }

  /**
   * {@link ShortListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ShortListIterator
   * ShortListIterator} returned by my underlying {@link ShortList ShortList}, if
   * any.
   */
  public ListIterator <Short> listIterator ()
  {
    return ShortListIteratorListIterator.wrap (getShortList ().listIterator ());
  }

  /**
   * {@link ShortListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ShortListIterator
   * ShortListIterator} returned by my underlying {@link ShortList ShortList}, if
   * any.
   */
  public ListIterator <Short> listIterator (final int index)
  {
    return ShortListIteratorListIterator.wrap (getShortList ().listIterator (index));
  }

  @Nonnull
  public Short remove (final int index)
  {
    return Short.valueOf (getShortList ().removeElementAt (index));
  }

  @Nonnull
  public Short set (final int index, final Short element)
  {
    return Short.valueOf (getShortList ().set (index, element.shortValue ()));
  }

  public List <Short> subList (final int nFromIndex, final int nToIndex)
  {
    return ShortListList.wrap (getShortList ().subList (nFromIndex, nToIndex));
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
    return getShortList ().hashCode ();
  }
}
