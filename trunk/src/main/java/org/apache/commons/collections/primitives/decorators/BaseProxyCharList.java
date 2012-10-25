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

import org.apache.commons.collections.primitives.CharCollection;
import org.apache.commons.collections.primitives.CharList;
import org.apache.commons.collections.primitives.CharListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480463 $ $Date: 2006-11-29 09:15:23 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class BaseProxyCharList extends BaseProxyCharCollection implements CharList
{
  protected abstract CharList getProxiedList ();

  @Override
  protected final CharCollection getProxiedCollection ()
  {
    return getProxiedList ();
  }

  protected BaseProxyCharList ()
  {}

  public void add (final int index, final char element)
  {
    getProxiedList ().add (index, element);
  }

  public boolean addAll (final int index, final CharCollection collection)
  {
    return getProxiedList ().addAll (index, collection);
  }

  public char get (final int index)
  {
    return getProxiedList ().get (index);
  }

  public int indexOf (final char element)
  {
    return getProxiedList ().indexOf (element);
  }

  public int lastIndexOf (final char element)
  {
    return getProxiedList ().lastIndexOf (element);
  }

  public CharListIterator listIterator ()
  {
    return getProxiedList ().listIterator ();
  }

  public CharListIterator listIterator (final int index)
  {
    return getProxiedList ().listIterator (index);
  }

  public char removeElementAt (final int index)
  {
    return getProxiedList ().removeElementAt (index);
  }

  public char set (final int index, final char element)
  {
    return getProxiedList ().set (index, element);
  }

  public CharList subList (final int fromIndex, final int toIndex)
  {
    return getProxiedList ().subList (fromIndex, toIndex);
  }

}
