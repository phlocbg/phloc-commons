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
package com.phloc.settings.factory;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.factory.IFactoryWithParameter;
import com.phloc.settings.ISettings;

/**
 * This is just a type definition for the settings factory. The factory
 * parameter is the name of the factory set to read. This may e.g. be a file
 * name for file based settings factories.
 * 
 * @author philip
 */
public interface ISettingsFactory extends IFactoryWithParameter <ISettings, String>
{
  /**
   * Create a new settings object.
   * 
   * @param sName
   *        The name of the settings. May neither be <code>null</code> nor
   *        empty.
   * @return The created settings object. May not be <code>null</code>.
   */
  @Nonnull
  ISettings create (@Nonnull @Nonempty String sName);
}
