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
package com.phloc.commons.xml;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Empty implementation of {@link IXMLIterationHandler} that does nothing.
 * 
 * @author philip
 */
public class DefaultXMLIterationHandler implements IXMLIterationHandler
{
  public DefaultXMLIterationHandler ()
  {}

  public void onDocumentStart (@Nullable final EXMLVersion eVersion,
                               @Nullable final String sEncoding,
                               final boolean bStandalone)
  {}

  public void onDocumentType (@Nonnull final String sQualifiedElementName,
                              @Nullable final String sPublicID,
                              @Nullable final String sSystemID)
  {}

  public void onProcessingInstruction (@Nonnull final String sTarget, @Nullable final String sData)
  {}

  public void onEntityReference (@Nonnull final String sEntityRef)
  {}

  public void onContentElementWhitspace (@Nullable final CharSequence aWhitespaces)
  {}

  public void onComment (@Nullable final String sComment)
  {}

  public void onText (@Nullable final String sText)
  {}

  public void onCDATA (@Nullable final String sText)
  {}

  public void onElementStart (@Nullable final String sNamespacePrefix,
                              @Nonnull final String sTagName,
                              @Nullable final Map <String, String> aAttrs,
                              final boolean bHasChildren)
  {}

  public void onElementEnd (@Nullable final String sNamespacePrefix, @Nonnull final String sTagName)
  {}
}
