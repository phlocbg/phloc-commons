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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.state.IClearable;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A simple graph object with some sanity methods.
 * 
 * @author philip
 */
@NotThreadSafe
public class SimpleGraph <VALUETYPE> implements IReadonlySimpleGraph <VALUETYPE>, IClearable
{
  private final Map <String, IGraphNode <VALUETYPE>> m_aNodes = new HashMap <String, IGraphNode <VALUETYPE>> ();
  private ETriState m_eHasCycles = ETriState.UNDEFINED;

  /**
   * Create a new graph node and add it to the graph. A new ID is generated.
   * 
   * @param aValue
   *        The value to be added. May be <code>null</code>.
   * @return The created graph node. Never <code>null</code>.
   */
  @Nonnull
  public IGraphNode <VALUETYPE> addNode (@Nullable final VALUETYPE aValue)
  {
    // Create node with new ID
    final IGraphNode <VALUETYPE> aNode = GraphNode.create (aValue);
    addNode (aNode);
    return aNode;
  }

  /**
   * Create a new graph node with the given ID and value.
   * 
   * @param sID
   *        The ID of the graph node to be created. If the ID is
   *        <code>null</code> a new graph ID is created.
   * @param aValue
   *        The value to be added. May be <code>null</code>.
   * @return <code>null</code> if another graph node with the same ID is already
   *         present - the non-<code>null</code> graph node upon successful
   *         adding.
   */
  @Nullable
  public IGraphNode <VALUETYPE> addNode (@Nullable final String sID, @Nullable final VALUETYPE aValue)
  {
    final IGraphNode <VALUETYPE> aNode = GraphNode.create (sID, aValue);
    return addNode (aNode).isChanged () ? aNode : null;
  }

  @Nonnull
  public EChange addNode (@Nonnull final IGraphNode <VALUETYPE> aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    final String sID = aNode.getID ();
    if (m_aNodes.containsKey (sID))
      return EChange.UNCHANGED;
    m_aNodes.put (sID, aNode);
    m_eHasCycles = ETriState.UNDEFINED;
    return EChange.CHANGED;
  }

  @Nonnull
  public IGraphNode <VALUETYPE> getSingleStartNode ()
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
  public EChange clear ()
  {
    if (m_aNodes.isEmpty ())
      return EChange.UNCHANGED;
    m_aNodes.clear ();
    m_eHasCycles = ETriState.FALSE;
    return EChange.CHANGED;
  }

  public boolean containsCycles ()
  {
    // Use cached result?
    if (m_eHasCycles.isUndefined ())
    {
      m_eHasCycles = ETriState.FALSE;
      Set <IGraphNode <VALUETYPE>> aNodes = getAllStartNodes ();
      if (aNodes.isEmpty ())
      {
        // we have no unique start nodes -> use all nodes
        aNodes = new HashSet <IGraphNode <VALUETYPE>> (m_aNodes.values ());
      }
      for (final IGraphNode <VALUETYPE> aCurNode : aNodes)
      {
        final GraphIterator <VALUETYPE> it = GraphIterator.create (aCurNode);
        while (it.hasNext () && !it.hasCycles ())
          it.next ();
        if (it.hasCycles ())
        {
          m_eHasCycles = ETriState.TRUE;
          break;
        }
      }
    }
    return m_eHasCycles.getAsBooleanValue (true);
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
    return new ToStringGenerator (this).append ("nodes", m_aNodes).append ("hasCycles", m_eHasCycles).toString ();
  }
}
