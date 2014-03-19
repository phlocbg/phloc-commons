/**
 * Copyright (C) 2006-2014 phloc systems
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.charset.Charset;

import org.junit.Test;

import com.phloc.commons.CGlobal;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.namespace.MapBasedNamespaceContext;

/**
 * Test class for class {@link XMLWriterSettings}.
 *
 * @author Philip Helger
 */
public final class XMLWriterSettingsTest
{
  private static final boolean [] BOOLS = new boolean [] { true, false };

  @Test
  public void testDefault ()
  {
    IXMLWriterSettings mws = XMLWriterSettings.DEFAULT_XML_SETTINGS;
    assertEquals (EXMLSerializeDocType.EMIT, mws.getSerializeDocType ());
    assertEquals (EXMLSerializeComments.EMIT, mws.getSerializeComments ());
    assertEquals (XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ, mws.getCharsetObj ());
    assertEquals (EXMLSerializeFormat.XML, mws.getFormat ());
    assertEquals (EXMLSerializeIndent.INDENT_AND_ALIGN, mws.getIndent ());
    assertEquals (CCharset.CHARSET_UTF_8_OBJ, mws.getCharsetObj ());
    assertTrue (mws.isSpaceOnSelfClosedElement ());
    assertTrue (mws.isUseDoubleQuotesForAttributes ());
    assertEquals (CGlobal.LINE_SEPARATOR, mws.getNewlineString ());
    assertEquals ("  ", mws.getIndentationString ());
    assertTrue (mws.isEmitNamespaces ());
    assertFalse (mws.isPutNamespaceContextPrefixesInRoot ());

    mws = new XMLWriterSettings ();
    assertEquals (EXMLSerializeDocType.EMIT, mws.getSerializeDocType ());
    assertEquals (EXMLSerializeComments.EMIT, mws.getSerializeComments ());
    assertEquals (XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ, mws.getCharsetObj ());
    assertEquals (EXMLSerializeFormat.XML, mws.getFormat ());
    assertEquals (EXMLSerializeIndent.INDENT_AND_ALIGN, mws.getIndent ());
    assertEquals (CCharset.CHARSET_UTF_8_OBJ, mws.getCharsetObj ());
    assertTrue (mws.isSpaceOnSelfClosedElement ());
    assertTrue (mws.isUseDoubleQuotesForAttributes ());
    assertTrue (mws.isEmitNamespaces ());
    assertFalse (mws.isPutNamespaceContextPrefixesInRoot ());

    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setFormat (EXMLSerializeFormat.HTML));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setSerializeDocType (EXMLSerializeDocType.IGNORE));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setSerializeComments (EXMLSerializeComments.IGNORE));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setIndent (EXMLSerializeIndent.NONE));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setCharset (CCharset.CHARSET_US_ASCII_OBJ));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setNamespaceContext (new MapBasedNamespaceContext ().addMapping ("prefix",
                                                                                                                                                                  "uri")));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setSpaceOnSelfClosedElement (false));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setUseDoubleQuotesForAttributes (false));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setNewlineString ("abc"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setIndentationString ("\t"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setEmitNamespaces (false));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setPutNamespaceContextPrefixesInRoot (true));

    // Now try all permutations
    final XMLWriterSettings aXWS = new XMLWriterSettings ();
    for (final EXMLSerializeDocType eDocType : EXMLSerializeDocType.values ())
    {
      aXWS.setSerializeDocType (eDocType);
      assertEquals (eDocType, aXWS.getSerializeDocType ());
      for (final EXMLSerializeComments eComments : EXMLSerializeComments.values ())
      {
        aXWS.setSerializeComments (eComments);
        assertEquals (eComments, aXWS.getSerializeComments ());
        for (final EXMLSerializeFormat eFormat : EXMLSerializeFormat.values ())
        {
          aXWS.setFormat (eFormat);
          assertEquals (eFormat, aXWS.getFormat ());
          for (final EXMLSerializeIndent eIndent : EXMLSerializeIndent.values ())
          {
            aXWS.setIndent (eIndent);
            assertEquals (eIndent, aXWS.getIndent ());
            for (final EXMLIncorrectCharacterHandling eIncorrectCharHandling : EXMLIncorrectCharacterHandling.values ())
            {
              aXWS.setIncorrectCharacterHandling (eIncorrectCharHandling);
              assertEquals (eIncorrectCharHandling, aXWS.getIncorrectCharacterHandling ());
              for (final Charset aCS : CharsetManager.getAllCharsets ().values ())
              {
                aXWS.setCharset (aCS);
                assertEquals (aCS, aXWS.getCharsetObj ());
                for (final boolean bUseDoubleQuotesForAttributes : BOOLS)
                {
                  aXWS.setUseDoubleQuotesForAttributes (bUseDoubleQuotesForAttributes);
                  assertTrue (bUseDoubleQuotesForAttributes == aXWS.isUseDoubleQuotesForAttributes ());
                  for (final boolean bSpaceOnSelfClosedElement : BOOLS)
                  {
                    aXWS.setSpaceOnSelfClosedElement (bSpaceOnSelfClosedElement);
                    assertTrue (bSpaceOnSelfClosedElement == aXWS.isSpaceOnSelfClosedElement ());
                    for (final String sNewline : new String [] { "\r\n", "\n" })
                    {
                      aXWS.setNewlineString (sNewline);
                      assertEquals (sNewline, aXWS.getNewlineString ());
                      for (final String sIndentation : new String [] { "\t", "  " })
                      {
                        aXWS.setIndentationString (sIndentation);
                        assertEquals (sIndentation, aXWS.getIndentationString ());
                        for (final boolean bEmitNamespaces : BOOLS)
                        {
                          aXWS.setEmitNamespaces (bEmitNamespaces);
                          assertTrue (bEmitNamespaces == aXWS.isEmitNamespaces ());
                          for (final boolean bPutNamespaceContextPrefixesInRoot : BOOLS)
                          {
                            aXWS.setPutNamespaceContextPrefixesInRoot (bPutNamespaceContextPrefixesInRoot);
                            assertTrue (bPutNamespaceContextPrefixesInRoot == aXWS.isPutNamespaceContextPrefixesInRoot ());
                            final XMLWriterSettings aXWS2 = new XMLWriterSettings ().setSerializeDocType (eDocType)
                                                                                    .setSerializeComments (eComments)
                                                                                    .setFormat (eFormat)
                                                                                    .setIndent (eIndent)
                                                                                    .setIncorrectCharacterHandling (eIncorrectCharHandling)
                                                                                    .setCharset (aCS)
                                                                                    .setUseDoubleQuotesForAttributes (bUseDoubleQuotesForAttributes)
                                                                                    .setSpaceOnSelfClosedElement (bSpaceOnSelfClosedElement)
                                                                                    .setNewlineString (sNewline)
                                                                                    .setIndentationString (sIndentation)
                                                                                    .setEmitNamespaces (bEmitNamespaces)
                                                                                    .setPutNamespaceContextPrefixesInRoot (bPutNamespaceContextPrefixesInRoot);
                            PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aXWS, aXWS2);
                          }
                          assertTrue (bEmitNamespaces == aXWS.isEmitNamespaces ());
                        }
                        assertEquals (sIndentation, aXWS.getIndentationString ());
                      }
                      assertEquals (sNewline, aXWS.getNewlineString ());
                    }
                    assertTrue (bSpaceOnSelfClosedElement == aXWS.isSpaceOnSelfClosedElement ());
                  }
                  assertTrue (bUseDoubleQuotesForAttributes == aXWS.isUseDoubleQuotesForAttributes ());
                }
                assertEquals (aCS, aXWS.getCharsetObj ());
              }
              assertEquals (eIncorrectCharHandling, aXWS.getIncorrectCharacterHandling ());
            }
            assertEquals (eIndent, aXWS.getIndent ());
          }
          assertEquals (eFormat, aXWS.getFormat ());
        }
        assertEquals (eComments, aXWS.getSerializeComments ());
      }
      assertEquals (eDocType, aXWS.getSerializeDocType ());
    }

    try
    {
      new XMLWriterSettings ().setCharset ((Charset) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new XMLWriterSettings ().setFormat (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new XMLWriterSettings ().setIndent (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new XMLWriterSettings ().setSerializeDocType (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new XMLWriterSettings ().setSerializeComments (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
