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
package com.phloc.commons.microdom.serialize;

import javax.annotation.Nonnull;

import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.serialize.EXMLSerializeComments;
import com.phloc.commons.xml.serialize.EXMLSerializeDocType;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.EXMLSerializeIndent;

/**
 * Interface for the settings to be used for serializing micro nodes.
 * 
 * @author philip
 */
public interface IMicroWriterSettings
{
  /**
   * @return Create XML or HTML? Default is <code>XML</code>.
   */
  @Nonnull
  EXMLSerializeFormat getFormat ();

  /**
   * @return The XML version to use. Default is <code>1.0</code>
   */
  @Nonnull
  EXMLVersion getXMLVersion ();

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
   * @return The charset to use. May never be <code>null</code>.
   */
  @Nonnull
  String getCharset ();
}
