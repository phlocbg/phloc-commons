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

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sText;
  }

  @Nonnull
  @Nonempty
  public String getText ()
  {
    return m_sText;
  }

  @Nonnull
  public IMimeType buildMimeType (@Nonnull final String sContentSubType)
  {
    return new MimeType (this, sContentSubType);
  }

  public boolean isTypeOf (@Nullable final String sMimeType)
  {
    return StringHelper.startsWith (sMimeType, m_sText + CMimeType.CONTENTTYPE_SUBTYPE_SEPARATOR);
  }

  @Nullable
  public static EMimeContentType getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EMimeContentType.class, sID);
  }
}
