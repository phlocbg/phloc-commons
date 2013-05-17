/**
 * Copyright (C) 2006-2013 phloc systems
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.phloc.commons.CGlobal;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.streams.StringInputStream;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.name.MockHasDisplayName;
import com.phloc.commons.xml.XMLFactory;

/**
 * Test class for class {@link MicroUtils}.
 * 
 * @author Philip Helger
 */
public final class MicroUtilsTest
{
  @SuppressWarnings ("deprecation")
  @Test
  public void testNl2Br ()
  {
    assertNull (MicroUtils.nl2br (null));
    List <IMicroNode> aList = MicroUtils.nl2br ("");
    assertNotNull (aList);
    assertEquals (0, aList.size ());
    aList = MicroUtils.nl2br ("abc");
    assertEquals (1, aList.size ());
    aList = MicroUtils.nl2br ("a\nc");
    assertEquals (3, aList.size ());
    assertTrue (aList.get (0) instanceof IMicroText);
    assertTrue (aList.get (1) instanceof IMicroElement);
    assertTrue (aList.get (2) instanceof IMicroText);
    aList = MicroUtils.nl2br ("a\n");
    assertEquals (2, aList.size ());
    assertTrue (aList.get (0) instanceof IMicroText);
    assertTrue (aList.get (1) instanceof IMicroElement);
    aList = MicroUtils.nl2br ("\na");
    assertEquals (2, aList.size ());
    assertTrue (aList.get (0) instanceof IMicroElement);
    assertTrue (aList.get (1) instanceof IMicroText);
    aList = MicroUtils.nl2br ("\n\n");
    assertEquals (2, aList.size ());
    assertTrue (aList.get (0) instanceof IMicroElement);
    assertTrue (aList.get (1) instanceof IMicroElement);
    aList = MicroUtils.nl2br ("\n\na\n\n");
    assertEquals (5, aList.size ());
  }

