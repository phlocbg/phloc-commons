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
package org.apache.commons.collections.primitives.decorators;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.BooleanCollection;
import org.apache.commons.collections.primitives.BooleanIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480463 $ $Date: 2006-11-29 09:15:23 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class BaseProxyBooleanCollection implements BooleanCollection
{
  protected BaseProxyBooleanCollection ()
  {}

  @Nonnull
  protected abstract BooleanCollection getProxiedCollection ();

  public boolean add (final boolean aElement)
  {
    return getProxiedCollection ().add (aElement);
  }

  public boolean addAll (@Nonnull final BooleanCollection c)
  {
    return getProxiedCollection ().addAll (c);
  }

  public void clear ()
  {
    getProxiedCollection ().clear ();
  }

  public boolean contains (final boolean aElement)
  {
    return getProxiedCollection ().contains (aElement);
  }

  public boolean containsAll (@Nonnull final BooleanCollection c)
  {
    return getProxiedCollection ().containsAll (c);
  }

  public boolean isEmpty ()
  {
    return getProxiedCollection ().isEmpty ();
  }

  @Nonnull
  public BooleanIterator iterator ()
  {
    return getProxiedCollection ().iterator ();
  }

  public boolean removeAll (@Nonnull final BooleanCollection c)
  {
    return getProxiedCollection ().removeAll (c);
  }

  public boolean removeElement (final boolean aElement)
  {
    return getProxiedCollection ().removeElement (aElement);
  }

  public boolean retainAll (@Nonnull final BooleanCollection c)
  {
    return getProxiedCollection ().retainAll (c);
  }

  @Nonnegative
  public int size ()
  {
    return getProxiedCollection ().size ();
  }

  @Nonnull
  public boolean [] toArray ()
  {
    return getProxiedCollection ().toArray ();
  }

  @Nonnull
  public boolean [] toArray (@Nonnull final boolean [] a)
  {
    return getProxiedCollection ().toArray (a);
  }

  /*
   * Important: is equal to the proxied collection!<br>
   * Therefore violates the contract of equals!
   */
  @Override
  public boolean equals (@Nullable final Object obj)
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
