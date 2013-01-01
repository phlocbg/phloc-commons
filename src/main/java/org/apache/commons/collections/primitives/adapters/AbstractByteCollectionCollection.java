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

import org.apache.commons.collections.primitives.ByteCollection;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractByteCollectionCollection implements Collection <Byte>
{
  @Nonnull
  protected abstract ByteCollection getByteCollection ();

  public boolean add (final Byte element)
  {
    return getByteCollection ().add (element.byteValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final Collection <? extends Byte> c)
  {
    return getByteCollection ().addAll (CollectionByteCollection.wrap ((Collection <Byte>) c));
  }

  public void clear ()
  {
    getByteCollection ().clear ();
  }

  public boolean contains (final Object element)
  {
    return getByteCollection ().contains (((Byte) element).byteValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean containsAll (final Collection <?> c)
  {
    return getByteCollection ().containsAll (CollectionByteCollection.wrap ((Collection <Byte>) c));
  }

  @Override
  public String toString ()
  {
    return getByteCollection ().toString ();
  }

  public boolean isEmpty ()
  {
    return getByteCollection ().isEmpty ();
  }

  /**
   * {@link ByteIteratorIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.ByteIterator ByteIterator}
   * returned by my underlying {@link ByteCollection ByteCollection}, if any.
   */
  public Iterator <Byte> iterator ()
  {
    return ByteIteratorIterator.wrap (getByteCollection ().iterator ());
  }

  public boolean remove (final Object element)
  {
    return getByteCollection ().removeElement (((Byte) element).byteValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean removeAll (final Collection <?> c)
  {
    return getByteCollection ().removeAll (CollectionByteCollection.wrap ((Collection <Byte>) c));
  }

  @SuppressWarnings ("unchecked")
  public boolean retainAll (final Collection <?> c)
  {
    return getByteCollection ().retainAll (CollectionByteCollection.wrap ((Collection <Byte>) c));
  }

  public int size ()
  {
    return getByteCollection ().size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Byte [] toArray ()
  {
    final byte [] a = getByteCollection ().toArray ();
    final Byte [] A = new Byte [a.length];
    for (int i = 0; i < a.length; i++)
      A[i] = Byte.valueOf (a[i]);
    return A;
  }

  @SuppressWarnings ("unchecked")
  public <T> T [] toArray (final T [] a)
  {
    final Byte [] elementData = toArray ();
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
