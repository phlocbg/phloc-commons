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

import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;
import javax.xml.namespace.NamespaceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.xml.EXMLVersion;

/**
 * This is a helper class to serialize DOM nodes to a String.
 * 
 * @author philip
 */
@Immutable
public final class XMLWriter
{
  public static final EXMLVersion DEFAULT_XML_VERSION = EXMLVersion.DEFAULT;
  public static final NamespaceContext DEFAULT_NAMESPACE_CTX = null;

  private static final Logger s_aLogger = LoggerFactory.getLogger (XMLWriter.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final XMLWriter s_aInstance = new XMLWriter ();

  private XMLWriter ()
  {}

  /**
   * Serialized the given DOM node to the given {@link OutputStream} using the
   * passed settings. Uses default XML version 1.0 and no namespace URL/prefix
   * mapping.
   * 
   * @param aNode
   *        The node to serialize. May not be <code>null</code>.
   * @param aOS
   *        The output stream to serialize to. May not be <code>null</code>.
   * @param eFormat
   *        Format to be emitted
   * @param eDocType
   *        Should the document type be emitted?
   * @param eComments
   *        Should the contained comments be emitted?
   * @param eIndent
   *        Should the output be pretty printed? May not be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  @Deprecated
  public static ESuccess writeToStream (@Nonnull final Node aNode,
                                        @Nonnull @WillClose final OutputStream aOS,
                                        @Nonnull final EXMLSerializeFormat eFormat,
                                        @Nonnull final EXMLSerializeDocType eDocType,
                                        @Nonnull final EXMLSerializeComments eComments,
                                        @Nonnull final EXMLSerializeIndent eIndent,
                                        @Nonnull final String sCharset)
  {
    return writeToStream (aNode,
                          aOS,
                          DEFAULT_XML_VERSION,
                          eFormat,
                          eDocType,
                          eComments,
                          eIndent,
                          sCharset,
                          DEFAULT_NAMESPACE_CTX);
  }

  /**
   * Serialized the given DOM node to the given {@link OutputStream} using the
   * passed settings. mapping.
   * 
   * @param aNode
   *        The node to serialize. May not be <code>null</code>.
   * @param aOS
   *        The output stream to serialize to. May not be <code>null</code>.
   * @param eVersion
   *        The XML version to use. May not be <code>null</code>.
   * @param eFormat
   *        Format to be emitted. May not be <code>null</code>.
   * @param eDocType
   *        Should the document type be emitted? May not be <code>null</code>.
   * @param eComments
   *        Should the contained comments be emitted? May not be
   *        <code>null</code>.
   * @param eIndent
   *        Should the output be pretty printed? May not be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param aNamespaceCtx
   *        The mapping from namespace URI to namespace prefix. May be
   *        <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  @Deprecated
  public static ESuccess writeToStream (@Nonnull final Node aNode,
                                        @Nonnull @WillClose final OutputStream aOS,
                                        @Nonnull final EXMLVersion eVersion,
                                        @Nonnull final EXMLSerializeFormat eFormat,
                                        @Nonnull final EXMLSerializeDocType eDocType,
                                        @Nonnull final EXMLSerializeComments eComments,
                                        @Nonnull final EXMLSerializeIndent eIndent,
                                        @Nonnull final String sCharset,
                                        @Nullable final NamespaceContext aNamespaceCtx)
  {
    return writeToStream (aNode, aOS, new XMLWriterSettings ().setXMLVersion (eVersion)
                                                              .setFormat (eFormat)
                                                              .setSerializeDocType (eDocType)
                                                              .setSerializeComments (eComments)
                                                              .setIndent (eIndent)
                                                              .setCharset (sCharset)
                                                              .setNamespaceContext (aNamespaceCtx));
  }

  /**
   * Write a ode to an output stream using the default settings.
   * 
   * @param aNode
   *        The node to be serialized. May be any kind of node (incl.
   *        documents). May not be <code>null</code>.
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>. The
   *        output stream is closed anyway directly after the operation finishes
   *        (on success and on error).
   */
  @Nonnull
  public static ESuccess writeToStream (@Nonnull final Node aNode, @Nonnull @WillClose final OutputStream aOS)
  {
    return writeToStream (aNode, aOS, XMLWriterSettings.DEFAULT_XML_SETTINGS);
  }

  @Nonnull
  public static ESuccess writeToStream (@Nonnull final Node aNode,
                                        @Nonnull @WillClose final OutputStream aOS,
                                        @Nonnull final IXMLWriterSettings aSettings)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (aOS == null)
      throw new NullPointerException ("outputStream");
    if (aSettings == null)
      throw new NullPointerException ("settings");

    try
    {
      final IXMLSerializer <Node> aSerializer = new XMLSerializerPhloc (aSettings);
      aSerializer.write (aNode, aOS);
      return ESuccess.SUCCESS;
    }
    catch (final RuntimeException ex)
    {
      s_aLogger.error ("Error in XML serialization", ex);
      throw ex;
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Error in XML serialization", ex);
    }
    finally
    {
      StreamUtils.close (aOS);
    }
    return ESuccess.FAILURE;
  }

