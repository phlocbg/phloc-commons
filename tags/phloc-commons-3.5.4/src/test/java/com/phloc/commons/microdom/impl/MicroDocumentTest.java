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
package com.phloc.commons.microdom.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.xml.CXML;

/**
 * Test class for class {@link MicroDocument}.
 * 
 * @author philip
 */
public final class MicroDocumentTest
{
  @Test
  public void testNew ()
  {
    final IMicroDocument e = new MicroDocument ();
    assertNotNull (e);
    assertNull (e.getDocType ());
    assertNull (e.getDocumentElement ());
    assertFalse (e.hasParent ());
    assertFalse (e.hasChildren ());
    assertTrue (e.isStandalone ());

    assertTrue (e.isEqualContent (e));
    assertFalse (e.isEqualContent (null));
    assertFalse (e.isEqualContent (new MicroText ("any")));

    assertTrue (e.isEqualContent (e.getClone ()));
    assertTrue (new MicroDocument ().isEqualContent (new MicroDocument ()));
    assertFalse (new MicroDocument ()
                             .isEqualContent (new MicroDocument (new MicroDocumentType ("any", "public", "system"))));

    // Clone with children
    e.appendElement ("root");
    assertTrue (e.isEqualContent (e.getClone ()));
  }

  @Test
  public void testNewWithDocType ()
  {
    final IMicroDocument d = new MicroDocument (new MicroDocumentType ("html", "public ID", "system ID"));
    assertNotNull (d);
    assertNotNull (d.getDocType ());
    assertNull (d.getDocumentElement ());
    assertTrue (d.hasChildren ());
    assertFalse (d.isStandalone ());
    assertEquals (1, d.getChildren ().size ());
  }

  @Test
  public void testAppendToRoot ()
  {
    IMicroDocument d = new MicroDocument ();
    assertNotNull (d.appendElement ("root"));

    try
    {
      // Can only add comments, document types or one element
      d.appendEntityReference ("lt");
      fail ();
    }
    catch (final MicroException ex)
    {}

    try
    {
      // Cannot append a second root element!
      d.appendElement ("root2");
      fail ();
    }
    catch (final MicroException ex)
    {}

    d = new MicroDocument ();
    assertNotNull (d.appendComment ("This is a root comment"));
    assertNotNull (d.appendComment ("Well I forgot something"));
    assertNotNull (d.appendElement ("root"));
    assertNotNull (d.appendComment ("Some more comment after the root element"));
  }

  @Test
  public void testToString ()
  {
    final IMicroDocument d = new MicroDocument ();
    PhlocTestUtils.testToStringImplementation (d);
  }

  @Test
  public void testIsStandalone ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    assertTrue (aDoc.isStandalone ());
    final IMicroElement eRoot = aDoc.appendElement ("root");
    assertTrue (aDoc.isStandalone ());
    eRoot.setAttribute ("any", "Value");
    assertTrue (aDoc.isStandalone ());
    eRoot.setAttribute ("xml:lang", "de");
    assertTrue (aDoc.isStandalone ());
    eRoot.setAttribute ("xmlns:foo", "http://www.phloc.com/foo");
    assertTrue (aDoc.isStandalone ());
    eRoot.setAttribute ("xmlns:xsi", CXML.XML_NS_XSI);
    assertTrue (aDoc.isStandalone ());
    eRoot.setAttribute ("xsi:schemaLocation", "my URL");
    assertFalse (aDoc.isStandalone ());
  }
}
