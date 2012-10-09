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
package com.phloc.commons.graph.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;

import com.phloc.commons.graph.IGraphRelation;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.string.StringHelper;

/**
 * Test class for class {@link GraphNode}.
 * 
 * @author philip
 */
public final class GraphNodeTest
{
  @Test
  public void testCtor ()
  {
    final GraphNode n = new GraphNode ();
    assertNotNull (n.getID ());
    assertTrue (StringHelper.hasText (n.getID ()));

    final GraphNode n1 = new GraphNode ("");
    assertNotNull (n1.getID ());
    assertTrue (StringHelper.hasText (n1.getID ()));
    assertEquals (0, n1.getAllFromNodes ().size ());
    assertEquals (0, n1.getIncomingRelationCount ());
    assertEquals (0, n1.getAllToNodes ().size ());
    assertEquals (0, n1.getOutgoingRelationCount ());
    assertFalse (n1.hasIncomingRelations ());
    assertFalse (n1.hasOutgoingRelations ());
    assertFalse (n1.hasIncomingOrOutgoingRelations ());
    assertFalse (n1.hasIncomingAndOutgoingRelations ());

    final GraphNode n3 = new GraphNode ("id1");
    assertNotNull (n3.getID ());
    assertEquals (n3.getID (), "id1");
  }

  @Test
  public void testRelationsOutgoing1 ()
  {
    final GraphNode nf = new GraphNode ();
    final GraphNode nt = new GraphNode ();
    final IGraphRelation gr = new GraphRelation (nf, nt);
    nf.addOutgoingRelation (gr);

    assertFalse (nf.isIncomingRelation (gr));
    assertTrue (nf.isOutgoingRelation (gr));
    assertFalse (nt.isIncomingRelation (gr));
    assertFalse (nt.isOutgoingRelation (gr));
    assertTrue (nf.isConnectedWith (nt));
    assertFalse (nt.isConnectedWith (nf));
    assertFalse (nf.hasIncomingRelations ());
    assertTrue (nf.hasOutgoingRelations ());
    assertTrue (nf.hasIncomingOrOutgoingRelations ());
    assertFalse (nf.hasIncomingAndOutgoingRelations ());

    assertEquals (0, nf.getAllFromNodes ().size ());
    assertEquals (1, nf.getAllToNodes ().size ());
    assertEquals (0, nt.getAllFromNodes ().size ());
    assertEquals (0, nt.getAllToNodes ().size ());

    assertTrue (nf.isConnectedWith (nt));
    assertFalse (nf.isConnectedWith (nf));
    assertFalse (nt.isConnectedWith (nf));
    assertFalse (nt.isConnectedWith (nt));

    Iterator <IGraphRelation> it = nf.getOutgoingRelations ().iterator ();
    assertNotNull (it);
    assertTrue (it.hasNext ());
    assertTrue (it.hasNext ());
    assertEquals (it.next ().getTo (), nt);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nf.getIncomingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nt.getIncomingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nt.getOutgoingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());
  }

  @Test
  public void testRelationsOutgoing2 ()
  {
    final GraphNode nf = new GraphNode ();
    final GraphNode nt = new GraphNode ();
    nf.addOutgoingRelation (new GraphRelation (nf, nt));
    Iterator <IGraphRelation> it = nf.getOutgoingRelations ().iterator ();
    assertNotNull (it);
    assertTrue (it.hasNext ());
    assertTrue (it.hasNext ());
    assertEquals (it.next ().getTo (), nt);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nf.getIncomingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nt.getIncomingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nt.getOutgoingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());
  }

  @Test
  public void testRelationsIncoming1 ()
  {
    final GraphNode nf = new GraphNode ();
    final GraphNode nt = new GraphNode ();
    nt.addIncomingRelation (new GraphRelation (nf, nt));
    Iterator <IGraphRelation> it = nf.getOutgoingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nf.getIncomingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nt.getIncomingRelations ().iterator ();
    assertNotNull (it);
    assertTrue (it.hasNext ());
    assertTrue (it.hasNext ());
    assertEquals (it.next ().getFrom (), nf);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nt.getOutgoingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());
  }

  @Test
  public void testRelationsIncoming2 ()
  {
    final GraphNode nf = new GraphNode ();
    final GraphNode nt = new GraphNode ();
    nt.addIncomingRelation (new GraphRelation (nf, nt));
    Iterator <IGraphRelation> it = nf.getOutgoingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nf.getIncomingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nt.getIncomingRelations ().iterator ();
    assertNotNull (it);
    assertTrue (it.hasNext ());
    assertTrue (it.hasNext ());
    assertEquals (it.next ().getFrom (), nf);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nt.getOutgoingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());
  }

  @Test
  public void testSelfRelations ()
  {
    final GraphNode nf = new GraphNode ();
    nf.addOutgoingRelation (new GraphRelation (nf, nf));
    Iterator <IGraphRelation> it = nf.getOutgoingRelations ().iterator ();
    assertNotNull (it);
    assertTrue (it.hasNext ());
    assertTrue (it.hasNext ());
    assertEquals (it.next ().getTo (), nf);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());

    it = nf.getIncomingRelations ().iterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());
  }

  @Test
  public void testStdMethods ()
  {
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new GraphNode ("id0"), new GraphNode ("id0"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new GraphNode ("id0"), new GraphNode ("id1"));
    final GraphNode n1 = new GraphNode ("id0");
    n1.setAttribute ("a", "b");
    final GraphNode n2 = new GraphNode ("id0");
    n2.setAttribute ("a", "c");
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (n1, n2);
  }

  @Test
  public void testRelationsInvalid ()
  {
    try
    {
      final GraphNode n = new GraphNode ();
      // null not allowed
      n.addOutgoingRelation ((IGraphRelation) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      final GraphNode n = new GraphNode ();
      // null not allowed
      n.addIncomingRelation ((IGraphRelation) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      final GraphNode n = new GraphNode ();
      // relation does not match node
      n.addOutgoingRelation (new GraphRelation (new GraphNode (), new GraphNode ()));
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      final GraphNode n = new GraphNode ();
      // relation does not match node
      n.addIncomingRelation (new GraphRelation (new GraphNode (), new GraphNode ()));
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      final GraphNode n = new GraphNode ();
      final GraphRelation r = new GraphRelation (n, new GraphNode ());
      n.addOutgoingRelation (r);
      // exactly the same relation already added
      n.addOutgoingRelation (r);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      final GraphNode n = new GraphNode ();
      final GraphRelation r = new GraphRelation (new GraphNode (), n);
      n.addIncomingRelation (r);
      // exactly the same relation already added
      n.addIncomingRelation (r);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      final GraphNode nf = new GraphNode ();
      final GraphNode nt = new GraphNode ();
      final IGraphRelation r = new GraphRelation (nf, nt);
      nf.addOutgoingRelation (r);
      assertFalse (nf.hasIncomingRelations ());
      assertTrue (nf.hasOutgoingRelations ());
      assertFalse (nt.hasIncomingRelations ());
      assertFalse (nt.hasOutgoingRelations ());
      // relation already contained
      nf.addOutgoingRelation (r);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      final GraphNode nf = new GraphNode ();
      final GraphNode nt = new GraphNode ();
      final IGraphRelation r = new GraphRelation (nf, nt);
      nt.addIncomingRelation (r);
      assertFalse (nf.hasIncomingRelations ());
      assertFalse (nf.hasOutgoingRelations ());
      assertTrue (nt.hasIncomingRelations ());
      assertFalse (nt.hasOutgoingRelations ());
      // relation already contained
      nt.addIncomingRelation (r);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