  /**
   * Write the passed DOM node to an output stream using the following default
   * settings: serialization format XML, write document type (if present), write
   * comments, pretty print XML.
   * 
   * @param aNode
   *        The node to be written. May not be <code>null</code>.
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>.
   * @param sCharset
   *        The charset to be used. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  @Deprecated
  public static ESuccess writeXMLToStream (@Nonnull final Node aNode,
                                           @Nonnull @WillClose final OutputStream aOS,
                                           @Nonnull final String sCharset)
  {
    return writeToStream (aNode,
                          aOS,
                          EXMLSerializeFormat.XML,
                          EXMLSerializeDocType.EMIT,
                          EXMLSerializeComments.EMIT,
                          EXMLSerializeIndent.INDENT_AND_ALIGN,
                          sCharset);
  }

  @Nullable
  @Deprecated
  public static String getAsString (@Nonnull final Node aNode,
                                    @Nonnull final EXMLSerializeFormat eFormat,
                                    @Nonnull final EXMLSerializeDocType eDocType,
                                    @Nonnull final EXMLSerializeComments eComments,
                                    @Nonnull final EXMLSerializeIndent eIndent,
                                    @Nonnull final String sCharset)
  {
    return getAsString (aNode,
                        DEFAULT_XML_VERSION,
                        eFormat,
                        eDocType,
                        eComments,
                        eIndent,
                        sCharset,
                        DEFAULT_NAMESPACE_CTX);
  }

  @Nullable
  @Deprecated
  public static String getAsString (@Nonnull final Node aNode,
                                    @Nonnull final EXMLVersion eVersion,
                                    @Nonnull final EXMLSerializeFormat eFormat,
                                    @Nonnull final EXMLSerializeDocType eDocType,
                                    @Nonnull final EXMLSerializeComments eComments,
                                    @Nonnull final EXMLSerializeIndent eIndent,
                                    @Nonnull final String sCharset,
                                    @Nullable final NamespaceContext aNamespaceCtx)
  {
    return getNodeAsString (aNode, new XMLWriterSettings ().setXMLVersion (eVersion)
                                                           .setFormat (eFormat)
                                                           .setSerializeDocType (eDocType)
                                                           .setSerializeComments (eComments)
                                                           .setIndent (eIndent)
                                                           .setCharset (sCharset)
                                                           .setNamespaceContext (aNamespaceCtx));
  }

  @Nullable
  public static String getNodeAsString (@Nonnull final Node aNode, @Nonnull final IXMLWriterSettings aSettings)
  {
    NonBlockingByteArrayOutputStream aOS = null;
    try
    {
      // start serializing
      aOS = new NonBlockingByteArrayOutputStream (8192);
      if (writeToStream (aNode, aOS, aSettings).isFailure ())
      {
        // Some exception was thrown....
        return null;
      }
      return aOS.getAsString (aSettings.getCharset ());
    }
    finally
    {
      // don't forget to close the stream!
      StreamUtils.close (aOS);
    }
  }

  /**
   * Convert the passed micro node to an XML string using
   * {@link XMLWriterSettings#DEFAULT_XML_SETTINGS}. This is a specialized
   * version of {@link #getNodeAsString(Node, IXMLWriterSettings)}.
   * 
   * @param aNode
   *        The node to be converted to a string. May not be <code>null</code> .
   * @return The string representation of the passed node.
   */
  @Nullable
  public static String getXMLString (@Nonnull final Node aNode)
  {
    return getNodeAsString (aNode, XMLWriterSettings.DEFAULT_XML_SETTINGS);
  }

  /**
   * Get the passed node as an XHTML string that is indented and contains the
   * document type.
   * 
   * @param aNode
   *        The node to be serialized.
   * @param sCharset
   *        The charset to be used.
   * @return The XHTML string representation of the node.
   */
  @Nullable
  @Deprecated
  public static String getXHTMLString (@Nonnull final Node aNode, @Nonnull final String sCharset)
  {
    return getXHTMLString (aNode, EXMLSerializeDocType.EMIT, EXMLSerializeIndent.INDENT_AND_ALIGN, sCharset);
  }

