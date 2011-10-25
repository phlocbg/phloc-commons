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

import com.phloc.commons.microdom.IMicroDocumentType;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MicroDocumentType}.
 * 
 * @author philip
 */
public final class MicroDocumentTypeTest
{
  @Test
  public void testAll ()
  {
    final IMicroDocumentType e = MicroFactory.newDocumentType ("qname", "pid", "sid");
    assertNotNull (e);
    assertEquals ("qname", e.getQualifiedName ());
    assertEquals ("pid", e.getPublicID ());
    assertEquals ("sid", e.getSystemID ());
    assertFalse (e.hasParent ());
    assertFalse (e.hasChildren ());
    assertNull (e.getFirstChild ());
    assertNull (e.getLastChild ());
    assertNull (e.getChildAtIndex (0));
    assertEquals (0, e.getChildCount ());
    assertNotNull (e.getNodeName ());
    assertNotNull (e.getNodeValue ());
    PhlocTestUtils.testToStringImplementation (e);

    assertFalse (e.isEqualContent (null));
    assertTrue (e.isEqualContent (e));
    assertTrue (e.isEqualContent (e.getClone ()));
    assertTrue (MicroFactory.newDocumentType ("qname", "pid", "sid")
                            .isEqualContent (MicroFactory.newDocumentType ("qname", "pid", "sid")));
    assertTrue (MicroFactory.newDocumentType ("qname", null, null)
                            .isEqualContent (MicroFactory.newDocumentType ("qname", null, null)));
    assertFalse (MicroFactory.newDocumentType ("qname", "pid", "sid")
                             .isEqualContent (MicroFactory.newDocumentType ("qname", "pid", "sid2")));
    assertFalse (MicroFactory.newDocumentType ("qname", "pid", "sid")
                             .isEqualContent (MicroFactory.newDocumentType ("qname", "pid", null)));
    assertFalse (MicroFactory.newDocumentType ("qname", "pid", "sid")
                             .isEqualContent (MicroFactory.newDocumentType ("qname", "pid2", "sid")));
    assertFalse (MicroFactory.newDocumentType ("qname", "pid", "sid")
                             .isEqualContent (MicroFactory.newDocumentType ("qname", null, "sid")));
    assertFalse (MicroFactory.newDocumentType ("qname", "pid", "sid")
                             .isEqualContent (MicroFactory.newDocumentType ("qname", null, null)));
    assertFalse (MicroFactory.newDocumentType ("qname", "pid", "sid")
                             .isEqualContent (MicroFactory.newDocumentType ("qname2", "pid", "sid")));

    try
    {
      MicroFactory.newDocumentType ("", "pid", "sid");
      fail ();
    }
    catch (final IllegalArgumentException ex)
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
