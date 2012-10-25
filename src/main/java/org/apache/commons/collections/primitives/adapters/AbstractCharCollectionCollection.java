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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.primitives.CharCollection;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractCharCollectionCollection implements Collection
{

  public boolean add (final Object element)
  {
    return getCharCollection ().add (((Character) element).charValue ());
  }

  public boolean addAll (final Collection c)
  {
    return getCharCollection ().addAll (CollectionCharCollection.wrap (c));
  }

  public void clear ()
  {
    getCharCollection ().clear ();
  }

  public boolean contains (final Object element)
  {
    return getCharCollection ().contains (((Character) element).charValue ());
  }

  public boolean containsAll (final Collection c)
  {
    return getCharCollection ().containsAll (CollectionCharCollection.wrap (c));
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
  public Iterator iterator ()
  {
    return CharIteratorIterator.wrap (getCharCollection ().iterator ());
  }

  public boolean remove (final Object element)
  {
    return getCharCollection ().removeElement (((Character) element).charValue ());
  }

  public boolean removeAll (final Collection c)
  {
    return getCharCollection ().removeAll (CollectionCharCollection.wrap (c));
  }

  public boolean retainAll (final Collection c)
  {
    return getCharCollection ().retainAll (CollectionCharCollection.wrap (c));
  }

  public int size ()
  {
    return getCharCollection ().size ();
  }

  public Object [] toArray ()
  {
    final char [] a = getCharCollection ().toArray ();
    final Object [] A = new Object [a.length];
    for (int i = 0; i < a.length; i++)
    {
      A[i] = new Character (a[i]);
    }
    return A;
  }

  public Object [] toArray (Object [] A)
  {
    final char [] a = getCharCollection ().toArray ();
    if (A.length < a.length)
    {
      A = (Object []) (Array.newInstance (A.getClass ().getComponentType (), a.length));
    }
    for (int i = 0; i < a.length; i++)
    {
      A[i] = new Character (a[i]);
    }
    if (A.length > a.length)
    {
      A[a.length] = null;
    }

    return A;
  }

  protected abstract CharCollection getCharCollection ();
}
