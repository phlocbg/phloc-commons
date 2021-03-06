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
package com.phloc.commons.mime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.string.StringHelper;

/**
 * Determines the most well known MIME content types.
 * 
 * @author Philip Helger
 */
public enum EMimeContentType implements IHasID <String>
{
  APPLICATION ("application"),
  AUDIO ("audio"),
  EXAMPLE ("example"),
  IMAGE ("image"),
  MESSAGE ("message"),
  MODEL ("model"),
  MULTIPART ("multipart"),
  TEXT ("text"),
  VIDEO ("video"),
  _STAR ("*");

  private final String m_sText;

  private EMimeContentType (@Nonnull @Nonempty final String sText)
  {
    m_sText = sText;
  }

  /**
   * @return The text representation of this MIME content type.
   */
  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sText;
  }

  /**
   * @return The same as {@link #getID()}
   */
  @Nonnull
  @Nonempty
  public String getText ()
  {
    return m_sText;
  }

  /**
   * Build a new {@link MimeType} based on this MIME content type and the
   * provided sub type.
   * 
   * @param sContentSubType
   *        The content sub type to append. May neither be <code>null</code> nor
   *        empty.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public MimeType buildMimeType (@Nonnull @Nonempty final String sContentSubType)
  {
    return new MimeType (this, sContentSubType);
  }

  /**
   * Check if the passed MIME type has the same content type as this
   * 
   * @param sMimeType
   *        The MIME type string to be checked. May be <code>null</code>.
   * @return <code>true</code> if the passed MIME type has this content type,
   *         <code>false</code> otherwise
   */
  public boolean isTypeOf (@Nullable final String sMimeType)
  {
    return StringHelper.startsWith (sMimeType, m_sText + CMimeType.SEPARATOR_CONTENTTYPE_SUBTYPE);
  }

  @Nullable
  public static EMimeContentType getFromIDOrNull (@Nullable final String sID)
  {
    // As MIME types are not case sensitive, this method may not be either :)
    return EnumHelper.getFromIDCaseInsensitiveOrNull (EMimeContentType.class, sID);
  }
}
