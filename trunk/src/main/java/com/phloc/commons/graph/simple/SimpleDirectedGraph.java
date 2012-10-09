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
import com.phloc.commons.graph.IDirectedGraphNode;
import com.phloc.commons.graph.IDirectedGraphObjectFactory;
import com.phloc.commons.graph.IDirectedGraphRelation;
import com.phloc.commons.graph.iterate.DirectedGraphIteratorForward;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A simple graph object that bidirectionally links graph nodes.
 * 
 * @author philip
 */
@NotThreadSafe
public class SimpleDirectedGraph implements ISimpleDirectedGraph
{
  public static final boolean DEFAULT_CHANGING_CONNECTED_OBJECTS_ALLOWED = true;

  private final IDirectedGraphObjectFactory m_aFactory;
  private final Map <String, IDirectedGraphNode> m_aNodes = new LinkedHashMap <String, IDirectedGraphNode> ();
  private boolean m_bIsChangingConnectedObjectsAllowed = DEFAULT_CHANGING_CONNECTED_OBJECTS_ALLOWED;
  private ETriState m_eCacheHasCycles = ETriState.UNDEFINED;

  public SimpleDirectedGraph ()
  {
    this (new SimpleDirectedGraphObjectFactory ());
  }

  public SimpleDirectedGraph (@Nonnull final IDirectedGraphObjectFactory aFactory)
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
  public IDirectedGraphNode createNode ()
  {
    // Create node with new ID
    final IDirectedGraphNode aNode = m_aFactory.createNode ();
    if (addNode (aNode).isUnchanged ())
      throw new IllegalStateException ("The ID factory created the ID '" + aNode.getID () + "' that is already in use");
    return aNode;
  }

  @Nullable
  public IDirectedGraphNode createNode (@Nullable final String sID)
  {
    final IDirectedGraphNode aNode = m_aFactory.createNode (sID);
    return addNode (aNode).isChanged () ? aNode : null;
  }

