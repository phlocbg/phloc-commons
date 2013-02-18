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
package com.phloc.commons.xml.serialize;

import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.xml.CXML;
import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.IXMLIterationHandler;
import com.phloc.commons.xml.XMLHelper;

/**
 * org.w3c.dom.Node serializer that correctly handles HTML empty elements
 * (&lt;span>&lt;/span> vs. &lt;span />).
 * 
 * @author philip
 */
public final class XMLSerializerPhloc extends AbstractSerializerPhloc <Node>
{
  public XMLSerializerPhloc ()
  {
    this (XMLWriterSettings.DEFAULT_XML_SETTINGS);
  }

  public XMLSerializerPhloc (@Nonnull final IXMLWriterSettings aSettings)
  {
    super (aSettings);
  }

  private void _writeNode (@Nonnull final IXMLIterationHandler aEmitter, @Nonnull final Node aNode)
  {
    final short nNodeType = aNode.getNodeType ();
    if (nNodeType == Node.ELEMENT_NODE)
      _writeElement (aEmitter, (Element) aNode);
    else
      if (nNodeType == Node.TEXT_NODE)
        _writeText (aEmitter, (Text) aNode);
      else
        if (nNodeType == Node.CDATA_SECTION_NODE)
          _writeCDATA (aEmitter, (CDATASection) aNode);
        else
          if (nNodeType == Node.COMMENT_NODE)
            _writeComment (aEmitter, (Comment) aNode);
          else
            if (nNodeType == Node.ENTITY_REFERENCE_NODE)
              _writeEntityReference (aEmitter, (EntityReference) aNode);
            else
              if (nNodeType == Node.DOCUMENT_NODE)
                _writeDocument (aEmitter, (Document) aNode);
              else
                if (nNodeType == Node.DOCUMENT_TYPE_NODE)
                  _writeDocumentType (aEmitter, (DocumentType) aNode);
                else
                  if (nNodeType == Node.PROCESSING_INSTRUCTION_NODE)
                    _writeProcessingInstruction (aEmitter, (ProcessingInstruction) aNode);
                  else
                    throw new IllegalArgumentException ("Passed node type " + nNodeType + " is not yet supported");
  }

  private void _writeDocument (@Nonnull final IXMLIterationHandler aEmitter, @Nonnull final Document aDocument)
  {
    if (m_aSettings.getFormat ().isXML ())
    {
      String sXMLVersion = null;
      boolean bIsDocumentStandalone = false;
      try
      {
        sXMLVersion = aDocument.getXmlVersion ();
        bIsDocumentStandalone = aDocument.getXmlStandalone ();
      }
      catch (final Throwable t)
      {
        // Happens e.g. in dom4j 1.6.1:
        // AbstractMethodError: getXmlVersion and getXmlStandalone
      }
      final EXMLVersion eXMLVersion = EXMLVersion.getFromVersionOrDefault (sXMLVersion, m_aSettings.getXMLVersion ());
      aEmitter.onDocumentStart (eXMLVersion,
                                m_aSettings.getCharset (),
                                bIsDocumentStandalone || aDocument.getDoctype () == null);
    }

    final NodeList aNL = aDocument.getChildNodes ();
    for (int i = 0; i < aNL.getLength (); ++i)
      _writeNode (aEmitter, aNL.item (i));
  }

  private void _writeDocumentType (@Nonnull final IXMLIterationHandler aEmitter, @Nonnull final DocumentType aDocType)
  {
    if (m_aSettings.getSerializeDocType ().isEmit ())
      aEmitter.onDocumentType (aDocType.getName (), aDocType.getPublicId (), aDocType.getSystemId ());
  }

  private static void _writeProcessingInstruction (@Nonnull final IXMLIterationHandler aEmitter,
                                                   @Nonnull final ProcessingInstruction aPI)
  {
    aEmitter.onProcessingInstruction (aPI.getTarget (), aPI.getData ());
  }

  private static void _writeEntityReference (@Nonnull final IXMLIterationHandler aEmitter,
                                             @Nonnull final EntityReference aEntRef)
  {
    aEmitter.onEntityReference (aEntRef.getNodeName ());
  }

