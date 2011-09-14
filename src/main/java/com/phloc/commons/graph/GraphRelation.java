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
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class represents a connection between 2 graph nodes.
 * 
 * @author philip
 */
@Immutable
public final class GraphRelation <VALUETYPE> extends AbstractGraphObject implements IGraphRelation <VALUETYPE>
{
  private final IGraphNode <VALUETYPE> m_aFrom;
  private final IGraphNode <VALUETYPE> m_aTo;

  public GraphRelation (@Nonnull final IGraphNode <VALUETYPE> aFrom, @Nonnull final IGraphNode <VALUETYPE> aTo)
  {
    this (null, aFrom, aTo);
  }

  public GraphRelation (@Nullable final String sID,
                        @Nonnull final IGraphNode <VALUETYPE> aFrom,
                        @Nonnull final IGraphNode <VALUETYPE> aTo)
  {
    super (sID);
    if (aFrom == null)
      throw new NullPointerException ("from");
    if (aTo == null)
      throw new NullPointerException ("to");
    m_aFrom = aFrom;
    m_aTo = aTo;
  }

  @Nonnull
  public IGraphNode <VALUETYPE> getFrom ()
  {
    return m_aFrom;
  }

  @Nullable
  public VALUETYPE getFromValue ()
  {
    return m_aFrom.getValue ();
  }

  @Nonnull
  public IGraphNode <VALUETYPE> getTo ()
  {
    return m_aTo;
  }

  @Nullable
  public VALUETYPE getToValue ()
  {
    return m_aTo.getValue ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final GraphRelation <?> rhs = (GraphRelation <?>) o;
    return m_aFrom.equals (rhs.m_aFrom) && m_aTo.equals (rhs.m_aTo);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aFrom).append (m_aTo).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("from", m_aFrom).append ("to", m_aTo).toString ();
  }

  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param <VALUETYPE>
   *        Graph relation element type
   * @param aFrom
   *        The from node. May not be <code>null</code>.
   * @param aTo
   *        The to node. May not be <code>null</code>.
   * @return The created graph relation and never <code>null</code>.
   */
  @Nonnull
  public static <VALUETYPE> GraphRelation <VALUETYPE> create (@Nonnull final IGraphNode <VALUETYPE> aFrom,
                                                              @Nonnull final IGraphNode <VALUETYPE> aTo)
  {
    return new GraphRelation <VALUETYPE> (aFrom, aTo);
  }

  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param <VALUETYPE>
   *        Graph relation element type
   * @param sID
   *        The ID of the relation to be created. May be <code>null</code>. If
   *        <code>null</code> than a new unique ID is created.
   * @param aFrom
   *        The from node. May not be <code>null</code>.
   * @param aTo
   *        The to node. May not be <code>null</code>.
   * @return The created graph relation and never <code>null</code>.
   */
  @Nonnull
  public static <VALUETYPE> GraphRelation <VALUETYPE> create (@Nullable final String sID,
                                                              @Nonnull final IGraphNode <VALUETYPE> aFrom,
                                                              @Nonnull final IGraphNode <VALUETYPE> aTo)
  {
    return new GraphRelation <VALUETYPE> (sID, aFrom, aTo);
  }
}