  @Nonnull
  public EChange addNode (@Nonnull final IDirectedGraphNode aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    if (!isChangingConnectedObjectsAllowed () && aNode.hasRelations ())
      throw new IllegalArgumentException ("The node to be added already has incoming and/or outgoing relations and this is not allowed!");

    final String sID = aNode.getID ();
    if (m_aNodes.containsKey (sID))
      return EChange.UNCHANGED;
    m_aNodes.put (sID, aNode);

    _invalidateCache ();
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange removeNode (@Nonnull final IDirectedGraphNode aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    if (!isChangingConnectedObjectsAllowed () && aNode.hasRelations ())
      throw new IllegalArgumentException ("The node to be removed already has incoming and/or outgoing relations and this is not allowed!");

    if (m_aNodes.remove (aNode.getID ()) == null)
      return EChange.UNCHANGED;

    _invalidateCache ();
    return EChange.CHANGED;
  }

  @Nonnull
  private IDirectedGraphRelation _connect (@Nonnull final IDirectedGraphRelation aRelation)
  {
    aRelation.getFrom ().addOutgoingRelation (aRelation);
    aRelation.getTo ().addIncomingRelation (aRelation);
    _invalidateCache ();
    return aRelation;
  }

  @Nonnull
  public IDirectedGraphRelation createRelation (@Nonnull final String sFromNodeID, @Nonnull final String sToNodeID)
  {
    final IDirectedGraphNode aFromNode = getNodeOfID (sFromNodeID);
    if (aFromNode == null)
      throw new IllegalArgumentException ("Failed to resolve from node ID '" + sFromNodeID + "'");
    final IDirectedGraphNode aToNode = getNodeOfID (sToNodeID);
    if (aToNode == null)
      throw new IllegalArgumentException ("Failed to resolve to node ID '" + sToNodeID + "'");
    return createRelation (aFromNode, aToNode);
  }

  @Nonnull
  public IDirectedGraphRelation createRelation (@Nonnull final IDirectedGraphNode aFrom,
                                                @Nonnull final IDirectedGraphNode aTo)
  {
    return _connect (m_aFactory.createRelation (aFrom, aTo));
  }

  @Nonnull
  public IDirectedGraphRelation createRelation (@Nullable final String sID,
                                                @Nonnull final IDirectedGraphNode aFrom,
                                                @Nonnull final IDirectedGraphNode aTo)
  {
    return _connect (m_aFactory.createRelation (sID, aFrom, aTo));
  }

  @Nonnull
  public EChange removeRelation (@Nullable final IDirectedGraphRelation aRelation)
  {
    EChange ret = EChange.UNCHANGED;
    if (aRelation != null)
    {
      ret = ret.or (aRelation.getFrom ().removeOutgoingRelation (aRelation));
      ret = ret.or (aRelation.getTo ().removeIncomingRelation (aRelation));
      if (ret.isChanged ())
        _invalidateCache ();
    }
    return ret;
  }

  @Nonnull
  public IDirectedGraphNode getSingleStartNode () throws IllegalStateException
  {
    final Set <IDirectedGraphNode> aStartNodes = getAllStartNodes ();
    if (aStartNodes.size () > 1)
      throw new IllegalStateException ("Graph has more than one starting node");
    if (aStartNodes.isEmpty ())
      throw new IllegalStateException ("Graph has no starting node");
    return ContainerHelper.getFirstElement (aStartNodes);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IDirectedGraphNode> getAllStartNodes ()
  {
    final Set <IDirectedGraphNode> aResult = new HashSet <IDirectedGraphNode> ();
    for (final IDirectedGraphNode aNode : m_aNodes.values ())
      if (!aNode.hasIncomingRelations ())
        aResult.add (aNode);
    return aResult;
  }

  @Nonnull
  public IDirectedGraphNode getSingleEndNode () throws IllegalStateException
  {
    final Set <IDirectedGraphNode> aEndNodes = getAllEndNodes ();
    if (aEndNodes.size () > 1)
      throw new IllegalStateException ("Graph has more than one ending node");
    if (aEndNodes.isEmpty ())
      throw new IllegalStateException ("Graph has no ending node");
    return ContainerHelper.getFirstElement (aEndNodes);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IDirectedGraphNode> getAllEndNodes ()
  {
    final Set <IDirectedGraphNode> aResult = new HashSet <IDirectedGraphNode> ();
    for (final IDirectedGraphNode aNode : m_aNodes.values ())
      if (!aNode.hasOutgoingRelations ())
        aResult.add (aNode);
    return aResult;
  }

  @Nullable
  public IDirectedGraphNode getNodeOfID (@Nullable final String sID)
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
  public Set <IDirectedGraphNode> getAllNodes ()
  {
    return ContainerHelper.newOrderedSet (m_aNodes.values ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IDirectedGraphRelation> getAllRelations ()
  {
    final Set <IDirectedGraphRelation> ret = new HashSet <IDirectedGraphRelation> ();
    for (final IDirectedGraphNode aNode : m_aNodes.values ())
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
      for (final IDirectedGraphNode aCurNode : m_aNodes.values ())
      {
        final DirectedGraphIteratorForward it = new DirectedGraphIteratorForward (aCurNode);
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
    for (final IDirectedGraphNode aNode : m_aNodes.values ())
    {
      for (final IDirectedGraphRelation aRelation : aNode.getIncomingRelations ())
        if (!m_aNodes.containsKey (aRelation.getFromID ()))
          return false;
      for (final IDirectedGraphRelation aRelation : aNode.getOutgoingRelations ())
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
    if (!(o instanceof SimpleDirectedGraph))
      return false;
    // Do not use m_eHasCycles because this is just a state variable
    final SimpleDirectedGraph rhs = (SimpleDirectedGraph) o;
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
  public static SimpleDirectedGraph create ()
  {
    return new SimpleDirectedGraph ();
  }

  @Nonnull
  public static SimpleDirectedGraph create (@Nonnull final IDirectedGraphObjectFactory aFactory)
  {
    return new SimpleDirectedGraph (aFactory);
  }
}
