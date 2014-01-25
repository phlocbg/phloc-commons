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
package com.phloc.settings.pool;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.cache.AbstractNotifyingCache;
import com.phloc.settings.ISettings;
import com.phloc.settings.factory.ISettingsFactory;

/**
 * A pool of settings based on a cache.
 * 
 * @author Philip Helger
 */
public class SettingsPool extends AbstractNotifyingCache <String, ISettings>
{
  private final ISettingsFactory m_aFactory;

  public SettingsPool (@Nonnull final ISettingsFactory aFactory)
  {
    super ("SettingsPool");
    if (aFactory == null)
      throw new NullPointerException ("factory");
    m_aFactory = aFactory;
  }

  @Override
  protected ISettings getValueToCache (@Nonnull @Nonempty final String sSettingName)
  {
    return m_aFactory.create (sSettingName);
  }
}
