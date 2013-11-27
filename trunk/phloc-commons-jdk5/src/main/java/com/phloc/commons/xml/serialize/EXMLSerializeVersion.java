/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.commons.xml.serialize;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.xml.EXMLVersion;

/**
 * XML serialization version.
 * 
 * @author Philip Helger
 */
public enum EXMLSerializeVersion
{
  /** XML 1.0 */
  XML_10 (EXMLVersion.XML_10),

  /** XML 1.1 */
  XML_11 (EXMLVersion.XML_11),

  /** HTML */
  HTML (null),

  /** XHTML */
  XHTML (EXMLVersion.XML_10);

  private final EXMLVersion m_eXMLVersion;

  private EXMLSerializeVersion (@Nullable final EXMLVersion eXMLVersion)
  {
    m_eXMLVersion = eXMLVersion;
  }

  public boolean isXML ()
  {
    return this == XML_10 || this == XML_11;
  }

  public boolean isHTML ()
  {
    return this == HTML || this == XHTML;
  }

  public boolean requiresXMLDeclaration ()
  {
    return m_eXMLVersion != null;
  }

  @Nullable
  public String getXMLVersionString ()
  {
    return m_eXMLVersion == null ? null : m_eXMLVersion.getVersion ();
  }

  @Nonnull
  public static EXMLSerializeVersion getFromXMLVersionOrThrow (@Nonnull final EXMLVersion eXMLVersion)
  {
    switch (eXMLVersion)
    {
      case XML_10:
        return EXMLSerializeVersion.XML_10;
      case XML_11:
        return EXMLSerializeVersion.XML_11;
      default:
        throw new IllegalStateException ("Unsupported XML version " + eXMLVersion);
    }
  }
}
