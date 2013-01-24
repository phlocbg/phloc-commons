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

import java.util.Collection;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.apache.commons.collections.primitives.ShortCollection;
import org.apache.commons.collections.primitives.ShortIterator;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * @since Commons Primitives 0.1
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractCollectionShortCollection implements ShortCollection
{
  protected AbstractCollectionShortCollection ()
  {}

  @Nonnull
  protected abstract Collection <Short> getCollection ();

  public boolean add (final short aElement)
  {
    return getCollection ().add (Short.valueOf (aElement));
  }

  public boolean addAll (@Nonnull final ShortCollection c)
  {
    return getCollection ().addAll (ShortCollectionCollection.wrap (c));
  }

  public void clear ()
  {
    getCollection ().clear ();
  }

  public boolean contains (final short aElement)
  {
    return getCollection ().contains (Short.valueOf (aElement));
  }

  public boolean containsAll (@Nonnull final ShortCollection c)
  {
    return getCollection ().containsAll (ShortCollectionCollection.wrap (c));
  }

  public boolean isEmpty ()
  {
    return getCollection ().isEmpty ();
  }

  /**
   * {@link IteratorShortIterator#wrap wraps} the {@link java.util.Iterator
   * Iterator} returned by my underlying {@link Collection Collection}, if any.
   */
  @Nonnull
  public ShortIterator iterator ()
  {
    return IteratorShortIterator.wrap (getCollection ().iterator ());
  }

  public boolean removeElement (final short aElement)
  {
    return getCollection ().remove (Short.valueOf (aElement));
  }

  public boolean removeAll (@Nonnull final ShortCollection c)
  {
    return getCollection ().removeAll (ShortCollectionCollection.wrap (c));
  }

  public boolean retainAll (@Nonnull final ShortCollection c)
  {
    return getCollection ().retainAll (ShortCollectionCollection.wrap (c));
  }

  @Nonnegative
  public int size ()
  {
    return getCollection ().size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public short [] toArray ()
  {
    final Object [] aElementData = getCollection ().toArray ();
    final short [] dest = new short [aElementData.length];
    for (int i = 0; i < aElementData.length; i++)
      dest[i] = ((Short) (aElementData[i])).shortValue ();
    return dest;
  }

  @Nonnull
  public short [] toArray (@Nonnull final short [] dest)
  {
    final Object [] aElementData = getCollection ().toArray ();
    short [] ret = dest;
    if (ret.length < aElementData.length)
      ret = new short [aElementData.length];
    for (int i = 0; i < aElementData.length; i++)
    {
      ret[i] = ((Short) (aElementData[i])).shortValue ();
    }
    return ret;
  }

  @Override
  public String toString ()
  {
    return getCollection ().toString ();
  }
}
