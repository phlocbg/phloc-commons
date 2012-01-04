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
package com.phloc.commons.microdom.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
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

/**
 * The factory for creating MicroDOM objects.
 * 
 * @author philip
 */
@Immutable
@Deprecated
public final class MicroFactory
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final MicroFactory s_aInstance = new MicroFactory ();

  private MicroFactory ()
  {}

  /**
   * Create a new document type.
   * 
   * @param sQualifiedName
   *        Qualified name. May not be <code>null</code>.
   * @param sPublicID
   *        Public ID. May be <code>null</code>.
   * @param sSystemID
   *        System ID. May be <code>null</code> if the public ID is also
   *        <code>null</code>!
   * @return The document type
   */
  @Nonnull
  public static IMicroDocumentType newDocumentType (@Nonnull final String sQualifiedName,
                                                    @Nullable final String sPublicID,
                                                    @Nullable final String sSystemID)
  {
    return new MicroDocumentType (sQualifiedName, sPublicID, sSystemID);
  }

  /**
   * Create a new document without a document type.
   * 
   * @return The created document.
   */
  @Nonnull
  public static IMicroDocument newDocument ()
  {
    return new MicroDocument ();
  }

  @Nonnull
  public static IMicroDocument newDocument (@Nonnull final String sQualifiedName,
                                            @Nullable final String sPublicID,
                                            @Nullable final String sSystemID)
  {
    return new MicroDocument (new MicroDocumentType (sQualifiedName, sPublicID, sSystemID));
  }

  @Nonnull
  public static IMicroDocument newDocument (@Nullable final IMicroDocumentType aDocType)
  {
    return new MicroDocument (aDocType);
  }

  /**
   * Create a new comment.
   * 
   * @param sText
   *        The initial text. May be <code>null</code> or empty.
   * @return The created element.
   */
  @Nonnull
  public static IMicroComment newComment (@Nullable final CharSequence sText)
  {
    return new MicroComment (sText);
  }

  @Nonnull
  public static IMicroText newText (@Nullable final CharSequence sText)
  {
    return new MicroText (sText);
  }

  @Nonnull
  public static IMicroCDATA newCDATA (@Nullable final CharSequence sText)
  {
    return new MicroCDATA (sText);
  }

  @Nonnull
  public static IMicroEntityReference newEntityReference (@Nonnull @Nonempty final String sName)
  {
    return new MicroEntityReference (sName);
  }

  @Nonnull
  public static IMicroElement newElement (@Nonnull final String sTagName)
  {
    return new MicroElement (sTagName);
  }

  @Nonnull
  public static IMicroElement newElement (@Nullable final String sNamespaceURI, @Nonnull final String sTagName)
  {
    return new MicroElement (sNamespaceURI, sTagName);
  }

  @Nonnull
  public static IMicroContainer newContainer ()
  {
    return new MicroContainer ();
  }

  @Nonnull
  public static IMicroContainer newContainer (@Nullable final IMicroNode... aChildNodes)
  {
    return new MicroContainer (aChildNodes);
  }

  @Nonnull
  public static IMicroProcessingInstruction newProcessingInstruction (@Nonnull @Nonempty final String sTarget)
  {
    return new MicroProcessingInstruction (sTarget);
  }

  @Nonnull
  public static IMicroProcessingInstruction newProcessingInstruction (@Nonnull @Nonempty final String sTarget,
                                                                      @Nullable final String sData)
  {
    return new MicroProcessingInstruction (sTarget, sData);
  }
}
