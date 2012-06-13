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
package com.phloc.commons.microdom;

import org.w3c.dom.Node;

/**
 * Denotes the type of {@link IMicroNode} objects.
 * 
 * @author philip
 */
public enum EMicroNodeType
{
  CDATA (Node.CDATA_SECTION_NODE),
  COMMENT (Node.COMMENT_NODE),
  CONTAINER,
  DOCUMENT (Node.DOCUMENT_NODE),
  DOCUMENT_TYPE (Node.DOCUMENT_TYPE_NODE),
  ELEMENT (Node.ELEMENT_NODE),
  ENTITY_REFERENCE (Node.ENTITY_REFERENCE_NODE),
  PROCESSING_INSTRUCTION (Node.PROCESSING_INSTRUCTION_NODE),
  TEXT (Node.TEXT_NODE);

  public static final short ILLEGAL_DOM_NODE_TYPE = 0;

  private final short m_nDOMNodeType;

  private EMicroNodeType ()
  {
    this (ILLEGAL_DOM_NODE_TYPE);
  }

  private EMicroNodeType (final short nDOMNodeType)
  {
    m_nDOMNodeType = nDOMNodeType;
  }

  /**
   * @return <code>true</code> if this micro node type has a corresponding DOM
   *         node type. <code>false</code> if not.
   */
  public boolean hasCorrespondingDOMNodeType ()
  {
    return m_nDOMNodeType != ILLEGAL_DOM_NODE_TYPE;
  }

  /**
   * @return The corresponding DOM node type or {@link #ILLEGAL_DOM_NODE_TYPE}
   *         if this micro node type has no corresponding DOM node type.
   */
  public short getDOMNodeType ()
  {
    return m_nDOMNodeType;
  }
}
