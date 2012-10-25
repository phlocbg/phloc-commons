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

import java.util.Collection;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.apache.commons.collections.primitives.IntCollection;
import org.apache.commons.collections.primitives.IntIterator;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * @since Commons Primitives 0.1
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractCollectionIntCollection implements IntCollection
{
  protected AbstractCollectionIntCollection ()
  {}

  @Nonnull
  protected abstract Collection <Integer> getCollection ();

  public boolean add (final int aElement)
  {
    return getCollection ().add (Integer.valueOf (aElement));
  }

  public boolean addAll (@Nonnull final IntCollection c)
  {
    return getCollection ().addAll (IntCollectionCollection.wrap (c));
  }

  public void clear ()
  {
    getCollection ().clear ();
  }

  public boolean contains (final int aElement)
  {
    return getCollection ().contains (Integer.valueOf (aElement));
  }

  public boolean containsAll (@Nonnull final IntCollection c)
  {
    return getCollection ().containsAll (IntCollectionCollection.wrap (c));
  }

  public boolean isEmpty ()
  {
    return getCollection ().isEmpty ();
  }

  /**
   * {@link IteratorIntIterator#wrap wraps} the {@link java.util.Iterator
   * Iterator} returned by my underlying {@link Collection Collection}, if any.
   */
  @Nonnull
  public IntIterator iterator ()
  {
    return IteratorIntIterator.wrap (getCollection ().iterator ());
  }

  public boolean removeElement (final int aElement)
  {
    return getCollection ().remove (Integer.valueOf (aElement));
  }

  public boolean removeAll (@Nonnull final IntCollection c)
  {
    return getCollection ().removeAll (IntCollectionCollection.wrap (c));
  }

  public boolean retainAll (@Nonnull final IntCollection c)
  {
    return getCollection ().retainAll (IntCollectionCollection.wrap (c));
  }

  @Nonnegative
  public int size ()
  {
    return getCollection ().size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public int [] toArray ()
  {
    final Object [] aElementData = getCollection ().toArray ();
    final int [] dest = new int [aElementData.length];
    for (int i = 0; i < aElementData.length; i++)
      dest[i] = ((Integer) (aElementData[i])).intValue ();
    return dest;
  }

  @Nonnull
  public int [] toArray (@Nonnull final int [] dest)
  {
    final Object [] aElementData = getCollection ().toArray ();
    int [] ret = dest;
    if (ret.length < aElementData.length)
      ret = new int [aElementData.length];
    for (int i = 0; i < aElementData.length; i++)
    {
      ret[i] = ((Integer) (aElementData[i])).intValue ();
    }
    return ret;
  }

  @Override
  public String toString ()
  {
    return getCollection ().toString ();
  }
}
