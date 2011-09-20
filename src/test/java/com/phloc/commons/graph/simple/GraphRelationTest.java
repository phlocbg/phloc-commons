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
package com.phloc.commons.graph.simple;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.graph.simple.GraphNode;
import com.phloc.commons.graph.simple.GraphRelation;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link GraphRelation}.
 * 
 * @author philip
 */
public final class GraphRelationTest
{
  @Test
  public void testCtor ()
  {
    new GraphRelation <String> (new GraphNode <String> (), new GraphNode <String> ());
    GraphRelation.create (new GraphNode <String> (), new GraphNode <String> ());

    try
    {
      new GraphRelation <String> (new GraphNode <String> (), null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      new GraphRelation <String> (null, new GraphNode <String> ());
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      new GraphRelation <String> (null, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGet ()
  {
    final GraphNode <String> nf = new GraphNode <String> ();
    final GraphNode <String> nt = new GraphNode <String> ();
    final GraphRelation <String> gr = GraphRelation.create (nf, nt);
    assertSame (gr.getFrom (), nf);
    assertSame (gr.getTo (), nt);

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (GraphRelation.create ("id1", nf, nt),
                                                                    GraphRelation.create ("id1", nf, nt));
    // different IDs
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (GraphRelation.create (nf, nt),
                                                                        GraphRelation.create (nf, nt));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (GraphRelation.create ("id1", nf, nt),
                                                                        GraphRelation.create ("id1",
                                                                                              nf,
                                                                                              new GraphNode <String> ()));
  }
}
