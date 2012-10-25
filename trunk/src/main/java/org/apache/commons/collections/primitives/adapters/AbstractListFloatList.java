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

import org.apache.commons.collections.primitives.FloatCollection;
import org.apache.commons.collections.primitives.FloatIterator;
import org.apache.commons.collections.primitives.FloatList;
import org.apache.commons.collections.primitives.FloatListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListFloatList extends AbstractCollectionFloatCollection implements FloatList
{
  @Override
  @Nonnull
  protected final Collection <Float> getCollection ()
  {
    return getList ();
  }

  @Nonnull
  protected abstract List <Float> getList ();

  public void add (final int nIndex, final float element)
  {
    getList ().add (nIndex, Float.valueOf (element));
  }

  public boolean addAll (final int index, final FloatCollection collection)
  {
    return getList ().addAll (index, FloatCollectionCollection.wrap (collection));
  }

  public float get (final int index)
  {
    return getList ().get (index).floatValue ();
  }

  public int indexOf (final float element)
  {
    return getList ().indexOf (Float.valueOf (element));
  }

  public int lastIndexOf (final float aElement)
  {
    return getList ().lastIndexOf (Float.valueOf (aElement));
  }

  /**
   * {@link ListIteratorFloatListIterator#wrap wraps} the {@link FloatList
   * FloatList} returned by my underlying {@link FloatListIterator
   * FloatListIterator}, if any.
   */
  @Nonnull
  public FloatListIterator listIterator ()
  {
    return ListIteratorFloatListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorFloatListIterator#wrap wraps} the {@link FloatList
   * FloatList} returned by my underlying {@link FloatListIterator
   * FloatListIterator}, if any.
   */
  @Nonnull
  public FloatListIterator listIterator (final int nIndex)
  {
    return ListIteratorFloatListIterator.wrap (getList ().listIterator (nIndex));
  }

  public float removeElementAt (final int index)
  {
    return getList ().remove (index).floatValue ();
  }

  public float set (final int index, final float element)
  {
    return getList ().set (index, new Float (element)).floatValue ();
  }

  public FloatList subList (final int fromIndex, final int toIndex)
  {
    return ListFloatList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (@Nullable final Object obj)
  {
    if (obj == this)
      return true;
    if (!(obj instanceof FloatList))
      return false;

    final FloatList that = (FloatList) obj;
    if (size () != that.size ())
      return false;
    final FloatIterator thisiter = iterator ();
    final FloatIterator thatiter = that.iterator ();
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
