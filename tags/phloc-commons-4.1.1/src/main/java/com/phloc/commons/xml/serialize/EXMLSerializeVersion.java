/**
 * Copyright (C) 2006-2014 phloc systems
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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.xml.EXMLVersion;

/**
 * XML serialization version.
 * 
 * @author Philip Helger
 */
public enum EXMLSerializeVersion implements IHasID <String>
{
  /** XML 1.0 */
  XML_10 ("xml10", EXMLVersion.XML_10),

  /** XML 1.1 */
  XML_11 ("xml11", EXMLVersion.XML_11),

  /** HTML */
  HTML ("html", null),

  /** XHTML */
  XHTML ("xhtml", EXMLVersion.XML_10);

  private final String m_sID;
  private final EXMLVersion m_eXMLVersion;

  private EXMLSerializeVersion (@Nonnull @Nonempty final String sID, @Nullable final EXMLVersion eXMLVersion)
  {
    m_sID = sID;
    m_eXMLVersion = eXMLVersion;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  /**
   * @return <code>true</code> if this is an XML version (1.0 or 1.1)
   */
  public boolean isXML ()
  {
    return this == XML_10 || this == XML_11;
  }

  /**
   * @return <code>true</code> if this is HTML (HTML or XHTML)
   */
  public boolean isHTML ()
  {
    return this == HTML || this == XHTML;
  }

  /**
   * @return <code>true</code> for all except HTML
   */
  public boolean requiresXMLDeclaration ()
  {
    return m_eXMLVersion != null;
  }

  /**
   * @return <code>null</code> if no XML version is required, the respective
   *         version string otherwise.
   * @see #requiresXMLDeclaration()
   */
  @Nullable
  public String getXMLVersionString ()
  {
    return m_eXMLVersion == null ? null : m_eXMLVersion.getVersion ();
  }

  /**
   * Get the {@link EXMLSerializeVersion} from the specified {@link EXMLVersion}
   * .
   * 
   * @param eXMLVersion
   *        XML version to query. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
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

  @Nullable
  public static EXMLSerializeVersion getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EXMLSerializeVersion.class, sID);
  }
}
