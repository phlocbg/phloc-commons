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
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.ShortList;

/**
 * Adapts an {@link ShortList ShortList} to the {@link List List} interface.
 * <p />
 * This implementation delegates most methods to the provided {@link ShortList
 * ShortList} implementation in the "obvious" way.
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public final class ShortListList extends AbstractShortListList implements Serializable
{
  private final ShortList m_aList;

  /**
   * Creates a {@link List List} wrapping the specified {@link ShortList
   * ShortList}.
   *
   * @see #wrap
   */
  public ShortListList (@Nonnull final ShortList list)
  {
    m_aList = list;
  }

  @Override
  protected ShortList getShortList ()
  {
    return m_aList;
  }

  /**
   * Create a {@link List List} wrapping the specified {@link ShortList ShortList}
   * . When the given <i>list</i> is <code>null</code>, returns
   * <code>null</code>.
   *
   * @param list
   *        the (possibly <code>null</code>) {@link ShortList ShortList} to wrap
   * @return a {@link List List} wrapping the given <i>list</i>, or
   *         <code>null</code> when <i>list</i> is <code>null</code>.
   */
  @Nullable
  public static List <Short> wrap (@Nullable final ShortList list)
  {
    if (null == list)
      return null;
    if (list instanceof Serializable)
      return new ShortListList (list);
    return new NonSerializableShortListList (list);
  }
}
