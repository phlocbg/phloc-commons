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
package com.phloc.settings.xchange.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.annotation.WillClose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.settings.IReadonlySettings;
import com.phloc.settings.ISettings;
import com.phloc.settings.factory.ISettingsFactory;
import com.phloc.settings.xchange.AbstractSettingsPersistence;

public class SettingsPersistenceProperties extends AbstractSettingsPersistence
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SettingsPersistenceProperties.class);

  private final ISettingsFactory m_aSettingsFactory;

  public SettingsPersistenceProperties (@Nonnull final ISettingsFactory aSettingsFactory)
  {
    super (CCharset.CHARSET_ISO_8859_1_OBJ);
    if (aSettingsFactory == null)
      throw new NullPointerException ("SettingsFactory");
    m_aSettingsFactory = aSettingsFactory;
  }

  @Nonnull
  @Nonempty
  protected String getReadSettingsName ()
  {
    return "anonymous";
  }

  @Nonnull
  public ISettings readSettings (@Nonnull @WillClose final InputStream aIS)
  {
    if (aIS == null)
      throw new NullPointerException ("inputStream");

    final Properties aProps = new Properties ();
    try
    {
      // Does not close IS!
      aProps.load (aIS);
    }
    catch (final IOException ex)
    {
      s_aLogger.error ("Failed to read properties file", ex);
    }
    finally
    {
      StreamUtils.close (aIS);
    }

    final ISettings aSettings = m_aSettingsFactory.create (getReadSettingsName ());
    for (final Map.Entry <Object, Object> aEntry : aProps.entrySet ())
      aSettings.setValue ((String) aEntry.getKey (), aEntry.getValue ());
    return aSettings;
  }

  @Nonnull
  public ESuccess writeSettings (@Nonnull final IReadonlySettings aSettings, @Nonnull @WillClose final OutputStream aOS)
  {
    if (aOS == null)
      throw new NullPointerException ("outputStream");

    try
    {
      final Properties aProps = new Properties ();
      for (final Map.Entry <String, Object> aEntry : aSettings.getAllEntries ().entrySet ())
        aProps.put (aEntry.getKey (), aEntry.getValue ());
      // Does not close the output stream!
      aProps.store (aOS, null);
      return ESuccess.SUCCESS;
    }
    catch (final IOException ex)
    {
      s_aLogger.error ("Failed to write settings to properties file", ex);
      return ESuccess.FAILURE;
    }
    finally
    {
      StreamUtils.close (aOS);
    }
  }
}
