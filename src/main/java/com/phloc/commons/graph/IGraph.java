/**
 * Copyright (C) 2006-2011 phloc systems
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

import com.phloc.commons.state.EChange;
import com.phloc.commons.state.IClearable;

/**
 * Interface for a modifiable graph.
 * 
 * @author philip
 */
public interface IGraph <VALUETYPE> extends IReadonlyGraph <VALUETYPE>, IClearable
{
  /**
   * Create a new graph node and add it to the graph. A new ID is generated.
   * 
   * @param aValue
   *        The value to be added. May be <code>null</code>.
   * @return The created graph node. Never <code>null</code>.
   */
  @Nonnull
  IGraphNode <VALUETYPE> addNode (@Nullable VALUETYPE aValue);

  /**
   * Create a new graph node with the given ID and value.
   * 
   * @param sID
   *        The ID of the graph node to be created. If the ID is
   *        <code>null</code> a new graph ID is created.
   * @param aValue
   *        The value to be added. May be <code>null</code>.
   * @return <code>null</code> if another graph node with the same ID is already
   *         present - the non-<code>null</code> graph node upon successful
   *         adding.
   */
  @Nullable
  IGraphNode <VALUETYPE> addNode (@Nullable String sID, @Nullable VALUETYPE aValue);

  /**
   * Add an existing node to this graph.
   * 
   * @param aNode
   *        The node to be added. May not be <code>null</code>.
   * @return {@link EChange}
   */
  @Nonnull
  EChange addNode (@Nonnull IGraphNode <VALUETYPE> aNode);

  /**
   * Remove an existing node from the graph-
   * 
   * @param aNode
   *        The node to be removed. May not be <code>null</code>.
   * @return {@link EChange}
   */
  @Nonnull
  EChange removeNode (@Nonnull IGraphNode <VALUETYPE> aNode);
}
