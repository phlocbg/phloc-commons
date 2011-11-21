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
package com.phloc.commons.xml;

import static org.junit.Assert.assertNotNull;

import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.xml.serialize.XMLWriter;

/**
 * Test class for {@link XMLWriter} for DOM4J objects.
 * 
 * @author philip
 */
public final class FuncTestDOM4J extends AbstractPhlocTestCase
{
  @Test
  public void testMisc ()
  {
    final Document aXML = new DOMDocument ();
    final Element aRoot = aXML.createElement ("rootElement");
    assertNotNull (XMLWriter.getXMLString (aRoot, CCharset.CHARSET_UTF_8));
  }

  @Test
  public void testMisc2 ()
  {
    final DOMDocument aXML = new DOMDocument ();
    aXML.appendChild (new DOMElement ("rootElement"));
    assertNotNull (XMLWriter.getXMLString (aXML, CCharset.CHARSET_UTF_8));
  }
}
