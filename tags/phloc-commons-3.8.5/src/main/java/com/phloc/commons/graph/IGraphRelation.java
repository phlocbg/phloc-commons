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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;

/**
 * Base interface for graph relation implementations.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
@MustImplementEqualsAndHashcode
public interface IGraphRelation <VALUETYPE> extends IGraphObject
{
  /**
   * @return The from-node of this relation. Never <code>null</code>.
   */
  @Nonnull
  IGraphNode <VALUETYPE> getFrom ();

  /**
   * @return The ID of the from-node of this relation. Never <code>null</code>.
   */
  @Nonnull
  String getFromID ();

  /**
   * This is a sanity method for <code>getFrom ().getValue ()</code>
   * 
   * @return <code>null</code> if the from-node has no value.
   */
  @Nullable
  VALUETYPE getFromValue ();

  /**
   * @return The to-node of this relation. Never <code>null</code>.
   */
  @Nonnull
  IGraphNode <VALUETYPE> getTo ();

  /**
   * @return The ID of the to-node of this relation. Never <code>null</code>.
   */
  @Nonnull
  String getToID ();

  /**
   * This is a sanity method for <code>getTo ().getValue ()</code>
   * 
   * @return <code>null</code> if the to-node has no value.
   */
  @Nullable
  VALUETYPE getToValue ();
}