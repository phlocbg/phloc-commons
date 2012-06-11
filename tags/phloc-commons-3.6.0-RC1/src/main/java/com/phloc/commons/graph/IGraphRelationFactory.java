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
package com.phloc.commons.graph;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Factory interface for creating graph relations.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
public interface IGraphRelationFactory <VALUETYPE>
{
  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param aFrom
   *        The from node. May not be <code>null</code>.
   * @param aTo
   *        The to node. May not be <code>null</code>.
   * @return The created graph relation and never <code>null</code>.
   */
  @Nonnull
  IGraphRelation <VALUETYPE> createRelation (@Nonnull IGraphNode <VALUETYPE> aFrom, @Nonnull IGraphNode <VALUETYPE> aTo);

  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param sID
   *        The ID of the relation to be created. May be <code>null</code>. If
   *        <code>null</code> than a new unique ID is created.
   * @param aFrom
   *        The from node. May not be <code>null</code>.
   * @param aTo
   *        The to node. May not be <code>null</code>.
   * @return The created graph relation and never <code>null</code>.
   */
  @Nonnull
  IGraphRelation <VALUETYPE> createRelation (@Nullable String sID,
                                             @Nonnull IGraphNode <VALUETYPE> aFrom,
                                             @Nonnull IGraphNode <VALUETYPE> aTo);
}
