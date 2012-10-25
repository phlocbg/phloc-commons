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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nonnull;

import org.apache.commons.collections.primitives.ShortCollection;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractShortCollectionCollection implements Collection <Short>
{
  @Nonnull
  protected abstract ShortCollection getShortCollection ();

  public boolean add (final Short element)
  {
    return getShortCollection ().add (element.shortValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final Collection <? extends Short> c)
  {
    return getShortCollection ().addAll (CollectionShortCollection.wrap ((Collection <Short>) c));
  }

  public void clear ()
  {
    getShortCollection ().clear ();
  }

  public boolean contains (final Object element)
  {
    return getShortCollection ().contains (((Short) element).shortValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean containsAll (final Collection <?> c)
  {
    return getShortCollection ().containsAll (CollectionShortCollection.wrap ((Collection <Short>) c));
  }

  @Override
  public String toString ()
  {
    return getShortCollection ().toString ();
  }

  public boolean isEmpty ()
  {
    return getShortCollection ().isEmpty ();
  }

  /**
   * {@link ShortIteratorIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ShortIterator ShortIterator}
   * returned by my underlying {@link ShortCollection ShortCollection}, if any.
   */
  public Iterator <Short> iterator ()
  {
    return ShortIteratorIterator.wrap (getShortCollection ().iterator ());
  }

  public boolean remove (final Object element)
  {
    return getShortCollection ().removeElement (((Short) element).shortValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean removeAll (final Collection <?> c)
  {
    return getShortCollection ().removeAll (CollectionShortCollection.wrap ((Collection <Short>) c));
  }

  @SuppressWarnings ("unchecked")
  public boolean retainAll (final Collection <?> c)
  {
    return getShortCollection ().retainAll (CollectionShortCollection.wrap ((Collection <Short>) c));
  }

  public int size ()
  {
    return getShortCollection ().size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Short [] toArray ()
  {
    final short [] a = getShortCollection ().toArray ();
    final Short [] A = new Short [a.length];
    for (int i = 0; i < a.length; i++)
      A[i] = Short.valueOf (a[i]);
    return A;
  }

  @SuppressWarnings ("unchecked")
  public <T> T [] toArray (final T [] a)
  {
    final Short [] elementData = toArray ();
    final int size = size ();
    if (a.length < size)
    {
      // Make a new array of a's runtime type, but my contents:
      return (T []) Arrays.copyOf (elementData, size, a.getClass ());
    }
    System.arraycopy (elementData, 0, a, 0, size);
    if (a.length > size)
      a[size] = null;
    return a;
  }
}
