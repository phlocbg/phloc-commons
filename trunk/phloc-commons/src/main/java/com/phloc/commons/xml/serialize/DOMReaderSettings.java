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
import javax.xml.validation.Schema;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import com.phloc.commons.ICloneable;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.IExceptionHandler;
import com.phloc.commons.callback.LoggingExceptionHandler;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.xml.EXMLParserFeature;
import com.phloc.commons.xml.EXMLParserProperty;
import com.phloc.commons.xml.XMLFactory;

/**
 * DOM reader settings
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class DOMReaderSettings implements ICloneable <DOMReaderSettings>, IDOMReaderSettings
{
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // Default exception handler
  @GuardedBy ("s_aRWLock")
  private static IExceptionHandler <Throwable> s_aDefaultExceptionHandler = new XMLLoggingExceptionHandler ();

  /** A constant object representing the default settings. */
  // Must be after RWLock!
  public static final IDOMReaderSettings DEFAULT_SETTINGS = new DOMReaderSettings ();

  // DocumentBuilderFactory properties
  private boolean m_bNamespaceAware = XMLFactory.DEFAULT_DOM_NAMESPACE_AWARE;
  private boolean m_bValidating = XMLFactory.DEFAULT_DOM_VALIDATING;
  private boolean m_bIgnoringElementContentWhitespace = XMLFactory.DEFAULT_DOM_IGNORING_ELEMENT_CONTENT_WHITESPACE;
  private boolean m_bExpandEntityReferences = XMLFactory.DEFAULT_DOM_EXPAND_ENTITY_REFERENCES;
  private boolean m_bIgnoringComments = XMLFactory.DEFAULT_DOM_IGNORING_COMMENTS;
  private boolean m_bCoalescing = XMLFactory.DEFAULT_DOM_COALESCING;
  private Schema m_aSchema;
  private boolean m_bXIncludeAware = XMLFactory.DEFAULT_DOM_XINCLUDE_AWARE;
  private final Map <EXMLParserProperty, Object> m_aProperties = new EnumMap <EXMLParserProperty, Object> (EXMLParserProperty.class);
  private final Map <EXMLParserFeature, Boolean> m_aFeatures = new EnumMap <EXMLParserFeature, Boolean> (EXMLParserFeature.class);

  // DocumentBuilder properties
  private EntityResolver m_aEntityResolver;
  private ErrorHandler m_aErrorHandler;

  // Handling properties
  private IExceptionHandler <Throwable> m_aExceptionHandler;

  /**
   * Constructor using the default settings.
   */
  public DOMReaderSettings ()
  {
    // Set default values
    setExceptionHandler (getDefaultExceptionHandler ());
  }

  /**
   * Copy constructor.
   * 
   * @param aOther
   *        The settings object to copy from. May not be <code>null</code>.
   */
  public DOMReaderSettings (@Nonnull final IDOMReaderSettings aOther)
  {
    if (aOther == null)
      throw new NullPointerException ("other");

    // DocumentBuilderFactory
    setNamespaceAware (aOther.isNamespaceAware ());
    setValidating (aOther.isValidating ());
    setIgnoringElementContentWhitespace (aOther.isIgnoringElementContentWhitespace ());
    setExpandEntityReferences (aOther.isExpandEntityReferences ());
    setIgnoringComments (aOther.isIgnoringComments ());
    setCoalescing (aOther.isCoalescing ());
    setSchema (aOther.getSchema ());
    setXIncludeAware (aOther.isXIncludeAware ());
    // DocumentBuilder
    setEntityResolver (aOther.getEntityResolver ());
    setErrorHandler (aOther.getErrorHandler ());
    // Custom
    setExceptionHandler (aOther.getExceptionHandler ());
  }

  public boolean isNamespaceAware ()
  {
    return m_bNamespaceAware;
  }

  @Nonnull
  public DOMReaderSettings setNamespaceAware (final boolean bNamespaceAware)
  {
    m_bNamespaceAware = bNamespaceAware;
    return this;
  }

  public boolean isValidating ()
  {
    return m_bValidating;
  }

  @Nonnull
  public DOMReaderSettings setValidating (final boolean bValidating)
  {
    m_bValidating = bValidating;
    return this;
  }

  public boolean isIgnoringElementContentWhitespace ()
  {
    return m_bIgnoringElementContentWhitespace;
  }

  @Nonnull
  public DOMReaderSettings setIgnoringElementContentWhitespace (final boolean bIgnoringElementContentWhitespace)
  {
    m_bIgnoringElementContentWhitespace = bIgnoringElementContentWhitespace;
    return this;
  }

  public boolean isExpandEntityReferences ()
  {
    return m_bExpandEntityReferences;
  }

  @Nonnull
  public DOMReaderSettings setExpandEntityReferences (final boolean bExpandEntityReferences)
  {
    m_bExpandEntityReferences = bExpandEntityReferences;
    return this;
  }

  public boolean isIgnoringComments ()
  {
    return m_bIgnoringComments;
  }

  @Nonnull
  public DOMReaderSettings setIgnoringComments (final boolean bIgnoringComments)
  {
    m_bIgnoringComments = bIgnoringComments;
    return this;
  }

  public boolean isCoalescing ()
  {
    return m_bCoalescing;
  }

  @Nonnull
  public DOMReaderSettings setCoalescing (final boolean bCoalescing)
  {
    m_bCoalescing = bCoalescing;
    return this;
  }

  @Nullable
  public Schema getSchema ()
  {
    return m_aSchema;
  }

  @Nonnull
  public DOMReaderSettings setSchema (@Nullable final Schema aSchema)
  {
    m_aSchema = aSchema;
    return this;
  }

  public boolean isXIncludeAware ()
  {
    return m_bXIncludeAware;
  }

  @Nonnull
  public DOMReaderSettings setXIncludeAware (final boolean bXIncludeAware)
  {
    m_bXIncludeAware = bXIncludeAware;
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
  public DOMReaderSettings setPropertyValue (@Nonnull final EXMLParserProperty eProperty,
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
  public DOMReaderSettings setAllPropertyValues (@Nullable final Map <EXMLParserProperty, ?> aProperties)
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

  public boolean requiresSeparateDocumentBuilderFactory ()
  {
    return m_bNamespaceAware != XMLFactory.DEFAULT_DOM_NAMESPACE_AWARE ||
           m_bValidating != XMLFactory.DEFAULT_DOM_VALIDATING ||
           m_bIgnoringElementContentWhitespace != XMLFactory.DEFAULT_DOM_IGNORING_ELEMENT_CONTENT_WHITESPACE ||
           m_bExpandEntityReferences != XMLFactory.DEFAULT_DOM_EXPAND_ENTITY_REFERENCES ||
           m_bIgnoringComments != XMLFactory.DEFAULT_DOM_IGNORING_COMMENTS ||
           m_bCoalescing != XMLFactory.DEFAULT_DOM_COALESCING ||
           m_aSchema != null ||
           m_bXIncludeAware != XMLFactory.DEFAULT_DOM_XINCLUDE_AWARE ||
           !m_aProperties.isEmpty () ||
           !m_aFeatures.isEmpty ();
  }

  @Nullable
  public EntityResolver getEntityResolver ()
  {
    return m_aEntityResolver;
  }

  @Nonnull
  public DOMReaderSettings setEntityResolver (@Nullable final EntityResolver aEntityResolver)
  {
    m_aEntityResolver = aEntityResolver;
    return this;
  }

  @Nullable
  public ErrorHandler getErrorHandler ()
  {
    return m_aErrorHandler;
  }

  @Nonnull
  public DOMReaderSettings setErrorHandler (@Nullable final ErrorHandler aErrorHandler)
  {
    m_aErrorHandler = aErrorHandler;
    return this;
  }

  @Nonnull
  public IExceptionHandler <Throwable> getExceptionHandler ()
  {
    return m_aExceptionHandler;
  }

  @Nonnull
  public DOMReaderSettings setExceptionHandler (@Nonnull final IExceptionHandler <Throwable> aExceptionHandler)
  {
    if (aExceptionHandler == null)
      throw new NullPointerException ("ExceptionHandler");

    m_aExceptionHandler = aExceptionHandler;
    return this;
  }

  @Nonnull
  public DOMReaderSettings getClone ()
  {
    return new DOMReaderSettings (this);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("namespaceAware", m_bNamespaceAware)
                                       .append ("validating", m_bValidating)
                                       .append ("ignoringElementContentWhitespace", m_bIgnoringElementContentWhitespace)
                                       .append ("expandEntityReferences", m_bExpandEntityReferences)
                                       .append ("ignoringComments", m_bIgnoringComments)
                                       .append ("coalescing", m_bCoalescing)
                                       .append ("schema", m_aSchema)
                                       .append ("XIncludeAware", m_bXIncludeAware)
                                       .append ("properties", m_aProperties)
                                       .append ("features", m_aFeatures)
                                       .append ("entityResolver", m_aEntityResolver)
                                       .append ("errorHandler", m_aErrorHandler)
                                       .append ("exceptionHandler", m_aExceptionHandler)
                                       .toString ();
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
   * Set a new global exception handler.
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
