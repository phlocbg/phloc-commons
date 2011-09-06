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

@NotThreadSafe
public final class GraphIterator <VALUETYPE> implements IIterableIterator <GraphNode <VALUETYPE>>
{
  private static final class IterationNode <VALUETYPE>
  {
    private final GraphNode <VALUETYPE> m_aNode;
    private final Iterator <GraphRelation <VALUETYPE>> m_aOutgoingIt;

    public IterationNode (@Nonnull final GraphNode <VALUETYPE> aNode)
    {
      m_aNode = aNode;
      m_aOutgoingIt = aNode.getOutgoingRelations ().iterator ();
    }

    @Nonnull
    public GraphNode <VALUETYPE> getNode ()
    {
      return m_aNode;
    }

    @Nonnull
    public Iterator <GraphRelation <VALUETYPE>> getOutgoingIterator ()
    {
      return m_aOutgoingIt;
    }
  }

  /**
   * Current stack. It contains the current node plus an iterator of the
   * outgoing relations of the node
   */
  private final NonBlockingStack <IterationNode <VALUETYPE>> m_aNodeStack = new NonBlockingStack <IterationNode <VALUETYPE>> ();

  /**
   * This set keeps track of all the nodes we already visited. This is important
   * for cyclic dependencies.
   */
  private final Set <String> m_aHandledNodes = new HashSet <String> ();

  /**
   * Does the graph have cycles?
   */
  private boolean m_bHasCycles = false;

  public GraphIterator (@Nonnull final GraphNode <VALUETYPE> aStartNode)
  {
    if (aStartNode == null)
      throw new NullPointerException ("startNode");
    m_aNodeStack.push (new IterationNode <VALUETYPE> (aStartNode));
  }

  public boolean hasNext ()
  {
    return !m_aNodeStack.isEmpty ();
  }

  @Nullable
  public GraphNode <VALUETYPE> next ()
  {
    // If no nodes are left, there ain't no next!
    if (!hasNext ())
      throw new NoSuchElementException ();

    // get the node to return
    final GraphNode <VALUETYPE> ret = m_aNodeStack.peek ().getNode ();
    m_aHandledNodes.add (ret.getID ());

    // find next node
    {
      boolean bFoundNewNode = false;
      while (!m_aNodeStack.isEmpty () && !bFoundNewNode)
      {
        // check all outgoing relations
        final Iterator <GraphRelation <VALUETYPE>> itPeek = m_aNodeStack.peek ().getOutgoingIterator ();
        while (itPeek.hasNext ())
        {
          final GraphNode <VALUETYPE> aCurrentOutgoingNode = itPeek.next ().getTo ();

          // check if the current node is already contained in the stack
          // If so, we have a cycle
          for (final IterationNode <VALUETYPE> aStackElement : m_aNodeStack)
            if (aStackElement.getNode () == aCurrentOutgoingNode)
            {
              // we found a cycle!
              m_bHasCycles = true;
              break;
            }

          // Ensure that each node is returned only once!
          if (!m_aHandledNodes.contains (aCurrentOutgoingNode.getID ()))
          {
            m_aNodeStack.push (new IterationNode <VALUETYPE> (aCurrentOutgoingNode));
            bFoundNewNode = true;
            break;
          }
        }

        // if we followed all relations of the current node, go to parent node
        if (!bFoundNewNode)
          m_aNodeStack.pop ();
      }
    }

    return ret;
  }

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
  public Iterator <GraphNode <VALUETYPE>> iterator ()
  {
    return this;
  }

  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param <VALUETYPE>
   *        Graph iterator element type
   * @param aStartNode
   *        The node to start iterating. May not be <code>null</code>.
   * @return The created graph node iterator and never <code>null</code>.
   */
  @Nonnull
  public static <VALUETYPE> GraphIterator <VALUETYPE> create (@Nonnull final GraphNode <VALUETYPE> aStartNode)
  {
    return new GraphIterator <VALUETYPE> (aStartNode);
  }
}
