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

import org.apache.commons.collections.primitives.FloatCollection;
import org.apache.commons.collections.primitives.FloatList;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractFloatListList extends AbstractFloatCollectionCollection implements List <Float>
{
  @Nonnull
  protected abstract FloatList getFloatList ();

  @Override
  @Nonnull
  protected final FloatCollection getFloatCollection ()
  {
    return getFloatList ();
  }

  public void add (final int index, @Nonnull final Float aElement)
  {
    getFloatList ().add (index, aElement.floatValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final int index, final Collection <? extends Float> c)
  {
    return getFloatList ().addAll (index, CollectionFloatCollection.wrap ((Collection <Float>) c));
  }

  @Nonnull
  public Float get (final int nIndex)
  {
    return Float.valueOf (getFloatList ().get (nIndex));
  }

  public int indexOf (final Object element)
  {
    return getFloatList ().indexOf (((Float) element).floatValue ());
  }

  public int lastIndexOf (final Object element)
  {
    return getFloatList ().lastIndexOf (((Float) element).floatValue ());
  }

  /**
   * @return {@link FloatListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.FloatListIterator
   * FloatListIterator} returned by my underlying {@link FloatList FloatList}, if
   * any.
   */
  public ListIterator <Float> listIterator ()
  {
    return FloatListIteratorListIterator.wrap (getFloatList ().listIterator ());
  }

  /**
   * @param index
   *        The index where iterator should start
   * @return {@link FloatListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.FloatListIterator
   * FloatListIterator} returned by my underlying {@link FloatList FloatList}, if
   * any.
   */
  public ListIterator <Float> listIterator (final int index)
  {
    return FloatListIteratorListIterator.wrap (getFloatList ().listIterator (index));
  }

  @Nonnull
  public Float remove (final int index)
  {
    return Float.valueOf (getFloatList ().removeElementAt (index));
  }

  @Nonnull
  public Float set (final int index, final Float element)
  {
    return Float.valueOf (getFloatList ().set (index, element.floatValue ()));
  }

  public List <Float> subList (final int nFromIndex, final int nToIndex)
  {
    return FloatListList.wrap (getFloatList ().subList (nFromIndex, nToIndex));
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
    return getFloatList ().hashCode ();
  }
}
