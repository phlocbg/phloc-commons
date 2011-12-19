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

import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MicroText}.
 * 
 * @author philip
 */
public final class MicroTextTest
{
  @Test
  public void testCreation ()
  {
    assertNotNull (new MicroText (null));
    assertNotNull (new MicroText (""));

    IMicroText e = new MicroText ("xyz");
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

    e = new MicroText ("xyz");
    assertNotNull (e);
    assertTrue (e.isEqualContent (e.getClone ()));
    e = new MicroText (null);
    assertNotNull (e);
    assertTrue (e.isEqualContent (e.getClone ()));

    assertTrue (e.isEqualContent (e));
    assertFalse (e.isEqualContent (null));
    assertFalse (e.isEqualContent (new MicroDocument ()));

    assertTrue (new MicroText ("xyz").isEqualContent (new MicroText ("xyz")));
    assertFalse (new MicroText ("xyz").isEqualContent (new MicroText ("xy")));
  }

  @Test
  public void testAddChildren ()
  {
    final IMicroText e = new MicroText ("xyz");

    try
    {
      // Cannot add any child to a comment
      e.appendChild (new MicroText ("other"));
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // Cannot add any child to a comment
      e.insertAfter (new MicroText ("other"), new MicroText ("comment"));
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // Cannot add any child to a comment
      e.insertBefore (new MicroText ("other"), new MicroText ("comment"));
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // Cannot add any child to this node
      e.insertAtIndex (0, new MicroCDATA ("other"));
      fail ();
    }
    catch (final MicroException ex)
    {}
  }
}
