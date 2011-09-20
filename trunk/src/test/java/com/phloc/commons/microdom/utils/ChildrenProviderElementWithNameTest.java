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
package com.phloc.commons.microdom.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroFactory;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link ChildrenProviderElementWithName}.
 * 
 * @author philip
 */
public final class ChildrenProviderElementWithNameTest extends AbstractPhlocTestCase
{
  private static IMicroDocument _buildTestDoc ()
  {
    final IMicroDocument aDoc = MicroFactory.newDocument ();
    final IMicroElement eRoot = aDoc.appendElement ("root");
    eRoot.appendElement ("any");
    eRoot.appendText ("Text");
    eRoot.appendElement ("else");
    eRoot.appendElement ("namespace", "any");
    return aDoc;
  }

  @Test
  public void testAll ()
  {
    final IMicroDocument aDoc = _buildTestDoc ();
    final IMicroElement aDocElement = aDoc.getDocumentElement ();

    ChildrenProviderElementWithName x = new ChildrenProviderElementWithName ("any");
    assertFalse (x.hasChildren (aDoc));
    assertEquals (0, x.getChildCount (aDoc));
    assertTrue (x.getChildren (aDoc).isEmpty ());

    assertTrue (x.hasChildren (aDocElement));
    assertEquals (2, x.getChildCount (aDocElement));
    assertEquals (2, x.getChildren (aDocElement).size ());

    x = new ChildrenProviderElementWithName ("namespace", "any");
    assertFalse (x.hasChildren (aDoc));
    assertEquals (0, x.getChildCount (aDoc));
    assertTrue (x.getChildren (aDoc).isEmpty ());

    assertTrue (x.hasChildren (aDocElement));
    assertEquals (1, x.getChildCount (aDocElement));
    assertEquals (1, x.getChildren (aDocElement).size ());

    try
    {
      new ChildrenProviderElementWithName ("");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
