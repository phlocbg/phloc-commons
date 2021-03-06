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
package com.phloc.settings.xchange;

import java.io.File;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StringInputStream;
import com.phloc.commons.state.ESuccess;
import com.phloc.settings.IReadonlySettings;
import com.phloc.settings.ISettings;

/**
 * Base implementation of {@link ISettingsPersistence} that handles the special
 * typed implementations with String and File.
 * 
 * @author Philip Helger
 */
public abstract class AbstractSettingsPersistence implements ISettingsPersistence
{
  private final Charset m_aCharset;

  /**
   * Constructor
   * 
   * @param aCharset
   *        The charset to use. May not be <code>null</code>.
   */
  public AbstractSettingsPersistence (@Nonnull final Charset aCharset)
  {
    m_aCharset = ValueEnforcer.notNull (aCharset, "Charset");
  }

  /**
   * @return The charset passed in the constructor. Never <code>null</code>.
   */
  @Nonnull
  public final Charset getCharset ()
  {
    return m_aCharset;
  }

  @Nonnull
  public ISettings readSettings (@Nonnull final String sSettings)
  {
    ValueEnforcer.notNull (sSettings, "Settings");

    return readSettings (new StringInputStream (sSettings, m_aCharset));
  }

  @Nonnull
  public ISettings readSettings (@Nonnull final File aFile)
  {
    ValueEnforcer.notNull (aFile, "File");

    return readSettings (FileUtils.getInputStream (aFile));
  }

  @Nonnull
  public ISettings readSettings (@Nonnull final IInputStreamProvider aISP)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");

    return readSettings (aISP.getInputStream ());
  }

  @Nullable
  public String writeSettings (@Nonnull final IReadonlySettings aSettings)
  {
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    if (writeSettings (aSettings, aBAOS).isFailure ())
      return null;
    return aBAOS.getAsString (m_aCharset);
  }

  @Nullable
  public ESuccess writeSettings (@Nonnull final IReadonlySettings aSettings, @Nonnull final File aFile)
  {
    return writeSettings (aSettings, FileUtils.getOutputStream (aFile));
  }
}
