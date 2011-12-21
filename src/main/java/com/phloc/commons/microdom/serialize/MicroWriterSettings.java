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
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.namespace.NamespaceContext;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.serialize.EXMLSerializeComments;
import com.phloc.commons.xml.serialize.EXMLSerializeDocType;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.EXMLSerializeIndent;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

/**
 * Default implementation of the {@link IMicroWriterSettings} interface.<br>
 * Describes the export settings for the MicroWriter. Defaults to indented and
 * aligned XML in the UTF-8 charset.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MicroWriterSettings extends XMLWriterSettings implements IMicroWriterSettings
{
  @SuppressWarnings ("hiding")
  public static final IMicroWriterSettings DEFAULT_XML_SETTINGS = new MicroWriterSettings ();

  private EXMLVersion m_eXMLVersion = EXMLVersion.XML_10;

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

  @Override
  @Nonnull
  public MicroWriterSettings setFormat (@Nonnull final EXMLSerializeFormat eFormat)
  {
    super.setFormat (eFormat);
    return this;
  }

  @Override
  @Nonnull
  public MicroWriterSettings setSerializeDocType (@Nonnull final EXMLSerializeDocType eSerializeDocType)
  {
    super.setSerializeDocType (eSerializeDocType);
    return this;
  }

  @Override
  @Nonnull
  public MicroWriterSettings setSerializeComments (@Nonnull final EXMLSerializeComments eSerializeComments)
  {
    super.setSerializeComments (eSerializeComments);
    return this;
  }

  @Override
  @Nonnull
  public MicroWriterSettings setIndent (@Nonnull final EXMLSerializeIndent eIndent)
  {
    super.setIndent (eIndent);
    return this;
  }

  @Override
  @Nonnull
  public MicroWriterSettings setIncorrectCharacterHandling (@Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharacterHandling)
  {
    super.setIncorrectCharacterHandling (eIncorrectCharacterHandling);
    return this;
  }

  @Override
  @Nonnull
  public MicroWriterSettings setCharset (@Nonnull final String sCharset)
  {
    super.setCharset (sCharset);
    return this;
  }

  @Override
  @Nonnull
  public MicroWriterSettings setNamespaceContext (@Nullable final NamespaceContext aNamespaceContext)
  {
    super.setNamespaceContext (aNamespaceContext);
    return this;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    if (!(o instanceof MicroWriterSettings))
      return false;
    final MicroWriterSettings rhs = (MicroWriterSettings) o;
    return m_eXMLVersion.equals (rhs.m_eXMLVersion);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_eXMLVersion).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("xmlVersion", m_eXMLVersion).toString ();
  }
}
