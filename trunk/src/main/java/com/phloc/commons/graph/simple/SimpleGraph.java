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
import com.phloc.commons.graph.IGraphObjectFactory;
import com.phloc.commons.graph.IGraphRelation;
import com.phloc.commons.graph.iterate.GraphIteratorForward;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A simple graph object that bidirectionally links graph nodes.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
@NotThreadSafe
public class SimpleGraph <VALUETYPE> implements ISimpleGraph <VALUETYPE>
{
  public static final boolean DEFAULT_CHANGING_CONNECTED_OBJECTS_ALLOWED = true;

  private IGraphObjectFactory <VALUETYPE> m_aFactory;
  private final Map <String, IGraphNode <VALUETYPE>> m_aNodes = new LinkedHashMap <String, IGraphNode <VALUETYPE>> ();
  private boolean m_bIsChangingConnectedObjectsAllowed = DEFAULT_CHANGING_CONNECTED_OBJECTS_ALLOWED;
  private ETriState m_eCacheHasCycles = ETriState.UNDEFINED;

  public SimpleGraph ()
  {
    this (new SimpleGraphObjectFactory <VALUETYPE> ());
  }

  public SimpleGraph (@Nonnull final IGraphObjectFactory <VALUETYPE> aFactory)
  {
    if (aFactory == null)
      throw new NullPointerException ("factory");
    m_aFactory = aFactory;
  }

  private void _invalidateCache ()
  {
    // Reset the "has cycles" cached value
    m_eCacheHasCycles = ETriState.UNDEFINED;
  }

  public void setChangingConnectedObjectsAllowed (final boolean bIsChangingConnectedObjectsAllowed)
  {
    m_bIsChangingConnectedObjectsAllowed = bIsChangingConnectedObjectsAllowed;
  }

  public boolean isChangingConnectedObjectsAllowed ()
  {
    return m_bIsChangingConnectedObjectsAllowed;
  }

  @Nonnull
  public IGraphNode <VALUETYPE> createNode ()
  {
    return createNode (null);
  }

  @Nonnull
  public IGraphNode <VALUETYPE> createNode (@Nullable final VALUETYPE aValue)
  {
    // Create node with new ID
    final IGraphNode <VALUETYPE> aNode = m_aFactory.createNode (aValue);
    if (addNode (aNode).isUnchanged ())
      throw new IllegalStateException ("The ID factory created the ID '" + aNode.getID () + "' that is already in use");
    return aNode;
  }

  @Nullable
  public IGraphNode <VALUETYPE> createNode (@Nullable final String sID, @Nullable final VALUETYPE aValue)
  {
    final IGraphNode <VALUETYPE> aNode = m_aFactory.createNode (sID, aValue);
    return addNode (aNode).isChanged () ? aNode : null;
  }

