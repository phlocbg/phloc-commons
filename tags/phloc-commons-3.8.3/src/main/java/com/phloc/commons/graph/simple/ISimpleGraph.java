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
package com.phloc.commons.graph.simple;

import javax.annotation.Nonnull;

import com.phloc.commons.graph.IGraph;
import com.phloc.commons.graph.IGraphRelation;

/**
 * Interface for a modifiable simple graph.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
public interface ISimpleGraph <VALUETYPE> extends IGraph <VALUETYPE>
{
  /**
   * Create a new relation from the passed fromNode to the toNode. Internally
   * the IDs are resolved to the respective graph nodes and later on calls
   * {@link #createRelation(com.phloc.commons.graph.IGraphNode, com.phloc.commons.graph.IGraphNode)}
   * 
   * @param sFromNodeID
   *        The from-node ID. May not be <code>null</code>.
   * @param sToNodeID
   *        The to-node ID. May not be <code>null</code>.
   * @return The created graph relation and never <code>null</code>.
   */
  @Nonnull
  IGraphRelation <VALUETYPE> createRelation (@Nonnull String sFromNodeID, @Nonnull String sToNodeID);
}
