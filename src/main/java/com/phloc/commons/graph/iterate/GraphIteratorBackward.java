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
package com.phloc.commons.graph.iterate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.graph.IGraphNode;
import com.phloc.commons.graph.IGraphRelation;

/**
 * A simple backward iterator for simple graphs (following the incoming nodes).
 * 
 * @author philip
 */
@NotThreadSafe
public final class GraphIteratorBackward implements IIterableIterator <IGraphNode>
{
  /**
   * This class represents a node in the current iteration process. It is
   * relevant to easily keep the current iterator status and the node together.
   * 
   * @author philip
   */
  private static final class IterationNode
  {
    private final IGraphNode m_aNode;
    private final Iterator <IGraphRelation> m_aIncomingIt;

    private IterationNode (@Nonnull final IGraphNode aNode)
    {
      if (aNode == null)
        throw new NullPointerException ("node");
      m_aNode = aNode;
      m_aIncomingIt = aNode.getIncomingRelations ().iterator ();
    }

    @Nonnull
    public IGraphNode getNode ()
    {
      return m_aNode;
    }

    @Nonnull
    public Iterator <IGraphRelation> getIncomingRelationIterator ()
    {
      return m_aIncomingIt;
    }

    @Nonnull
    public static IterationNode create (@Nonnull final IGraphNode aNode)
    {
      return new IterationNode (aNode);
    }
  }

  /**
   * Current stack. It contains the current node plus an iterator of the
   * incoming relations of the node
   */
  private final NonBlockingStack <IterationNode> m_aNodeStack = new NonBlockingStack <IterationNode> ();

  /**
   * Optional filter for graph relations to defined whether thy should be
   * followed or not. May be <code>null</code>.
   */
  private final IFilter <IGraphRelation> m_aRelationFilter;

  /**
   * This set keeps track of all the nodes we already visited. This is important
   * for cyclic dependencies.
   */
  private final Set <String> m_aHandledNodes = new HashSet <String> ();

  /**
   * Does the graph have cycles?
   */
  private boolean m_bHasCycles = false;

  public GraphIteratorBackward (@Nonnull final IGraphNode aStartNode)
  {
    this (aStartNode, null);
  }

  public GraphIteratorBackward (@Nonnull final IGraphNode aStartNode,
                                @Nullable final IFilter <IGraphRelation> aRelationFilter)
  {
    if (aStartNode == null)
      throw new NullPointerException ("startNode");

    m_aRelationFilter = aRelationFilter;

    // Ensure that the start node is present
    m_aNodeStack.push (IterationNode.create (aStartNode));
  }

  public boolean hasNext ()
  {
    return !m_aNodeStack.isEmpty ();
  }

  @Nullable
  public IGraphNode next ()
  {
    // If no nodes are left, there ain't no next!
    if (!hasNext ())
      throw new NoSuchElementException ();

    // get the node to return
    final IGraphNode ret = m_aNodeStack.peek ().getNode ();
    m_aHandledNodes.add (ret.getID ());

    // find next node
    {
      boolean bFoundNewNode = false;
      while (!m_aNodeStack.isEmpty () && !bFoundNewNode)
      {
        // check all incoming relations
        final Iterator <IGraphRelation> itPeek = m_aNodeStack.peek ().getIncomingRelationIterator ();
        while (itPeek.hasNext ())
        {
          final IGraphRelation aCurrentRelation = itPeek.next ();

          // Callback to check whether the current relation should be followed
          // or not
          if (m_aRelationFilter != null && !m_aRelationFilter.matchesFilter (aCurrentRelation))
            continue;

          // from-node of the current relation
          final IGraphNode aCurrentIncomingNode = aCurrentRelation.getFrom ();

          // check if the current node is already contained in the stack
          // If so, we have a cycle
          for (final IterationNode aStackElement : m_aNodeStack)
            if (aStackElement.getNode () == aCurrentIncomingNode)
            {
              // we found a cycle!
              m_bHasCycles = true;
              break;
            }

          // Ensure that each node is returned only once!
          if (!m_aHandledNodes.contains (aCurrentIncomingNode.getID ()))
          {
            // Okay, we have a new node
            m_aNodeStack.push (IterationNode.create (aCurrentIncomingNode));
            bFoundNewNode = true;
            break;
          }
        }

        // if we followed all relations of the current node, go to previous node
        if (!bFoundNewNode)
          m_aNodeStack.pop ();
      }
    }

    return ret;
  }

  /**
   * @return <code>true</code> if the iterator determined a cycle while
   *         iterating the graph
   */
  public boolean hasCycles ()
  {
    return m_bHasCycles;
  }

  /**
   * @throws UnsupportedOperationException
   *         every time!
   */
  @UnsupportedOperation
  public void remove ()
  {
    throw new UnsupportedOperationException ("This iterator has no remove!");
  }

  @Nonnull
  public Iterator <IGraphNode> iterator ()
  {
    return this;
  }

  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param aStartNode
   *        The node to start iterating. May not be <code>null</code>.
   * @return The created graph node iterator and never <code>null</code>.
   */
  @Nonnull
  public static GraphIteratorBackward create (@Nonnull final IGraphNode aStartNode)
  {
    return new GraphIteratorBackward (aStartNode);
  }

  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param aStartNode
   *        The node to start iterating. May not be <code>null</code>.
   * @param aRelationFilter
   *        a relation filter to specify whether to follow a certain graph
   *        relation or not. May be <code>null</code>.
   * @return The created graph node iterator and never <code>null</code>.
   */
  @Nonnull
  public static GraphIteratorBackward create (@Nonnull final IGraphNode aStartNode,
                                              @Nullable final IFilter <IGraphRelation> aRelationFilter)
  {
    return new GraphIteratorBackward (aStartNode, aRelationFilter);
  }
}
