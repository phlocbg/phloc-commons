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

import org.apache.commons.collections.primitives.ZZZCollection;
import org.apache.commons.collections.primitives.ZZZIterator;
import org.apache.commons.collections.primitives.ZZZList;
import org.apache.commons.collections.primitives.ZZZListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListZZZList extends AbstractCollectionZZZCollection implements ZZZList
{
  @Override
  @Nonnull
  protected final Collection <XXX> getCollection ()
  {
    return getList ();
  }

  @Nonnull
  protected abstract List <XXX> getList ();

  public void add (final int nIndex, final YYY element)
  {
    getList ().add (nIndex, XXX.valueOf (element));
  }

  public boolean addAll (final int index, final ZZZCollection collection)
  {
    return getList ().addAll (index, ZZZCollectionCollection.wrap (collection));
  }

  public YYY get (final int index)
  {
    return getList ().get (index).YYYValue ();
  }

  public int indexOf (final YYY element)
  {
    return getList ().indexOf (XXX.valueOf (element));
  }

  public int lastIndexOf (final YYY aElement)
  {
    return getList ().lastIndexOf (XXX.valueOf (aElement));
  }

  /**
   * {@link ListIteratorZZZListIterator#wrap wraps} the {@link ZZZList
   * ZZZList} returned by my underlying {@link ZZZListIterator
   * ZZZListIterator}, if any.
   */
  @Nonnull
  public ZZZListIterator listIterator ()
  {
    return ListIteratorZZZListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorZZZListIterator#wrap wraps} the {@link ZZZList
   * ZZZList} returned by my underlying {@link ZZZListIterator
   * ZZZListIterator}, if any.
   */
  @Nonnull
  public ZZZListIterator listIterator (final int nIndex)
  {
    return ListIteratorZZZListIterator.wrap (getList ().listIterator (nIndex));
  }

  public YYY removeElementAt (final int index)
  {
    return getList ().remove (index).YYYValue ();
  }

  public YYY set (final int index, final YYY element)
  {
    return getList ().set (index, new XXX (element)).YYYValue ();
  }

  public ZZZList subList (final int fromIndex, final int toIndex)
  {
    return ListZZZList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (@Nullable final Object obj)
  {
    if (obj == this)
      return true;
    if (!(obj instanceof ZZZList))
      return false;

    final ZZZList that = (ZZZList) obj;
    if (size () != that.size ())
      return false;
    final ZZZIterator thisiter = iterator ();
    final ZZZIterator thatiter = that.iterator ();
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
