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
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.graph.IGraphNode;
import com.phloc.commons.graph.IGraphRelation;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation if the {@link IGraphNode} interface
 * 
 * @author philip
 */
@NotThreadSafe
public class GraphNode extends AbstractGraphObject implements IGraphNode
{
  private Map <String, IGraphRelation> m_aIncoming;
  private Map <String, IGraphRelation> m_aOutgoing;

  public GraphNode ()
  {
    this (null);
  }

  public GraphNode (@Nullable final String sID)
  {
    super (sID);
  }

  public void addIncomingRelation (@Nonnull final IGraphRelation aNewRelation)
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
      for (final IGraphRelation aRelation : m_aIncoming.values ())
        if (aRelation.getFrom () == aNewRelation.getFrom ())
          throw new IllegalArgumentException ("The from-node of the passed relation (" +
                                              aNewRelation +
                                              ") is already contained");
    }
    else
    {
      m_aIncoming = new LinkedHashMap <String, IGraphRelation> ();
    }

    // Add!
    m_aIncoming.put (aNewRelation.getID (), aNewRelation);
  }

  public boolean hasIncomingRelations ()
  {
    return ContainerHelper.isNotEmpty (m_aIncoming);
  }

  @Nonnegative
  public int getIncomingRelationCount ()
  {
    return ContainerHelper.getSize (m_aIncoming);
  }

  public boolean isIncomingRelation (@Nullable final IGraphRelation aRelation)
  {
    return m_aIncoming != null && aRelation != null && aRelation.equals (m_aIncoming.get (aRelation.getID ()));
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IGraphRelation> getIncomingRelations ()
  {
    return m_aIncoming == null ? new ArrayList <IGraphRelation> () : ContainerHelper.newList (m_aIncoming.values ());
  }

  @Nonnull
  public EChange removeIncomingRelation (@Nonnull final IGraphRelation aRelation)
  {
    return aRelation == null || m_aIncoming == null ? EChange.UNCHANGED
                                                   : EChange.valueOf (m_aIncoming.remove (aRelation.getID ()) != null);
  }

  public boolean isFromNode (@Nullable final IGraphNode aNode)
  {
    return getIncomingRelationFrom (aNode) != null;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IGraphNode> getAllFromNodes ()
  {
    final Set <IGraphNode> ret = new HashSet <IGraphNode> ();
    if (m_aIncoming != null)
      for (final IGraphRelation aRelation : m_aIncoming.values ())
        ret.add (aRelation.getFrom ());
    return ret;
  }

  @Nullable
  public IGraphRelation getIncomingRelationFrom (@Nullable final IGraphNode aFromNode)
  {
    if (m_aIncoming != null && aFromNode != null)
      for (final IGraphRelation aRelation : m_aIncoming.values ())
        if (aRelation.getFrom ().equals (aFromNode))
          return aRelation;
    return null;
  }

  public void addOutgoingRelation (@Nonnull final IGraphRelation aNewRelation)
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
      for (final IGraphRelation aRelation : m_aOutgoing.values ())
        if (aRelation.getTo () == aNewRelation.getTo ())
          throw new IllegalArgumentException ("The to-node of the passed relation " +
                                              aNewRelation +
                                              " is already contained");
    }
    else
    {
      m_aOutgoing = new LinkedHashMap <String, IGraphRelation> ();
    }

    // Add!
    m_aOutgoing.put (aNewRelation.getID (), aNewRelation);
  }

  public boolean hasOutgoingRelations ()
  {
    return ContainerHelper.isNotEmpty (m_aOutgoing);
  }

  @Nonnegative
  public int getOutgoingRelationCount ()
  {
    return ContainerHelper.getSize (m_aOutgoing);
  }

  public boolean isOutgoingRelation (@Nullable final IGraphRelation aRelation)
  {
    return m_aOutgoing != null && aRelation != null && aRelation.equals (m_aOutgoing.get (aRelation.getID ()));
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IGraphRelation> getOutgoingRelations ()
  {
    return m_aOutgoing == null ? new ArrayList <IGraphRelation> () : ContainerHelper.newList (m_aOutgoing.values ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IGraphNode> getAllToNodes ()
  {
    final Set <IGraphNode> ret = new HashSet <IGraphNode> ();
    if (m_aOutgoing != null)
      for (final IGraphRelation aRelation : m_aOutgoing.values ())
        ret.add (aRelation.getTo ());
    return ret;
  }

  @Nonnull
  public EChange removeOutgoingRelation (@Nonnull final IGraphRelation aRelation)
  {
    return aRelation == null || m_aOutgoing == null ? EChange.UNCHANGED
                                                   : EChange.valueOf (m_aOutgoing.remove (aRelation.getID ()) != null);
  }

  public boolean isToNode (@Nullable final IGraphNode aNode)
  {
    return getOutgoingRelationTo (aNode) != null;
  }

  @Nullable
  public IGraphRelation getOutgoingRelationTo (@Nullable final IGraphNode aToNode)
  {
    if (m_aOutgoing != null && aToNode != null)
      for (final IGraphRelation aRelation : m_aOutgoing.values ())
        if (aRelation.getTo ().equals (aToNode))
          return aRelation;
    return null;
  }

  public boolean isConnectedWith (@Nullable final IGraphNode aNode)
  {
    return getRelationFromOrTo (aNode) != null;
  }

  @Nullable
  public IGraphRelation getRelationFromOrTo (@Nullable final IGraphNode aNode)
  {
    IGraphRelation ret = getIncomingRelationFrom (aNode);
    if (ret == null)
      ret = getOutgoingRelationTo (aNode);
    return ret;
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
  public Set <IGraphRelation> getAllRelations ()
  {
    final Set <IGraphRelation> ret = new HashSet <IGraphRelation> ();
    if (m_aIncoming != null)
      ret.addAll (m_aIncoming.values ());
    if (m_aOutgoing != null)
      ret.addAll (m_aOutgoing.values ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IGraphNode> getAllRelatedNodes ()
  {
    final Set <IGraphNode> ret = new HashSet <IGraphNode> ();
    if (m_aIncoming != null)
      for (final IGraphRelation aRelation : m_aIncoming.values ())
        ret.add (aRelation.getFrom ());
    if (m_aOutgoing != null)
      for (final IGraphRelation aRelation : m_aOutgoing.values ())
        ret.add (aRelation.getTo ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    return super.equals (o);
  }

  @Override
  public int hashCode ()
  {
    return super.hashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("incomingIDs", m_aIncoming == null ? null : m_aIncoming.keySet ())
                            .append ("outgoingIDs", m_aOutgoing == null ? null : m_aOutgoing.keySet ())
                            .toString ();
  }
}
