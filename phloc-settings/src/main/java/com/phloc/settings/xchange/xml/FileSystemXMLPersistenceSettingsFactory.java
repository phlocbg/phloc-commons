/**
 * Copyright (C) 2013-2014 phloc systems
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
package com.phloc.settings.xchange.xml;

import java.io.File;

import javax.annotation.Nonnull;

import com.phloc.commons.io.resolver.FileSystemStreamResolver;

/**
 * A settings resolver that uses the standard XML settings persistence interface
 * to read settings. The determination of {@link java.io.InputStream} objects is
 * performed by an {@link FileSystemStreamResolver}.
 * 
 * @author philip
 */
public class FileSystemXMLPersistenceSettingsFactory extends PersistenceSettingsFactoryXML
{
  public FileSystemXMLPersistenceSettingsFactory (@Nonnull final File aBasePath)
  {
    super (new FileSystemStreamResolver (aBasePath));
  }
}
