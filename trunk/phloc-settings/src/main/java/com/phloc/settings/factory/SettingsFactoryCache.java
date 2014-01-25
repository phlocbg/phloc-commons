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
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.cache.AbstractNotifyingCache;
import com.phloc.settings.ISettings;

/**
 * {@link ISettingsFactory} implementation that uses a cache and if an element
 * is not in the cache, another {@link ISettingsFactory} is invoked to retrieve
 * the underlying data.
 * 
 * @author philip
 */
@ThreadSafe
public final class SettingsFactoryCache extends AbstractNotifyingCache <String, ISettings>
{
  private final ISettingsFactory m_aCreationFactory;

  /**
   * Constructor
   * 
   * @param aCreationFactory
   *        The settings factory to be used to create new settings object if
   *        they are not in the cache.
   */
  public SettingsFactoryCache (@Nonnull final ISettingsFactory aCreationFactory)
  {
    super (SettingsFactoryCache.class.getName ());
    if (aCreationFactory == null)
      throw new NullPointerException ("creationFactory");
    m_aCreationFactory = aCreationFactory;
  }

  @Override
  protected ISettings getValueToCache (@Nonnull @Nonempty final String sSettingsName)
  {
    return m_aCreationFactory.create (sSettingsName);
  }
}
