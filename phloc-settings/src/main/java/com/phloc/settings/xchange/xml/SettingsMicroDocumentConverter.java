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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.settings.IReadonlySettings;
import com.phloc.settings.ISettings;
import com.phloc.settings.impl.Settings;

public class SettingsMicroDocumentConverter implements IMicroTypeConverter
{
  private static final String ELEMENT_SETTING = "setting";
  private static final String ATTR_NAME = "name";
  private static final String ATTR_CLASS = "class";
  private static final String ELEMENT_VALUE = "value";

  private final boolean m_bMarshalTypes;

  /**
   * Constructor that uses the default settings factory.
   * 
   * @param bMarshalTypes
   *        if <code>true</code> the types are emitted as well in the
   *        persistence unit. Use <code>false</code> to have a maximum
   *        interoperability.
   */
  public SettingsMicroDocumentConverter (final boolean bMarshalTypes)
  {
    m_bMarshalTypes = bMarshalTypes;
  }

  @Nonnull
  protected Class <?> getClassFromName (@Nullable final String sClass)
  {
    return sClass == null ? String.class : GenericReflection.getClassFromNameSafe (sClass);
  }

  @Nonnull
  public ISettings convertToNative (final IMicroElement aElement)
  {
    // Create new settings object
    final ISettings aSettings = new Settings (aElement.getAttribute (ATTR_NAME));

    // settings are only on the top-level
    for (final IMicroElement eSetting : aElement.getAllChildElements ())
    {
      final String sFieldName = eSetting.getAttribute (ATTR_NAME);
      final String sClass = eSetting.getAttribute (ATTR_CLASS);
      final Class <?> aDestClass = getClassFromName (sClass);
      final Object aValue = MicroTypeConverter.convertToNative (eSetting.getFirstChildElement (ELEMENT_VALUE),
                                                                aDestClass);
      aSettings.restoreValue (sFieldName, aValue);
    }
    return aSettings;
  }

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull final String sTagName)
  {
    final IReadonlySettings aSettings = (IReadonlySettings) aObject;
    final IMicroElement eRoot = new MicroElement (sNamespaceURI, sTagName);
    eRoot.setAttribute (ATTR_NAME, aSettings.getName ());

    // Sort fields to have them deterministic
    for (final String sFieldName : ContainerHelper.getSorted (aSettings.getAllFieldNames ()))
    {
      final Object aValue = aSettings.getValue (sFieldName);

      final IMicroElement eSetting = eRoot.appendElement (sNamespaceURI, ELEMENT_SETTING);
      eSetting.setAttribute (ATTR_NAME, sFieldName);
      if (m_bMarshalTypes)
        eSetting.setAttribute (ATTR_CLASS, aValue.getClass ().getName ());
      eSetting.appendChild (MicroTypeConverter.convertToMicroElement (aValue, ELEMENT_VALUE));
    }
    return eRoot;
  }
}
