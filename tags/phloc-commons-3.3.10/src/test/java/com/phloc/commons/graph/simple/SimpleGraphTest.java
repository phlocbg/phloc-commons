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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.graph.AbstractGraphTestCase;
import com.phloc.commons.graph.IGraphNode;
import com.phloc.commons.graph.IReadonlyGraph;
import com.phloc.commons.graph.impl.GraphNode;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link SimpleGraph}.
 * 
 * @author philip
 */
public final class SimpleGraphTest extends AbstractGraphTestCase
{
  @Test
  public void testCtor ()
  {
    final IReadonlyGraph <String> sg = new SimpleGraph <String> ();
    assertTrue (sg.getAllStartNodes ().isEmpty ());
    assertTrue (sg.getAllEndNodes ().isEmpty ());
    assertFalse (sg.containsCycles ());
    assertNotNull (sg.toString ());

    try
    {
      // no node contained
      sg.getSingleStartNode ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
  }

  @Test
  public void testAddNode ()
  {
    final SimpleGraph <String> sg = new SimpleGraph <String> ();
    assertEquals (0, sg.getNodeCount ());
    try
    {
      // null node not allowed
      sg.addNode ((GraphNode <String>) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    final GraphNode <String> n = new GraphNode <String> ();
    assertTrue (sg.addNode (n).isChanged ());
    assertEquals (1, sg.getNodeCount ());

    // node already contained
    assertFalse (sg.addNode (n).isChanged ());

    assertTrue (sg.getAllStartNodes ().contains (n));
    assertTrue (sg.getAllEndNodes ().contains (n));
    assertFalse (sg.containsCycles ());
    assertEquals (sg.getSingleStartNode (), n);

    final GraphNode <String> n2 = new GraphNode <String> ();
    assertTrue (sg.addNode (n2).isChanged ());

    // node already contained
    assertFalse (sg.addNode (n2).isChanged ());

    assertTrue (sg.getAllStartNodes ().contains (n));
    assertTrue (sg.getAllStartNodes ().contains (n2));
    assertTrue (sg.getAllEndNodes ().contains (n));
    assertTrue (sg.getAllEndNodes ().contains (n2));
    assertFalse (sg.containsCycles ());
    try
    {
      sg.getSingleStartNode ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {}

    assertNotNull (sg.createNode ("value"));
    assertNotNull (sg.createNode ("value"));
    assertNotNull (sg.createNode ("id4711", "value"));
    assertNull (sg.createNode ("id4711", "value"));
  }

  @Test
  public void testClear ()
  {
    final SimpleGraph <String> sg = new SimpleGraph <String> ();

    final GraphNode <String> n = new GraphNode <String> ();
    assertTrue (sg.addNode (n).isChanged ());

    final GraphNode <String> n2 = new GraphNode <String> ();
    assertTrue (sg.addNode (n2).isChanged ());

    assertTrue (sg.clear ().isChanged ());
    assertFalse (sg.clear ().isChanged ());

    assertTrue (sg.getAllStartNodes ().isEmpty ());
    assertTrue (sg.getAllEndNodes ().isEmpty ());
    assertFalse (sg.containsCycles ());

    try
    {
      // no node contained
      sg.getSingleStartNode ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
  }

  @Test
  public void testCycles ()
  {
    SimpleGraph <Integer> sg = _buildGraph ();
    assertFalse (sg.containsCycles ());

    sg = new SimpleGraph <Integer> ();
    final IGraphNode <Integer> n1 = sg.createNode (null);
    final IGraphNode <Integer> n2 = sg.createNode (null);
    sg.createRelation (n1, n2);
    sg.createRelation (n2, n1);

    assertTrue (sg.containsCycles ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (_buildGraph (), _buildGraph ());
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new SimpleGraph <Integer> (),
                                                                    new SimpleGraph <Integer> ());
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (_buildGraph (), new SimpleGraph <Integer> ());
  }
}
