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

import org.apache.commons.collections.primitives.FloatCollection;
import org.apache.commons.collections.primitives.FloatIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractCollectionFloatCollection implements FloatCollection
{
  protected AbstractCollectionFloatCollection ()
  {}

  public boolean add (final float element)
  {
    return getCollection ().add (new Float (element));
  }

  public boolean addAll (final FloatCollection c)
  {
    return getCollection ().addAll (FloatCollectionCollection.wrap (c));
  }

  public void clear ()
  {
    getCollection ().clear ();
  }

  public boolean contains (final float element)
  {
    return getCollection ().contains (new Float (element));
  }

  public boolean containsAll (final FloatCollection c)
  {
    return getCollection ().containsAll (FloatCollectionCollection.wrap (c));
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
   * {@link IteratorFloatIterator#wrap wraps} the {@link java.util.Iterator
   * Iterator} returned by my underlying {@link Collection Collection}, if any.
   */
  public FloatIterator iterator ()
  {
    return IteratorFloatIterator.wrap (getCollection ().iterator ());
  }

  public boolean removeElement (final float element)
  {
    return getCollection ().remove (new Float (element));
  }

  public boolean removeAll (final FloatCollection c)
  {
    return getCollection ().removeAll (FloatCollectionCollection.wrap (c));
  }

  public boolean retainAll (final FloatCollection c)
  {
    return getCollection ().retainAll (FloatCollectionCollection.wrap (c));
  }

  public int size ()
  {
    return getCollection ().size ();
  }

  public float [] toArray ()
  {
    final Object [] src = getCollection ().toArray ();
    final float [] dest = new float [src.length];
    for (int i = 0; i < src.length; i++)
    {
      dest[i] = ((Number) (src[i])).floatValue ();
    }
    return dest;
  }

  public float [] toArray (float [] dest)
  {
    final Object [] src = getCollection ().toArray ();
    if (dest.length < src.length)
    {
      dest = new float [src.length];
    }
    for (int i = 0; i < src.length; i++)
    {
      dest[i] = ((Number) (src[i])).floatValue ();
    }
    return dest;
  }

  protected abstract Collection getCollection ();

}
