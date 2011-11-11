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

import com.phloc.commons.microdom.IMicroEntityReference;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MicroEntityReference}.
 *
 * @author philip
 */
public final class MicroEntityReferenceTest
{
  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testCreation ()
  {
    IMicroEntityReference e = MicroFactory.newEntityReference ("xyz");
    assertNotNull (e);
    assertEquals ("xyz", e.getName ());
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

    assertTrue (e.isEqualContent (e));
    assertFalse (e.isEqualContent (null));
    assertFalse (e.isEqualContent (MicroFactory.newDocument ()));

    e = MicroFactory.newEntityReference ("xyz");
    assertTrue (e.isEqualContent (e.getClone ()));

    assertTrue (MicroFactory.newEntityReference ("xyz").isEqualContent (MicroFactory.newEntityReference ("xyz")));
    assertFalse (MicroFactory.newEntityReference ("xyz").isEqualContent (MicroFactory.newEntityReference ("xy")));

    try
    {
      MicroFactory.newEntityReference (null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      MicroFactory.newEntityReference ("");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testAddChildren ()
  {
    final IMicroEntityReference e = MicroFactory.newEntityReference ("xyz");

    try
    {
      // Cannot add any child to a comment
      e.appendChild (MicroFactory.newEntityReference ("other"));
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // Cannot add any child to a comment
      e.insertAfter (MicroFactory.newEntityReference ("other"), MicroFactory.newEntityReference ("comment"));
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // Cannot add any child to a comment
      e.insertBefore (MicroFactory.newEntityReference ("other"), MicroFactory.newEntityReference ("comment"));
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
