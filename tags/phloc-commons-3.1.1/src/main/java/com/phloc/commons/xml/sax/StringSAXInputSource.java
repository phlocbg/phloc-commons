/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.xml.sax;

import java.io.Reader;
import java.io.StringReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xml.sax.InputSource;

import com.phloc.commons.string.ToStringGenerator;

/**
 * Special {@link InputSource} implementation that reads from a predefined
 * String.
 * 
 * @author philip
 */
public final class StringSAXInputSource extends InputSource
{
  private final String m_sText;

  public StringSAXInputSource (@Nonnull final String sText)
  {
    this (sText, null);
  }

  public StringSAXInputSource (@Nonnull final String sText, @Nullable final String sSystemID)
  {
    if (sText == null)
      throw new NullPointerException ("resource");
    m_sText = sText;
    setSystemId (sSystemID);
  }

  @Override
  public Reader getCharacterStream ()
  {
    return new StringReader (m_sText);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("text", m_sText).append ("systemID", getSystemId ()).toString ();
  }
}
