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
package org.apache.commons.collections.primitives.decorators;

import javax.annotation.Nonnull;

import org.apache.commons.collections.primitives.LongCollection;
import org.apache.commons.collections.primitives.LongList;
import org.apache.commons.collections.primitives.LongListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480463 $ $Date: 2006-11-29 09:15:23 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class BaseProxyLongList extends BaseProxyLongCollection implements LongList
{
  protected BaseProxyLongList ()
  {}

  @Nonnull
  protected abstract LongList getProxiedList ();

  @Override
  @Nonnull
  protected final LongCollection getProxiedCollection ()
  {
    return getProxiedList ();
  }

  public void add (final int nIndex, final long aElement)
  {
    getProxiedList ().add (nIndex, aElement);
  }

  public boolean addAll (final int nIndex, @Nonnull final LongCollection collection)
  {
    return getProxiedList ().addAll (nIndex, collection);
  }

  public long get (final int nIndex)
  {
    return getProxiedList ().get (nIndex);
  }

  public int indexOf (final long aElement)
  {
    return getProxiedList ().indexOf (aElement);
  }

  public int lastIndexOf (final long aElement)
  {
    return getProxiedList ().lastIndexOf (aElement);
  }

  @Nonnull
  public LongListIterator listIterator ()
  {
    return getProxiedList ().listIterator ();
  }

  @Nonnull
  public LongListIterator listIterator (final int nIndex)
  {
    return getProxiedList ().listIterator (nIndex);
  }

  public long removeElementAt (final int nIndex)
  {
    return getProxiedList ().removeElementAt (nIndex);
  }

  public long set (final int nIndex, final long aElement)
  {
    return getProxiedList ().set (nIndex, aElement);
  }

  @Nonnull
  public LongList subList (final int nFromIndex, final int nToIndex)
  {
    return getProxiedList ().subList (nFromIndex, nToIndex);
  }
}
