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

import org.apache.commons.collections.primitives.ZZZCollection;
import org.apache.commons.collections.primitives.ZZZIterator;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * @since Commons Primitives 0.1
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class AbstractCollectionZZZCollection implements ZZZCollection
{
  protected AbstractCollectionZZZCollection ()
  {}

  @Nonnull
  protected abstract Collection <XXX> getCollection ();

  public boolean add (final YYY aElement)
  {
    return getCollection ().add (XXX.valueOf (aElement));
  }

  public boolean addAll (@Nonnull final ZZZCollection c)
  {
    return getCollection ().addAll (ZZZCollectionCollection.wrap (c));
  }

  public void clear ()
  {
    getCollection ().clear ();
  }

  public boolean contains (final YYY aElement)
  {
    return getCollection ().contains (XXX.valueOf (aElement));
  }

  public boolean containsAll (@Nonnull final ZZZCollection c)
  {
    return getCollection ().containsAll (ZZZCollectionCollection.wrap (c));
  }

  public boolean isEmpty ()
  {
    return getCollection ().isEmpty ();
  }

  /**
   * {@link IteratorZZZIterator#wrap wraps} the {@link java.util.Iterator
   * Iterator} returned by my underlying {@link Collection Collection}, if any.
   */
  @Nonnull
  public ZZZIterator iterator ()
  {
    return IteratorZZZIterator.wrap (getCollection ().iterator ());
  }

  public boolean removeElement (final YYY aElement)
  {
    return getCollection ().remove (XXX.valueOf (aElement));
  }

  public boolean removeAll (@Nonnull final ZZZCollection c)
  {
    return getCollection ().removeAll (ZZZCollectionCollection.wrap (c));
  }

  public boolean retainAll (@Nonnull final ZZZCollection c)
  {
    return getCollection ().retainAll (ZZZCollectionCollection.wrap (c));
  }

  @Nonnegative
  public int size ()
  {
    return getCollection ().size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public YYY [] toArray ()
  {
    final Object [] aElementData = getCollection ().toArray ();
    final YYY [] dest = new YYY [aElementData.length];
    for (int i = 0; i < aElementData.length; i++)
      dest[i] = ((XXX) (aElementData[i])).YYYValue ();
    return dest;
  }

  @Nonnull
  public YYY [] toArray (@Nonnull final YYY [] dest)
  {
    final Object [] aElementData = getCollection ().toArray ();
    YYY [] ret = dest;
    if (ret.length < aElementData.length)
      ret = new YYY [aElementData.length];
    for (int i = 0; i < aElementData.length; i++)
    {
      ret[i] = ((XXX) (aElementData[i])).YYYValue ();
    }
    return ret;
  }

  @Override
  public String toString ()
  {
    return getCollection ().toString ();
  }
}
