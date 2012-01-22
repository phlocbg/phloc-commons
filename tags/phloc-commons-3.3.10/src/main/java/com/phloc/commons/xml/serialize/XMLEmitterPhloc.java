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
package com.phloc.commons.xml.serialize;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.commons.microdom.IMicroDocumentType;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.xml.CXML;
import com.phloc.commons.xml.DefaultXMLIterationHandler;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.XMLHelper;

/**
 * Converts XML constructs into a string representation.
 * 
 * @author philip
 */
public final class XMLEmitterPhloc extends DefaultXMLIterationHandler
{
  private static final String CDATA_START = "<![CDATA[";
  private static final String CDATA_END = "]]>";
  private static final String CRLF = CGlobal.LINE_SEPARATOR;

  private final Writer m_aWriter;
  private final EXMLIncorrectCharacterHandling m_eIncorrectCharHandling;
  private EXMLVersion m_eXMLVersion = EXMLVersion.DEFAULT;

  public XMLEmitterPhloc (@Nonnull @WillNotClose final Writer aWriter,
                          @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling)
  {
    if (aWriter == null)
      throw new NullPointerException ("writer");
    if (eIncorrectCharHandling == null)
      throw new NullPointerException ("incorrectCharHandling");
    m_aWriter = aWriter;
    m_eIncorrectCharHandling = eIncorrectCharHandling;
  }

  @Nonnull
  private XMLEmitterPhloc _append (@Nonnull final String aValue)
  {
    try
    {
      m_aWriter.write (aValue);
      return this;
    }
    catch (final IOException ex)
    {
      throw new IllegalStateException (ex);
    }
  }

  @Nonnull
  private XMLEmitterPhloc _append (final char aValue)
  {
    try
    {
      m_aWriter.write (aValue);
      return this;
    }
    catch (final IOException ex)
    {
      throw new IllegalStateException (ex);
    }
  }

  @Nonnull
  private XMLEmitterPhloc _appendMasked (@Nullable final String sValue)
  {
    try
    {
      XMLHelper.maskXMLTextTo (m_eXMLVersion, m_eIncorrectCharHandling, sValue, m_aWriter);
      return this;
    }
    catch (final IOException ex)
    {
      throw new IllegalStateException (ex);
    }
  }

  @Override
  public void onDocumentStart (@Nullable final EXMLVersion eVersion,
                               @Nullable final String sEncoding,
                               final boolean bStandalone)
  {
    if (eVersion != null)
      m_eXMLVersion = eVersion;
    _append ("<?xml version=\"")._appendMasked (m_eXMLVersion.getVersion ())._append ('"');
    if (sEncoding != null)
      _append (" encoding=\"")._appendMasked (sEncoding)._append ('"');
    if (bStandalone)
      _append (" standalone=\"yes\"");
    _append ("?>")._append (CRLF);
  }

  @Nonnull
  @Deprecated
  @DevelopersNote ("Use the version with the XMK version")
  public static String getDocTypeHTMLRepresentation (@Nonnull final IMicroDocumentType aDocType)
  {
    return getDocTypeHTMLRepresentation (EXMLVersion.DEFAULT, EXMLIncorrectCharacterHandling.DEFAULT, aDocType);
  }

  @Nonnull
  public static String getDocTypeHTMLRepresentation (@Nonnull final EXMLVersion eXMLVersion,
                                                     @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                                     @Nonnull final IMicroDocumentType aDocType)
  {
    return getDocTypeHTMLRepresentation (eXMLVersion,
                                         eIncorrectCharHandling,
                                         aDocType.getQualifiedName (),
                                         aDocType.getPublicID (),
                                         aDocType.getSystemID ());
  }

  @Nonnull
  @Deprecated
  @DevelopersNote ("Use the version with the XMK version")
  public static String getDocTypeHTMLRepresentation (@Nonnull final String sQualifiedName,
                                                     @Nullable final String sPublicID,
                                                     @Nullable final String sSystemID)
  {
    return getDocTypeHTMLRepresentation (EXMLVersion.DEFAULT,
                                         EXMLIncorrectCharacterHandling.DEFAULT,
                                         sQualifiedName,
                                         sPublicID,
                                         sSystemID);
  }

