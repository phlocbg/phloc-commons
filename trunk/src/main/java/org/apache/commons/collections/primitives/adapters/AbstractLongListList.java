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

import org.apache.commons.collections.primitives.LongCollection;
import org.apache.commons.collections.primitives.LongList;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractLongListList extends AbstractLongCollectionCollection implements List <Long>
{
  @Nonnull
  protected abstract LongList getLongList ();

  @Override
  @Nonnull
  protected final LongCollection getLongCollection ()
  {
    return getLongList ();
  }

  public void add (final int index, @Nonnull final Long aElement)
  {
    getLongList ().add (index, aElement.longValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final int index, final Collection <? extends Long> c)
  {
    return getLongList ().addAll (index, CollectionLongCollection.wrap ((Collection <Long>) c));
  }

  @Nonnull
  public Long get (final int nIndex)
  {
    return Long.valueOf (getLongList ().get (nIndex));
  }

  public int indexOf (final Object element)
  {
    return getLongList ().indexOf (((Long) element).longValue ());
  }

  public int lastIndexOf (final Object element)
  {
    return getLongList ().lastIndexOf (((Long) element).longValue ());
  }

  /**
   * {@link LongListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.LongListIterator
   * LongListIterator} returned by my underlying {@link LongList LongList}, if
   * any.
   */
  public ListIterator <Long> listIterator ()
  {
    return LongListIteratorListIterator.wrap (getLongList ().listIterator ());
  }

  /**
   * {@link LongListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.LongListIterator
   * LongListIterator} returned by my underlying {@link LongList LongList}, if
   * any.
   */
  public ListIterator <Long> listIterator (final int index)
  {
    return LongListIteratorListIterator.wrap (getLongList ().listIterator (index));
  }

  @Nonnull
  public Long remove (final int index)
  {
    return Long.valueOf (getLongList ().removeElementAt (index));
  }

  @Nonnull
  public Long set (final int index, final Long element)
  {
    return Long.valueOf (getLongList ().set (index, element.longValue ()));
  }

  public List <Long> subList (final int nFromIndex, final int nToIndex)
  {
    return LongListList.wrap (getLongList ().subList (nFromIndex, nToIndex));
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
    return getLongList ().hashCode ();
  }
}
