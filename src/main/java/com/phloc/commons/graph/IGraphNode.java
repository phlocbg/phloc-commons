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

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Base interface for graph node implementations.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
@MustImplementEqualsAndHashcode
public interface IGraphNode <VALUETYPE> extends IGraphObject
{
  /**
   * @return The contained value of this object. May be <code>null</code>.
   */
  @Nullable
  VALUETYPE getValue ();

  // --- incoming ---

  /**
   * Add a new incoming relation to this node
   * 
   * @param aRelation
   *        The relation to be added. May not be <code>null</code>.
   */
  void addIncomingRelation (@Nonnull IGraphRelation <VALUETYPE> aRelation);

  /**
   * @return <code>true</code> if this node has at least one incoming relation.
   */
  boolean hasIncomingRelations ();

  /**
   * @return The number of incoming relations. Always &ge; 0.
   */
  @Nonnegative
  int getIncomingRelationCount ();

  /**
   * Check if this node has the passed relation as an incoming relations.
   * 
   * @param aRelation
   *        The relation to be checked. May be <code>null</code>.
   * @return <code>true</code> if the passed relation is an incoming relation,
   *         <code>false</code> if not
   */
  boolean isIncomingRelation (@Nullable IGraphRelation <VALUETYPE> aRelation);

  /**
   * @return All incoming relations and never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Collection <IGraphRelation <VALUETYPE>> getIncomingRelations ();

  /**
   * Remove the passed relation from the set of incoming relations.
   * 
   * @param aRelation
   *        The relation to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the passed relation was successfully
   *         removed from the incoming relations.
   */
  @Nonnull
  EChange removeIncomingRelation (@Nonnull IGraphRelation <VALUETYPE> aRelation);

  /**
   * Check if this graph node is directly connected to the passed node via an
   * incoming relation.
   * 
   * @param aNode
   *        The node to be checked. May be <code>null</code>.
   * @return <code>true</code> if is connected, <code>false</code> if not
   */
  boolean isFromNode (@Nullable IGraphNode <VALUETYPE> aNode);

  /**
   * @return All nodes that are connected via incoming relations.
   */
  @Nonnull
  @ReturnsMutableCopy
  Collection <IGraphNode <VALUETYPE>> getAllFromNodes ();

  /**
   * @return All values from all nodes connected via incoming relations.
   */
  @Nonnull
  @ReturnsMutableCopy
  Collection <VALUETYPE> getAllFromValues ();

  /**
   * Find the incoming relation from the passed node to this node.
   * 
   * @param aFromNode
   *        The from node to use. May be <code>null</code>.
   * @return <code>null</code> if there exists no incoming relation from the
   *         passed node to this node.
   */
  @Nullable
  IGraphRelation <VALUETYPE> getIncomingRelationFrom (@Nullable IGraphNode <VALUETYPE> aFromNode);

  // --- outgoing ---

  /**
   * Add a new outgoing relation from this node
   * 
   * @param aRelation
   *        The relation to be added. May not be <code>null</code>.
   */
  void addOutgoingRelation (@Nonnull IGraphRelation <VALUETYPE> aRelation);

  /**
   * @return <code>true</code> if this node has at least one outgoing relation.
   */
  boolean hasOutgoingRelations ();

  /**
   * @return The number of outgoing relations. Always &ge; 0.
   */
  @Nonnegative
  int getOutgoingRelationCount ();

  /**
   * Check if this node has the passed relation as an outgoing relations.
   * 
   * @param aRelation
   *        The relation to be checked. May be <code>null</code>.
   * @return <code>true</code> if the passed relation is an outgoing relation,
   *         <code>false</code> if not
   */
  boolean isOutgoingRelation (@Nullable IGraphRelation <VALUETYPE> aRelation);

  /**
   * @return All outgoing relations and never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Collection <IGraphRelation <VALUETYPE>> getOutgoingRelations ();

  /**
   * Remove the passed relation from the set of outgoing relations.
   * 
   * @param aRelation
   *        The relation to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the passed relation was successfully
   *         removed from the outgoing relations.
   */
  @Nonnull
  EChange removeOutgoingRelation (@Nonnull IGraphRelation <VALUETYPE> aRelation);

  /**
   * Check if this graph node is directly connected to the passed node via an
   * outgoing relation.
   * 
   * @param aNode
   *        The node to be checked. May be <code>null</code>.
   * @return <code>true</code> if is connected, <code>false</code> if not
   */
  boolean isToNode (@Nullable IGraphNode <VALUETYPE> aNode);

  /**
   * @return All nodes that are connected via outgoing relations.
   */
  @Nonnull
  @ReturnsMutableCopy
  Collection <IGraphNode <VALUETYPE>> getAllToNodes ();

  /**
   * @return All values from all nodes connected via outgoing relations.
   */
  @Nonnull
  @ReturnsMutableCopy
  Collection <VALUETYPE> getAllToValues ();

  /**
   * Find the incoming relation from this node to the passed node.
   * 
   * @param aToNode
   *        The to node to use. May be <code>null</code>.
   * @return <code>null</code> if there exists no incoming relation from this
   *         node to the passed node.
   */
  @Nullable
  IGraphRelation <VALUETYPE> getOutgoingRelationTo (@Nullable IGraphNode <VALUETYPE> aToNode);

  // --- incoming and/or outgoing

  /**
   * Check if this graph node is directly connected to the passed node, either
   * via an incoming or via an outgoing relation.<br>
   * This is the same as calling
   * <code>isFromNode(aNode) || isToNode(aNode)</code>
   * 
   * @param aNode
   *        The node to be checked. May be <code>null</code>.
   * @return <code>true</code> if is connected, <code>false</code> if not
   */
  boolean isConnectedWith (@Nullable IGraphNode <VALUETYPE> aNode);

  /**
   * Find the incoming relation from this node to the passed node or from the
   * passed node to this node.
   * 
   * @param aNode
   *        The to node to use. May be <code>null</code>.
   * @return <code>null</code> if there exists no relation between this node and
   *         the passed node.
   */
  @Nullable
  IGraphRelation <VALUETYPE> getRelationFromOrTo (@Nullable IGraphNode <VALUETYPE> aNode);

  /**
   * Check if this node has incoming <b>or</b> outgoing relations. This is equal
   * to calling <code>hasIncomingRelations() || hasOutgoingRelations()</code>
   * 
   * @return <code>true</code> if this node has at least one incoming or
   *         outgoing relation.
   */
  boolean hasIncomingOrOutgoingRelations ();

  /**
   * Check if this node has incoming <b>and</b> outgoing relations. This is
   * equal to calling
   * <code>hasIncomingRelations() && hasOutgoingRelations()</code>
   * 
   * @return <code>true</code> if this node has at least one incoming and at
   *         least one outgoing relation.
   */
  boolean hasIncomingAndOutgoingRelations ();

  /**
   * @return A container with all incoming and outgoing relations. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <IGraphRelation <VALUETYPE>> getAllRelations ();

  /**
   * @return A container with all nodes directly connected via incoming or
   *         outgoing relations. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <IGraphNode <VALUETYPE>> getAllRelatedNodes ();

  /**
   * @return A container with all values of all nodes directly connected via
   *         incoming or outgoing relations. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <VALUETYPE> getAllRelatedValues ();
}
