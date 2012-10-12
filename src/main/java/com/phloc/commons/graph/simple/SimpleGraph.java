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
import java.util.List;
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
import com.phloc.commons.graph.iterate.GraphIterator;
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
public class SimpleGraph implements ISimpleGraph
{
  public static final boolean DEFAULT_CHANGING_CONNECTED_OBJECTS_ALLOWED = true;

  private final IGraphObjectFactory m_aFactory;
  private final Map <String, IGraphNode> m_aNodes = new LinkedHashMap <String, IGraphNode> ();
  private boolean m_bIsChangingConnectedObjectsAllowed = DEFAULT_CHANGING_CONNECTED_OBJECTS_ALLOWED;
  private ETriState m_eCacheHasCycles = ETriState.UNDEFINED;

  public SimpleGraph ()
  {
    this (new SimpleGraphObjectFactory ());
  }

  public SimpleGraph (@Nonnull final IGraphObjectFactory aFactory)
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
  public IGraphNode createNode ()
  {
    // Create node with new ID
    final IGraphNode aNode = m_aFactory.createNode ();
    if (addNode (aNode).isUnchanged ())
      throw new IllegalStateException ("The ID factory created the ID '" + aNode.getID () + "' that is already in use");
    return aNode;
  }

  @Nullable
  public IGraphNode createNode (@Nullable final String sID)
  {
    final IGraphNode aNode = m_aFactory.createNode (sID);
    return addNode (aNode).isChanged () ? aNode : null;
  }

  @Nonnull
  public EChange addNode (@Nonnull final IGraphNode aNode)
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
  public EChange removeNode (@Nonnull final IGraphNode aNode)
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
  private IGraphRelation _connect (@Nonnull final IGraphRelation aRelation)
  {
    for (final IGraphNode aNode : aRelation.getAllConnectedNodes ())
      aNode.addRelation (aRelation);
    _invalidateCache ();
    return aRelation;
  }

  @Nonnull
  public IGraphRelation createRelation (@Nonnull final String sFromNodeID, @Nonnull final String sToNodeID)
  {
    final IGraphNode aFromNode = getNodeOfID (sFromNodeID);
    if (aFromNode == null)
      throw new IllegalArgumentException ("Failed to resolve from node ID '" + sFromNodeID + "'");
    final IGraphNode aToNode = getNodeOfID (sToNodeID);
    if (aToNode == null)
      throw new IllegalArgumentException ("Failed to resolve to node ID '" + sToNodeID + "'");
    return createRelation (aFromNode, aToNode);
  }

  @Nonnull
  public IGraphRelation createRelation (@Nonnull final IGraphNode aFrom, @Nonnull final IGraphNode aTo)
  {
    return _connect (m_aFactory.createRelation (aFrom, aTo));
  }

  @Nonnull
  public IGraphRelation createRelation (@Nullable final String sID,
                                        @Nonnull final IGraphNode aFrom,
                                        @Nonnull final IGraphNode aTo)
  {
    return _connect (m_aFactory.createRelation (sID, aFrom, aTo));
  }

  @Nonnull
  public EChange removeRelation (@Nullable final IGraphRelation aRelation)
  {
    EChange ret = EChange.UNCHANGED;
    if (aRelation != null)
    {
      for (final IGraphNode aNode : aRelation.getAllConnectedNodes ())
        ret = ret.or (aNode.removeRelation (aRelation));
      if (ret.isChanged ())
        _invalidateCache ();
    }
    return ret;
  }

  @Nullable
  public IGraphNode getNodeOfID (@Nullable final String sID)
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
  public Set <IGraphNode> getAllNodes ()
  {
    return ContainerHelper.newOrderedSet (m_aNodes.values ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IGraphRelation> getAllRelations ()
  {
    final Set <IGraphRelation> ret = new HashSet <IGraphRelation> ();
    for (final IGraphNode aNode : m_aNodes.values ())
      ret.addAll (aNode.getAllRelations ());
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
      final List <IGraphNode> aAllNodes = ContainerHelper.newList (m_aNodes.values ());
      while (!aAllNodes.isEmpty ())
      {
        // Iterate from the first node
        final GraphIterator it = new GraphIterator (aAllNodes.remove (0));
        if (it.hasCycles ())
        {
          m_eCacheHasCycles = ETriState.TRUE;
          break;
        }
        while (it.hasNext ())
        {
          // Remove from remaining list, because node is reachable from some
          // other node
          aAllNodes.remove (it.next ());
        }
      }
    }

    // cannot be undefined here
    return m_eCacheHasCycles.getAsBooleanValue (true);
  }

  public boolean isSelfContained ()
  {
    for (final IGraphNode aNode : m_aNodes.values ())
      for (final IGraphRelation aRelation : aNode.getAllRelations ())
        for (final IGraphNode aRelNode : aRelation.getAllConnectedNodes ())
          if (!m_aNodes.containsKey (aRelNode.getID ()))
            return false;
    return true;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SimpleGraph))
      return false;
    // Do not use m_eHasCycles because this is just a state variable
    final SimpleGraph rhs = (SimpleGraph) o;
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
}
