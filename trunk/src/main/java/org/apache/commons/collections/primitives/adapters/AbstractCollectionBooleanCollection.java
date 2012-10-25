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

import org.apache.commons.collections.primitives.BooleanCollection;
import org.apache.commons.collections.primitives.BooleanIterator;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * @since Commons Primitives 0.1
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractCollectionBooleanCollection implements BooleanCollection
{
  protected AbstractCollectionBooleanCollection ()
  {}

  @Nonnull
  protected abstract Collection <Boolean> getCollection ();

  public boolean add (final boolean aElement)
  {
    return getCollection ().add (Boolean.valueOf (aElement));
  }

  public boolean addAll (@Nonnull final BooleanCollection c)
  {
    return getCollection ().addAll (BooleanCollectionCollection.wrap (c));
  }

  public void clear ()
  {
    getCollection ().clear ();
  }

  public boolean contains (final boolean aElement)
  {
    return getCollection ().contains (Boolean.valueOf (aElement));
  }

  public boolean containsAll (@Nonnull final BooleanCollection c)
  {
    return getCollection ().containsAll (BooleanCollectionCollection.wrap (c));
  }

  public boolean isEmpty ()
  {
    return getCollection ().isEmpty ();
  }

  /**
   * {@link IteratorBooleanIterator#wrap wraps} the {@link java.util.Iterator
   * Iterator} returned by my underlying {@link Collection Collection}, if any.
   */
  @Nonnull
  public BooleanIterator iterator ()
  {
    return IteratorBooleanIterator.wrap (getCollection ().iterator ());
  }

  public boolean removeElement (final boolean aElement)
  {
    return getCollection ().remove (Boolean.valueOf (aElement));
  }

  public boolean removeAll (@Nonnull final BooleanCollection c)
  {
    return getCollection ().removeAll (BooleanCollectionCollection.wrap (c));
  }

  public boolean retainAll (@Nonnull final BooleanCollection c)
  {
    return getCollection ().retainAll (BooleanCollectionCollection.wrap (c));
  }

  @Nonnegative
  public int size ()
  {
    return getCollection ().size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public boolean [] toArray ()
  {
    final Object [] aElementData = getCollection ().toArray ();
    final boolean [] dest = new boolean [aElementData.length];
    for (int i = 0; i < aElementData.length; i++)
      dest[i] = ((Boolean) (aElementData[i])).booleanValue ();
    return dest;
  }

  @Nonnull
  public boolean [] toArray (@Nonnull final boolean [] dest)
  {
    final Object [] aElementData = getCollection ().toArray ();
    boolean [] ret = dest;
    if (ret.length < aElementData.length)
      ret = new boolean [aElementData.length];
    for (int i = 0; i < aElementData.length; i++)
    {
      ret[i] = ((Boolean) (aElementData[i])).booleanValue ();
    }
    return ret;
  }

  @Override
  public String toString ()
  {
    return getCollection ().toString ();
  }
}
