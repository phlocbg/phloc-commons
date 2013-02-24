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
package org.apache.commons.collections.primitives.adapters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.ZZZCollection;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Adapts an {@link ZZZCollection ZZZCollection} to the
 * {@link java.util.Collection Collection} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link ZZZCollection ZZZCollection} implementation in the "obvious" way.
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
@SuppressFBWarnings ("SE_NO_SERIALVERSIONID")
public final class ZZZCollectionCollection extends AbstractZZZCollectionCollection implements Serializable
{
  private transient ZZZCollection m_aCollection;

  /**
   * Creates a {@link Collection Collection} wrapping the specified
   * {@link ZZZCollection ZZZCollection}.
   *
   * @param aCollection
   *        The collection to be wrapped. May not be <code>null</code>.
   * @see #wrap
   */
  public ZZZCollectionCollection (@Nonnull final ZZZCollection aCollection)
  {
    m_aCollection = aCollection;
  }

  @Override
  @Nonnull
  protected ZZZCollection getZZZCollection ()
  {
    return m_aCollection;
  }

  private void writeObject (@Nonnull final ObjectOutputStream out) throws IOException
  {
    out.defaultWriteObject ();
    out.writeObject (m_aCollection);
  }

  private void readObject (@Nonnull final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject ();
    m_aCollection = (ZZZCollection) in.readObject ();
  }

  /**
   * Create a {@link Collection Collection} wrapping the specified
   * {@link ZZZCollection ZZZCollection}. When the given <i>collection</i> is
   * <code>null</code>, returns <code>null</code>.
   *
   * @param collection
   *        the (possibly <code>null</code>) {@link ZZZCollection
   *        ZZZCollection} to wrap
   * @return a {@link Collection Collection} wrapping the given
   *         <i>collection</i>, or <code>null</code> when <i>collection</i> is
   *         <code>null</code>.
   */
  @Nullable
  public static Collection <XXX> wrap (@Nullable final ZZZCollection collection)
  {
    if (null == collection)
      return null;
    if (collection instanceof Serializable)
      return new ZZZCollectionCollection (collection);
    return new NonSerializableZZZCollectionCollection (collection);
  }
}
