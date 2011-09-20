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

/**
 * Factory interface for creating graph nodes
 * 
 * @author philip
 * @param <VALUETYPE>
 *        Graph node value type
 */
public interface IGraphNodeFactory <VALUETYPE>
{
  /**
   * Create a new graph node with a new ID.
   * 
   * @param aValue
   *        The value to be assigned to the graph node. May be <code>null</code>
   *        .
   * @return The created graph node. May not be <code>null</code>.
   */
  @Nonnull
  IGraphNode <VALUETYPE> createNode (@Nullable VALUETYPE aValue);

  /**
   * Create a new graph node with a known ID.
   * 
   * @param sID
   *        The ID of the graph node. If it is <code>null</code> a new ID is
   *        automatically created.
   * @param aValue
   *        The value to be assigned to the graph node. May be <code>null</code>
   *        .
   * @return The created graph node. May not be <code>null</code>.
   */
  @Nonnull
  IGraphNode <VALUETYPE> createNode (@Nullable String sID, @Nullable VALUETYPE aValue);
}
