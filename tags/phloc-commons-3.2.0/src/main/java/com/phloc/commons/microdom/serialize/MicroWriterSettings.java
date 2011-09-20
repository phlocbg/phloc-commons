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
package com.phloc.commons.microdom.serialize;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.serialize.EXMLSerializeComments;
import com.phloc.commons.xml.serialize.EXMLSerializeDocType;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.EXMLSerializeIndent;

/**
 * Default implementation of the {@link IMicroWriterSettings} interface.<br>
 * Describes the export settings for the MicroWriter. Defaults to indented and
 * aligned XML in the UTF-8 charset.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MicroWriterSettings implements IMicroWriterSettings
{
  public static final String DEFAULT_XML_CHARSET = CCharset.CHARSET_UTF_8;
  public static final IMicroWriterSettings DEFAULT_XML_SETTINGS = new MicroWriterSettings ();

  private EXMLSerializeFormat m_eFormat = EXMLSerializeFormat.XML;
  private EXMLVersion m_eXMLVersion = EXMLVersion.DEFAULT;
  private EXMLSerializeDocType m_eSerializeDocType = EXMLSerializeDocType.EMIT;
  private EXMLSerializeComments m_eSerializeComments = EXMLSerializeComments.EMIT;
  private EXMLSerializeIndent m_eIndent = EXMLSerializeIndent.INDENT_AND_ALIGN;
  private String m_sCharset = DEFAULT_XML_CHARSET;

  /**
   * Creates a default settings object with the following settings:
   * <ul>
   * <li>XML output</li>
   * <li>Indented</li>
   * <li>Aligned</li>
   * <li>with document type</li>
   * <li>with comments</li>
   * <li>Default character set</li>
   * </ul>
   */
  public MicroWriterSettings ()// NOPMD
  {}

  @Nonnull
  public MicroWriterSettings setFormat (@Nonnull final EXMLSerializeFormat eFormat)
  {
    if (eFormat == null)
      throw new NullPointerException ("format");
    m_eFormat = eFormat;
    return this;
  }

  @Nonnull
  public EXMLSerializeFormat getFormat ()
  {
    return m_eFormat;
  }

  @Nonnull
  public MicroWriterSettings setXMLVersion (@Nonnull final EXMLVersion eVersion)
  {
    if (eVersion == null)
      throw new NullPointerException ("version");
    m_eXMLVersion = eVersion;
    return this;
  }

  @Nonnull
  public EXMLVersion getXMLVersion ()
  {
    return m_eXMLVersion;
  }

  @Nonnull
  public MicroWriterSettings setSerializeDocType (@Nonnull final EXMLSerializeDocType eSerializeDocType)
  {
    if (eSerializeDocType == null)
      throw new NullPointerException ("serializeDocType");
    m_eSerializeDocType = eSerializeDocType;
    return this;
  }

  public EXMLSerializeDocType getSerializeDocType ()
  {
    return m_eSerializeDocType;
  }

  @Nonnull
  public MicroWriterSettings setSerializeComments (@Nonnull final EXMLSerializeComments eSerializeComments)
  {
    if (eSerializeComments == null)
      throw new NullPointerException ("serializeComments");
    m_eSerializeComments = eSerializeComments;
    return this;
  }

  public EXMLSerializeComments getSerializeComments ()
  {
    return m_eSerializeComments;
  }

  @Nonnull
  public MicroWriterSettings setIndent (@Nonnull final EXMLSerializeIndent eIndent)
  {
    if (eIndent == null)
      throw new NullPointerException ("indent");
    m_eIndent = eIndent;
    return this;
  }

  @Nonnull
  public EXMLSerializeIndent getIndent ()
  {
    return m_eIndent;
  }

  @Nonnull
  public MicroWriterSettings setCharset (@Nonnull final String sCharset)
  {
    if (StringHelper.hasNoText (sCharset))
      throw new IllegalArgumentException ("illegal charset passed");
    m_sCharset = sCharset;
    return this;
  }

  @Nonnull
  public String getCharset ()
  {
    return m_sCharset;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MicroWriterSettings))
      return false;
    final MicroWriterSettings rhs = (MicroWriterSettings) o;
    return m_eFormat.equals (rhs.m_eFormat) &&
           m_eXMLVersion.equals (rhs.m_eXMLVersion) &&
           m_eSerializeDocType.equals (rhs.m_eSerializeDocType) &&
           m_eSerializeComments.equals (rhs.m_eSerializeComments) &&
           m_eIndent.equals (rhs.m_eIndent) &&
           m_sCharset.equals (rhs.m_sCharset);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eFormat)
                                       .append (m_eXMLVersion)
                                       .append (m_eIndent)
                                       .append (m_eSerializeDocType)
                                       .append (m_eSerializeComments)
                                       .append (m_sCharset)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("format", m_eFormat)
                                       .append ("xmlVersion", m_eXMLVersion)
                                       .append ("serializeDocType", m_eSerializeDocType)
                                       .append ("serializeComments", m_eSerializeComments)
                                       .append ("indent", m_eIndent)
                                       .append ("charset", m_sCharset)
                                       .toString ();
  }
}
