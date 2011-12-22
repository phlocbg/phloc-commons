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

import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.xml.serialize.AbstractXMLWriterSettings;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

/**
 * This class is deprecated. Please use {@link XMLWriterSettings} instead.
 * 
 * @author philip
 */
@SuppressWarnings ("deprecation")
@NotThreadSafe
@Deprecated
public final class MicroWriterSettings extends AbstractXMLWriterSettings <MicroWriterSettings> implements
                                                                                              IMicroWriterSettings
{
  public static final IMicroWriterSettings DEFAULT_XML_SETTINGS = new MicroWriterSettings ();

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
  public MicroWriterSettings ()// NOPMD
  {}

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
