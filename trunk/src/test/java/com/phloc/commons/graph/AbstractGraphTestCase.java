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
package com.phloc.commons.graph;

import javax.annotation.Nonnull;

import com.phloc.commons.graph.simple.SimpleGraph;
import com.phloc.commons.mock.AbstractPhlocTestCase;

public abstract class AbstractGraphTestCase extends AbstractPhlocTestCase
{
  @Nonnull
  private static final IGraphNode <Integer> _createGN (final SimpleGraph <Integer> aGraph, final int i)
  {
    return aGraph.createNode (Integer.toString (i), Integer.valueOf (i + 1));
  }

  @Nonnull
  protected SimpleGraph <Integer> _buildGraph ()
  {
    final SimpleGraph <Integer> aGraph = new SimpleGraph <Integer> ();

    final IGraphNode <Integer> node0 = _createGN (aGraph, 0);
    final IGraphNode <Integer> node1 = _createGN (aGraph, 1);
    final IGraphNode <Integer> node2 = _createGN (aGraph, 2);
    final IGraphNode <Integer> node3 = _createGN (aGraph, 3);
    final IGraphNode <Integer> node4 = _createGN (aGraph, 4);
    final IGraphNode <Integer> node5 = _createGN (aGraph, 5);
    final IGraphNode <Integer> node6 = _createGN (aGraph, 6);
    aGraph.createRelation (node0.getID (), node1.getID ());
    aGraph.createRelation (node1, node2);
    aGraph.createRelation (node2, node3);
    aGraph.createRelation (node3, node4);
    aGraph.createRelation (node0, node5);
    aGraph.createRelation (node5, node3);
    aGraph.createRelation (node5, node6);
    aGraph.createRelation (node6, node3);

    return aGraph;
  }

  @Nonnull
  protected IReadonlyGraph <Integer> _buildCycleGraphSimple ()
  {
    final SimpleGraph <Integer> aGraph = new SimpleGraph <Integer> ();
    final IGraphNode <Integer> node0 = _createGN (aGraph, 0);
    final IGraphNode <Integer> node1 = _createGN (aGraph, 1);
    aGraph.createRelation (node0, node1);
    aGraph.createRelation (node1, node0);
    return aGraph;
  }

  @Nonnull
  protected IReadonlyGraph <Integer> _buildCycleGraphSimple2 ()
  {
    final SimpleGraph <Integer> aGraph = new SimpleGraph <Integer> ();
    final IGraphNode <Integer> node0 = _createGN (aGraph, 0);
    final IGraphNode <Integer> node1 = _createGN (aGraph, 1);
    final IGraphNode <Integer> node2 = _createGN (aGraph, 2);
    final IGraphNode <Integer> node3 = _createGN (aGraph, 3);
    aGraph.createRelation (node0, node1);
    aGraph.createRelation (node1, node2);
    aGraph.createRelation (node2, node3);
    aGraph.createRelation (node3, node0);
    return aGraph;
  }
}
