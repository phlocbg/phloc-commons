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

import java.util.Collection;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * Base interface for graph node implementations.
 * 
 * @author philip
 */
public interface IGraphNode <VALUETYPE> extends IGraphObject
{
  /**
   * @return The contained value of this object. May be <code>null</code>.
   */
  @Nullable
  VALUETYPE getValue ();

  boolean isConnectedWith (IGraphNode <VALUETYPE> aNode);

  // --- incoming ---

  void addIncomingRelation (@Nonnull IGraphRelation <VALUETYPE> aRelation);

  void addIncomingRelation (@Nonnull IGraphNode <VALUETYPE> aFromNode);

  boolean hasIncomingRelations ();

  @Nonnegative
  int getIncomingRelationCount ();

  @Nonnull
  @ReturnsImmutableObject
  Collection <IGraphRelation <VALUETYPE>> getIncomingRelations ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <IGraphNode <VALUETYPE>> getAllFromNodes ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <VALUETYPE> getAllFromValues ();

  // --- outgoing ---

  void addOutgoingRelation (@Nonnull IGraphRelation <VALUETYPE> aRelation);

  void addOutgoingRelation (@Nonnull IGraphNode <VALUETYPE> aToNode);

  boolean hasOutgoingRelations ();

  @Nonnegative
  int getOutgoingRelationCount ();

  @Nonnull
  @ReturnsImmutableObject
  Collection <IGraphRelation <VALUETYPE>> getOutgoingRelations ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <IGraphNode <VALUETYPE>> getAllToNodes ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <VALUETYPE> getAllToValues ();
}
