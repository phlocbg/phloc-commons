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
package com.phloc.commons.xml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;

/**
 * XML version determinator.
 * 
 * @author boris
 */
public enum EXMLVersion
{
  /** XML 1.0 */
  XML_10 ("1.0"),

  /** XML 1.1 */
  XML_11 ("1.1");

  /**
   * The default XML version to be used, if none is specified. This resolves to
   * XML version 1.0
   */
  public static final EXMLVersion DEFAULT = XML_10;

  private final String m_sVersion;

  private EXMLVersion (@Nonnull @Nonempty final String sVersion)
  {
    m_sVersion = sVersion;
  }

  /**
   * @return The textual representation of the XML version number.
   */
  @Nonnull
  @Nonempty
  public String getVersion ()
  {
    return m_sVersion;
  }

  /**
   * Find the {@link EXMLVersion} object matching the passed version string.
   * 
   * @param sVersion
   *        The version string to search. May be <code>null</code>.
   * @return <code>null</code> if no such {@link EXMLVersion} object is present.
   */
  @Nullable
  public static EXMLVersion getFromVersionOrNull (@Nullable final String sVersion)
  {
    return getFromVersionOrDefault (sVersion, null);
  }

  /**
   * Find the {@link EXMLVersion} object matching the passed version string.
   * 
   * @param sVersion
   *        The version string to search. May be <code>null</code>.
   * @param eDefault
   *        The default version to be returned, if no such enum value is
   *        present. May be <code>null</code>.
   * @return The provided default version (which may be <code>null</code>) if no
   *         such {@link EXMLVersion} object is present.
   */
  @Nullable
  public static EXMLVersion getFromVersionOrDefault (@Nullable final String sVersion,
                                                     @Nullable final EXMLVersion eDefault)
  {
    if (StringHelper.hasText (sVersion))
      for (final EXMLVersion eVersion : values ())
        if (eVersion.getVersion ().equals (sVersion))
          return eVersion;
    // Not found
    return eDefault;
  }
}
