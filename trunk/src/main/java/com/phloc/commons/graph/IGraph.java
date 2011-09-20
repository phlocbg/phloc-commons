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

import com.phloc.commons.state.EChange;
import com.phloc.commons.state.IClearable;

/**
 * Interface for a modifiable graph.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
public interface IGraph <VALUETYPE> extends IReadonlyGraph <VALUETYPE>, IClearable, IGraphObjectFactory <VALUETYPE>
{
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
