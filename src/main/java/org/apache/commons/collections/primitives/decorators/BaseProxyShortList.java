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
  protected abstract ShortList getProxiedList ();

  @Override
  protected final ShortCollection getProxiedCollection ()
  {
    return getProxiedList ();
  }

  protected BaseProxyShortList ()
  {}

  public void add (final int index, final short element)
  {
    getProxiedList ().add (index, element);
  }

  public boolean addAll (final int index, @Nonnull final ShortCollection collection)
  {
    return getProxiedList ().addAll (index, collection);
  }

  public short get (final int index)
  {
    return getProxiedList ().get (index);
  }

  public int indexOf (final short element)
  {
    return getProxiedList ().indexOf (element);
  }

  public int lastIndexOf (final short element)
  {
    return getProxiedList ().lastIndexOf (element);
  }

  public ShortListIterator listIterator ()
  {
    return getProxiedList ().listIterator ();
  }

  public ShortListIterator listIterator (final int index)
  {
    return getProxiedList ().listIterator (index);
  }

  public short removeElementAt (final int index)
  {
    return getProxiedList ().removeElementAt (index);
  }

  public short set (final int index, final short element)
  {
    return getProxiedList ().set (index, element);
  }

  public ShortList subList (final int fromIndex, final int toIndex)
  {
    return getProxiedList ().subList (fromIndex, toIndex);
  }
}
