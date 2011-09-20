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
package com.phloc.commons.microdom.serialize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroFactory;
import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.serialize.EXMLSerializeDocType;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.EXMLSerializeIndent;

/**
 * Test class for class {@link MicroWriter}.
 * 
 * @author philip
 */
public final class MicroWriterTest
{
  private static final String TEST_XML = "<?xml version=\"1.0\"?>"
                                         + "<!DOCTYPE verrryoot>"
                                         + "<verrryoot xmlns=\"sthgelse\">"
                                         + "<!-- arg - a comment -->"
                                         + "<root xmlns=\"myuri\">"
                                         + "<child xmlns=\"www.phloc.com\">"
                                         + "<a:child2 xmlns:a=\"foo\">Value text - no entities!</a:child2>"
                                         + "&lt;entity&gt;<![CDATA[xxx]]></child>"
                                         + "</root>"
                                         + "<?target value?>"
                                         + "</verrryoot>";

  private static void _testGetNodeAsXHTMLString (final IMicroNode aNode)
  {
    // try all permutations
    final MicroWriterSettings aSettings = new MicroWriterSettings ().setFormat (EXMLSerializeFormat.HTML);
    for (int nCharSet = 0; nCharSet < 2; ++nCharSet)
    {
      aSettings.setCharset (nCharSet == 1 ? CCharset.CHARSET_ISO_8859_1 : CCharset.CHARSET_UTF_8);
      for (final EXMLSerializeIndent eIndent : EXMLSerializeIndent.values ())
      {
        aSettings.setIndent (eIndent);
        for (final EXMLSerializeDocType eDocType : EXMLSerializeDocType.values ())
        {
          aSettings.setSerializeDocType (eDocType);
          assertNotNull (MicroWriter.getNodeAsString (aNode, aSettings));
        }
      }
    }
  }

  @Test
  public void testGetXHTMLString ()
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (TEST_XML);
    _testGetNodeAsXHTMLString (aDoc);
    _testGetNodeAsXHTMLString (aDoc.getDocumentElement ());
    _testGetNodeAsXHTMLString (MicroFactory.newDocument ());
    _testGetNodeAsXHTMLString (MicroFactory.newElement ("xyz"));
    _testGetNodeAsXHTMLString (MicroFactory.newContainer ());
    _testGetNodeAsXHTMLString (MicroFactory.newComment ("useless"));
    _testGetNodeAsXHTMLString (MicroFactory.newEntityReference ("xyz"));
    try
    {
      _testGetNodeAsXHTMLString (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  private static void _testGetNodeAsXMLString (final IMicroNode aNode)
  {
    // try all permutations
    final MicroWriterSettings aSettings = new MicroWriterSettings ();
    for (int nCharSet = 0; nCharSet < 2; ++nCharSet)
    {
      aSettings.setCharset (nCharSet == 1 ? CCharset.CHARSET_ISO_8859_1 : CCharset.CHARSET_UTF_8);
      for (final EXMLSerializeIndent eIndent : EXMLSerializeIndent.values ())
      {
        aSettings.setIndent (eIndent);
        for (final EXMLSerializeDocType eDocType : EXMLSerializeDocType.values ())
        {
          aSettings.setSerializeDocType (eDocType);
          assertNotNull (MicroWriter.getNodeAsString (aNode, aSettings));
        }
      }
    }
  }

  @Test
  public void testGetXMLString ()
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (TEST_XML);
    _testGetNodeAsXMLString (aDoc);
    _testGetNodeAsXMLString (aDoc.getDocumentElement ());
    _testGetNodeAsXMLString (MicroFactory.newElement ("xyz"));
    _testGetNodeAsXMLString (MicroFactory.newContainer ());
    _testGetNodeAsXMLString (MicroFactory.newComment ("useless"));
    _testGetNodeAsXMLString (MicroFactory.newEntityReference ("xyz"));
    _testGetNodeAsXMLString (MicroFactory.newCDATA ("xyz"));
    try
    {
      _testGetNodeAsXMLString (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      MicroWriter.getNodeAsString (null, MicroWriterSettings.DEFAULT_XML_SETTINGS);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      MicroWriter.getNodeAsString (aDoc, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    // Illegal charset
    assertNull (MicroWriter.getNodeAsString (aDoc, new MicroWriterSettings ().setCharset ("sichaNed")));
  }

  @Test
  public void testSaveToStream ()
  {
    try
    {
      MicroWriter.saveToStream (null, new NonBlockingByteArrayOutputStream (), MicroWriterSettings.DEFAULT_XML_SETTINGS);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      MicroWriter.saveToStream (MicroFactory.newDocument (), null, MicroWriterSettings.DEFAULT_XML_SETTINGS);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      MicroWriter.saveToStream (MicroFactory.newDocument (), new NonBlockingByteArrayOutputStream (), null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testXMLVersion ()
  {
    for (final EXMLVersion eVersion : EXMLVersion.values ())
    {
      final IMicroDocument aDoc = MicroReader.readMicroXML (TEST_XML);
      final MicroWriterSettings aSettings = new MicroWriterSettings ();
      aSettings.setXMLVersion (eVersion);
      final String sXML = MicroWriter.getNodeAsString (aDoc, aSettings);
      assertNotNull (sXML);
      assertTrue (sXML.contains ("version=\"" + eVersion.getVersion () + "\""));
    }
  }

  @Test
  public void testNestedCDATASections ()
  {
    final MicroWriterSettings aSettings = new MicroWriterSettings ().setIndent (EXMLSerializeIndent.NONE);

    // Containing the forbidden CDATA end marker
    IMicroElement e = MicroFactory.newElement ("a");
    e.appendCDATA ("a]]>b");
    assertEquals ("<a><![CDATA[a]]>]]&gt;<![CDATA[b]]></a>", MicroWriter.getNodeAsString (e, aSettings));

    // Containing more than one forbidden CDATA end marker
    e = MicroFactory.newElement ("a");
    e.appendCDATA ("a]]>b]]>c");
    assertEquals ("<a><![CDATA[a]]>]]&gt;<![CDATA[b]]>]]&gt;<![CDATA[c]]></a>",
                  MicroWriter.getNodeAsString (e, aSettings));

    // Containing a complete CDATA section
    e = MicroFactory.newElement ("a");
    e.appendCDATA ("a<![CDATA[x]]>b");
    assertEquals ("<a><![CDATA[a<![CDATA[x]]>]]&gt;<![CDATA[b]]></a>", MicroWriter.getNodeAsString (e, aSettings));
  }
}
