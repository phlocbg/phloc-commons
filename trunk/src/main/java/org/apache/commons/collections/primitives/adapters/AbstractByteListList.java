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

import org.apache.commons.collections.primitives.ByteCollection;
import org.apache.commons.collections.primitives.ByteList;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractByteListList extends AbstractByteCollectionCollection implements List <Byte>
{
  @Nonnull
  protected abstract ByteList getByteList ();

  @Override
  @Nonnull
  protected final ByteCollection getByteCollection ()
  {
    return getByteList ();
  }

  public void add (final int index, @Nonnull final Byte aElement)
  {
    getByteList ().add (index, aElement.byteValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final int index, final Collection <? extends Byte> c)
  {
    return getByteList ().addAll (index, CollectionByteCollection.wrap ((Collection <Byte>) c));
  }

  @Nonnull
  public Byte get (final int nIndex)
  {
    return Byte.valueOf (getByteList ().get (nIndex));
  }

  public int indexOf (final Object element)
  {
    return getByteList ().indexOf (((Byte) element).byteValue ());
  }

  public int lastIndexOf (final Object element)
  {
    return getByteList ().lastIndexOf (((Byte) element).byteValue ());
  }

  /**
   * {@link ByteListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ByteListIterator
   * ByteListIterator} returned by my underlying {@link ByteList ByteList}, if
   * any.
   */
  public ListIterator <Byte> listIterator ()
  {
    return ByteListIteratorListIterator.wrap (getByteList ().listIterator ());
  }

  /**
   * {@link ByteListIteratorListIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ByteListIterator
   * ByteListIterator} returned by my underlying {@link ByteList ByteList}, if
   * any.
   */
  public ListIterator <Byte> listIterator (final int index)
  {
    return ByteListIteratorListIterator.wrap (getByteList ().listIterator (index));
  }

  @Nonnull
  public Byte remove (final int index)
  {
    return Byte.valueOf (getByteList ().removeElementAt (index));
  }

  @Nonnull
  public Byte set (final int index, final Byte element)
  {
    return Byte.valueOf (getByteList ().set (index, element.byteValue ()));
  }

  public List <Byte> subList (final int nFromIndex, final int nToIndex)
  {
    return ByteListList.wrap (getByteList ().subList (nFromIndex, nToIndex));
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
    return getByteList ().hashCode ();
  }
}
