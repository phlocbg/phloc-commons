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

import org.apache.commons.collections.primitives.BooleanCollection;
import org.apache.commons.collections.primitives.BooleanList;
import org.apache.commons.collections.primitives.BooleanListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480463 $ $Date: 2006-11-29 09:15:23 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class BaseProxyBooleanList extends BaseProxyBooleanCollection implements BooleanList
{
  protected abstract BooleanList getProxiedList ();

  @Override
  protected final BooleanCollection getProxiedCollection ()
  {
    return getProxiedList ();
  }

  protected BaseProxyBooleanList ()
  {}

  public void add (final int index, final boolean element)
  {
    getProxiedList ().add (index, element);
  }

  public boolean addAll (final int index, @Nonnull final BooleanCollection collection)
  {
    return getProxiedList ().addAll (index, collection);
  }

  public boolean get (final int index)
  {
    return getProxiedList ().get (index);
  }

  public int indexOf (final boolean element)
  {
    return getProxiedList ().indexOf (element);
  }

  public int lastIndexOf (final boolean element)
  {
    return getProxiedList ().lastIndexOf (element);
  }

  public BooleanListIterator listIterator ()
  {
    return getProxiedList ().listIterator ();
  }

  public BooleanListIterator listIterator (final int index)
  {
    return getProxiedList ().listIterator (index);
  }

  public boolean removeElementAt (final int index)
  {
    return getProxiedList ().removeElementAt (index);
  }

  public boolean set (final int index, final boolean element)
  {
    return getProxiedList ().set (index, element);
  }

  public BooleanList subList (final int fromIndex, final int toIndex)
  {
    return getProxiedList ().subList (fromIndex, toIndex);
  }
}
