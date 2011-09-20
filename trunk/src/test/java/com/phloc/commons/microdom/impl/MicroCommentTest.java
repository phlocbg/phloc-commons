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
package com.phloc.commons.microdom.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroComment;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MicroComment}.
 * 
 * @author philip
 */
public final class MicroCommentTest
{
  @Test
  public void testCreation ()
  {
    assertNotNull (MicroFactory.newComment (null));
    assertNotNull (MicroFactory.newComment (""));

    IMicroComment e = MicroFactory.newComment ("xyz");
    assertNotNull (e);
    assertEquals ("xyz", e.getData ());
    assertFalse (e.hasParent ());
    assertFalse (e.hasChildren ());
    assertNull (e.getChildren ());
    assertNull (e.getFirstChild ());
    assertNull (e.getLastChild ());
    assertNull (e.getChildAtIndex (0));
    assertNull (e.getAllChildrenRecursive ());
    assertEquals (0, e.getChildCount ());
    assertNotNull (e.getNodeName ());
    assertNotNull (e.getNodeValue ());
    PhlocTestUtils.testToStringImplementation (e);

    e.setData ("allo");
    assertEquals ("allo", e.getData ());
    assertFalse (e.hasChildren ());
    assertNull (e.getChildren ());

    e.appendData (" Welt");
    assertEquals ("allo Welt", e.getData ());

    e.prependData ("H");
    assertEquals ("Hallo Welt", e.getData ());

    e = MicroFactory.newComment (null);
    assertNotNull (e);
    assertTrue (e.isEqualContent (e.getClone ()));
    e = MicroFactory.newComment ("xyz");
    assertNotNull (e);
    assertTrue (e.isEqualContent (e.getClone ()));

    assertTrue (e.isEqualContent (e));
    assertFalse (e.isEqualContent (null));
    assertFalse (e.isEqualContent (MicroFactory.newDocument ()));

    assertTrue (MicroFactory.newComment ("xyz").isEqualContent (MicroFactory.newComment ("xyz")));
    assertFalse (MicroFactory.newComment ("xyz").isEqualContent (MicroFactory.newComment ("xy")));
  }

  @Test
  public void testAddChildren ()
  {
    final IMicroComment e = MicroFactory.newComment ("xyz");

    try
    {
      // Cannot add any child to a comment
      e.appendChild (MicroFactory.newComment ("other"));
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // Cannot add any child to a comment
      e.insertAfter (MicroFactory.newComment ("other"), MicroFactory.newComment ("comment"));
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // Cannot add any child to a comment
      e.insertBefore (MicroFactory.newComment ("other"), MicroFactory.newComment ("comment"));
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // No children -> no remove
      e.removeChild (MicroFactory.newContainer ());
      fail ();
    }
    catch (final MicroException ex)
    {}
    try
    {
      // Cannot add any child to this node
      e.insertAtIndex (0, MicroFactory.newCDATA ("other"));
      fail ();
    }
    catch (final MicroException ex)
    {}
  }
}
