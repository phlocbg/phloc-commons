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
  @Nonnull
  IGraphNode <VALUETYPE> getSingleStartNode ();

  @Nonnull
  @ReturnsMutableCopy
  Set <IGraphNode <VALUETYPE>> getAllStartNodes ();

  @Nonnull
  @ReturnsMutableCopy
  Set <IGraphNode <VALUETYPE>> getAllEndNodes ();

  @Nonnegative
  int getNodeCount ();

  @Nullable
  IGraphNode <VALUETYPE> getNodeOfID (@Nullable String sID);

  boolean containsCycles ();
}
