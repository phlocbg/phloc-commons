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

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.settings.IReadonlySettings;
import com.phloc.settings.impl.Settings;
import com.phloc.settings.impl.SettingsWithDefault;

/**
 * The default implementation of {@link ISettingsFactory} creating a new
 * instance of {@link SettingsWithDefault} with a custom default.
 * 
 * @author Philip Helger
 */
public class SettingsFactoryNewInstanceWithDefault implements ISettingsFactory
{
  private final IReadonlySettings m_aDefaultSettings;

  public SettingsFactoryNewInstanceWithDefault (@Nonnull final IReadonlySettings aDefaultSettings)
  {
    m_aDefaultSettings = ValueEnforcer.notNull (aDefaultSettings, "DefaultSettings");
  }

  @Nonnull
  public IReadonlySettings getDefaultSettings ()
  {
    return m_aDefaultSettings;
  }

  @Nonnull
  public Settings create (@Nonnull @Nonempty final String sName)
  {
    return new SettingsWithDefault (sName, m_aDefaultSettings);
  }
}
