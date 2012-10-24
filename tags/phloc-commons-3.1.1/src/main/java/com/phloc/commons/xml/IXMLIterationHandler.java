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

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Callback interface when iterating XML nodes.
 * 
 * @author philip
 */
public interface IXMLIterationHandler
{
  /**
   * At the very beginning of the document.
   * 
   * @param eVersion
   *        The XML version to use. If <code>null</code> is passed,
   *        {@link EXMLVersion#DEFAULT} will be used.
   * @param sEncoding
   *        The encoding to be used for this document. It may be
   *        <code>null</code> but it is strongly recommended to write a correct
   *        charset.
   * @param bStandalone
   *        if <code>true</code> this is a standalone XML document without a
   *        connection to an existing DTD or XML schema
   */
  void onDocumentStart (@Nullable EXMLVersion eVersion, @Nullable String sEncoding, boolean bStandalone);

  /**
   * On XML document type.
   * 
   * @param sQualifiedElementName
   *        Qualified name of the root element.
   * @param sPublicID
   *        Document type public ID
   * @param sSystemID
   *        Document type system ID
   */
  void onDocumentType (@Nonnull final String sQualifiedElementName,
                       @Nullable final String sPublicID,
                       @Nullable final String sSystemID);

  /**
   * On processing instruction
   * 
   * @param sTarget
   *        The target
   * @param sData
   *        The data (attributes as a string)
   */
  void onProcessingInstruction (@Nonnull String sTarget, @Nullable String sData);

  /**
   * On entity reference.
   * 
   * @param sEntityRef
   *        The reference (without '&' and ';' !!)
   */
  void onEntityReference (@Nonnull String sEntityRef);

  /**
   * Ignorable whitespace characters.
   * 
   * @param aWhitespaces
   *        The whitespace character sequence
   */
  void onContentElementWhitspace (@Nullable CharSequence aWhitespaces);

  /**
   * Comment node.
   * 
   * @param sComment
   *        The comment text
   */
  void onComment (@Nullable String sComment);

  /**
   * Text node.
   * 
   * @param sText
   *        The contained text
   */
  void onText (@Nullable String sText);

  /**
   * CDATA node.
   * 
   * @param sText
   *        The contained text
   */
  void onCDATA (@Nullable String sText);

  /**
   * Start of an element.
   * 
   * @param sNSURI
   *        Optional namespace URI. May be <code>null</code>.
   * @param sTagName
   *        Tag name
   * @param aAttrs
   *        Optional set of attributes.
   * @param bHasChildren
   *        <code>true</code> if the current element has children
   */
  void onElementStart (@Nullable String sNSURI,
                       @Nonnull String sTagName,
                       @Nullable Map <String, String> aAttrs,
                       boolean bHasChildren);

  /**
   * End of an element.
   * 
   * @param sNSURI
   *        Optional namespace URI. May be <code>null</code>.
   * @param sTagName
   *        Tag name
   */
  void onElementEnd (@Nullable String sNSURI, @Nonnull String sTagName);
}