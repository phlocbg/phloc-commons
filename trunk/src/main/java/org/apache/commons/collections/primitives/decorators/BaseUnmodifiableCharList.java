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

import org.apache.commons.collections.primitives.CharCollection;
import org.apache.commons.collections.primitives.CharIterator;
import org.apache.commons.collections.primitives.CharList;
import org.apache.commons.collections.primitives.CharListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480463 $ $Date: 2006-11-29 09:15:23 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class BaseUnmodifiableCharList extends BaseProxyCharList
{
  @Override
  public final void add (final int index, final char element)
  {
    throw new UnsupportedOperationException ("This CharList is not modifiable.");
  }

  @Override
  public final boolean addAll (final int index, @Nonnull final CharCollection collection)
  {
    throw new UnsupportedOperationException ("This CharList is not modifiable.");
  }

  @Override
  public final char removeElementAt (final int index)
  {
    throw new UnsupportedOperationException ("This CharList is not modifiable.");
  }

  @Override
  public final char set (final int index, final char element)
  {
    throw new UnsupportedOperationException ("This CharList is not modifiable.");
  }

  @Override
  public final boolean add (final char element)
  {
    throw new UnsupportedOperationException ("This CharList is not modifiable.");
  }

  @Override
  public final boolean addAll (@Nonnull final CharCollection c)
  {
    throw new UnsupportedOperationException ("This CharList is not modifiable.");
  }

  @Override
  public final void clear ()
  {
    throw new UnsupportedOperationException ("This CharList is not modifiable.");
  }

  @Override
  public final boolean removeAll (@Nonnull final CharCollection c)
  {
    throw new UnsupportedOperationException ("This CharList is not modifiable.");
  }

  @Override
  public final boolean removeElement (final char element)
  {
    throw new UnsupportedOperationException ("This CharList is not modifiable.");
  }

  @Override
  public final boolean retainAll (@Nonnull final CharCollection c)
  {
    throw new UnsupportedOperationException ("This CharList is not modifiable.");
  }

  @Override
  public final CharList subList (final int fromIndex, final int toIndex)
  {
    return UnmodifiableCharList.wrap (getProxiedList ().subList (fromIndex, toIndex));
  }

  @Override
  @Nonnull
  public final CharIterator iterator ()
  {
    return UnmodifiableCharIterator.wrap (getProxiedList ().iterator ());
  }

  @Override
  @Nonnull
  public CharListIterator listIterator ()
  {
    return UnmodifiableCharListIterator.wrap (getProxiedList ().listIterator ());
  }

  @Override
  @Nonnull
  public CharListIterator listIterator (final int index)
  {
    return UnmodifiableCharListIterator.wrap (getProxiedList ().listIterator (index));
  }

}
