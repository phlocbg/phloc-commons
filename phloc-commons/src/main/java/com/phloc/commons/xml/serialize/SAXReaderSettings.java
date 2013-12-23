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
import javax.annotation.concurrent.ThreadSafe;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.ext.LexicalHandler;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.IExceptionHandler;
import com.phloc.commons.callback.LoggingExceptionHandler;
import com.phloc.commons.xml.EXMLParserFeature;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SAX reader settings
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class SAXReaderSettings implements ISAXReaderSettings
{
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // Default parser features
  @GuardedBy ("s_aRWLock")
  private static final EnumMap <EXMLParserFeature, Boolean> s_aDefaultParserFeatures = new EnumMap <EXMLParserFeature, Boolean> (EXMLParserFeature.class);

  // Default exception handler
  @GuardedBy ("s_aRWLock")
  private static IExceptionHandler <Throwable> s_aDefaultExceptionHandler = new XMLLoggingExceptionHandler ();

  static
  {
    // By default enabled in XMLFactory
    if (false)
    {
      s_aDefaultParserFeatures.put (EXMLParserFeature.NAMESPACES, Boolean.TRUE);
      s_aDefaultParserFeatures.put (EXMLParserFeature.SAX_NAMESPACE_PREFIXES, Boolean.TRUE);
    }
    if (false)
      s_aDefaultParserFeatures.put (EXMLParserFeature.AUGMENT_PSVI, Boolean.FALSE);
  }

  private EntityResolver m_aEntityResolver;
  private DTDHandler m_aDTDHandler;
  private ContentHandler m_aContentHandler;
  private ErrorHandler m_aErrorHandler;
  private LexicalHandler m_aLexicalHandler;
  private final EnumMap <EXMLParserFeature, Boolean> m_aParserFeatures = new EnumMap <EXMLParserFeature, Boolean> (EXMLParserFeature.class);
  private IExceptionHandler <Throwable> m_aExceptionHandler;

  public SAXReaderSettings ()
  {
    // Set default values
    setExceptionHandler (getDefaultExceptionHandler ());
    m_aParserFeatures.putAll (getAllDefaultParserFeatureValues ());
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
    return m_aLexicalHandler;
  }

  @Nonnull
  public SAXReaderSettings setLexicalHandler (@Nullable final LexicalHandler aLexicalHandler)
  {
    m_aLexicalHandler = aLexicalHandler;
    return this;
  }

  @Nullable
  public Boolean getParserFeatureValue (@Nullable final EXMLParserFeature eFeature)
  {
    return eFeature == null ? null : m_aParserFeatures.get (eFeature);
  }

  public boolean hasParserFeatureValues ()
  {
    return !m_aParserFeatures.isEmpty ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <EXMLParserFeature, Boolean> getAllParserFeatureValues ()
  {
    return new EnumMap <EXMLParserFeature, Boolean> (m_aParserFeatures);
  }

  @Nonnull
  public SAXReaderSettings setParserFeatureValue (@Nonnull final EXMLParserFeature eFeature, final boolean bValue)
  {
    if (eFeature == null)
      throw new NullPointerException ("feature");

    m_aParserFeatures.put (eFeature, Boolean.valueOf (bValue));
    return this;
  }

  @Nonnull
  public SAXReaderSettings setParserFeatureValue (@Nonnull final EXMLParserFeature eFeature,
                                                  @Nullable final Boolean aValue)
  {
    if (eFeature == null)
      throw new NullPointerException ("feature");

    if (aValue == null)
      m_aParserFeatures.remove (eFeature);
    else
      m_aParserFeatures.put (eFeature, aValue);
    return this;
  }

  @Nonnull
  public SAXReaderSettings setParserFeatureValues (@Nullable final Map <EXMLParserFeature, Boolean> aValues)
  {
    if (aValues != null)
      m_aParserFeatures.putAll (aValues);
    return this;
  }

  @Nonnull
  public SAXReaderSettings removeParserFeatureValue (@Nonnull final EXMLParserFeature eFeature)
  {
    if (eFeature == null)
      throw new NullPointerException ("feature");

    m_aParserFeatures.remove (eFeature);
    return this;
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
      return s_aDefaultParserFeatures.get (eFeature);
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
  public static Map <EXMLParserFeature, Boolean> getAllDefaultParserFeatureValues ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return new EnumMap <EXMLParserFeature, Boolean> (s_aDefaultParserFeatures);
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
  public static void setDefaultParserFeatureValue (@Nonnull final EXMLParserFeature eFeature,
                                                   @Nullable final Boolean aValue)
  {
    if (eFeature == null)
      throw new NullPointerException ("feature");

    s_aRWLock.writeLock ().lock ();
    try
    {
      if (aValue == null)
        s_aDefaultParserFeatures.remove (eFeature);
      else
        s_aDefaultParserFeatures.put (eFeature, aValue);
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
