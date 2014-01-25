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

import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.xml.serialize.IXMLWriterSettings;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.settings.IReadonlySettings;
import com.phloc.settings.ISettings;
import com.phloc.settings.factory.DefaultSettingsFactory;
import com.phloc.settings.factory.ISettingsFactory;
import com.phloc.settings.xchange.AbstractSettingsPersistence;

public class SettingsPersistenceXML extends AbstractSettingsPersistence
{
  public static final boolean DEFAULT_MARSHAL_TYPES = true;

  private final boolean m_bMarshalTypes;
  private final IXMLWriterSettings m_aXWS;
  private final ISettingsFactory m_aSettingsFactory;

  public SettingsPersistenceXML ()
  {
    this (DEFAULT_MARSHAL_TYPES);
  }

  public SettingsPersistenceXML (final boolean bMarshalTypes)
  {
    this (bMarshalTypes, XMLWriterSettings.DEFAULT_XML_SETTINGS, DefaultSettingsFactory.getInstance ());
  }

  public SettingsPersistenceXML (final boolean bMarshalTypes,
                                 @Nonnull final IXMLWriterSettings aXWS,
                                 @Nonnull final ISettingsFactory aSettingsFactory)
  {
    super (aXWS.getCharsetObj ());
    if (aSettingsFactory == null)
      throw new NullPointerException ("SettingsFactory");
    m_bMarshalTypes = bMarshalTypes;
    m_aXWS = aXWS;
    m_aSettingsFactory = aSettingsFactory;
  }

  @Nonnull
  public ISettings readSettings (@Nonnull @WillClose final InputStream aIS)
  {
    if (aIS == null)
      throw new NullPointerException ("inputStream");

    final IMicroDocument aDoc = MicroReader.readMicroXML (aIS);
    if (aDoc == null)
      throw new IllegalArgumentException ("Passed XML document is illegal");

    // read items
    final SettingsMicroDocumentConverter aConverter = new SettingsMicroDocumentConverter (m_bMarshalTypes,
                                                                                          m_aSettingsFactory);
    return aConverter.convertToNative (aDoc.getDocumentElement ());
  }

  @Nullable
  @OverrideOnDemand
  protected String getWriteNamespaceURI ()
  {
    return null;
  }

  @Nonnull
  @Nonempty
  @OverrideOnDemand
  protected String getWriteElementName ()
  {
    return "settings";
  }

  @Nonnull
  public ESuccess writeSettings (@Nonnull final IReadonlySettings aSettings, @Nonnull @WillClose final OutputStream aOS)
  {
    if (aOS == null)
      throw new NullPointerException ("outputStream");

    try
    {
      // Inside try so that OS is closed
      if (aSettings == null)
        throw new NullPointerException ("settings");

      // No event manager invocation on writing
      final SettingsMicroDocumentConverter aConverter = new SettingsMicroDocumentConverter (m_bMarshalTypes,
                                                                                            m_aSettingsFactory);
      final IMicroDocument aDoc = new MicroDocument ();
      aDoc.appendChild (aConverter.convertToMicroElement (aSettings, getWriteNamespaceURI (), getWriteElementName ()));

      // auto-closes the stream
      return MicroWriter.writeToStream (aDoc, aOS, m_aXWS);
    }
    finally
    {
      StreamUtils.close (aOS);
    }
  }
}
