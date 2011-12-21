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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.namespace.NamespaceContext;

import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;

/**
 * Interface for the settings to be used for serializing XML nodes.
 * 
 * @author philip
 */
public interface IXMLWriterSettings
{
  /**
   * @return Create XML or HTML? Default is <code>XML</code>.
   */
  @Nonnull
  EXMLSerializeFormat getFormat ();

  /**
   * @return Write document type? Default is <code>true</code>.
   */
  @Nonnull
  EXMLSerializeDocType getSerializeDocType ();

  /**
   * @return Write comments? Default is <code>true</code>.
   */
  @Nonnull
  EXMLSerializeComments getSerializeComments ();

  /**
   * @return Indent code? Default is <code>indent and aligned</code>.
   */
  @Nonnull
  EXMLSerializeIndent getIndent ();

  /**
   * @return The incorrect character handling to be used. May not be
   *         <code>null</code>.
   */
  @Nonnull
  EXMLIncorrectCharacterHandling getIncorrectCharacterHandling ();

  /**
   * @return The charset to use. May never be <code>null</code>.
   */
  @Nonnull
  String getCharset ();

  /**
   * @return The namespace context to be used. May be <code>null</code> to
   *         dynamically create the namespace prefixes.
   */
  @Nullable
  NamespaceContext getNamespaceContext ();
}
