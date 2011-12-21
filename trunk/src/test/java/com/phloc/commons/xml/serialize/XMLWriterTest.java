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
package com.phloc.commons.xml.serialize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.phloc.commons.CGlobal;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.xml.DefaultXMLIterationHandler;
import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.XMLFactory;
import com.phloc.commons.xml.XMLHelper;
import com.phloc.commons.xml.namespace.MapBasedNamespaceContext;
import com.phloc.commons.xml.transform.StringStreamResult;
import com.phloc.commons.xml.transform.XMLTransformerFactory;

/**
 * Test class for {@link XMLWriter}
 * 
 * @author philip
 */
public final class XMLWriterTest extends AbstractPhlocTestCase
{
  private static final String DOCTYPE_XHTML10_QNAME = "-//W3C//DTD XHTML 1.0 Strict//EN";
  private static final String DOCTYPE_XHTML10_URI = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";

  /**
   * Test the method getXHTMLString
   */
  @SuppressWarnings ("deprecation")
  @Test
  public void testGetXHTMLString ()
  {
    final String sCRLF = CGlobal.LINE_SEPARATOR;
    final String sSPACER = " ";
    final String sINDENT = "  ";
    final String sTAGNAME = "notext";

    // Java 1.6 JAXP handles things differently
    final String sSerTagName = "<" + sTAGNAME + "></" + sTAGNAME + ">";

    final Document doc = XMLFactory.newDocument ("html", DOCTYPE_XHTML10_QNAME, DOCTYPE_XHTML10_URI);
    final Element aHead = (Element) doc.getDocumentElement ().appendChild (doc.createElement ("head"));
    aHead.appendChild (doc.createTextNode ("Hallo"));
    final Element aNoText = (Element) doc.getDocumentElement ().appendChild (doc.createElement (sTAGNAME));
    aNoText.appendChild (doc.createTextNode (""));

    // test including doc type
    {
      final String sResult = XMLWriter.getXHTMLString (doc,
                                                       EXMLSerializeDocType.EMIT,
                                                       EXMLSerializeIndent.INDENT_AND_ALIGN,
                                                       CCharset.CHARSET_UTF_8);
      assertEquals ("<!DOCTYPE html PUBLIC \"" +
                    DOCTYPE_XHTML10_QNAME +
                    "\"" +
                    sSPACER +
                    "\"" +
                    DOCTYPE_XHTML10_URI +
                    "\">" +
                    sCRLF +
                    "<html xmlns=\"" +
                    DOCTYPE_XHTML10_URI +
                    "\">" +
                    sCRLF +
                    sINDENT +
                    "<head>Hallo</head>" +
                    sCRLF +
                    sINDENT +
                    sSerTagName +
                    sCRLF +
                    "</html>" +
                    sCRLF, sResult);
      assertEquals (sResult, XMLWriter.getXHTMLString (doc, CCharset.CHARSET_UTF_8));
    }

    // test without doc type
    {
      final String sResult = XMLWriter.getXHTMLString (doc,
                                                       EXMLSerializeDocType.IGNORE,
                                                       EXMLSerializeIndent.INDENT_AND_ALIGN,
                                                       CCharset.CHARSET_UTF_8);
      assertEquals ("<html xmlns=\"" +
                    DOCTYPE_XHTML10_URI +
                    "\">" +
                    sCRLF +
                    sINDENT +
                    "<head>Hallo</head>" +
                    sCRLF +
                    sINDENT +
                    sSerTagName +
                    sCRLF +
                    "</html>" +
                    sCRLF, sResult);
    }

    {
      final String sResult = XMLWriter.getXHTMLString (doc,
                                                       EXMLSerializeDocType.IGNORE,
                                                       EXMLSerializeIndent.NONE,
                                                       CCharset.CHARSET_UTF_8);
      assertEquals ("<html xmlns=\"" + DOCTYPE_XHTML10_URI + "\"><head>Hallo</head>" + sSerTagName + "</html>", sResult);
      assertEquals (sResult, XMLWriter.getXHTMLString (doc,
                                                       EXMLSerializeDocType.IGNORE,
                                                       EXMLSerializeIndent.NONE,
                                                       CCharset.CHARSET_UTF_8));
    }

    // add text element
    aNoText.appendChild (doc.createTextNode ("Hallo "));
    final Element b = (Element) aNoText.appendChild (doc.createElement ("strong"));
    b.appendChild (doc.createTextNode ("Welt"));
    aNoText.appendChild (doc.createCDATASection ("!!!"));
    aNoText.appendChild (doc.createComment ("No"));

    // test including doc type
    {
      final String sResult = XMLWriter.getXHTMLString (doc,
                                                       EXMLSerializeDocType.IGNORE,
                                                       EXMLSerializeIndent.INDENT_AND_ALIGN,
                                                       CCharset.CHARSET_UTF_8);
      assertEquals ("<html xmlns=\"" +
                    DOCTYPE_XHTML10_URI +
                    "\">" +
                    sCRLF +
                    sINDENT +
                    "<head>Hallo</head>" +
                    sCRLF +
                    sINDENT +
                    "<notext>Hallo <strong>Welt</strong><![CDATA[!!!]]><!--No--></notext>" +
                    sCRLF +
                    "</html>" +
                    sCRLF, sResult);
    }

    // test as XML (with doc type and indent)
    {
      final String sResult = XMLWriter.getXMLString (doc, CCharset.CHARSET_UTF_8);
      assertEquals ("<?xml version=\"1.0\" encoding=\"" +
                    CCharset.CHARSET_UTF_8 +
                    "\" standalone=\"yes\"?>" +
                    sCRLF +
                    "<!DOCTYPE html PUBLIC \"" +
                    DOCTYPE_XHTML10_QNAME +
                    "\"" +
                    sSPACER +
                    "\"" +
                    DOCTYPE_XHTML10_URI +
                    "\">" +
                    sCRLF +
                    "<html xmlns=\"" +
                    DOCTYPE_XHTML10_URI +
                    "\">" +
                    sCRLF +
                    sINDENT +
                    "<head>Hallo</head>" +
                    sCRLF +
                    sINDENT +
                    "<notext>Hallo <strong>Welt</strong><![CDATA[!!!]]><!--No--></notext>" +
                    sCRLF +
                    "</html>" +
                    sCRLF, sResult);
    }

    // test as XML (without doc type and comments but indented)
    {
      final String sResult = XMLWriter.getXMLString (doc,
                                                     EXMLSerializeDocType.IGNORE,
                                                     EXMLSerializeComments.IGNORE,
                                                     EXMLSerializeIndent.INDENT_AND_ALIGN,
                                                     CCharset.CHARSET_UTF_8);
      assertEquals ("<?xml version=\"1.0\" encoding=\"" +
                    CCharset.CHARSET_UTF_8 +
                    "\" standalone=\"yes\"?>" +
                    sCRLF +
                    "<html xmlns=\"" +
                    DOCTYPE_XHTML10_URI +
                    "\">" +
                    sCRLF +
                    sINDENT +
                    "<head>Hallo</head>" +
                    sCRLF +
                    sINDENT +
                    "<notext>Hallo <strong>Welt</strong><![CDATA[!!!]]></notext>" +
                    sCRLF +
                    "</html>" +
                    sCRLF, sResult);
    }

    assertTrue (XMLWriter.writeXMLToStream (doc, new NonBlockingByteArrayOutputStream (), CCharset.CHARSET_ISO_8859_1)
                         .isSuccess ());
    new XMLSerializerPhloc (CCharset.CHARSET_ISO_8859_1).write (doc, new DefaultXMLIterationHandler ());

    try
    {
      // null node not allowed
      XMLWriter.getXHTMLString (null, CCharset.CHARSET_UTF_8);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // null charset not allowed
      XMLWriter.getXHTMLString (doc, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testNestedCDATAs ()
  {
    final Document doc = XMLFactory.newDocument ();

    // Containing the forbidden CDATA end marker
    Element e = doc.createElement ("a");
    e.appendChild (doc.createCDATASection ("a]]>b"));
    assertEquals ("<a><![CDATA[a]]>]]&gt;<![CDATA[b]]></a>" + CGlobal.LINE_SEPARATOR, XMLWriter.getXMLString (e));

    // Containing more than one forbidden CDATA end marker
    e = doc.createElement ("a");
    e.appendChild (doc.createCDATASection ("a]]>b]]>c"));
    assertEquals ("<a><![CDATA[a]]>]]&gt;<![CDATA[b]]>]]&gt;<![CDATA[c]]></a>" + CGlobal.LINE_SEPARATOR,
                  XMLWriter.getXMLString (e));

    // Containing a complete CDATA section
    e = doc.createElement ("a");
    e.appendChild (doc.createCDATASection ("a<![CDATA[x]]>b"));
    assertEquals ("<a><![CDATA[a<![CDATA[x]]>]]&gt;<![CDATA[b]]></a>" + CGlobal.LINE_SEPARATOR,
                  XMLWriter.getXMLString (e));
  }

  @Test
  public void testWithNamespaceContext ()
  {
    final Document aDoc = XMLFactory.newDocument ();
    final Element eRoot = (Element) aDoc.appendChild (aDoc.createElementNS ("ns1url", "root"));
    eRoot.appendChild (aDoc.createElementNS ("ns2url", "child1"));
    eRoot.appendChild (aDoc.createElementNS ("ns2url", "child2"));

    final XMLWriterSettings aSettings = new XMLWriterSettings ().setCharset (CCharset.CHARSET_ISO_8859_1)
                                                                .setIndent (EXMLSerializeIndent.NONE);
    String s = XMLWriter.getNodeAsString (aDoc, aSettings);
    assertEquals ("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>" +
                      CGlobal.LINE_SEPARATOR +
                      "<root xmlns=\"ns1url\"><ns0:child1 xmlns:ns0=\"ns2url\" /><ns0:child2 xmlns:ns0=\"ns2url\" /></root>",
                  s);

    final MapBasedNamespaceContext aCtx = new MapBasedNamespaceContext ();
    aCtx.addMapping ("a", "ns1url");
    aSettings.setNamespaceContext (aCtx);
    s = XMLWriter.getNodeAsString (aDoc, aSettings);
    assertEquals ("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>" +
                      CGlobal.LINE_SEPARATOR +
                      "<a:root xmlns:a=\"ns1url\"><ns0:child1 xmlns:ns0=\"ns2url\" /><ns0:child2 xmlns:ns0=\"ns2url\" /></a:root>",
                  s);

    aCtx.addMapping ("xy", "ns2url");
    s = XMLWriter.getNodeAsString (aDoc, aSettings);
    assertEquals ("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>" +
                      CGlobal.LINE_SEPARATOR +
                      "<a:root xmlns:a=\"ns1url\"><xy:child1 xmlns:xy=\"ns2url\" /><xy:child2 xmlns:xy=\"ns2url\" /></a:root>",
                  s);
  }

  @Test
  public void testXMLVersionNumber ()
  {
    Document aDoc = XMLFactory.newDocument (EXMLVersion.XML_10);
    aDoc.appendChild (aDoc.createElement ("any"));
    String sXML = XMLWriter.getXMLString (aDoc);
    assertEquals ("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                  CGlobal.LINE_SEPARATOR +
                  "<any />" +
                  CGlobal.LINE_SEPARATOR, sXML);

    aDoc = XMLFactory.newDocument (EXMLVersion.XML_11);
    aDoc.appendChild (aDoc.createElement ("any"));
    sXML = XMLWriter.getXMLString (aDoc);
    assertEquals ("<?xml version=\"1.1\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                  CGlobal.LINE_SEPARATOR +
                  "<any />" +
                  CGlobal.LINE_SEPARATOR, sXML);
  }

  @Test
  public void testNumericReferencesXML10 () throws SAXException, TransformerException
  {
    for (char i = Character.MIN_VALUE; i < Character.MAX_VALUE; ++i)
      if (!XMLHelper.isInvalidXMLCharacter (i))
      {
        final String sText = "abc" + i + "def";
        final Document aDoc = XMLFactory.newDocument (EXMLVersion.XML_11);
        final Element eRoot = (Element) aDoc.appendChild (aDoc.createElement ("root"));
        eRoot.appendChild (aDoc.createTextNode (sText));

        // Use regular transformer
        final Transformer aTransformer = XMLTransformerFactory.newTransformer ();
        aTransformer.setOutputProperty (OutputKeys.ENCODING, CCharset.CHARSET_UTF_8);
        aTransformer.setOutputProperty (OutputKeys.INDENT, "yes");
        aTransformer.setOutputProperty (OutputKeys.VERSION, EXMLVersion.XML_10.getVersion ());
        final StringStreamResult aRes = new StringStreamResult ();
        aTransformer.transform (new DOMSource (aDoc), aRes);

        final String sXML = aRes.getAsString ();
        final Document aDoc2 = XMLReader.readXMLDOM (sXML);
        assertNotNull (aDoc2);
      }
  }

  @Test
  public void testNumericReferencesXML11 () throws SAXException, TransformerException
  {
    for (char i = Character.MIN_VALUE; i < Character.MAX_VALUE; ++i)
      if (!XMLHelper.isInvalidXMLCharacter (i))
      {
        final String sText = "abc" + i + "def";
        final Document aDoc = XMLFactory.newDocument (EXMLVersion.XML_11);
        final Element eRoot = (Element) aDoc.appendChild (aDoc.createElement ("root"));
        eRoot.appendChild (aDoc.createTextNode (sText));

        final Transformer aTransformer = XMLTransformerFactory.newTransformer ();
        aTransformer.setOutputProperty (OutputKeys.ENCODING, CCharset.CHARSET_UTF_8);
        aTransformer.setOutputProperty (OutputKeys.INDENT, "no");
        aTransformer.setOutputProperty (OutputKeys.VERSION, EXMLVersion.XML_11.getVersion ());
        final StringStreamResult aRes = new StringStreamResult ();
        aTransformer.transform (new DOMSource (aDoc), aRes);

        final String sXML = aRes.getAsString ();
        final Document aDoc2 = XMLReader.readXMLDOM (sXML);
        assertNotNull (aDoc2);
      }
  }
}
