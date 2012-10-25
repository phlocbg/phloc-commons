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

import org.apache.commons.collections.primitives.ByteCollection;
import org.apache.commons.collections.primitives.ByteIterator;
import org.apache.commons.collections.primitives.ByteList;
import org.apache.commons.collections.primitives.ByteListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListByteList extends AbstractCollectionByteCollection implements ByteList
{
  @Override
  @Nonnull
  protected final Collection <Byte> getCollection ()
  {
    return getList ();
  }

  @Nonnull
  protected abstract List <Byte> getList ();

  public void add (final int nIndex, final byte element)
  {
    getList ().add (nIndex, Byte.valueOf (element));
  }

  public boolean addAll (final int index, final ByteCollection collection)
  {
    return getList ().addAll (index, ByteCollectionCollection.wrap (collection));
  }

  public byte get (final int index)
  {
    return getList ().get (index).byteValue ();
  }

  public int indexOf (final byte element)
  {
    return getList ().indexOf (Byte.valueOf (element));
  }

  public int lastIndexOf (final byte aElement)
  {
    return getList ().lastIndexOf (Byte.valueOf (aElement));
  }

  /**
   * {@link ListIteratorByteListIterator#wrap wraps} the {@link ByteList
   * ByteList} returned by my underlying {@link ByteListIterator
   * ByteListIterator}, if any.
   */
  @Nonnull
  public ByteListIterator listIterator ()
  {
    return ListIteratorByteListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorByteListIterator#wrap wraps} the {@link ByteList
   * ByteList} returned by my underlying {@link ByteListIterator
   * ByteListIterator}, if any.
   */
  @Nonnull
  public ByteListIterator listIterator (final int nIndex)
  {
    return ListIteratorByteListIterator.wrap (getList ().listIterator (nIndex));
  }

  public byte removeElementAt (final int index)
  {
    return getList ().remove (index).byteValue ();
  }

  public byte set (final int index, final byte element)
  {
    return getList ().set (index, new Byte (element)).byteValue ();
  }

  public ByteList subList (final int fromIndex, final int toIndex)
  {
    return ListByteList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (@Nullable final Object obj)
  {
    if (obj == this)
      return true;
    if (!(obj instanceof ByteList))
      return false;

    final ByteList that = (ByteList) obj;
    if (size () != that.size ())
      return false;
    final ByteIterator thisiter = iterator ();
    final ByteIterator thatiter = that.iterator ();
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
