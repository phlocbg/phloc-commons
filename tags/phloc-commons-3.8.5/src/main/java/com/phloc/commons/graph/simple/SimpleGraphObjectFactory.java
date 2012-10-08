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
import javax.annotation.Nullable;

import com.phloc.commons.graph.IGraphNode;
import com.phloc.commons.graph.IGraphObjectFactory;
import com.phloc.commons.graph.IGraphRelation;
import com.phloc.commons.graph.impl.GraphNode;
import com.phloc.commons.graph.impl.GraphRelation;

/**
 * Default implementation of the {@link IGraphObjectFactory} with
 * {@link GraphNode} and {@link GraphRelation}.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
public class SimpleGraphObjectFactory <VALUETYPE> implements IGraphObjectFactory <VALUETYPE>
{
  @Nonnull
  public IGraphNode <VALUETYPE> createNode ()
  {
    return createNode (null);
  }

  @Nonnull
  public IGraphNode <VALUETYPE> createNode (@Nullable final VALUETYPE aValue)
  {
    return new GraphNode <VALUETYPE> (null, aValue);
  }

  @Nonnull
  public IGraphNode <VALUETYPE> createNode (@Nullable final String sID, @Nullable final VALUETYPE aValue)
  {
    return new GraphNode <VALUETYPE> (sID, aValue);
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> createRelation (@Nonnull final IGraphNode <VALUETYPE> aFrom,
                                                    @Nonnull final IGraphNode <VALUETYPE> aTo)
  {
    return new GraphRelation <VALUETYPE> (aFrom, aTo);
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> createRelation (@Nullable final String sID,
                                                    @Nonnull final IGraphNode <VALUETYPE> aFrom,
                                                    @Nonnull final IGraphNode <VALUETYPE> aTo)
  {
    return new GraphRelation <VALUETYPE> (sID, aFrom, aTo);
  }
}
