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
package com.phloc.settings.factory.xchange;

import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.IInputStreamResolver;
import com.phloc.commons.string.StringHelper;
import com.phloc.settings.ISettings;
import com.phloc.settings.factory.ISettingsFactory;
import com.phloc.settings.xchange.ISettingsPersistence;

/**
 * A settings resolver that uses the settings persistence interface to read
 * settings. The determination of {@link java.io.Reader} objects is performed by
 * an {@link IInputStreamResolver}.
 * 
 * @author philip
 */
public class PersistenceSettingsFactory implements ISettingsFactory
{
  private final ISettingsPersistence m_aPersistence;
  private final IInputStreamResolver m_aISResolver;

  /**
   * @param aPersistence
   *        The main persistence handler. May not be <code>null</code>.
   * @param aISResolver
   *        Resolve settings input stream from the name passed to
   *        {@link #create(String)}. May not be <code>null</code>.
   */
  public PersistenceSettingsFactory (@Nonnull final ISettingsPersistence aPersistence,
                                     @Nonnull final IInputStreamResolver aISResolver)
  {
    if (aPersistence == null)
      throw new NullPointerException ("persistence");
    if (aISResolver == null)
      throw new NullPointerException ("inputStreamResolver");
    m_aPersistence = aPersistence;
    m_aISResolver = aISResolver;
  }

  @Nonnull
  public ISettingsPersistence getPersistence ()
  {
    return m_aPersistence;
  }

  @Nonnull
  public IInputStreamResolver getISResolver ()
  {
    return m_aISResolver;
  }

  @Nullable
  public final ISettings create (@Nonnull @Nonempty final String sName)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("Name");

    // Resolve the settings input stream
    final InputStream aIS = m_aISResolver.getInputStream (sName);
    if (aIS == null)
      throw new IllegalArgumentException ("Failed to resolve input stream for settings '" + sName + "'");

    // And read it from the settings
    return m_aPersistence.readSettings (aIS);
  }
}
