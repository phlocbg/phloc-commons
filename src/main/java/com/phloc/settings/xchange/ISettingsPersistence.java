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
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.state.ESuccess;
import com.phloc.settings.IReadonlySettings;
import com.phloc.settings.ISettings;

/**
 * Interface for persisting settings via input- and output streams.
 * 
 * @author philip
 */
public interface ISettingsPersistence
{
  /**
   * Read settings from a String and convert it to an {@link ISettings} object.
   * 
   * @param sSettings
   *        The settings string. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, a non-<code>null</code>
   *         settings object otherwise.
   */
  @Nonnull
  ISettings readSettings (@Nonnull String sSettings);

  /**
   * Read settings from a file and convert it to an {@link ISettings} object.
   * 
   * @param aFile
   *        The settings file. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, a non-<code>null</code>
   *         settings object otherwise.
   */
  @Nonnull
  ISettings readSettings (@Nonnull File aFile);

  /**
   * Read settings from an InputStream provider and convert it to an
   * {@link ISettings} object.
   * 
   * @param aISP
   *        The InputStream provider to read from. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, a non-<code>null</code>
   *         settings object otherwise.
   */
  @Nonnull
  ISettings readSettings (@Nonnull IInputStreamProvider aISP);

  /**
   * Read settings from an input stream and convert it to an {@link ISettings}
   * object.
   * 
   * @param aIS
   *        The input stream to read from. May not be <code>null</code>. Must be
   *        closed by the implementing method.
   * @return <code>null</code> if reading failed, a non-<code>null</code>
   *         settings object otherwise.
   */
  @Nullable
  ISettings readSettings (@Nonnull @WillClose InputStream aIS);

  /**
   * Write settings to a string.
   * 
   * @param aSettings
   *        The settings to be written. May not be <code>null</code>.
   * @return The string representation of the settings. <code>null</code> when
   *         writing/conversion fails.
   */
  @Nullable
  String writeSettings (@Nonnull IReadonlySettings aSettings);

  /**
   * Write settings to a file.
   * 
   * @param aSettings
   *        The settings to be written. May not be <code>null</code>.
   * @param aFile
   *        The file where the settings should be written to. May not be
   *        <code>null</code>.
   * @return Success and never <code>null</code>.
   */
  @Nonnull
  ESuccess writeSettings (@Nonnull IReadonlySettings aSettings, @Nonnull File aFile);

  /**
   * Write settings to a stream.
   * 
   * @param aSettings
   *        The settings to be written. May not be <code>null</code>.
   * @param aOS
   *        The output stream where the settings should be written to. May not
   *        be <code>null</code>. After writing to the stream the output stream
   *        must be closed by the implementing method.
   * @return Success and never <code>null</code>.
   */
  @Nonnull
  ESuccess writeSettings (@Nonnull IReadonlySettings aSettings, @Nonnull @WillClose OutputStream aOS);
}
