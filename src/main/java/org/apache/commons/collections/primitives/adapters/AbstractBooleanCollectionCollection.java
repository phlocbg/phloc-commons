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

import org.apache.commons.collections.primitives.BooleanCollection;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractBooleanCollectionCollection implements Collection <Boolean>
{
  @Nonnull
  protected abstract BooleanCollection getBooleanCollection ();

  public boolean add (final Boolean element)
  {
    return getBooleanCollection ().add (element.booleanValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final Collection <? extends Boolean> c)
  {
    return getBooleanCollection ().addAll (CollectionBooleanCollection.wrap ((Collection <Boolean>) c));
  }

  public void clear ()
  {
    getBooleanCollection ().clear ();
  }

  public boolean contains (final Object element)
  {
    return getBooleanCollection ().contains (((Boolean) element).booleanValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean containsAll (final Collection <?> c)
  {
    return getBooleanCollection ().containsAll (CollectionBooleanCollection.wrap ((Collection <Boolean>) c));
  }

  @Override
  public String toString ()
  {
    return getBooleanCollection ().toString ();
  }

  public boolean isEmpty ()
  {
    return getBooleanCollection ().isEmpty ();
  }

  /**
   * {@link BooleanIteratorIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.BooleanIterator BooleanIterator}
   * returned by my underlying {@link BooleanCollection BooleanCollection}, if any.
   */
  public Iterator <Boolean> iterator ()
  {
    return BooleanIteratorIterator.wrap (getBooleanCollection ().iterator ());
  }

  public boolean remove (final Object element)
  {
    return getBooleanCollection ().removeElement (((Boolean) element).booleanValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean removeAll (final Collection <?> c)
  {
    return getBooleanCollection ().removeAll (CollectionBooleanCollection.wrap ((Collection <Boolean>) c));
  }

  @SuppressWarnings ("unchecked")
  public boolean retainAll (final Collection <?> c)
  {
    return getBooleanCollection ().retainAll (CollectionBooleanCollection.wrap ((Collection <Boolean>) c));
  }

  public int size ()
  {
    return getBooleanCollection ().size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Boolean [] toArray ()
  {
    final boolean [] a = getBooleanCollection ().toArray ();
    final Boolean [] A = new Boolean [a.length];
    for (int i = 0; i < a.length; i++)
      A[i] = Boolean.valueOf (a[i]);
    return A;
  }

  @SuppressWarnings ("unchecked")
  public <T> T [] toArray (final T [] a)
  {
    final Boolean [] elementData = toArray ();
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
