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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.graph.IGraphNode;
import com.phloc.commons.graph.IGraphRelation;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation if the {@link IGraphNode} interface
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
@NotThreadSafe
public class GraphNode <VALUETYPE> extends AbstractGraphObject implements IGraphNode <VALUETYPE>
{
  private final VALUETYPE m_aValue;
  private Map <String, IGraphRelation <VALUETYPE>> m_aIncoming;
  private Map <String, IGraphRelation <VALUETYPE>> m_aOutgoing;

  public GraphNode ()
  {
    this (null, null);
  }

  public GraphNode (@Nullable final VALUETYPE aValue)
  {
    this (null, aValue);
  }

  public GraphNode (@Nullable final String sID, @Nullable final VALUETYPE aValue)
  {
    super (sID);
    m_aValue = aValue;
  }

  @Nullable
  public VALUETYPE getValue ()
  {
    return m_aValue;
  }

  public void addIncomingRelation (@Nonnull final IGraphRelation <VALUETYPE> aNewRelation)
  {
    if (aNewRelation == null)
      throw new NullPointerException ("relation");
    if (aNewRelation.getTo () != this)
      throw new IllegalArgumentException ("Passed incoming relation is not based on this node");
    if (m_aIncoming != null)
    {
      if (m_aIncoming.containsKey (aNewRelation.getID ()))
        throw new IllegalArgumentException ("The passed relation (" +
                                            aNewRelation +
                                            ") is already contained as an incoming relation");

      // check if the relation from-node is already contained
      for (final IGraphRelation <VALUETYPE> aRelation : m_aIncoming.values ())
        if (aRelation.getFrom () == aNewRelation.getFrom ())
          throw new IllegalArgumentException ("The from-node of the passed relation (" +
                                              aNewRelation +
                                              ") is already contained");
    }
    else
    {
      m_aIncoming = new LinkedHashMap <String, IGraphRelation <VALUETYPE>> ();
    }

    // Add!
    m_aIncoming.put (aNewRelation.getID (), aNewRelation);
  }

  public boolean hasIncomingRelations ()
  {
    return !ContainerHelper.isEmpty (m_aIncoming);
  }

  @Nonnegative
  public int getIncomingRelationCount ()
  {
    return ContainerHelper.getSize (m_aIncoming);
  }

