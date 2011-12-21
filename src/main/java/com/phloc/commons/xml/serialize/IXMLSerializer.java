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
import javax.annotation.WillNotClose;

import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;

/**
 * Base interface for XML like serializers. Works both for IMicroNode and
 * org.w3c.dom.Node objects.
 * 
 * @author philip
 * @param <NODETYPE>
 *        The node type to be serialized.
 */
public interface IXMLSerializer <NODETYPE>
{
  /**
   * Serialize the XML declaration? defaults to true
   * 
   * @param eFormat
   *        The format to be serialized. May not be <code>null</code>.
   */
  void setFormat (@Nonnull EXMLSerializeFormat eFormat);

  /**
   * Serialize the XML standalone attribute in the XML declaration? Defaults to
   * true.
   * 
   * @param bStandalone
   *        <code>true</code> to enable, <code>false</code> otherwise
   */
  void setStandalone (boolean bStandalone);

  /**
   * Serialize the DocumentType (if present)? defaults to true
   * 
   * @param eDocType
   *        Enable of disable emitting of the document type? May not be
   *        <code>null</code>.
   */
  void setSerializeDocType (@Nonnull EXMLSerializeDocType eDocType);

  /**
   * Serialize the comments (if present)? defaults to true
   * 
   * @param eComments
   *        Enable of disable emitting of the comments? May not be
   *        <code>null</code>.
   */
  void setSerializeComments (@Nonnull EXMLSerializeComments eComments);

  /**
   * Indent the created XML document (newline and indentation after each tag?).
   * If true, alignment is set as well
   * 
   * @param eIndent
   *        Indentation type. May not be <code>null</code>.
   */
  void setIndent (@Nonnull EXMLSerializeIndent eIndent);

  /**
   * Define how to handling incorrect characters on writing.
   * 
   * @param eIncorrectCharacterHandling
   *        The incorrect handling to be used. May not be <code>null</code>.
   */
  void setIncorrectCharacterHandling (@Nonnull EXMLIncorrectCharacterHandling eIncorrectCharacterHandling);

  /**
   * Write the specified node to the specified {@link OutputStream}.
   * 
   * @param aNode
   *        The node to write. May not be <code>null</code>.
   * @param aOS
   *        The stream to serialize onto.
   */
  void write (@Nonnull NODETYPE aNode, @Nonnull @WillNotClose OutputStream aOS);
}
