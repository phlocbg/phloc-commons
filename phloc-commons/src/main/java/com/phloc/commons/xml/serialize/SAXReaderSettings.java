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

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.NotThreadSafe;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.IExceptionHandler;
import com.phloc.commons.callback.LoggingExceptionHandler;
import com.phloc.commons.state.EChange;
import com.phloc.commons.xml.EXMLParserFeature;
import com.phloc.commons.xml.EXMLParserProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SAX reader settings
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class SAXReaderSettings implements ISAXReaderSettings
{
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // Default parser features
  @GuardedBy ("s_aRWLock")
  private static final EnumMap <EXMLParserFeature, Boolean> s_aDefaultFeatures = new EnumMap <EXMLParserFeature, Boolean> (EXMLParserFeature.class);

  // Default exception handler
  @GuardedBy ("s_aRWLock")
  private static IExceptionHandler <Throwable> s_aDefaultExceptionHandler = new XMLLoggingExceptionHandler ();

  static
  {
    // By default enabled in XMLFactory
    if (false)
    {
      s_aDefaultFeatures.put (EXMLParserFeature.NAMESPACES, Boolean.TRUE);
      s_aDefaultFeatures.put (EXMLParserFeature.SAX_NAMESPACE_PREFIXES, Boolean.TRUE);
    }
    if (false)
      s_aDefaultFeatures.put (EXMLParserFeature.AUGMENT_PSVI, Boolean.FALSE);
  }

  private EntityResolver m_aEntityResolver;
  private DTDHandler m_aDTDHandler;
  private ContentHandler m_aContentHandler;
  private ErrorHandler m_aErrorHandler;
  private final EnumMap <EXMLParserProperty, Object> m_aProperties = new EnumMap <EXMLParserProperty, Object> (EXMLParserProperty.class);
  private final EnumMap <EXMLParserFeature, Boolean> m_aFeatures = new EnumMap <EXMLParserFeature, Boolean> (EXMLParserFeature.class);
  private IExceptionHandler <Throwable> m_aExceptionHandler;

  /**
   * Default constructor
   */
  public SAXReaderSettings ()
  {
    // Set default values
    setExceptionHandler (getDefaultExceptionHandler ());
    m_aFeatures.putAll (getAllDefaultFeatureValues ());
  }

  @Nullable
  public EntityResolver getEntityResolver ()
  {
    return m_aEntityResolver;
  }

  @Nonnull
  public SAXReaderSettings setEntityResolver (@Nullable final EntityResolver aEntityResolver)
  {
    m_aEntityResolver = aEntityResolver;
    return this;
  }

  @Nullable
  public DTDHandler getDTDHandler ()
  {
    return m_aDTDHandler;
  }

  @Nonnull
  public SAXReaderSettings setDTDHandler (@Nullable final DTDHandler aDTDHandler)
  {
    m_aDTDHandler = aDTDHandler;
    return this;
  }

  @Nullable
  public ContentHandler getContentHandler ()
  {
    return m_aContentHandler;
  }

  @Nonnull
  public SAXReaderSettings setContentHandler (@Nullable final ContentHandler aContentHandler)
  {
    m_aContentHandler = aContentHandler;
    return this;
  }

  @Nullable
  public ErrorHandler getErrorHandler ()
  {
    return m_aErrorHandler;
  }

  @Nonnull
  public SAXReaderSettings setErrorHandler (@Nullable final ErrorHandler aErrorHandler)
  {
    m_aErrorHandler = aErrorHandler;
    return this;
  }

  @Nullable
  public LexicalHandler getLexicalHandler ()
  {
    return (LexicalHandler) m_aProperties.get (EXMLParserProperty.SAX_LEXICAL_HANDLER);
  }

  @Nonnull
  public SAXReaderSettings setLexicalHandler (@Nullable final LexicalHandler aLexicalHandler)
  {
    m_aProperties.put (EXMLParserProperty.SAX_LEXICAL_HANDLER, aLexicalHandler);
    return this;
  }

  @Nullable
  public DeclHandler getDeclarationHandler ()
  {
    return (DeclHandler) m_aProperties.get (EXMLParserProperty.SAX_DECLARATION_HANDLER);
  }

  @Nonnull
  public SAXReaderSettings setDeclarationHandler (@Nullable final DeclHandler aDeclHandler)
  {
    m_aProperties.put (EXMLParserProperty.SAX_DECLARATION_HANDLER, aDeclHandler);
    return this;
  }

  public boolean hasAnyProperties ()
  {
    return !m_aProperties.isEmpty ();
  }

  @Nullable
  public Object getPropertyValue (@Nullable final EXMLParserProperty eProperty)
  {
    return eProperty == null ? null : m_aProperties.get (eProperty);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <EXMLParserProperty, Object> getAllPropertyValues ()
  {
    return new EnumMap <EXMLParserProperty, Object> (m_aProperties);
  }

  @Nonnull
  public SAXReaderSettings setPropertyValue (@Nonnull final EXMLParserProperty eProperty,
                                             @Nullable final Object aPropertyValue)
  {
    if (eProperty == null)
      throw new NullPointerException ("property");

    if (aPropertyValue != null)
      m_aProperties.put (eProperty, aPropertyValue);
    else
      m_aProperties.remove (eProperty);
    return this;
  }

  @Nonnull
  public SAXReaderSettings setPropertyValues (@Nullable final Map <EXMLParserProperty, ?> aProperties)
  {
    if (aProperties != null)
      m_aProperties.putAll (aProperties);
    return this;
  }

  @Nonnull
  public EChange removePropertyValue (@Nullable final EXMLParserProperty eProperty)
  {
    return EChange.valueOf (eProperty != null && m_aProperties.remove (eProperty) != null);
  }

  @Nonnull
  public EChange removeAllPropertyValues ()
  {
    if (m_aProperties.isEmpty ())
      return EChange.UNCHANGED;
    m_aProperties.clear ();
    return EChange.CHANGED;
  }

  public boolean hasAnyFeature ()
  {
    return !m_aFeatures.isEmpty ();
  }

  @Nullable
  public Boolean getFeatureValue (@Nullable final EXMLParserFeature eFeature)
  {
    return eFeature == null ? null : m_aFeatures.get (eFeature);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <EXMLParserFeature, Boolean> getAllFeatureValues ()
  {
    return new EnumMap <EXMLParserFeature, Boolean> (m_aFeatures);
  }

  @Nonnull
  public SAXReaderSettings setFeatureValue (@Nonnull final EXMLParserFeature eFeature, final boolean bValue)
  {
    if (eFeature == null)
      throw new NullPointerException ("feature");

    m_aFeatures.put (eFeature, Boolean.valueOf (bValue));
    return this;
  }

  @Nonnull
  public SAXReaderSettings setFeatureValue (@Nonnull final EXMLParserFeature eFeature, @Nullable final Boolean aValue)
  {
    if (eFeature == null)
      throw new NullPointerException ("feature");

    if (aValue == null)
      m_aFeatures.remove (eFeature);
    else
      m_aFeatures.put (eFeature, aValue);
    return this;
  }

  @Nonnull
  public SAXReaderSettings setFeatureValues (@Nullable final Map <EXMLParserFeature, Boolean> aValues)
  {
    if (aValues != null)
      m_aFeatures.putAll (aValues);
    return this;
  }

  @Nonnull
  public EChange removeFeature (@Nullable final EXMLParserFeature eFeature)
  {
    return EChange.valueOf (eFeature != null && m_aFeatures.remove (eFeature) != null);
  }

  @Nonnull
  public EChange removeAllFeatures ()
  {
    if (m_aFeatures.isEmpty ())
      return EChange.UNCHANGED;
    m_aFeatures.clear ();
    return EChange.CHANGED;
  }

  @Nonnull
  public IExceptionHandler <Throwable> getExceptionHandler ()
  {
    return m_aExceptionHandler;
  }

  @Nonnull
  public SAXReaderSettings setExceptionHandler (@Nonnull final IExceptionHandler <Throwable> aExceptionHandler)
  {
    if (aExceptionHandler == null)
      throw new NullPointerException ("ExceptionHandler");

    m_aExceptionHandler = aExceptionHandler;
    return this;
  }

  /**
   * Check if the specified XML Parser feature is enabled by default or not.
   * 
   * @param eFeature
   *        Feature to check.
   * @return <code>null</code> if nothing is specified.
   */
  @Nullable
  @SuppressFBWarnings ("NP_BOOLEAN_RETURN_NULL")
  public static Boolean getDefaultParserFeatureValue (@Nullable final EXMLParserFeature eFeature)
  {
    if (eFeature == null)
      return null;

    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aDefaultFeatures.get (eFeature);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Get all default parser features
   * 
   * @return Never <code>null</code>
   */
  @Nonnull
  @ReturnsMutableCopy
  public static Map <EXMLParserFeature, Boolean> getAllDefaultFeatureValues ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return new EnumMap <EXMLParserFeature, Boolean> (s_aDefaultFeatures);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set a default parser feature that is automatically applied to all SAX
   * readings
   * 
   * @param eFeature
   *        The feature to set.
   * @param aValue
   *        Use <code>null</code> to remove a feature.
   */
  public static void setDefaultFeatureValue (@Nonnull final EXMLParserFeature eFeature, @Nullable final Boolean aValue)
  {
    if (eFeature == null)
      throw new NullPointerException ("feature");

    s_aRWLock.writeLock ().lock ();
    try
    {
      if (aValue == null)
        s_aDefaultFeatures.remove (eFeature);
      else
        s_aDefaultFeatures.put (eFeature, aValue);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return The default exception handler. By default it is an implementation
   *         of {@link LoggingExceptionHandler}. Never <code>null</code>.
   */
  @Nonnull
  public static IExceptionHandler <Throwable> getDefaultExceptionHandler ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aDefaultExceptionHandler;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set a new default SAX exception handler.
   * 
   * @param aExceptionHandler
   *        The new handler to be set. May not be <code>null</code>.
   */
  public static void setDefaultExceptionHandler (@Nonnull final IExceptionHandler <Throwable> aExceptionHandler)
  {
    if (aExceptionHandler == null)
      throw new NullPointerException ("ExceptionHandler");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aDefaultExceptionHandler = aExceptionHandler;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }
}