  public boolean isIncomingRelation (@Nullable final IGraphRelation <VALUETYPE> aRelation)
  {
    return m_aIncoming != null && aRelation != null && aRelation.equals (m_aIncoming.get (aRelation.getID ()));
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IGraphRelation <VALUETYPE>> getIncomingRelations ()
  {
    return m_aIncoming == null ? new ArrayList <IGraphRelation <VALUETYPE>> ()
                              : ContainerHelper.newList (m_aIncoming.values ());
  }

  public boolean isFromNode (@Nullable final IGraphNode <VALUETYPE> aNode)
  {
    if (m_aIncoming != null && aNode != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aIncoming.values ())
        if (aRelation.getFrom ().equals (aNode))
          return true;
    return false;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IGraphNode <VALUETYPE>> getAllFromNodes ()
  {
    final Set <IGraphNode <VALUETYPE>> ret = new HashSet <IGraphNode <VALUETYPE>> ();
    if (m_aIncoming != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aIncoming.values ())
        ret.add (aRelation.getFrom ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <VALUETYPE> getAllFromValues ()
  {
    final List <VALUETYPE> ret = new ArrayList <VALUETYPE> ();
    if (m_aIncoming != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aIncoming.values ())
        ret.add (aRelation.getFromValue ());
    return ret;
  }

  public void addOutgoingRelation (@Nonnull final IGraphRelation <VALUETYPE> aNewRelation)
  {
    if (aNewRelation == null)
      throw new NullPointerException ("relation");
    if (aNewRelation.getFrom () != this)
      throw new IllegalArgumentException ("Passed outgoing relation is not based on this node");
    if (m_aOutgoing != null)
    {
      if (m_aOutgoing.containsKey (aNewRelation.getID ()))
        throw new IllegalArgumentException ("The passed relation " +
                                            aNewRelation +
                                            " is already contained as an outgoing relation");
      // check if the relation to-node is already contained
      for (final IGraphRelation <VALUETYPE> aRelation : m_aOutgoing.values ())
        if (aRelation.getTo () == aNewRelation.getTo ())
          throw new IllegalArgumentException ("The to-node of the passed relation " +
                                              aNewRelation +
                                              " is already contained");
    }
    else
    {
      m_aOutgoing = new LinkedHashMap <String, IGraphRelation <VALUETYPE>> ();
    }

    // Add!
    m_aOutgoing.put (aNewRelation.getID (), aNewRelation);
  }

  public boolean hasOutgoingRelations ()
  {
    return !ContainerHelper.isEmpty (m_aOutgoing);
  }

  @Nonnegative
  public int getOutgoingRelationCount ()
  {
    return ContainerHelper.getSize (m_aOutgoing);
  }

  public boolean isOutgoingRelation (@Nullable final IGraphRelation <VALUETYPE> aRelation)
  {
    return m_aOutgoing != null && aRelation != null && aRelation.equals (m_aOutgoing.get (aRelation.getID ()));
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IGraphRelation <VALUETYPE>> getOutgoingRelations ()
  {
    return m_aOutgoing == null ? new ArrayList <IGraphRelation <VALUETYPE>> ()
                              : ContainerHelper.newList (m_aOutgoing.values ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IGraphNode <VALUETYPE>> getAllToNodes ()
  {
    final Set <IGraphNode <VALUETYPE>> ret = new HashSet <IGraphNode <VALUETYPE>> ();
    if (m_aOutgoing != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aOutgoing.values ())
        ret.add (aRelation.getTo ());
    return ret;
  }

  public boolean isToNode (@Nullable final IGraphNode <VALUETYPE> aNode)
  {
    if (m_aOutgoing != null && aNode != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aOutgoing.values ())
        if (aRelation.getTo ().equals (aNode))
          return true;
    return false;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <VALUETYPE> getAllToValues ()
  {
    final List <VALUETYPE> ret = new ArrayList <VALUETYPE> ();
    if (m_aOutgoing != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aOutgoing.values ())
        ret.add (aRelation.getToValue ());
    return ret;
  }

  public boolean isConnectedWith (@Nullable final IGraphNode <VALUETYPE> aNode)
  {
    return isFromNode (aNode) || isToNode (aNode);
  }

  public boolean hasIncomingOrOutgoingRelations ()
  {
    return hasIncomingRelations () || hasOutgoingRelations ();
  }

  public boolean hasIncomingAndOutgoingRelations ()
  {
    return hasIncomingRelations () && hasOutgoingRelations ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IGraphRelation <VALUETYPE>> getAllRelations ()
  {
    final Set <IGraphRelation <VALUETYPE>> ret = new HashSet <IGraphRelation <VALUETYPE>> ();
    if (m_aIncoming != null)
      ret.addAll (m_aIncoming.values ());
    if (m_aOutgoing != null)
      ret.addAll (m_aOutgoing.values ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IGraphNode <VALUETYPE>> getAllRelatedNodes ()
  {
    final Set <IGraphNode <VALUETYPE>> ret = new HashSet <IGraphNode <VALUETYPE>> ();
    if (m_aIncoming != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aIncoming.values ())
        ret.add (aRelation.getFrom ());
    if (m_aOutgoing != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aOutgoing.values ())
        ret.add (aRelation.getTo ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <VALUETYPE> getAllRelatedValues ()
  {
    final Set <VALUETYPE> ret = new HashSet <VALUETYPE> ();
    if (m_aIncoming != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aIncoming.values ())
        ret.add (aRelation.getFromValue ());
    if (m_aOutgoing != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aOutgoing.values ())
        ret.add (aRelation.getToValue ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final GraphNode <?> rhs = (GraphNode <?>) o;
    // Comparing the relations may lead to infinite loops in case of cycles!
    return EqualsUtils.equals (m_aValue, rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    // Comparing the relations may lead to infinite loops in case of cycles!
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("value", m_aValue)
                            .append ("incomingIDs", m_aIncoming == null ? null : m_aIncoming.keySet ())
                            .append ("outgoingIDs", m_aOutgoing == null ? null : m_aOutgoing.keySet ())
                            .toString ();
  }
}
