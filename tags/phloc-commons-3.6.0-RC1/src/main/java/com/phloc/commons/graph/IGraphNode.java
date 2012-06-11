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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * Base interface for graph node implementations.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
public interface IGraphNode <VALUETYPE> extends IGraphObject
{
  /**
   * @return The contained value of this object. May be <code>null</code>.
   */
  @Nullable
  VALUETYPE getValue ();

  boolean isConnectedWith (@Nullable IGraphNode <VALUETYPE> aNode);

  // --- incoming ---

  /**
   * Add a new incoming relation to this node
   * 
   * @param aRelation
   *        The relation to be added. May not be <code>null</code>.
   */
  void addIncomingRelation (@Nonnull IGraphRelation <VALUETYPE> aRelation);

  boolean hasIncomingRelations ();

  @Nonnegative
  int getIncomingRelationCount ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <IGraphRelation <VALUETYPE>> getIncomingRelations ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <IGraphNode <VALUETYPE>> getAllFromNodes ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <VALUETYPE> getAllFromValues ();

  // --- outgoing ---

  /**
   * Add a new outgoing relation from this node
   * 
   * @param aRelation
   *        The relation to be added. May not be <code>null</code>.
   */
  void addOutgoingRelation (@Nonnull IGraphRelation <VALUETYPE> aRelation);

  boolean hasOutgoingRelations ();

  @Nonnegative
  int getOutgoingRelationCount ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <IGraphRelation <VALUETYPE>> getOutgoingRelations ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <IGraphNode <VALUETYPE>> getAllToNodes ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <VALUETYPE> getAllToValues ();
}