  @Test
  public void testAppend ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement ("root");
    MicroUtils.append (eRoot, "Any text");
    MicroUtils.append (eRoot, new MicroElement ("child"));
    MicroUtils.append (eRoot, ContainerHelper.newList ("t1", "t2"));
    MicroUtils.append (eRoot, ArrayHelper.newArray ("t1", "t2"));
    try
    {
      MicroUtils.append (null, "any");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      MicroUtils.append (eRoot, new MockHasDisplayName (5));
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testGetPath ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    assertEquals ("#document", MicroUtils.getPath (aDoc, "/"));
    final IMicroElement eRoot = aDoc.appendElement ("root");
    assertEquals ("#document/root", MicroUtils.getPath (eRoot, "/"));
    final IMicroElement eChild = eRoot.appendElement ("child");
    assertEquals ("#document/root/child", MicroUtils.getPath (eChild, "/"));
    assertEquals ("", MicroUtils.getPath (null, "/"));
    try
    {
      MicroUtils.getPath (eChild, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGetDocumentRootElementTagName ()
  {
    assertNull (MicroUtils.getDocumentRootElementTagName (null));
    final IMicroDocument aDoc = new MicroDocument ();
    assertNull (MicroUtils.getDocumentRootElementTagName (aDoc));
    aDoc.appendElement ("root");
    assertEquals ("root", MicroUtils.getDocumentRootElementTagName (aDoc));
  }

  @Test
  public void testConvertToMicroNode () throws SAXException, IOException, ParserConfigurationException
  {
    final String sXML = "<?xml version='1.0'?>"
                        + "<!DOCTYPE root [ <!ENTITY sc \"sc.exe\"> <!ELEMENT root (child, child2)> <!ELEMENT child (#PCDATA)> <!ELEMENT child2 (#PCDATA)> ]>"
                        + "<root attr='value'>"
                        + "<![CDATA[hihi]]>"
                        + "text"
                        + "&sc;"
                        + "<child xmlns='http://myns' a='b' />"
                        + "<child2 />"
                        + "<!-- comment -->"
                        + "<?stylesheet x y z?>"
                        + "</root>";
    final DocumentBuilderFactory aDBF = XMLFactory.createDefaultDocumentBuilderFactory ();
    aDBF.setCoalescing (false);
    aDBF.setIgnoringComments (false);
    final Document doc = aDBF.newDocumentBuilder ()
                             .parse (new StringInputStream (sXML, CCharset.CHARSET_ISO_8859_1_OBJ));
    assertNotNull (doc);
    final IMicroNode aNode = MicroUtils.convertToMicroNode (doc);
    assertNotNull (aNode);
    try
    {
      MicroUtils.convertToMicroNode (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGetChildTextContent ()
  {
    final IMicroElement e = new MicroElement ("x");
    assertNull (MicroUtils.getChildTextContent (e, "y"));
    final IMicroElement y = e.appendElement ("y");
    assertNull (MicroUtils.getChildTextContent (e, "y"));
    y.appendText ("Text");
    assertEquals ("Text", MicroUtils.getChildTextContent (e, "y"));
    y.appendElement ("z1");
    assertEquals ("Text", MicroUtils.getChildTextContent (e, "y"));
    y.appendCDATA ("data");
    assertEquals ("Textdata", MicroUtils.getChildTextContent (e, "y"));
  }

  @Test
  public void testGetChildTextContentWithConversion ()
  {
    final IMicroElement e = new MicroElement ("x");
    assertNull (MicroUtils.getChildTextContentWithConversion (e, "y", BigInteger.class));
    final IMicroElement y = e.appendElement ("y");
    assertNull (MicroUtils.getChildTextContentWithConversion (e, "y", BigInteger.class));
    y.appendText ("100");
    assertEquals (CGlobal.BIGINT_100, MicroUtils.getChildTextContentWithConversion (e, "y", BigInteger.class));
    y.appendElement ("1");
    assertEquals (CGlobal.BIGINT_100, MicroUtils.getChildTextContentWithConversion (e, "y", BigInteger.class));
    y.appendCDATA ("234");
    assertEquals (BigInteger.valueOf (100234), MicroUtils.getChildTextContentWithConversion (e, "y", BigInteger.class));
  }

  @Test
  public void testGetChildTextContentWithNS ()
  {
    final String sNSURI = "my-namespace-uri";
    final IMicroElement e = new MicroElement (sNSURI, "x");
    assertNull (MicroUtils.getChildTextContent (e, sNSURI, "y"));
    final IMicroElement y = e.appendElement (sNSURI, "y");
    assertNull (MicroUtils.getChildTextContent (e, sNSURI, "y"));
    y.appendText ("Text");
    assertEquals ("Text", MicroUtils.getChildTextContent (e, sNSURI, "y"));
    y.appendElement ("z1");
    assertEquals ("Text", MicroUtils.getChildTextContent (e, sNSURI, "y"));
    y.appendCDATA ("data");
    assertEquals ("Textdata", MicroUtils.getChildTextContent (e, sNSURI, "y"));
  }

  @Test
  public void testGetChildTextContentWithConversionAndNS ()
  {
    final String sNSURI = "my-namespace-uri";
    final IMicroElement e = new MicroElement (sNSURI, "x");
    assertNull (MicroUtils.getChildTextContentWithConversion (e, sNSURI, "y", BigInteger.class));
    final IMicroElement y = e.appendElement (sNSURI, "y");
    assertNull (MicroUtils.getChildTextContentWithConversion (e, sNSURI, "y", BigInteger.class));
    y.appendText ("100");
    assertEquals (CGlobal.BIGINT_100, MicroUtils.getChildTextContentWithConversion (e, sNSURI, "y", BigInteger.class));
    y.appendElement ("1");
    assertEquals (CGlobal.BIGINT_100, MicroUtils.getChildTextContentWithConversion (e, sNSURI, "y", BigInteger.class));
    y.appendCDATA ("234");
    assertEquals (BigInteger.valueOf (100234),
                  MicroUtils.getChildTextContentWithConversion (e, sNSURI, "y", BigInteger.class));
  }
}
