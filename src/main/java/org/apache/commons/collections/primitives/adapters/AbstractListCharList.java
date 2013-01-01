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
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.CharCollection;
import org.apache.commons.collections.primitives.CharIterator;
import org.apache.commons.collections.primitives.CharList;
import org.apache.commons.collections.primitives.CharListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListCharList extends AbstractCollectionCharCollection implements CharList
{
  @Override
  @Nonnull
  protected final Collection <Character> getCollection ()
  {
    return getList ();
  }

  @Nonnull
  protected abstract List <Character> getList ();

  public void add (final int nIndex, final char element)
  {
    getList ().add (nIndex, Character.valueOf (element));
  }

  public boolean addAll (final int index, @Nonnull final CharCollection collection)
  {
    return getList ().addAll (index, CharCollectionCollection.wrap (collection));
  }

  public char get (final int index)
  {
    return getList ().get (index).charValue ();
  }

  public int indexOf (final char element)
  {
    return getList ().indexOf (Character.valueOf (element));
  }

  public int lastIndexOf (final char aElement)
  {
    return getList ().lastIndexOf (Character.valueOf (aElement));
  }

  /**
   * {@link ListIteratorCharListIterator#wrap wraps} the {@link CharList
   * CharList} returned by my underlying {@link CharListIterator
   * CharListIterator}, if any.
   */
  @Nonnull
  public CharListIterator listIterator ()
  {
    return ListIteratorCharListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorCharListIterator#wrap wraps} the {@link CharList
   * CharList} returned by my underlying {@link CharListIterator
   * CharListIterator}, if any.
   */
  @Nonnull
  public CharListIterator listIterator (final int nIndex)
  {
    return ListIteratorCharListIterator.wrap (getList ().listIterator (nIndex));
  }

  public char removeElementAt (final int index)
  {
    return getList ().remove (index).charValue ();
  }

  public char set (final int index, final char element)
  {
    return getList ().set (index, Character.valueOf (element)).charValue ();
  }

  @Nonnull
  public CharList subList (final int nFromIndex, final int nToIndex)
  {
    return ListCharList.wrap (getList ().subList (nFromIndex, nToIndex));
  }

  @Override
  public boolean equals (@Nullable final Object obj)
  {
    if (obj == this)
      return true;
    if (!(obj instanceof CharList))
      return false;

    final CharList that = (CharList) obj;
    if (size () != that.size ())
      return false;
    final CharIterator thisiter = iterator ();
    final CharIterator thatiter = that.iterator ();
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
