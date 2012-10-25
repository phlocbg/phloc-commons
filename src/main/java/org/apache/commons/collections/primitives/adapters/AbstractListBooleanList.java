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

import org.apache.commons.collections.primitives.BooleanCollection;
import org.apache.commons.collections.primitives.BooleanIterator;
import org.apache.commons.collections.primitives.BooleanList;
import org.apache.commons.collections.primitives.BooleanListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListBooleanList extends AbstractCollectionBooleanCollection implements BooleanList
{
  @Override
  @Nonnull
  protected final Collection <Boolean> getCollection ()
  {
    return getList ();
  }

  @Nonnull
  protected abstract List <Boolean> getList ();

  public void add (final int nIndex, final boolean element)
  {
    getList ().add (nIndex, Boolean.valueOf (element));
  }

  public boolean addAll (final int index, final BooleanCollection collection)
  {
    return getList ().addAll (index, BooleanCollectionCollection.wrap (collection));
  }

  public boolean get (final int index)
  {
    return getList ().get (index).booleanValue ();
  }

  public int indexOf (final boolean element)
  {
    return getList ().indexOf (Boolean.valueOf (element));
  }

  public int lastIndexOf (final boolean aElement)
  {
    return getList ().lastIndexOf (Boolean.valueOf (aElement));
  }

  /**
   * {@link ListIteratorBooleanListIterator#wrap wraps} the {@link BooleanList
   * BooleanList} returned by my underlying {@link BooleanListIterator
   * BooleanListIterator}, if any.
   */
  @Nonnull
  public BooleanListIterator listIterator ()
  {
    return ListIteratorBooleanListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorBooleanListIterator#wrap wraps} the {@link BooleanList
   * BooleanList} returned by my underlying {@link BooleanListIterator
   * BooleanListIterator}, if any.
   */
  @Nonnull
  public BooleanListIterator listIterator (final int nIndex)
  {
    return ListIteratorBooleanListIterator.wrap (getList ().listIterator (nIndex));
  }

  public boolean removeElementAt (final int index)
  {
    return getList ().remove (index).booleanValue ();
  }

  public boolean set (final int index, final boolean element)
  {
    return getList ().set (index, new Boolean (element)).booleanValue ();
  }

  public BooleanList subList (final int fromIndex, final int toIndex)
  {
    return ListBooleanList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (@Nullable final Object obj)
  {
    if (obj == this)
      return true;
    if (!(obj instanceof BooleanList))
      return false;

    final BooleanList that = (BooleanList) obj;
    if (size () != that.size ())
      return false;
    final BooleanIterator thisiter = iterator ();
    final BooleanIterator thatiter = that.iterator ();
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
