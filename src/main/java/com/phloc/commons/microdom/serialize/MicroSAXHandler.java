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
package com.phloc.commons.microdom.serialize;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;

import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroDocumentType;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.impl.MicroDocumentType;
import com.phloc.commons.string.StringHelper;

/**
 * The SAX handler used by the {@link MicroReader}.
 * 
 * @author philip
 */
final class MicroSAXHandler implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler, LexicalHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MicroSAXHandler.class);

  private IMicroDocument m_aDoc;
  private IMicroDocumentType m_aDocType;
  private IMicroNode m_aParent;
  private boolean m_bDTDMode = false;
  private boolean m_bCDATAMode = false;
  private final boolean m_bSaveIgnorableWhitespaces;
  private final EntityResolver m_aEntityResolver;

  MicroSAXHandler (final boolean bSaveIgnorableWhitespaces, @Nullable final EntityResolver aEntityResolver)
  {
    m_bSaveIgnorableWhitespaces = bSaveIgnorableWhitespaces;
    m_aEntityResolver = aEntityResolver;
  }

  private void _createParentDocument ()
  {
    if (m_aParent == null)
    {
      m_aDoc = new MicroDocument (m_aDocType);
      m_aParent = m_aDoc;
    }
  }

  // Called before startDocument (if called)
  public void setDocumentLocator (final Locator aLocator)
  {}

  public void startDocument ()
  {}

  public void endDocument ()
  {}

  public void startDTD (final String sName, final String sPublicId, final String sSystemId) throws SAXException
  {
    if (m_aDocType == null)
      m_aDocType = new MicroDocumentType (sName, sPublicId, sSystemId);
    else
      s_aLogger.warn ("DocType already present!");
    m_bDTDMode = true;
  }

  public void endDTD () throws SAXException
  {
    m_bDTDMode = false;
  }

  public void startElement (@Nullable final String sURI,
                            @Nonnull final String sLocalName,
                            @Nullable final String sQName,
                            @Nullable final Attributes aAttributes)
  {
    _createParentDocument ();

    IMicroElement aElement;
    if (StringHelper.hasText (sURI))
      aElement = m_aParent.appendElement (sURI, sLocalName);
    else
      aElement = m_aParent.appendElement (sLocalName);

    // copy attributes
    if (aAttributes != null)
    {
      final int nAttrCount = aAttributes.getLength ();
      for (int i = 0; i < nAttrCount; ++i)
        aElement.setAttribute (aAttributes.getQName (i), aAttributes.getValue (i));
    }

    m_aParent = aElement;
  }

  public void endElement (final String sURI, final String sLocalName, final String sQName)
  {
    m_aParent = m_aParent.getParent ();
  }

  public void characters (final char [] aChars, final int nStart, final int nLength)
  {
    final String sText = new String (aChars, nStart, nLength);

    if (m_bCDATAMode)
    {
      // CDATA mode
      m_aParent.appendCDATA (sText);
    }
    else
    {
      // Regular text node
      final IMicroNode aLastChild = m_aParent.getLastChild ();
      if (aLastChild instanceof IMicroText && !(((IMicroText) aLastChild).isElementContentWhitespace ()))
      {
        // Merge directly following text elements to one!
        // This happens when compiling on the command line with JDK 1.6.0_04 in
        // an ReaderXHTMLText in pDAF3!
        ((IMicroText) aLastChild).appendData (sText);
      }
      else
      {
        m_aParent.appendText (sText);
      }
    }
  }

  public void ignorableWhitespace (final char [] aChars, final int nStart, final int nLength)
  {
    if (m_bSaveIgnorableWhitespaces)
    {
      final String sText = new String (aChars, nStart, nLength);

      final IMicroNode aLastChild = m_aParent.getLastChild ();
      if (aLastChild instanceof IMicroText && ((IMicroText) aLastChild).isElementContentWhitespace ())
      {
        // Merge directly following text elements to one!
        // This happens when compiling on the command line with JDK 1.6.0_04 in
        // an ReaderXHTMLText in pDAF3!
        ((IMicroText) aLastChild).appendData (sText);
      }
      else
        m_aParent.appendIgnorableWhitespaceText (sText);
    }
  }

  public void processingInstruction (final String sTarget, final String sData)
  {
    _createParentDocument ();
    m_aParent.appendProcessingInstruction (sTarget, sData);
  }

  @Nullable
  public InputSource resolveEntity (final String sPublicId, final String sSystemId) throws IOException, SAXException
  {
    if (m_aEntityResolver != null)
      return m_aEntityResolver.resolveEntity (sPublicId, sSystemId);

    // If using XHTML this should be replaced by using the LocalEntityResolver
    // instead
    s_aLogger.warn ("Resolve entity '" + sPublicId + "' from '" + sSystemId + "'");
    return null;
  }

  public void unparsedEntityDecl (final String sName,
                                  final String sPublicId,
                                  final String sSystemId,
                                  final String sNotationName)
  {
    s_aLogger.warn ("Unparsed entity decl: " + sName + "--" + sPublicId + "--" + sSystemId + "--" + sNotationName);
  }

  public void notationDecl (final String sName, final String sPublicId, final String sSystemId) throws SAXException
  {
    s_aLogger.warn ("Unparsed notation decl: " + sName + "--" + sPublicId + "--" + sSystemId);
  }

  public void skippedEntity (final String sName)
  {
    s_aLogger.warn ("Skipped entity: " + sName);
  }

  // For namespace handling
  public void startPrefixMapping (final String sPrefix, final String sURI) throws SAXException
  {}

  // for namespace handling
  public void endPrefixMapping (final String sPrefix) throws SAXException
  {}

  private static String _getMsg (final SAXParseException ex)
  {
    return "[SAX] [" + ex.getLineNumber () + ":" + ex.getColumnNumber () + "] " + ex.getMessage ();
  }

  public void warning (final SAXParseException ex)
  {
    s_aLogger.warn (_getMsg (ex));
  }

  public void error (final SAXParseException ex)
  {
    s_aLogger.error (_getMsg (ex));
  }

  public void fatalError (final SAXParseException ex)
  {
    s_aLogger.error (_getMsg (ex));
  }

  public void startEntity (final String sName) throws SAXException
  {}

  public void endEntity (final String sName) throws SAXException
  {}

  public void startCDATA () throws SAXException
  {
    // Begin of CDATA
    m_bCDATAMode = true;
  }

  public void endCDATA () throws SAXException
  {
    // End of CDATA
    m_bCDATAMode = false;
  }

  public void comment (final char [] aChars, final int nStart, final int nLength) throws SAXException
  {
    // Ignore comments in DTD
    if (!m_bDTDMode)
    {
      // In case the comment comes before the root element....
      _createParentDocument ();

      final String sComment = new String (aChars, nStart, nLength);
      m_aParent.appendComment (sComment);
    }
  }

  @Nullable
  public IMicroDocument getDocument ()
  {
    return m_aDoc;
  }
}
