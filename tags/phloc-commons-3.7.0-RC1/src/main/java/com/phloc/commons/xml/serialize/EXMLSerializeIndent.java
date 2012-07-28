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

/**
 * Determines the indentation and alignment mode of XML serialization. Alignment
 * means: newlines after certain elements. Indent means: adding blanks at the
 * beginning of the line to reflect the tree structure of an XML document more
 * visibly.
 * 
 * @author philip
 */
public enum EXMLSerializeIndent
{
  NONE (false, false),
  ALIGN_ONLY (true, false),
  INDENT_AND_ALIGN (true, true);

  private final boolean m_bAlign;
  private final boolean m_bIndent;

  private EXMLSerializeIndent (final boolean bAlign, final boolean bIndent)
  {
    m_bAlign = bAlign;
    m_bIndent = bIndent;
  }

  public boolean isAlign ()
  {
    return m_bAlign;
  }

  public boolean isIndent ()
  {
    return m_bIndent;
  }
}
