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
  protected abstract LongList getProxiedList ();

  @Override
  protected final LongCollection getProxiedCollection ()
  {
    return getProxiedList ();
  }

  protected BaseProxyLongList ()
  {}

  public void add (final int index, final long element)
  {
    getProxiedList ().add (index, element);
  }

  public boolean addAll (final int index, final LongCollection collection)
  {
    return getProxiedList ().addAll (index, collection);
  }

  public long get (final int index)
  {
    return getProxiedList ().get (index);
  }

  public int indexOf (final long element)
  {
    return getProxiedList ().indexOf (element);
  }

  public int lastIndexOf (final long element)
  {
    return getProxiedList ().lastIndexOf (element);
  }

  public LongListIterator listIterator ()
  {
    return getProxiedList ().listIterator ();
  }

  public LongListIterator listIterator (final int index)
  {
    return getProxiedList ().listIterator (index);
  }

  public long removeElementAt (final int index)
  {
    return getProxiedList ().removeElementAt (index);
  }

  public long set (final int index, final long element)
  {
    return getProxiedList ().set (index, element);
  }

  public LongList subList (final int fromIndex, final int toIndex)
  {
    return getProxiedList ().subList (fromIndex, toIndex);
  }

}
