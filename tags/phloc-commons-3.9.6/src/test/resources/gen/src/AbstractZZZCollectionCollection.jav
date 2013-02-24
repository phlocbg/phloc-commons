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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nonnull;

import org.apache.commons.collections.primitives.ZZZCollection;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractZZZCollectionCollection implements Collection <XXX>
{
  @Nonnull
  protected abstract ZZZCollection getZZZCollection ();

  public boolean add (final XXX element)
  {
    return getZZZCollection ().add (element.YYYValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final Collection <? extends XXX> c)
  {
    return getZZZCollection ().addAll (CollectionZZZCollection.wrap ((Collection <XXX>) c));
  }

  public void clear ()
  {
    getZZZCollection ().clear ();
  }

  public boolean contains (final Object element)
  {
    return getZZZCollection ().contains (((XXX) element).YYYValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean containsAll (final Collection <?> c)
  {
    return getZZZCollection ().containsAll (CollectionZZZCollection.wrap ((Collection <XXX>) c));
  }

  @Override
  public String toString ()
  {
    return getZZZCollection ().toString ();
  }

  public boolean isEmpty ()
  {
    return getZZZCollection ().isEmpty ();
  }

  /**
   * @return {@link ZZZIteratorIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ZZZIterator ZZZIterator}
   * returned by my underlying {@link ZZZCollection ZZZCollection}, if any.
   */
  public Iterator <XXX> iterator ()
  {
    return ZZZIteratorIterator.wrap (getZZZCollection ().iterator ());
  }

  public boolean remove (final Object element)
  {
    return getZZZCollection ().removeElement (((XXX) element).YYYValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean removeAll (final Collection <?> c)
  {
    return getZZZCollection ().removeAll (CollectionZZZCollection.wrap ((Collection <XXX>) c));
  }

  @SuppressWarnings ("unchecked")
  public boolean retainAll (final Collection <?> c)
  {
    return getZZZCollection ().retainAll (CollectionZZZCollection.wrap ((Collection <XXX>) c));
  }

  public int size ()
  {
    return getZZZCollection ().size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public XXX [] toArray ()
  {
    final YYY [] a = getZZZCollection ().toArray ();
    final XXX [] A = new XXX [a.length];
    for (int i = 0; i < a.length; i++)
      A[i] = XXX.valueOf (a[i]);
    return A;
  }

  @SuppressWarnings ("unchecked")
  public <T> T [] toArray (final T [] a)
  {
    final XXX [] elementData = toArray ();
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
