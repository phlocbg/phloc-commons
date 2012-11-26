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

import org.apache.commons.collections.primitives.LongCollection;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractLongCollectionCollection implements Collection <Long>
{
  @Nonnull
  protected abstract LongCollection getLongCollection ();

  public boolean add (final Long element)
  {
    return getLongCollection ().add (element.longValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean addAll (final Collection <? extends Long> c)
  {
    return getLongCollection ().addAll (CollectionLongCollection.wrap ((Collection <Long>) c));
  }

  public void clear ()
  {
    getLongCollection ().clear ();
  }

  public boolean contains (final Object element)
  {
    return getLongCollection ().contains (((Long) element).longValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean containsAll (final Collection <?> c)
  {
    return getLongCollection ().containsAll (CollectionLongCollection.wrap ((Collection <Long>) c));
  }

  @Override
  public String toString ()
  {
    return getLongCollection ().toString ();
  }

  public boolean isEmpty ()
  {
    return getLongCollection ().isEmpty ();
  }

  /**
   * {@link LongIteratorIterator#wrap wraps} the
   * {@link org.apache.commons.collections.primitives.LongIterator LongIterator}
   * returned by my underlying {@link LongCollection LongCollection}, if any.
   */
  public Iterator <Long> iterator ()
  {
    return LongIteratorIterator.wrap (getLongCollection ().iterator ());
  }

  public boolean remove (final Object element)
  {
    return getLongCollection ().removeElement (((Long) element).longValue ());
  }

  @SuppressWarnings ("unchecked")
  public boolean removeAll (final Collection <?> c)
  {
    return getLongCollection ().removeAll (CollectionLongCollection.wrap ((Collection <Long>) c));
  }

  @SuppressWarnings ("unchecked")
  public boolean retainAll (final Collection <?> c)
  {
    return getLongCollection ().retainAll (CollectionLongCollection.wrap ((Collection <Long>) c));
  }

  public int size ()
  {
    return getLongCollection ().size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Long [] toArray ()
  {
    final long [] a = getLongCollection ().toArray ();
    final Long [] A = new Long [a.length];
    for (int i = 0; i < a.length; i++)
      A[i] = Long.valueOf (a[i]);
    return A;
  }

  @SuppressWarnings ("unchecked")
  public <T> T [] toArray (final T [] a)
  {
    final Long [] elementData = toArray ();
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