  /**
   * Get the passed node as an XHTML string that is indented and contains the
   * document type. Comments are also emitted but no initial &lt;?xml... header
   * is written.
   * 
   * @param aNode
   *        The node to be serialized.
   * @param eDocType
   *        Write doc type or not?
   * @param eIndent
   *        Indent the output or not?
   * @param sCharset
   *        The charset to be used.
   * @return The XHTML string representation of the node.
   */
  @Nullable
  @Deprecated
  public static String getXHTMLString (@Nonnull final Node aNode,
                                       @Nonnull final EXMLSerializeDocType eDocType,
                                       @Nonnull final EXMLSerializeIndent eIndent,
                                       @Nonnull final String sCharset)
  {
    return getAsString (aNode, EXMLSerializeFormat.HTML, eDocType, EXMLSerializeComments.EMIT, eIndent, sCharset);
  }

  /**
   * Get the passed node as an XML string that is indented and contains the
   * document type.
   * 
   * @param aNode
   *        The node to be serialized.
   * @param sCharset
   *        The charset to be used.
   * @return The XML string representation of the node.
   */
  @Nullable
  @Deprecated
  public static String getXMLString (@Nonnull final Node aNode, @Nonnull final String sCharset)
  {
    return getAsString (aNode,
                        DEFAULT_XML_VERSION,
                        EXMLSerializeFormat.XML,
                        EXMLSerializeDocType.EMIT,
                        EXMLSerializeComments.EMIT,
                        EXMLSerializeIndent.INDENT_AND_ALIGN,
                        sCharset,
                        DEFAULT_NAMESPACE_CTX);
  }

  /**
   * Get the passed node as an XML string that is indented and contains the
   * document type.
   * 
   * @param aNode
   *        The node to be serialized.
   * @param eDocType
   *        Write doc type or not?
   * @param eIndent
   *        Indent the output or not?
   * @param sCharset
   *        The charset to be used.
   * @return The XML string representation of the node.
   */
  @Nullable
  @Deprecated
  public static String getXMLString (@Nonnull final Node aNode,
                                     @Nonnull final EXMLSerializeDocType eDocType,
                                     @Nonnull final EXMLSerializeIndent eIndent,
                                     @Nonnull final String sCharset)
  {
    return getAsString (aNode,
                        DEFAULT_XML_VERSION,
                        EXMLSerializeFormat.XML,
                        eDocType,
                        EXMLSerializeComments.EMIT,
                        eIndent,
                        sCharset,
                        DEFAULT_NAMESPACE_CTX);
  }

  /**
   * Get the passed node as an XML string that is indented and contains the
   * document type.
   * 
   * @param aNode
   *        The node to be serialized.
   * @param eDocType
   *        Write doc type or not?
   * @param eComments
   *        Write comments or not?
   * @param eIndent
   *        Indent the output or not?
   * @param sCharset
   *        The charset to be used.
   * @return The XML string representation of the node.
   */
  @Nullable
  @Deprecated
  public static String getXMLString (@Nonnull final Node aNode,
                                     @Nonnull final EXMLSerializeDocType eDocType,
                                     @Nonnull final EXMLSerializeComments eComments,
                                     @Nonnull final EXMLSerializeIndent eIndent,
                                     @Nonnull final String sCharset)
  {
    return getAsString (aNode,
                        DEFAULT_XML_VERSION,
                        EXMLSerializeFormat.XML,
                        eDocType,
                        eComments,
                        eIndent,
                        sCharset,
                        DEFAULT_NAMESPACE_CTX);
  }

  /**
   * Get the passed node as an XML string that is indented and contains the
   * document type.
   * 
   * @param aNode
   *        The node to be serialized.
   * @param eVersion
   *        The XML version to be used.
   * @param eDocType
   *        Write doc type or not?
   * @param eComments
   *        Write comments or not?
   * @param eIndent
   *        Indent the output or not?
   * @param sCharset
   *        The charset to be used.
   * @param aNamespaceCtx
   *        The optional namespace URL to namespace prefix mapping to be used.
   *        May be <code>null</code>.
   * @return The XML string representation of the node.
   */
  @Nullable
  @Deprecated
  public static String getXMLString (@Nonnull final Node aNode,
                                     @Nonnull final EXMLVersion eVersion,
                                     @Nonnull final EXMLSerializeDocType eDocType,
                                     @Nonnull final EXMLSerializeComments eComments,
                                     @Nonnull final EXMLSerializeIndent eIndent,
                                     @Nonnull final String sCharset,
                                     @Nullable final NamespaceContext aNamespaceCtx)
  {
    return getAsString (aNode, eVersion, EXMLSerializeFormat.XML, eDocType, eComments, eIndent, sCharset, aNamespaceCtx);
  }
}
