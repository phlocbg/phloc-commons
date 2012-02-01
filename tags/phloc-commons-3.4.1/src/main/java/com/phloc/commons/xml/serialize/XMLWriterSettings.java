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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;

/**
 * Default implementation of the {@link IXMLWriterSettings} interface.<br>
 * Describes the export settings for the MicroWriter. Defaults to indented and
 * aligned XML in the UTF-8 charset.
 * 
 * @author philip
 */
@NotThreadSafe
public final class XMLWriterSettings extends AbstractXMLWriterSettings <XMLWriterSettings>
{
  /** The default settings to use */
  public static final IXMLWriterSettings DEFAULT_XML_SETTINGS = new XMLWriterSettings ();

  /**
   * The suggested settings to be used. Difference from the default settings is
   * the invalid character handling
   */
  public static final IXMLWriterSettings SUGGESTED_XML_SETTINGS = new XMLWriterSettings ().setIncorrectCharacterHandling (EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING);

  /**
   * Creates a default settings object with the following settings:
   * <ul>
   * <li>XML output</li>
   * <li>XML version 1.0</li>
   * <li>with document type</li>
   * <li>with comments</li>
   * <li>Indented and aligned</li>
   * <li>Writing invalid characters to the file as is - may result in invalid
   * XML files</li>
   * <li>Default character set UTF-8</li>
   * <li>No namespace context</li>
   * </ul>
   */
  public XMLWriterSettings ()
  {}

  /**
   * Copy constructor.
   * 
   * @param aOther
   *        The object to copy the settings from. May not be <code>null</code>.
   */
  public XMLWriterSettings (@Nonnull final IXMLWriterSettings aOther)
  {
    super (aOther);
  }

  @Override
  public boolean equals (final Object o)
  {
    return super.equals (o);
  }

  @Override
  public int hashCode ()
  {
    return super.hashCode ();
  }
}
