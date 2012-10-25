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

import org.apache.commons.collections.primitives.CharCollection;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractCharCollectionCollection implements Collection <Character>
{
  @Nonnull
  protected abstract CharCollection getCharCollection ();

  public boolean add (final Character element)
  {
    return getCharCollection ().add (element.charValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final Collection <? extends Character> c)
  {
    return getCharCollection ().addAll (CollectionCharCollection.wrap ((Collection <Character>) c));
  }

  public void clear ()
  {
    getCharCollection ().clear ();
  }

  public boolean contains (final Object element)
  {
    return getCharCollection ().contains (((Character) element).charValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean containsAll (final Collection <?> c)
  {
    return getCharCollection ().containsAll (CollectionCharCollection.wrap ((Collection <Character>) c));
  }

  @Override
  public String toString ()
  {
    return getCharCollection ().toString ();
  }

  public boolean isEmpty ()
  {
    return getCharCollection ().isEmpty ();
  }

  /**
   * {@link CharIteratorIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.CharIterator CharIterator}
   * returned by my underlying {@link CharCollection CharCollection}, if any.
   */
  public Iterator <Character> iterator ()
  {
    return CharIteratorIterator.wrap (getCharCollection ().iterator ());
  }

  public boolean remove (final Object element)
  {
    return getCharCollection ().removeElement (((Character) element).charValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean removeAll (final Collection <?> c)
  {
    return getCharCollection ().removeAll (CollectionCharCollection.wrap ((Collection <Character>) c));
  }

  @SuppressWarnings ("unchecked")
  public boolean retainAll (final Collection <?> c)
  {
    return getCharCollection ().retainAll (CollectionCharCollection.wrap ((Collection <Character>) c));
  }

  public int size ()
  {
    return getCharCollection ().size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Character [] toArray ()
  {
    final char [] a = getCharCollection ().toArray ();
    final Character [] A = new Character [a.length];
    for (int i = 0; i < a.length; i++)
      A[i] = Character.valueOf (a[i]);
    return A;
  }

  @SuppressWarnings ("unchecked")
  public <T> T [] toArray (final T [] a)
  {
    final Character [] elementData = toArray ();
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
