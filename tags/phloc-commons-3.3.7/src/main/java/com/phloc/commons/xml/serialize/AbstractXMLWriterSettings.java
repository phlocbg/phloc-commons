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
package com.phloc.commons.xml.serialize;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.namespace.NamespaceContext;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.EXMLVersion;

/**
 * Default implementation of the {@link IXMLWriterSettings} interface.<br>
 * Describes the export settings for the MicroWriter. Defaults to indented and
 * aligned XML in the UTF-8 charset.
 * 
 * @author philip
 */
@NotThreadSafe
public abstract class AbstractXMLWriterSettings <T extends AbstractXMLWriterSettings <T>> implements IXMLWriterSettings
{
  public static final String DEFAULT_XML_CHARSET = CCharset.CHARSET_UTF_8;

  private EXMLSerializeFormat m_eFormat = EXMLSerializeFormat.XML;
  private EXMLVersion m_eXMLVersion = EXMLVersion.XML_10;
  private EXMLSerializeDocType m_eSerializeDocType = EXMLSerializeDocType.EMIT;
  private EXMLSerializeComments m_eSerializeComments = EXMLSerializeComments.EMIT;
  private EXMLSerializeIndent m_eIndent = EXMLSerializeIndent.INDENT_AND_ALIGN;
  private EXMLIncorrectCharacterHandling m_eIncorrectCharacterHandling = EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG;
  private String m_sCharset = DEFAULT_XML_CHARSET;
  private NamespaceContext m_aNamespaceContext;

  /**
   * Creates a default settings object with the following settings:
   * <ul>
   * <li>XML output</li>
   * <li>XML version 1.0</li>
   * <li>with document type</li>
   * <li>with comments</li>
   * <li>Indented and aligned</li>
   * <li>Writing invalid characters to the file as is - may result in invalid
   * XML files</li>
   * <li>Default character set UTF-8</li>
   * <li>No namespace context</li>
   * </ul>
   */
  public AbstractXMLWriterSettings ()// NOPMD
  {}

  /**
   * Copy constructor. Note: the namespace context is not deep-copied, so the
   * original reference is reused!
   * 
   * @param aOther
   *        The object to copy the settings from. May not be <code>null</code>.
   */
  public AbstractXMLWriterSettings (@Nonnull final IXMLWriterSettings aOther)
  {
    if (aOther == null)
      throw new NullPointerException ("other");
    setFormat (aOther.getFormat ());
    setXMLVersion (aOther.getXMLVersion ());
    setSerializeDocType (aOther.getSerializeDocType ());
    setSerializeComments (aOther.getSerializeComments ());
    setIndent (aOther.getIndent ());
    setIncorrectCharacterHandling (aOther.getIncorrectCharacterHandling ());
    setCharset (aOther.getCharset ());
    setNamespaceContext (aOther.getNamespaceContext ());
  }

  @SuppressWarnings ("unchecked")
  @Nonnull
  private T _thisAsT ()
  {
    return (T) this;
  }

