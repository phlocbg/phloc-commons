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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.CharCollection;
import org.apache.commons.collections.primitives.CharList;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractCharListList extends AbstractCharCollectionCollection implements List <Character>
{
  @Nonnull
  protected abstract CharList getCharList ();

  @Override
  @Nonnull
  protected final CharCollection getCharCollection ()
  {
    return getCharList ();
  }

  public void add (final int index, @Nonnull final Character aElement)
  {
    getCharList ().add (index, aElement.charValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final int index, final Collection <? extends Character> c)
  {
    return getCharList ().addAll (index, CollectionCharCollection.wrap ((Collection <Character>) c));
  }

  @Nonnull
  public Character get (final int nIndex)
  {
    return Character.valueOf (getCharList ().get (nIndex));
  }

  public int indexOf (final Object element)
  {
    return getCharList ().indexOf (((Character) element).charValue ());
  }

  public int lastIndexOf (final Object element)
  {
    return getCharList ().lastIndexOf (((Character) element).charValue ());
  }

  /**
   * {@link CharListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.CharListIterator
   * CharListIterator} returned by my underlying {@link CharList CharList}, if
   * any.
   */
  public ListIterator <Character> listIterator ()
  {
    return CharListIteratorListIterator.wrap (getCharList ().listIterator ());
  }

  /**
   * {@link CharListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.CharListIterator
   * CharListIterator} returned by my underlying {@link CharList CharList}, if
   * any.
   */
  public ListIterator <Character> listIterator (final int index)
  {
    return CharListIteratorListIterator.wrap (getCharList ().listIterator (index));
  }

  @Nonnull
  public Character remove (final int index)
  {
    return Character.valueOf (getCharList ().removeElementAt (index));
  }

  @Nonnull
  public Character set (final int index, final Character element)
  {
    return Character.valueOf (getCharList ().set (index, element.charValue ()));
  }

  public List <Character> subList (final int nFromIndex, final int nToIndex)
  {
    return CharListList.wrap (getCharList ().subList (nFromIndex, nToIndex));
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
    return getCharList ().hashCode ();
  }
}