  private void _writeComment (@Nonnull final IXMLIterationHandler aEmitter, @Nonnull final Comment aComment)
  {
    if (m_aSettings.getSerializeComments ().isEmit ())
      aEmitter.onComment (aComment.getData ());
  }

  private static void _writeText (@Nonnull final IXMLIterationHandler aEmitter, @Nonnull final Text aText)
  {
    // DOM text is always escaped!
    aEmitter.onText (aText.getData (), true);
  }

  private static void _writeCDATA (@Nonnull final IXMLIterationHandler aEmitter, @Nonnull final Text aText)
  {
    aEmitter.onCDATA (aText.getData ());
  }

  private void _writeElement (@Nonnull final IXMLIterationHandler aEmitter, @Nonnull final Element aElement)
  {
    // use either local name or tag name (depending on namespace prefix)
    final String sTagName = aElement.getLocalName () != null ? aElement.getLocalName () : aElement.getTagName ();

    // May be null!
    final Document aDoc = aElement.getOwnerDocument ();
    final NodeList aChildNodeList = aElement.getChildNodes ();
    final boolean bHasChildren = aChildNodeList.getLength () > 0;

    final boolean bIsRootElement = aDoc != null && aElement.equals (aDoc.getDocumentElement ());
    final boolean bIndentPrev = aElement.getPreviousSibling () == null ||
                                !XMLHelper.isTextNode (aElement.getPreviousSibling ()) ||
                                bIsRootElement;
    final boolean bIndentNext = aElement.getNextSibling () == null ||
                                !XMLHelper.isTextNode (aElement.getNextSibling ());
    final boolean bHasChildElement = bHasChildren && !XMLHelper.isTextNode (aElement.getFirstChild ());

    // get all attributes (sorting is important!)
    final Map <String, String> aAttrMap = new TreeMap <String, String> ();
    final NamedNodeMap aAttrs = aElement.getAttributes ();
    for (int i = 0; i < aAttrs.getLength (); ++i)
    {
      final Attr aAttr = (Attr) aAttrs.item (i);
      aAttrMap.put (aAttr.getName (), aAttr.getValue ());
    }

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
        aEmitter.onContentElementWhitespace (m_aIndent);

      aEmitter.onElementStart (sNSPrefix, sTagName, aAttrMap, bHasChildren);

      // write child nodes (if present)
      if (bHasChildren)
      {
        // do we have enclosing elements?
        if (m_aSettings.getIndent ().isAlign () && bHasChildElement)
          aEmitter.onContentElementWhitespace (NEWLINE);

        // increment indent
        m_aIndent.append (INDENT);

        // recursively process child nodes
        for (int i = 0; i < aChildNodeList.getLength (); ++i)
          _writeNode (aEmitter, aChildNodeList.item (i));

        // decrement indent
        m_aIndent.delete (m_aIndent.length () - INDENT.length (), m_aIndent.length ());

        // add closing tag
        if (m_aSettings.getIndent ().isIndent () && bHasChildElement && m_aIndent.length () > 0)
          aEmitter.onContentElementWhitespace (m_aIndent);
      }

      aEmitter.onElementEnd (sNSPrefix, sTagName, bHasChildren);

      if (m_aSettings.getIndent ().isAlign () && bIndentNext)
        aEmitter.onContentElementWhitespace (NEWLINE);
    }
    finally
    {
      m_aNSStack.pop ();
    }
  }

  public void write (@Nonnull final Node aNode, @Nonnull @WillNotClose final Writer aWriter)
  {
    final IXMLIterationHandler aXMLWriter = new XMLEmitterPhloc (aWriter, m_aSettings);
    _writeNode (aXMLWriter, aNode);
    // Flush is important for Writer!
    StreamUtils.flush (aWriter);
  }

  public void write (@Nonnull final Node aNode, @Nonnull final IXMLIterationHandler aXMLEmitter)
  {
    _writeNode (aXMLEmitter, aNode);
  }
}