  @Nonnull
  public EChange addNode (@Nonnull final IGraphNode <VALUETYPE> aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    if (!isChangingConnectedObjectsAllowed () && aNode.hasIncomingOrOutgoingRelations ())
      throw new IllegalArgumentException ("The node to be added already has incoming and/or outgoing relations and this is not allowed!");

    final String sID = aNode.getID ();
    if (m_aNodes.containsKey (sID))
      return EChange.UNCHANGED;
    m_aNodes.put (sID, aNode);

    _invalidateCache ();
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange removeNode (@Nonnull final IGraphNode <VALUETYPE> aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    if (!isChangingConnectedObjectsAllowed () && aNode.hasIncomingOrOutgoingRelations ())
      throw new IllegalArgumentException ("The node to be removed already has incoming and/or outgoing relations and this is not allowed!");

    if (m_aNodes.remove (aNode.getID ()) == null)
      return EChange.UNCHANGED;

    _invalidateCache ();
    return EChange.CHANGED;
  }

  @Nonnull
  private IGraphRelation <VALUETYPE> _connect (@Nonnull final IGraphRelation <VALUETYPE> aRelation)
  {
    aRelation.getFrom ().addOutgoingRelation (aRelation);
    aRelation.getTo ().addIncomingRelation (aRelation);
    return aRelation;
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> createRelation (@Nonnull final String sFromNodeID, @Nonnull final String sToNodeID)
  {
    final IGraphNode <VALUETYPE> aFromNode = getNodeOfID (sFromNodeID);
    if (aFromNode == null)
      throw new IllegalArgumentException ("Failed to resolve from node ID '" + sFromNodeID + "'");
    final IGraphNode <VALUETYPE> aToNode = getNodeOfID (sToNodeID);
    if (aToNode == null)
      throw new IllegalArgumentException ("Failed to resolve to node ID '" + sToNodeID + "'");
    return createRelation (aFromNode, aToNode);
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> createRelation (@Nonnull final IGraphNode <VALUETYPE> aFrom,
                                                    @Nonnull final IGraphNode <VALUETYPE> aTo)
  {
    return _connect (m_aFactory.createRelation (aFrom, aTo));
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> createRelation (@Nullable final String sID,
                                                    @Nonnull final IGraphNode <VALUETYPE> aFrom,
                                                    @Nonnull final IGraphNode <VALUETYPE> aTo)
  {
    return _connect (m_aFactory.createRelation (sID, aFrom, aTo));
  }

  @Nonnull
  public IGraphNode <VALUETYPE> getSingleStartNode () throws IllegalStateException
  {
    final Set <IGraphNode <VALUETYPE>> aStartNodes = getAllStartNodes ();
    if (aStartNodes.size () > 1)
      throw new IllegalStateException ("Graph has more than one starting node");
    if (aStartNodes.isEmpty ())
      throw new IllegalStateException ("Graph has no starting node");
    return ContainerHelper.getFirstElement (aStartNodes);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IGraphNode <VALUETYPE>> getAllStartNodes ()
  {
    final Set <IGraphNode <VALUETYPE>> aResult = new HashSet <IGraphNode <VALUETYPE>> ();
    for (final IGraphNode <VALUETYPE> aNode : m_aNodes.values ())
      if (!aNode.hasIncomingRelations ())
        aResult.add (aNode);
    return aResult;
  }

  @Nonnull
  public IGraphNode <VALUETYPE> getSingleEndNode () throws IllegalStateException
  {
    final Set <IGraphNode <VALUETYPE>> aEndNodes = getAllEndNodes ();
    if (aEndNodes.size () > 1)
      throw new IllegalStateException ("Graph has more than one ending node");
    if (aEndNodes.isEmpty ())
      throw new IllegalStateException ("Graph has no ending node");
    return ContainerHelper.getFirstElement (aEndNodes);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IGraphNode <VALUETYPE>> getAllEndNodes ()
  {
    final Set <IGraphNode <VALUETYPE>> aResult = new HashSet <IGraphNode <VALUETYPE>> ();
    for (final IGraphNode <VALUETYPE> aNode : m_aNodes.values ())
      if (!aNode.hasOutgoingRelations ())
        aResult.add (aNode);
    return aResult;
  }

  @Nullable
  public IGraphNode <VALUETYPE> getNodeOfID (@Nullable final String sID)
  {
    return m_aNodes.get (sID);
  }

  @Nonnegative
  public int getNodeCount ()
  {
    return m_aNodes.size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IGraphNode <VALUETYPE>> getAllNodes ()
  {
    return ContainerHelper.newOrderedSet (m_aNodes.values ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IGraphRelation <VALUETYPE>> getAllRelations ()
  {
    final Set <IGraphRelation <VALUETYPE>> ret = new HashSet <IGraphRelation <VALUETYPE>> ();
    for (final IGraphNode <VALUETYPE> aNode : m_aNodes.values ())
      ret.addAll (aNode.getOutgoingRelations ());
    return ret;
  }

  @Nonnull
  public EChange clear ()
  {
    if (m_aNodes.isEmpty ())
      return EChange.UNCHANGED;
    m_aNodes.clear ();

    _invalidateCache ();
    return EChange.CHANGED;
  }

  public boolean containsCycles ()
  {
    // Use cached result?
    if (m_eCacheHasCycles.isUndefined ())
    {
      m_eCacheHasCycles = ETriState.FALSE;
      // Check all nodes, in case we a small cycle and a set of other nodes (see
      // test case testCycles2)
      for (final IGraphNode <VALUETYPE> aCurNode : m_aNodes.values ())
      {
        final GraphIteratorForward <VALUETYPE> it = GraphIteratorForward.create (aCurNode);
        while (it.hasNext () && !it.hasCycles ())
          it.next ();
        if (it.hasCycles ())
        {
          m_eCacheHasCycles = ETriState.TRUE;
          break;
        }
      }
    }

    // cannot be undefined here
    return m_eCacheHasCycles.getAsBooleanValue (true);
  }

  public boolean isSelfContained ()
  {
    for (final IGraphNode <VALUETYPE> aNode : m_aNodes.values ())
    {
      for (final IGraphRelation <VALUETYPE> aRelation : aNode.getIncomingRelations ())
        if (!m_aNodes.containsKey (aRelation.getFromID ()))
          return false;
      for (final IGraphRelation <VALUETYPE> aRelation : aNode.getOutgoingRelations ())
        if (!m_aNodes.containsKey (aRelation.getToID ()))
          return false;
    }
    return true;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SimpleGraph <?>))
      return false;
    // Do not use m_eHasCycles because this is just a state variable
    final SimpleGraph <?> rhs = (SimpleGraph <?>) o;
    return m_aNodes.equals (rhs.m_aNodes);
  }

  @Override
  public int hashCode ()
  {
    // Do not use m_eHasCycles because this is just a state variable
    return new HashCodeGenerator (this).append (m_aNodes).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("nodes", m_aNodes).append ("hasCycles", m_eCacheHasCycles).toString ();
  }

  @Nonnull
  public static <VALUETYPE> SimpleGraph <VALUETYPE> create ()
  {
    return new SimpleGraph <VALUETYPE> ();
  }

  @Nonnull
  public static <VALUETYPE> SimpleGraph <VALUETYPE> create (@Nonnull final IGraphObjectFactory <VALUETYPE> aFactory)
  {
    return new SimpleGraph <VALUETYPE> (aFactory);
  }
}
