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

import com.phloc.commons.microdom.IMicroProcessingInstruction;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MicroProcessingInstruction}.
 *
 * @author philip
 */
public final class MicroProcessingInstructionTest
{
  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testCreation ()
  {
    IMicroProcessingInstruction e = MicroFactory.newProcessingInstruction ("xyz");
    assertNotNull (e);
    assertEquals ("xyz", e.getTarget ());
    assertNull (e.getData ());
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

    e = MicroFactory.newProcessingInstruction ("xyz", "data");
    assertNotNull (e);
    assertEquals ("xyz", e.getTarget ());
    assertEquals ("data", e.getData ());
    assertFalse (e.hasChildren ());
    assertNull (e.getChildren ());

    e = MicroFactory.newProcessingInstruction ("targ");
    assertNotNull (e);
    assertTrue (e.isEqualContent (e.getClone ()));
    e = MicroFactory.newProcessingInstruction ("targ", "data");
    assertNotNull (e);
    assertTrue (e.isEqualContent (e.getClone ()));

    assertTrue (e.isEqualContent (e));
    assertFalse (e.isEqualContent (null));
    assertFalse (e.isEqualContent (MicroFactory.newDocument ()));

    assertTrue (MicroFactory.newProcessingInstruction ("xyz")
                            .isEqualContent (MicroFactory.newProcessingInstruction ("xyz")));
    assertTrue (MicroFactory.newProcessingInstruction ("xyz", "data")
                            .isEqualContent (MicroFactory.newProcessingInstruction ("xyz", "data")));
    assertFalse (MicroFactory.newProcessingInstruction ("xyz")
                             .isEqualContent (MicroFactory.newProcessingInstruction ("xy")));
    assertFalse (MicroFactory.newProcessingInstruction ("xyz", "data")
                             .isEqualContent (MicroFactory.newProcessingInstruction ("xyz", null)));
    assertFalse (MicroFactory.newProcessingInstruction ("xyz", "data")
                             .isEqualContent (MicroFactory.newProcessingInstruction ("xyz", "dat")));

    try
    {
      MicroFactory.newProcessingInstruction (null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      MicroFactory.newProcessingInstruction ("");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testAddChildren ()
  {
    final IMicroProcessingInstruction e = MicroFactory.newProcessingInstruction ("xyz");

    try
    {
      // Cannot add any child to a comment
      e.appendChild (MicroFactory.newProcessingInstruction ("other"));
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // Cannot add any child to a comment
      e.insertAfter (MicroFactory.newProcessingInstruction ("other"), MicroFactory.newProcessingInstruction ("comment"));
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // Cannot add any child to a comment
      e.insertBefore (MicroFactory.newProcessingInstruction ("other"),
                      MicroFactory.newProcessingInstruction ("comment"));
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
