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
package com.phloc.commons.graph.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.graph.IGraphNode;
import com.phloc.commons.graph.IGraphRelation;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IGraphRelation} interface
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
@Immutable
public class GraphRelation <VALUETYPE> extends AbstractGraphObject implements IGraphRelation <VALUETYPE>
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
  public String getFromID ()
  {
    return m_aFrom.getID ();
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

  @Nonnull
  public String getToID ()
  {
    return m_aTo.getID ();
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
}
