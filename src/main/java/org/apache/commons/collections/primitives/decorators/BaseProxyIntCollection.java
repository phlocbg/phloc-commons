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

import org.apache.commons.collections.primitives.IntCollection;
import org.apache.commons.collections.primitives.IntIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480463 $ $Date: 2006-11-29 09:15:23 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class BaseProxyIntCollection implements IntCollection
{
  protected abstract IntCollection getProxiedCollection ();

  protected BaseProxyIntCollection ()
  {}

  public boolean add (final int element)
  {
    return getProxiedCollection ().add (element);
  }

  public boolean addAll (final IntCollection c)
  {
    return getProxiedCollection ().addAll (c);
  }

  public void clear ()
  {
    getProxiedCollection ().clear ();
  }

  public boolean contains (final int element)
  {
    return getProxiedCollection ().contains (element);
  }

  public boolean containsAll (final IntCollection c)
  {
    return getProxiedCollection ().containsAll (c);
  }

  public boolean isEmpty ()
  {
    return getProxiedCollection ().isEmpty ();
  }

  public IntIterator iterator ()
  {
    return getProxiedCollection ().iterator ();
  }

  public boolean removeAll (final IntCollection c)
  {
    return getProxiedCollection ().removeAll (c);
  }

  public boolean removeElement (final int element)
  {
    return getProxiedCollection ().removeElement (element);
  }

  public boolean retainAll (final IntCollection c)
  {
    return getProxiedCollection ().retainAll (c);
  }

  public int size ()
  {
    return getProxiedCollection ().size ();
  }

  public int [] toArray ()
  {
    return getProxiedCollection ().toArray ();
  }

  public int [] toArray (final int [] a)
  {
    return getProxiedCollection ().toArray (a);
  }

  // TODO: Add note about possible contract violations here.

  @Override
  public boolean equals (final Object obj)
  {
    return getProxiedCollection ().equals (obj);
  }

  @Override
  public int hashCode ()
  {
    return getProxiedCollection ().hashCode ();
  }

  @Override
  public String toString ()
  {
    return getProxiedCollection ().toString ();
  }

}
