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

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation if the {@link IGraphNode} interface
 * 
 * @author philip
 * @param <VALUETYPE>
 *        Value type contained in this node
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

  @Nonnull
  private Map <String, IGraphRelation <VALUETYPE>> _getIncoming ()
  {
    if (m_aIncoming == null)
      m_aIncoming = new LinkedHashMap <String, IGraphRelation <VALUETYPE>> ();
    return m_aIncoming;
  }

  @Nonnull
  private Map <String, IGraphRelation <VALUETYPE>> _getOutgoing ()
  {
    if (m_aOutgoing == null)
      m_aOutgoing = new LinkedHashMap <String, IGraphRelation <VALUETYPE>> ();
    return m_aOutgoing;
  }

  @Nullable
  public VALUETYPE getValue ()
  {
    return m_aValue;
  }

  private void _addIncoming (@Nonnull final IGraphRelation <VALUETYPE> aNewRelation)
  {
    if (aNewRelation == null)
      throw new NullPointerException ("relation");
    if (aNewRelation.getTo () != this)
      throw new IllegalArgumentException ("Passed incoming relation is not based on this node");
    if (_getIncoming ().containsKey (aNewRelation.getID ()))
      throw new IllegalArgumentException ("The passed relation (" +
                                          aNewRelation +
                                          ") is already contained as an incoming relation");
    // check if the relation from-node is already contained
    if (m_aIncoming != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aIncoming.values ())
        if (aRelation.getFrom () == aNewRelation.getFrom ())
          throw new IllegalArgumentException ("The from-node of the passed relation (" +
                                              aNewRelation +
                                              ") is already contained");

    // Add!
    _getIncoming ().put (aNewRelation.getID (), aNewRelation);
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> addIncomingRelation (@Nonnull final IGraphRelation <VALUETYPE> aRelation)
  {
    _addIncoming (aRelation);
    ((GraphNode <VALUETYPE>) aRelation.getFrom ())._addOutgoing (aRelation);
    return aRelation;
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> addIncomingRelation (@Nonnull final IGraphNode <VALUETYPE> aFromNode)
  {
    return addIncomingRelation (GraphRelation.create (aFromNode, this));
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

  @Nonnull
  @ReturnsImmutableObject
  public Collection <IGraphRelation <VALUETYPE>> getIncomingRelations ()
  {
    return ContainerHelper.makeUnmodifiable (_getIncoming ().values ());
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
        ret.add (aRelation.getFrom ().getValue ());
    return ret;
  }

  private void _addOutgoing (@Nonnull final IGraphRelation <VALUETYPE> aNewRelation)
  {
    if (aNewRelation == null)
      throw new NullPointerException ("relation");
    if (aNewRelation.getFrom () != this)
      throw new IllegalArgumentException ("Passed outgoing relation is not based on this node");
    if (_getOutgoing ().containsKey (aNewRelation.getID ()))
      throw new IllegalArgumentException ("The passed relation " +
                                          aNewRelation +
                                          " is already contained as an outgoing relation");
    // check if the relation to-node is already contained
    if (m_aOutgoing != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aOutgoing.values ())
        if (aRelation.getTo () == aNewRelation.getTo ())
          throw new IllegalArgumentException ("The to-node of the passed relation " +
                                              aNewRelation +
                                              " is already contained");

    // Add!
    _getOutgoing ().put (aNewRelation.getID (), aNewRelation);
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> addOutgoingRelation (@Nonnull final IGraphRelation <VALUETYPE> aRelation)
  {
    _addOutgoing (aRelation);
    ((GraphNode <VALUETYPE>) aRelation.getTo ())._addIncoming (aRelation);
    return aRelation;
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> addOutgoingRelation (@Nonnull final IGraphNode <VALUETYPE> aToNode)
  {
    return addOutgoingRelation (GraphRelation.create (this, aToNode));
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

  @Nonnull
  @ReturnsImmutableObject
  public Collection <IGraphRelation <VALUETYPE>> getOutgoingRelations ()
  {
    return ContainerHelper.makeUnmodifiable (_getOutgoing ().values ());
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

  @Nonnull
  @ReturnsMutableCopy
  public Collection <VALUETYPE> getAllToValues ()
  {
    final List <VALUETYPE> ret = new ArrayList <VALUETYPE> ();
    if (m_aOutgoing != null)
      for (final IGraphRelation <VALUETYPE> aRelation : m_aOutgoing.values ())
        ret.add (aRelation.getFrom ().getValue ());
    return ret;
  }

  public boolean isConnectedWith (@Nullable final IGraphNode <VALUETYPE> aNode)
  {
    if (aNode != null)
    {
      if (m_aIncoming != null)
        for (final IGraphRelation <VALUETYPE> aRel : m_aIncoming.values ())
          if (aRel.getFrom () == aNode)
            return true;
      if (m_aOutgoing != null)
        for (final IGraphRelation <VALUETYPE> aRel : m_aOutgoing.values ())
          if (aRel.getTo () == aNode)
            return true;
    }
    return false;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final GraphNode <?> rhs = (GraphNode <?>) o;
    return CompareUtils.nullSafeEquals (m_aValue, rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("value", m_aValue)
                            .append ("incoming#", getIncomingRelationCount ())
                            .append ("outgoing#", getOutgoingRelationCount ())
                            .toString ();
  }

  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param <VALUETYPE>
   *        Graph node element type
   * @param aValue
   *        Contained value. May be <code>null</code>.
   * @return The created graph node and never <code>null</code>.
   */
  @Nonnull
  public static <VALUETYPE> IGraphNode <VALUETYPE> create (@Nullable final VALUETYPE aValue)
  {
    return new GraphNode <VALUETYPE> (aValue);
  }

  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param <VALUETYPE>
   *        Graph node element type
   * @param sID
   *        The ID of the graph node to use. May be <code>null</code>.
   * @param aValue
   *        Contained value. May be <code>null</code>.
   * @return The created graph node and never <code>null</code>.
   */
  @Nonnull
  public static <VALUETYPE> IGraphNode <VALUETYPE> create (@Nullable final String sID, @Nullable final VALUETYPE aValue)
  {
    return new GraphNode <VALUETYPE> (sID, aValue);
  }
}