  /**
   * Set the XML serialization format to use.
   * 
   * @param eFormat
   *        The new format. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final T setFormat (@Nonnull final EXMLSerializeFormat eFormat)
  {
    if (eFormat == null)
      throw new NullPointerException ("format");
    m_eFormat = eFormat;
    return _thisAsT ();
  }

  @Nonnull
  public final EXMLSerializeFormat getFormat ()
  {
    return m_eFormat;
  }

  /**
   * Set the preferred XML version to use.
   * 
   * @param eVersion
   *        The XML version. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final T setXMLVersion (@Nonnull final EXMLVersion eVersion)
  {
    if (eVersion == null)
      throw new NullPointerException ("version");
    m_eXMLVersion = eVersion;
    return _thisAsT ();
  }

  @Nonnull
  public final EXMLVersion getXMLVersion ()
  {
    return m_eXMLVersion;
  }

  /**
   * Set the way how to handle the doc type.
   * 
   * @param eSerializeDocType
   *        Doc type handling. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final T setSerializeDocType (@Nonnull final EXMLSerializeDocType eSerializeDocType)
  {
    if (eSerializeDocType == null)
      throw new NullPointerException ("serializeDocType");
    m_eSerializeDocType = eSerializeDocType;
    return _thisAsT ();
  }

  @Nonnull
  public final EXMLSerializeDocType getSerializeDocType ()
  {
    return m_eSerializeDocType;
  }

  /**
   * Set the way how comments should be handled.
   * 
   * @param eSerializeComments
   *        The comment handling. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final T setSerializeComments (@Nonnull final EXMLSerializeComments eSerializeComments)
  {
    if (eSerializeComments == null)
      throw new NullPointerException ("serializeComments");
    m_eSerializeComments = eSerializeComments;
    return _thisAsT ();
  }

  @Nonnull
  public final EXMLSerializeComments getSerializeComments ()
  {
    return m_eSerializeComments;
  }

  /**
   * Set the way how to indent/align
   * 
   * @param eIndent
   *        Indent and align definition. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final T setIndent (@Nonnull final EXMLSerializeIndent eIndent)
  {
    if (eIndent == null)
      throw new NullPointerException ("indent");
    m_eIndent = eIndent;
    return _thisAsT ();
  }

  @Nonnull
  public final EXMLSerializeIndent getIndent ()
  {
    return m_eIndent;
  }

  /**
   * Set the way how to handle invalid characters.
   * 
   * @param eIncorrectCharacterHandling
   *        The invalid character handling. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final T setIncorrectCharacterHandling (@Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharacterHandling)
  {
    if (eIncorrectCharacterHandling == null)
      throw new NullPointerException ("inccorectCharacterHandling");
    m_eIncorrectCharacterHandling = eIncorrectCharacterHandling;
    return _thisAsT ();
  }

  @Nonnull
  public final EXMLIncorrectCharacterHandling getIncorrectCharacterHandling ()
  {
    return m_eIncorrectCharacterHandling;
  }

  /**
   * Set the serialization charset.
   * 
   * @param sCharset
   *        The charset to be used. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final T setCharset (@Nonnull final String sCharset)
  {
    if (StringHelper.hasNoText (sCharset))
      throw new IllegalArgumentException ("illegal charset passed");
    m_sCharset = sCharset;
    return _thisAsT ();
  }

  @Nonnull
  public final String getCharset ()
  {
    return m_sCharset;
  }

  /**
   * Set the namespace context to be used.
   * 
   * @param aNamespaceContext
   *        The namespace context to be used. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final T setNamespaceContext (@Nullable final NamespaceContext aNamespaceContext)
  {
    m_aNamespaceContext = aNamespaceContext;
    return _thisAsT ();
  }

  @Nullable
  public final NamespaceContext getNamespaceContext ()
  {
    return m_aNamespaceContext;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractXMLWriterSettings <?> rhs = (AbstractXMLWriterSettings <?>) o;
    // namespace context does not necessarily implement equals/hashCode
    return m_eFormat.equals (rhs.m_eFormat) &&
           m_eXMLVersion.equals (rhs.m_eXMLVersion) &&
           m_eSerializeDocType.equals (rhs.m_eSerializeDocType) &&
           m_eSerializeComments.equals (rhs.m_eSerializeComments) &&
           m_eIndent.equals (rhs.m_eIndent) &&
           m_eIncorrectCharacterHandling.equals (rhs.m_eIncorrectCharacterHandling) &&
           m_sCharset.equals (rhs.m_sCharset);
  }

  @Override
  public int hashCode ()
  {
    // namespace context does not necessarily implement equals/hashCode
    return new HashCodeGenerator (this).append (m_eFormat)
                                       .append (m_eXMLVersion)
                                       .append (m_eSerializeDocType)
                                       .append (m_eSerializeComments)
                                       .append (m_eIndent)
                                       .append (m_eIncorrectCharacterHandling)
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
                                       .append ("incorrectCharHandling", m_eIncorrectCharacterHandling)
                                       .append ("charset", m_sCharset)
                                       .append ("namespaceContext", m_aNamespaceContext)
                                       .toString ();
  }
}
