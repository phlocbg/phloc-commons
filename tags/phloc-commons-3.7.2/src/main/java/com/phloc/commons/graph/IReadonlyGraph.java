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

import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * Base interface for a read-only simple graph.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
public interface IReadonlyGraph <VALUETYPE>
{
  /**
   * Try to retrieve the single start node of this graph. A start node is
   * identified by having no incoming relations.
   * 
   * @return The single start node and never <code>null</code>.
   * @throws IllegalStateException
   *         In case the graph has no or more than one start node.
   */
  @Nonnull
  IGraphNode <VALUETYPE> getSingleStartNode () throws IllegalStateException;

  /**
   * Get all start nodes of this graph. Start nodes are identified by having no
   * incoming relations.
   * 
   * @return A set with all start nodes. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <IGraphNode <VALUETYPE>> getAllStartNodes ();

  /**
   * Try to retrieve the single end node of this graph. An end node is
   * identified by having no outgoing relations.
   * 
   * @return The single end node and never <code>null</code>.
   * @throws IllegalStateException
   *         In case the graph has no or more than one end node.
   */
  @Nonnull
  IGraphNode <VALUETYPE> getSingleEndNode () throws IllegalStateException;

  /**
   * Get all end nodes of this graph. End nodes are identified by having no
   * outgoing relations.
   * 
   * @return A set with all end nodes. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <IGraphNode <VALUETYPE>> getAllEndNodes ();

  /**
   * @return The number of nodes currently in the graph. Always &ge; 0.
   */
  @Nonnegative
  int getNodeCount ();

  /**
   * Find the graph node with the specified ID.
   * 
   * @param sID
   *        The ID to be searched. Maybe <code>null</code>.
   * @return <code>null</code> if no such graph node exists in this graph.
   */
  @Nullable
  IGraphNode <VALUETYPE> getNodeOfID (@Nullable String sID);

  /**
   * @return A non-<code>null</code> collection of the nodes in this graph, in
   *         arbitrary order!
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <IGraphNode <VALUETYPE>> getAllNodes ();

  /**
   * Check if this graph contains cycles. An example for a cycle is e.g. if
   * <code>NodeA</code> has an outgoing relation to <code>NodeB</code>,
   * <code>NodeB</code> has an outgoing relation to <code>NodeC</code> and
   * finally <code>NodeC</code> has an outgoing relation to <code>NodeA</code>.
   * 
   * @return <code>true</code> if this graph contains at least one cycle,
   *         <code>false</code> if this graph is cycle-free.
   */
  boolean containsCycles ();
}
