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

import org.apache.commons.collections.primitives.ZZZCollection;
import org.apache.commons.collections.primitives.ZZZList;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractZZZListList extends AbstractZZZCollectionCollection implements List <XXX>
{
  @Nonnull
  protected abstract ZZZList getZZZList ();

  @Override
  @Nonnull
  protected final ZZZCollection getZZZCollection ()
  {
    return getZZZList ();
  }

  public void add (final int index, @Nonnull final XXX aElement)
  {
    getZZZList ().add (index, aElement.YYYValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final int index, final Collection <? extends XXX> c)
  {
    return getZZZList ().addAll (index, CollectionZZZCollection.wrap ((Collection <XXX>) c));
  }

  @Nonnull
  public XXX get (final int nIndex)
  {
    return XXX.valueOf (getZZZList ().get (nIndex));
  }

  public int indexOf (final Object element)
  {
    return getZZZList ().indexOf (((XXX) element).YYYValue ());
  }

  public int lastIndexOf (final Object element)
  {
    return getZZZList ().lastIndexOf (((XXX) element).YYYValue ());
  }

  /**
   * @return {@link ZZZListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ZZZListIterator
   * ZZZListIterator} returned by my underlying {@link ZZZList ZZZList}, if
   * any.
   */
  public ListIterator <XXX> listIterator ()
  {
    return ZZZListIteratorListIterator.wrap (getZZZList ().listIterator ());
  }

  /**
   * @param index
   *        The index where iterator should start
   * @return {@link ZZZListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ZZZListIterator
   * ZZZListIterator} returned by my underlying {@link ZZZList ZZZList}, if
   * any.
   */
  public ListIterator <XXX> listIterator (final int index)
  {
    return ZZZListIteratorListIterator.wrap (getZZZList ().listIterator (index));
  }

  @Nonnull
  public XXX remove (final int index)
  {
    return XXX.valueOf (getZZZList ().removeElementAt (index));
  }

  @Nonnull
  public XXX set (final int index, final XXX element)
  {
    return XXX.valueOf (getZZZList ().set (index, element.YYYValue ()));
  }

  public List <XXX> subList (final int nFromIndex, final int nToIndex)
  {
    return ZZZListList.wrap (getZZZList ().subList (nFromIndex, nToIndex));
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
    return getZZZList ().hashCode ();
  }
}
