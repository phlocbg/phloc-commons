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
package com.phloc.commons.microdom.serialize;

import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;

import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroCDATA;
import com.phloc.commons.microdom.IMicroComment;
import com.phloc.commons.microdom.IMicroContainer;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroDocumentType;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroEntityReference;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.IMicroProcessingInstruction;
import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.xml.CXML;
import com.phloc.commons.xml.IXMLIterationHandler;
import com.phloc.commons.xml.serialize.AbstractSerializerPhloc;
import com.phloc.commons.xml.serialize.IXMLWriterSettings;
import com.phloc.commons.xml.serialize.XMLEmitterPhloc;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

/**
 * Materializes micro nodes into a string representation.
 * 
 * @author Philip
 */
public final class MicroSerializer extends AbstractSerializerPhloc <IMicroNode>
{
  public MicroSerializer ()
  {
    this (XMLWriterSettings.DEFAULT_XML_SETTINGS);
  }

  public MicroSerializer (@Nonnull final IXMLWriterSettings aSettings)
  {
    super (aSettings);
  }

  private void _writeNode (@Nonnull final IXMLIterationHandler aXMLWriter,
                           @Nullable final IMicroNode aPrevSibling,
                           @Nonnull final IMicroNode aNode,
                           @Nullable final IMicroNode aNextSibling)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    if (aNode.isElement ())
      _writeElement (aXMLWriter, aPrevSibling, (IMicroElement) aNode, aNextSibling);
    else
      if (aNode.isText ())
        _writeText (aXMLWriter, (IMicroText) aNode);
      else
        if (aNode.isCDATA ())
          _writeCDATA (aXMLWriter, (IMicroCDATA) aNode);
        else
          if (aNode.isComment ())
            _writeComment (aXMLWriter, (IMicroComment) aNode);
          else
            if (aNode.isEntityReference ())
              _writeEntityReference (aXMLWriter, (IMicroEntityReference) aNode);
            else
              if (aNode.isDocument ())
                _writeDocument (aXMLWriter, (IMicroDocument) aNode);
              else
                if (aNode.isDocumentType ())
                  _writeDocumentType (aXMLWriter, (IMicroDocumentType) aNode);
                else
                  if (aNode.isProcessingInstruction ())
                    _writeProcessingInstruction (aXMLWriter, (IMicroProcessingInstruction) aNode);
                  else
                    if (aNode.isContainer ())
                      _writeContainer (aXMLWriter, (IMicroContainer) aNode);
                    else
                      throw new IllegalArgumentException ("Passed node type " +
                                                          aNode.getClass ().getName () +
                                                          " is not yet supported");
  }

  /**
   * Special helper method to write a list of nodes. This implementations is
   * used to avoid calling {@link IMicroNode#getPreviousSibling()} and
   * {@link IMicroNode#getNextSibling()} since there implementation is compute
   * intensive since the objects are not directly linked. So to avoid this call,
   * we're manually retrieving the previous and next sibling by their index in
   * the list.
   * 
   * @param aXMLWriter
   *        The XML writer to use. May not be <code>null</code>.
   * @param aChildren
   *        The node list to be serialized. May not be <code>null</code>.
   */
  private void _writeNodeList (@Nonnull final IXMLIterationHandler aXMLWriter,
                               @Nonnull final List <IMicroNode> aChildren)
  {
    final int nLastIndex = aChildren.size () - 1;
    for (int nIndex = 0; nIndex <= nLastIndex; ++nIndex)
    {
      _writeNode (aXMLWriter,
                  nIndex == 0 ? null : aChildren.get (nIndex - 1),
                  aChildren.get (nIndex),
                  nIndex == nLastIndex ? null : aChildren.get (nIndex + 1));
    }
  }

  private void _writeDocument (@Nonnull final IXMLIterationHandler aXMLWriter, final IMicroDocument aDocument)
  {
    if (m_aSettings.getFormat ().isXML ())
      aXMLWriter.onDocumentStart (m_aSettings.getXMLVersion (), m_aSettings.getCharset (), aDocument.isStandalone ());

    if (aDocument.hasChildren ())
      _writeNodeList (aXMLWriter, aDocument.getChildren ());
  }

  private void _writeDocumentType (@Nonnull final IXMLIterationHandler aXMLWriter, final IMicroDocumentType aDocType)
  {
    if (m_aSettings.getSerializeDocType ().isEmit ())
      aXMLWriter.onDocumentType (aDocType.getQualifiedName (), aDocType.getPublicID (), aDocType.getSystemID ());
  }

  private static void _writeProcessingInstruction (@Nonnull final IXMLIterationHandler aXMLWriter,
                                                   final IMicroProcessingInstruction aPI)
  {
    aXMLWriter.onProcessingInstruction (aPI.getTarget (), aPI.getData ());
  }

  private void _writeContainer (@Nonnull final IXMLIterationHandler aXMLWriter, final IMicroContainer aContainer)
  {
    // A container has no own properties!
    if (aContainer.hasChildren ())
      _writeNodeList (aXMLWriter, aContainer.getChildren ());
  }

  private static void _writeEntityReference (@Nonnull final IXMLIterationHandler aXMLWriter,
                                             @Nonnull final IMicroEntityReference aEntRef)
  {
    aXMLWriter.onEntityReference (aEntRef.getName ());
  }

  private static void _writeText (@Nonnull final IXMLIterationHandler aXMLWriter, @Nonnull final IMicroText aText)
  {
    aXMLWriter.onText (aText.getData ().toString (), aText.isEscape ());
  }

  private void _writeComment (@Nonnull final IXMLIterationHandler aXMLWriter, @Nonnull final IMicroComment aComment)
  {
    if (m_aSettings.getSerializeComments ().isEmit ())
    {
      if (m_aSettings.getIndent ().isIndent () && m_aIndent.length () > 0)
        aXMLWriter.onContentElementWhitespace (m_aIndent);
      aXMLWriter.onComment (aComment.getData ().toString ());
      if (m_aSettings.getIndent ().isAlign ())
        aXMLWriter.onContentElementWhitespace (NEWLINE);
    }
  }

  private static void _writeCDATA (@Nonnull final IXMLIterationHandler aXMLWriter, @Nonnull final IMicroCDATA aCDATA)
  {
    aXMLWriter.onCDATA (aCDATA.getData ().toString ());
  }

  private static boolean _isInlineNode (@Nonnull final IMicroNode aNode)
  {
    return ((aNode.isText () || aNode.isCDATA ()) && !aNode.isComment ()) || aNode.isEntityReference ();
  }

  private void _writeElement (@Nonnull final IXMLIterationHandler aXMLWriter,
                              @Nullable final IMicroNode aPrevSibling,
                              @Nonnull final IMicroElement aElement,
                              @Nullable final IMicroNode aNextSibling)
  {
    // use either local name or tag name (depending on namespace prefix)
    final String sTagName = aElement.getLocalName () != null ? aElement.getLocalName () : aElement.getTagName ();

    final List <IMicroNode> aChildNodeList = aElement.getChildren ();
    final boolean bHasChildren = aElement.hasChildren ();

    final boolean bIsRootElement = aElement.getParent () != null && aElement.getParent ().isDocument ();
    final boolean bIndentPrev = aPrevSibling == null || !_isInlineNode (aPrevSibling) || bIsRootElement;
    final boolean bIndentNext = aNextSibling == null || !_isInlineNode (aNextSibling);
    final boolean bHasChildElement = bHasChildren && !_isInlineNode (aElement.getFirstChild ());

    // get all attributes (sorting is important!)
    final Map <String, String> aAttrMap = new LinkedHashMap <String, String> ();
    if (aElement.hasAttributes ())
      aAttrMap.putAll (aElement.getAttributes ());

    m_aNSStack.push (aAttrMap);
    try
    {
      // resolve Namespace prefix
      final String sElementNamespaceURI = StringHelper.getNotNull (aElement.getNamespaceURI ());
      final String sDefaultNamespaceURI = StringHelper.getNotNull (m_aNSStack.getDefaultNamespaceURI ());
      final boolean bIsDefaultNamespace = sElementNamespaceURI.equals (sDefaultNamespaceURI);
      String sNSPrefix = bIsDefaultNamespace ? null : m_aNSStack.getUsedPrefixOfNamespace (sElementNamespaceURI);

      // Do we need to create a prefix?
      if (sNSPrefix == null && !bIsDefaultNamespace && (!bIsRootElement || sElementNamespaceURI.length () > 0))
      {
        // Ensure to use the correct prefix (namespace context)
        sNSPrefix = m_aNSStack.getMappedPrefix (sElementNamespaceURI);

        // Do not create a prefix for the root element
        if (sNSPrefix == null && !bIsRootElement)
          sNSPrefix = m_aNSStack.createUniquePrefix ();

        // Add and remember the attribute
        if (StringHelper.hasNoText (sNSPrefix))
          aAttrMap.put (CXML.XML_ATTR_XMLNS, sElementNamespaceURI);
        else
          aAttrMap.put (CXML.XML_ATTR_XMLNS_WITH_SEP + sNSPrefix, sElementNamespaceURI);
        m_aNSStack.addNamespaceMapping (sNSPrefix, sElementNamespaceURI);
      }

      // indent only if predecessor was an element
      if (m_aSettings.getIndent ().isIndent () && bIndentPrev && m_aIndent.length () > 0)
        aXMLWriter.onContentElementWhitespace (m_aIndent);

      aXMLWriter.onElementStart (sNSPrefix, sTagName, aAttrMap, bHasChildren);

      // write child nodes (if present)
      if (bHasChildren)
      {
        // do we have enclosing elements?
        if (m_aSettings.getIndent ().isAlign () && bHasChildElement)
          aXMLWriter.onContentElementWhitespace (NEWLINE);

        // increment indent
        m_aIndent.append (INDENT);

        // recursively process child nodes
        if (aChildNodeList != null)
          _writeNodeList (aXMLWriter, aChildNodeList);

        // decrement indent
        m_aIndent.delete (m_aIndent.length () - INDENT.length (), m_aIndent.length ());

        // add closing tag
        if (m_aSettings.getIndent ().isIndent () && bHasChildElement && m_aIndent.length () > 0)
          aXMLWriter.onContentElementWhitespace (m_aIndent);
      }

      aXMLWriter.onElementEnd (sNSPrefix, sTagName, bHasChildren);

      if (m_aSettings.getIndent ().isAlign () && bIndentNext)
        aXMLWriter.onContentElementWhitespace (NEWLINE);
    }
    finally
    {
      m_aNSStack.pop ();
    }
  }

  public void write (@Nonnull final IMicroNode aNode, @Nonnull @WillNotClose final Writer aWriter)
  {
    final IXMLIterationHandler aXMLWriter = new XMLEmitterPhloc (aWriter, m_aSettings);
    // No previous and no next sibling
    _writeNode (aXMLWriter, null, aNode, null);
    // Flush is important for Writer!
    StreamUtils.flush (aWriter);
  }

  public void write (@Nonnull final IMicroNode aNode, @Nonnull final IXMLIterationHandler aXMLEmitter)
  {
    // No previous and no next sibling
    _writeNode (aXMLEmitter, null, aNode, null);
  }
}
