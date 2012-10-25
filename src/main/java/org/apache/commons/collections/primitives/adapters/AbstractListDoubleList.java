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

import org.apache.commons.collections.primitives.DoubleCollection;
import org.apache.commons.collections.primitives.DoubleIterator;
import org.apache.commons.collections.primitives.DoubleList;
import org.apache.commons.collections.primitives.DoubleListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractListDoubleList extends AbstractCollectionDoubleCollection implements DoubleList
{
  @Override
  @Nonnull
  protected final Collection <Double> getCollection ()
  {
    return getList ();
  }

  @Nonnull
  protected abstract List <Double> getList ();

  public void add (final int nIndex, final double element)
  {
    getList ().add (nIndex, Double.valueOf (element));
  }

  public boolean addAll (final int index, final DoubleCollection collection)
  {
    return getList ().addAll (index, DoubleCollectionCollection.wrap (collection));
  }

  public double get (final int index)
  {
    return getList ().get (index).doubleValue ();
  }

  public int indexOf (final double element)
  {
    return getList ().indexOf (Double.valueOf (element));
  }

  public int lastIndexOf (final double aElement)
  {
    return getList ().lastIndexOf (Double.valueOf (aElement));
  }

  /**
   * {@link ListIteratorDoubleListIterator#wrap wraps} the {@link DoubleList
   * DoubleList} returned by my underlying {@link DoubleListIterator
   * DoubleListIterator}, if any.
   */
  @Nonnull
  public DoubleListIterator listIterator ()
  {
    return ListIteratorDoubleListIterator.wrap (getList ().listIterator ());
  }

  /**
   * {@link ListIteratorDoubleListIterator#wrap wraps} the {@link DoubleList
   * DoubleList} returned by my underlying {@link DoubleListIterator
   * DoubleListIterator}, if any.
   */
  @Nonnull
  public DoubleListIterator listIterator (final int nIndex)
  {
    return ListIteratorDoubleListIterator.wrap (getList ().listIterator (nIndex));
  }

  public double removeElementAt (final int index)
  {
    return getList ().remove (index).doubleValue ();
  }

  public double set (final int index, final double element)
  {
    return getList ().set (index, new Double (element)).doubleValue ();
  }

  public DoubleList subList (final int fromIndex, final int toIndex)
  {
    return ListDoubleList.wrap (getList ().subList (fromIndex, toIndex));
  }

  @Override
  public boolean equals (@Nullable final Object obj)
  {
    if (obj == this)
      return true;
    if (!(obj instanceof DoubleList))
      return false;

    final DoubleList that = (DoubleList) obj;
    if (size () != that.size ())
      return false;
    final DoubleIterator thisiter = iterator ();
    final DoubleIterator thatiter = that.iterator ();
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
