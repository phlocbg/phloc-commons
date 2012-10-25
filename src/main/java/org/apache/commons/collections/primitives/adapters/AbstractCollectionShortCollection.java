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

import org.apache.commons.collections.primitives.ShortCollection;
import org.apache.commons.collections.primitives.ShortIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractCollectionShortCollection implements ShortCollection
{
  protected AbstractCollectionShortCollection ()
  {}

  public boolean add (final short element)
  {
    return getCollection ().add (new Short (element));
  }

  public boolean addAll (final ShortCollection c)
  {
    return getCollection ().addAll (ShortCollectionCollection.wrap (c));
  }

  public void clear ()
  {
    getCollection ().clear ();
  }

  public boolean contains (final short element)
  {
    return getCollection ().contains (new Short (element));
  }

  public boolean containsAll (final ShortCollection c)
  {
    return getCollection ().containsAll (ShortCollectionCollection.wrap (c));
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
   * {@link IteratorShortIterator#wrap wraps} the {@link java.util.Iterator
   * Iterator} returned by my underlying {@link Collection Collection}, if any.
   */
  public ShortIterator iterator ()
  {
    return IteratorShortIterator.wrap (getCollection ().iterator ());
  }

  public boolean removeElement (final short element)
  {
    return getCollection ().remove (new Short (element));
  }

  public boolean removeAll (final ShortCollection c)
  {
    return getCollection ().removeAll (ShortCollectionCollection.wrap (c));
  }

  public boolean retainAll (final ShortCollection c)
  {
    return getCollection ().retainAll (ShortCollectionCollection.wrap (c));
  }

  public int size ()
  {
    return getCollection ().size ();
  }

  public short [] toArray ()
  {
    final Object [] src = getCollection ().toArray ();
    final short [] dest = new short [src.length];
    for (int i = 0; i < src.length; i++)
    {
      dest[i] = ((Number) (src[i])).shortValue ();
    }
    return dest;
  }

  public short [] toArray (short [] dest)
  {
    final Object [] src = getCollection ().toArray ();
    if (dest.length < src.length)
    {
      dest = new short [src.length];
    }
    for (int i = 0; i < src.length; i++)
    {
      dest[i] = ((Number) (src[i])).shortValue ();
    }
    return dest;
  }

  protected abstract Collection getCollection ();

}
