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
import com.phloc.commons.xml.serialize.IXMLWriterSettings;

/**
 * Interface for the settings to be used for serializing micro nodes.
 * 
 * @author philip
 */
public interface IMicroWriterSettings extends IXMLWriterSettings
{
  /**
   * @return The XML version to use. Default is <code>1.0</code>
   */
  @Nonnull
  EXMLVersion getXMLVersion ();
}
