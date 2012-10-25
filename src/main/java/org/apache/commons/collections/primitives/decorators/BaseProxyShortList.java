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

import org.apache.commons.collections.primitives.ShortCollection;
import org.apache.commons.collections.primitives.ShortList;
import org.apache.commons.collections.primitives.ShortListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480463 $ $Date: 2006-11-29 09:15:23 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class BaseProxyShortList extends BaseProxyShortCollection implements ShortList
{
  protected BaseProxyShortList ()
  {}

  @Nonnull
  protected abstract ShortList getProxiedList ();

  @Override
  @Nonnull
  protected final ShortCollection getProxiedCollection ()
  {
    return getProxiedList ();
  }

  public void add (final int nIndex, final short aElement)
  {
    getProxiedList ().add (nIndex, aElement);
  }

  public boolean addAll (final int nIndex, @Nonnull final ShortCollection collection)
  {
    return getProxiedList ().addAll (nIndex, collection);
  }

  public short get (final int nIndex)
  {
    return getProxiedList ().get (nIndex);
  }

  public int indexOf (final short aElement)
  {
    return getProxiedList ().indexOf (aElement);
  }

  public int lastIndexOf (final short aElement)
  {
    return getProxiedList ().lastIndexOf (aElement);
  }

  @Nonnull
  public ShortListIterator listIterator ()
  {
    return getProxiedList ().listIterator ();
  }

  @Nonnull
  public ShortListIterator listIterator (final int nIndex)
  {
    return getProxiedList ().listIterator (nIndex);
  }

  public short removeElementAt (final int nIndex)
  {
    return getProxiedList ().removeElementAt (nIndex);
  }

  public short set (final int nIndex, final short aElement)
  {
    return getProxiedList ().set (nIndex, aElement);
  }

  @Nonnull
  public ShortList subList (final int nFromIndex, final int nToIndex)
  {
    return getProxiedList ().subList (nFromIndex, nToIndex);
  }
}
