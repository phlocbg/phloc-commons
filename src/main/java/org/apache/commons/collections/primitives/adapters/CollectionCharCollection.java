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

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.CharCollection;

/**
 * Adapts a {@link java.lang.Number Number}-valued {@link java.util.Collection
 * Collection} to the {@link CharCollection CharCollection} interface.
 * <p />
 * This implementation delegates most methods to the provided {@link Collection
 * Collection} implementation in the "obvious" way.
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public final class CollectionCharCollection extends AbstractCollectionCharCollection implements Serializable
{
  private final Collection <Character> m_aCollection;

  /**
   * Creates an {@link CharCollection CharCollection} wrapping the specified
   * {@link Collection Collection}.
   *
   * @see #wrap
   */
  public CollectionCharCollection (@Nonnull final Collection <Character> collection)
  {
    m_aCollection = collection;
  }

  @Override
  @Nonnull
  protected Collection <Character> getCollection ()
  {
    return m_aCollection;
  }

  /**
   * Create an {@link CharCollection CharCollection} wrapping the specified
   * {@link Collection Collection}. When the given <i>collection</i> is
   * <code>null</code>, returns <code>null</code>.
   *
   * @param aCollection
   *        the (possibly <code>null</code>) {@link Collection} to wrap
   * @return an {@link CharCollection CharCollection} wrapping the given
   *         <i>collection</i>, or <code>null</code> when <i>collection</i> is
   *         <code>null</code>.
   */
  @Nullable
  public static CharCollection wrap (@Nullable final Collection <Character> aCollection)
  {
    if (null == aCollection)
      return null;
    if (aCollection instanceof Serializable)
      return new CollectionCharCollection (aCollection);
    return new NonSerializableCollectionCharCollection (aCollection);
  }
}