  /**
   * Get the XML representation of a document type.
   * 
   * @param sQualifiedName
   *        The qualified element name. May not be <code>null</code>.
   * @param sPublicID
   *        The optional public ID. May be <code>null</code>. If the public ID
   *        is not <code>null</code> the system ID must also be set!
   * @param sSystemID
   *        The optional system ID. May be <code>null</code>.
   * @return The string DOCTYPE representation.
   */
  @Nonnull
  public static String getDocTypeHTMLRepresentation (@Nonnull final EXMLVersion eXMLVersion,
                                                     @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                                     @Nonnull final String sQualifiedName,
                                                     @Nullable final String sPublicID,
                                                     @Nullable final String sSystemID)
  {
    // do not return a line break at the end! (JS variable assignment)
    final StringBuilder aSB = new StringBuilder (128);
    aSB.append ("<!DOCTYPE ").append (sQualifiedName);
    if (sPublicID != null && sSystemID != null)
    {
      // Public and system ID present
      aSB.append (" PUBLIC \"")
         .append (XMLHelper.getMaskedXMLText (eXMLVersion, eIncorrectCharHandling, sPublicID))
         .append ("\" \"")
         .append (XMLHelper.getMaskedXMLText (eXMLVersion, eIncorrectCharHandling, sSystemID))
         .append ('"');
    }
    else
      if (sSystemID != null)
      {
        // Only system ID present
        aSB.append (" SYSTEM \"")
           .append (XMLHelper.getMaskedXMLText (eXMLVersion, eIncorrectCharHandling, sSystemID))
           .append ('"');
      }
    return aSB.append ('>').append (CRLF).toString ();
  }

  @Override
  public void onDocumentType (@Nonnull final String sQualifiedElementName,
                              @Nullable final String sPublicID,
                              @Nullable final String sSystemID)
  {
    if (sQualifiedElementName == null)
      throw new NullPointerException ("qualifiedElementName");

    final String sDocType = getDocTypeHTMLRepresentation (m_eXMLVersion,
                                                          m_eIncorrectCharHandling,
                                                          sQualifiedElementName,
                                                          sPublicID,
                                                          sSystemID);
    _append (sDocType);
  }

  @Override
  public void onProcessingInstruction (@Nonnull final String sTarget, @Nullable final String sData)
  {
    _append ("<?")._append (sTarget);
    if (StringHelper.hasText (sData))
      _append (' ')._append (sData);
    _append ("?>")._append (CRLF);
  }

  @Override
  public void onEntityReference (@Nonnull final String sEntityRef)
  {
    _append ('&')._append (sEntityRef)._append (';');
  }

  @Override
  public void onContentElementWhitspace (@Nullable final CharSequence aWhitespaces)
  {
    if (StringHelper.hasText (aWhitespaces))
      _append (aWhitespaces.toString ());
  }

  @Override
  public void onComment (@Nullable final String sComment)
  {
    if (StringHelper.hasText (sComment))
    {
      if (sComment.contains ("<!--") || sComment.contains ("-->"))
        throw new IllegalArgumentException ("XML comment contains nested XML comment: " + sComment);

      _append ("<!--")._append (sComment)._append ("-->");
    }
  }

  @Override
  public void onText (@Nullable final String sText)
  {
    _appendMasked (sText);
  }

  @Override
  public void onCDATA (@Nullable final String sText)
  {
    if (StringHelper.hasText (sText))
    {
      // Split CDATA sections if the contain the illegal "]]>" marker
      final List <String> aParts = StringHelper.explode (CDATA_END, sText);
      final int nParts = aParts.size ();
      for (int i = 0; i < nParts; ++i)
      {
        _append (CDATA_START)._append (aParts.get (i))._append (CDATA_END);
        if (i < (nParts - 1))
        {
          // Add the CDATA separator as a text element :)
          _appendMasked (CDATA_END);
        }
      }
    }
  }

  @Override
  public void onElementStart (@Nullable final String sNSPrefix,
                              @Nonnull final String sTagName,
                              @Nullable final Map <String, String> aAttrs,
                              final boolean bHasChildren)
  {
    _append ('<');
    if (sNSPrefix != null)
      _append (sNSPrefix)._append (CXML.XML_PREFIX_NAMESPACE_SEP);
    _append (sTagName);
    if (aAttrs != null && !aAttrs.isEmpty ())
    {
      // assuming that the order of the passed attributes is consistent!
      // Emit all attributes
      for (final Map.Entry <String, String> aEntry : aAttrs.entrySet ())
      {
        _append (' ')._append (aEntry.getKey ())._append ("=\"")._appendMasked (aEntry.getValue ())._append ('"');
      }
    }

    // Either leave tag open or close it
    // Note: according to HTML compatibility guideline a space should be added
    // before the self-closing
    _append (bHasChildren ? ">" : " />");
  }

  @Override
  public void onElementEnd (@Nullable final String sNSPrefix, @Nonnull final String sTagName)
  {
    _append ("</");
    if (sNSPrefix != null)
      _append (sNSPrefix)._append (CXML.XML_PREFIX_NAMESPACE_SEP);
    _append (sTagName)._append ('>');
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("writer", m_aWriter)
                                       .append ("XMLversion", m_eXMLVersion)
                                       .append ("incorrectCharHandling", m_eIncorrectCharHandling)
                                       .toString ();
  }
}
