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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * Test class for class {@link GraphIterator}.
 * 
 * @author philip
 */
public final class GraphIteratorTest extends AbstractGraphTestCase
{
  @Test
  public void testGraphIterator ()
  {
    try
    {
      // null node not allowed
      new GraphIterator <String> (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // null node not allowed
      GraphIterator.<String> create (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    final IReadonlySimpleGraph <Integer> aGraph = _buildGraph ();
    final IGraphNode <Integer> aStart = aGraph.getSingleStartNode ();
    assertEquals (aStart.getID (), "0");
    final GraphIterator <Integer> it = GraphIterator.create (aStart);

    try
    {
      it.remove ();
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {
      // expected exception
    }

    // one of the following assertEquals may fail if the hashmap is sorted in a
    // different way
    for (int i = 0; i < 100; ++i)
      assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (1), it.next ().getValue ());
    for (int i = 0; i < 100; ++i)
      assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (2), it.next ().getValue ());
    for (int i = 0; i < 100; ++i)
      assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (3), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (4), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (5), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (6), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (7), it.next ().getValue ());
    assertFalse (it.hasNext ());
  }

  @Test
  public void testStartIteratingInTheMiddleOneWay ()
  {
    final IReadonlySimpleGraph <Integer> aGraph = _buildGraph ();
    final IGraphNode <Integer> aStartNode = aGraph.getNodeOfID ("1");
    final GraphIterator <Integer> it = GraphIterator.create (aStartNode);
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (2), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (3), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (4), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (5), it.next ().getValue ());
    assertFalse (it.hasNext ());
  }

  @Test
  public void testStartIteratingInTheMiddleTwoWays ()
  {
    final IReadonlySimpleGraph <Integer> aGraph = _buildGraph ();
    final IGraphNode <Integer> aStartNode = aGraph.getNodeOfID ("5");
    final GraphIterator <Integer> it = GraphIterator.create (aStartNode);
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (6), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (4), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (5), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (7), it.next ().getValue ());
    assertFalse (it.hasNext ());
  }

  @Test
  public void testCycleIterate1 ()
  {
    final IReadonlySimpleGraph <Integer> aGraph = _buildCycleGraphSimple ();
    final GraphIterator <Integer> it = GraphIterator.create (aGraph.getNodeOfID ("0"));
    assertTrue (it.hasNext ());
    // first item has ID 0 and value 1
    assertEquals (Integer.valueOf (1), it.next ().getValue ());
    assertTrue (it.hasNext ());
    // second item has ID 1 and value 2
    assertEquals (Integer.valueOf (2), it.next ().getValue ());
    assertFalse (it.hasNext ());
  }

  @Test
  public void testCycleIterate2 ()
  {
    final IReadonlySimpleGraph <Integer> aGraph = _buildCycleGraphSimple2 ();
    final GraphIterator <Integer> it = GraphIterator.create (aGraph.getNodeOfID ("0"));
    assertNotNull (it.iterator ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (1), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (2), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (3), it.next ().getValue ());
    assertTrue (it.hasNext ());
    assertEquals (Integer.valueOf (4), it.next ().getValue ());
    assertFalse (it.hasNext ());

    try
    {
      it.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}
  }
}
