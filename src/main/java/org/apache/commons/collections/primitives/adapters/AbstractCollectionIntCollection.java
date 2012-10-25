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

import org.apache.commons.collections.primitives.IntCollection;
import org.apache.commons.collections.primitives.IntIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractCollectionIntCollection implements IntCollection
{
  protected AbstractCollectionIntCollection ()
  {}

  public boolean add (final int element)
  {
    return getCollection ().add (new Integer (element));
  }

  public boolean addAll (final IntCollection c)
  {
    return getCollection ().addAll (IntCollectionCollection.wrap (c));
  }

  public void clear ()
  {
    getCollection ().clear ();
  }

  public boolean contains (final int element)
  {
    return getCollection ().contains (new Integer (element));
  }

  public boolean containsAll (final IntCollection c)
  {
    return getCollection ().containsAll (IntCollectionCollection.wrap (c));
  }

  @Override
  public String toString ()
  {
    return getCollection ().toString ();
  }

  public boolean isEmpty ()
  {
    return getCollection ().isEmpty ();
  }

  /**
   * {@link IteratorIntIterator#wrap wraps} the {@link java.util.Iterator
   * Iterator} returned by my underlying {@link Collection Collection}, if any.
   */
  public IntIterator iterator ()
  {
    return IteratorIntIterator.wrap (getCollection ().iterator ());
  }

  public boolean removeElement (final int element)
  {
    return getCollection ().remove (new Integer (element));
  }

  public boolean removeAll (final IntCollection c)
  {
    return getCollection ().removeAll (IntCollectionCollection.wrap (c));
  }

  public boolean retainAll (final IntCollection c)
  {
    return getCollection ().retainAll (IntCollectionCollection.wrap (c));
  }

  public int size ()
  {
    return getCollection ().size ();
  }

  public int [] toArray ()
  {
    final Object [] src = getCollection ().toArray ();
    final int [] dest = new int [src.length];
    for (int i = 0; i < src.length; i++)
    {
      dest[i] = ((Number) (src[i])).intValue ();
    }
    return dest;
  }

  public int [] toArray (int [] dest)
  {
    final Object [] src = getCollection ().toArray ();
    if (dest.length < src.length)
    {
      dest = new int [src.length];
    }
    for (int i = 0; i < src.length; i++)
    {
      dest[i] = ((Number) (src[i])).intValue ();
    }
    return dest;
  }

  protected abstract Collection getCollection ();

}
