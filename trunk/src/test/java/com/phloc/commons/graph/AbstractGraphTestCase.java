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

import com.phloc.commons.graph.simple.GraphNode;
import com.phloc.commons.graph.simple.Graph;

public abstract class AbstractGraphTestCase
{
  private static final IGraphNode <Integer> _createGN (final int i)
  {
    return GraphNode.create (Integer.toString (i), Integer.valueOf (i + 1));
  }

  protected Graph <Integer> _buildGraph ()
  {
    final IGraphNode <Integer> node0 = _createGN (0);
    final IGraphNode <Integer> node1 = _createGN (1);
    final IGraphNode <Integer> node2 = _createGN (2);
    final IGraphNode <Integer> node3 = _createGN (3);
    final IGraphNode <Integer> node4 = _createGN (4);
    final IGraphNode <Integer> node5 = _createGN (5);
    final IGraphNode <Integer> node6 = _createGN (6);
    node0.addOutgoingRelation (node1);
    node1.addOutgoingRelation (node2);
    node2.addOutgoingRelation (node3);
    node3.addOutgoingRelation (node4);
    node0.addOutgoingRelation (node5);
    node5.addOutgoingRelation (node3);
    node5.addOutgoingRelation (node6);
    node6.addOutgoingRelation (node3);

    final Graph <Integer> aGraph = new Graph <Integer> ();
    aGraph.addNode (node0);
    aGraph.addNode (node1);
    aGraph.addNode (node2);
    aGraph.addNode (node3);
    aGraph.addNode (node4);
    aGraph.addNode (node5);
    aGraph.addNode (node6);
    return aGraph;
  }

  protected IReadonlyGraph <Integer> _buildCycleGraphSimple ()
  {
    final IGraphNode <Integer> node0 = _createGN (0);
    final IGraphNode <Integer> node1 = _createGN (1);
    node0.addOutgoingRelation (node1);
    node1.addOutgoingRelation (node0);
    final Graph <Integer> aGraph = new Graph <Integer> ();
    aGraph.addNode (node0);
    aGraph.addNode (node1);
    return aGraph;
  }

  protected IReadonlyGraph <Integer> _buildCycleGraphSimple2 ()
  {
    final IGraphNode <Integer> node0 = _createGN (0);
    final IGraphNode <Integer> node1 = _createGN (1);
    final IGraphNode <Integer> node2 = _createGN (2);
    final IGraphNode <Integer> node3 = _createGN (3);
    node0.addOutgoingRelation (node1);
    node1.addOutgoingRelation (node2);
    node2.addOutgoingRelation (node3);
    node3.addOutgoingRelation (node0);

    final Graph <Integer> aGraph = new Graph <Integer> ();
    aGraph.addNode (node0);
    aGraph.addNode (node1);
    aGraph.addNode (node2);
    aGraph.addNode (node3);
    return aGraph;
  }
}
