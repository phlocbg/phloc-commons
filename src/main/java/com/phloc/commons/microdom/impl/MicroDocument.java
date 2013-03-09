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
package com.phloc.commons.microdom.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.microdom.EMicroNodeType;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroDocumentType;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.MicroException;

/**
 * Default implementation of the {@link IMicroDocument} interface.
 * 
 * @author philip
 */
public final class MicroDocument extends AbstractMicroNodeWithChildren implements IMicroDocument
{
  /** By default a document is not standalone */
  public static final boolean DEFAULT_STANDALONE = false;

  private boolean m_bIsStandalone = DEFAULT_STANDALONE;

  public MicroDocument ()
  {}

  public MicroDocument (@Nullable final IMicroDocumentType aDocType)
  {
    if (aDocType != null)
      appendChild (aDocType);
  }

  @Nonnull
  public EMicroNodeType getType ()
  {
    return EMicroNodeType.DOCUMENT;
  }

  public String getNodeName ()
  {
    return "#document";
  }

  private static boolean _canBeAppendedToDocumentRoot (@Nonnull final IMicroNode aNode)
  {
    return aNode.isDocumentType () || aNode.isProcessingInstruction () || aNode.isComment () || aNode.isElement ();
  }

  @Override
  protected void onAppendChild (@Nonnull final AbstractMicroNode aChildNode)
  {
    if (!_canBeAppendedToDocumentRoot (aChildNode))
      throw new MicroException ("Cannot add nodes of type " + aChildNode + " to a document");

    // Ensure that only one element is appended to the document root
    if (aChildNode.isElement () && directGetChildren () != null && !directGetChildren ().isEmpty ())
      for (final IMicroNode aCurChild : directGetChildren ())
        if (aCurChild.isElement ())
          throw new MicroException ("A document can only have one document element! Already has " +
                                    aCurChild +
                                    " and wants to add " +
                                    aChildNode);
    super.onAppendChild (aChildNode);
  }

  public boolean isStandalone ()
  {
    return m_bIsStandalone;
  }

  public void setStandalone (final boolean bIsStandalone)
  {
    m_bIsStandalone = bIsStandalone;
  }

  @Nullable
  public IMicroDocumentType getDocType ()
  {
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isDocumentType ())
          return (IMicroDocumentType) aChild;
    return null;
  }

  @Nullable
  public IMicroElement getDocumentElement ()
  {
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isElement ())
          return (IMicroElement) aChild;
    return null;
  }

  @Nonnull
  public IMicroDocument getClone ()
  {
    final MicroDocument ret = new MicroDocument ();
    if (hasChildren ())
      for (final IMicroNode aChildNode : getChildren ())
        ret.appendChild (aChildNode.getClone ());
    return ret;
  }
}
