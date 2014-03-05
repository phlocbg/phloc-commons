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
package com.phloc.commons.xml.serialize;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import com.phloc.commons.ICloneable;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.IExceptionHandler;
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
@NotThreadSafe
public class DOMReaderSettings implements ICloneable <DOMReaderSettings>, IDOMReaderSettings
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DOMReaderSettings.class);

  // DocumentBuilderFactory properties
  private boolean m_bNamespaceAware;
  private boolean m_bValidating;
  private boolean m_bIgnoringElementContentWhitespace;
  private boolean m_bExpandEntityReferences;
  private boolean m_bIgnoringComments;
  private boolean m_bCoalescing;
  private Schema m_aSchema;
  private boolean m_bXIncludeAware;
  private final EnumMap <EXMLParserProperty, Object> m_aProperties = new EnumMap <EXMLParserProperty, Object> (EXMLParserProperty.class);
  private final EnumMap <EXMLParserFeature, Boolean> m_aFeatures = new EnumMap <EXMLParserFeature, Boolean> (EXMLParserFeature.class);

  // DocumentBuilder properties
  private EntityResolver m_aEntityResolver;
  private ErrorHandler m_aErrorHandler;

  // Handling properties
  private IExceptionHandler <Throwable> m_aExceptionHandler;
  private boolean m_bRequiresNewXMLParserExplicitly;

  /**
   * Constructor using the default settings from
   * {@link DOMReaderDefaultSettings}.
   */
  public DOMReaderSettings ()
  {
    // DocumentBuilderFactory
    setNamespaceAware (DOMReaderDefaultSettings.isNamespaceAware ());
    setValidating (DOMReaderDefaultSettings.isValidating ());
    setIgnoringElementContentWhitespace (DOMReaderDefaultSettings.isIgnoringElementContentWhitespace ());
    setExpandEntityReferences (DOMReaderDefaultSettings.isExpandEntityReferences ());
    setIgnoringComments (DOMReaderDefaultSettings.isIgnoringComments ());
    setCoalescing (DOMReaderDefaultSettings.isCoalescing ());
    setSchema (DOMReaderDefaultSettings.getSchema ());
    setXIncludeAware (DOMReaderDefaultSettings.isXIncludeAware ());
    setPropertyValues (DOMReaderDefaultSettings.getAllPropertyValues ());
    setFeatureValues (DOMReaderDefaultSettings.getAllFeatureValues ());
    // DocumentBuilder
    setEntityResolver (DOMReaderDefaultSettings.getEntityResolver ());
    setErrorHandler (DOMReaderDefaultSettings.getErrorHandler ());
    // Custom
    setExceptionHandler (DOMReaderDefaultSettings.getExceptionHandler ());
    setRequiresNewXMLParserExplicitly (DOMReaderDefaultSettings.isRequiresNewXMLParserExplicitly ());
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
    setPropertyValues (aOther.getAllPropertyValues ());
    setFeatureValues (aOther.getAllFeatureValues ());
    // DocumentBuilder
    setEntityResolver (aOther.getEntityResolver ());
    setErrorHandler (aOther.getErrorHandler ());
    // Custom
    setExceptionHandler (aOther.getExceptionHandler ());
    setRequiresNewXMLParserExplicitly (aOther.isRequiresNewXMLParserExplicitly ());
  }

  public boolean isNamespaceAware ()
  {
    return m_bNamespaceAware;
  }

  @Nonnull
  public final DOMReaderSettings setNamespaceAware (final boolean bNamespaceAware)
  {
    m_bNamespaceAware = bNamespaceAware;
    return this;
  }

  public boolean isValidating ()
  {
    return m_bValidating;
  }

  @Nonnull
  public final DOMReaderSettings setValidating (final boolean bValidating)
  {
    m_bValidating = bValidating;
    return this;
  }

  public boolean isIgnoringElementContentWhitespace ()
  {
    return m_bIgnoringElementContentWhitespace;
  }

  @Nonnull
  public final DOMReaderSettings setIgnoringElementContentWhitespace (final boolean bIgnoringElementContentWhitespace)
  {
    m_bIgnoringElementContentWhitespace = bIgnoringElementContentWhitespace;
    return this;
  }

  public boolean isExpandEntityReferences ()
  {
    return m_bExpandEntityReferences;
  }

  @Nonnull
  public final DOMReaderSettings setExpandEntityReferences (final boolean bExpandEntityReferences)
  {
    m_bExpandEntityReferences = bExpandEntityReferences;
    return this;
  }

  public boolean isIgnoringComments ()
  {
    return m_bIgnoringComments;
  }

  @Nonnull
  public final DOMReaderSettings setIgnoringComments (final boolean bIgnoringComments)
  {
    m_bIgnoringComments = bIgnoringComments;
    return this;
  }

  public boolean isCoalescing ()
  {
    return m_bCoalescing;
  }

  @Nonnull
  public final DOMReaderSettings setCoalescing (final boolean bCoalescing)
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
  public final DOMReaderSettings setSchema (@Nullable final Schema aSchema)
  {
    m_aSchema = aSchema;
    return this;
  }

  public boolean isXIncludeAware ()
  {
    return m_bXIncludeAware;
  }

  @Nonnull
  public final DOMReaderSettings setXIncludeAware (final boolean bXIncludeAware)
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
  public final DOMReaderSettings setPropertyValue (@Nonnull final EXMLParserProperty eProperty,
                                                   @Nullable final Object aPropertyValue)
  {
    if (eProperty == null)
      throw new NullPointerException ("property");

    if (aPropertyValue != null &&
        eProperty.getValueClass () != null &&
        !eProperty.getValueClass ().isAssignableFrom (aPropertyValue.getClass ()))
      s_aLogger.warn ("Setting the XML parser property '" +
                      eProperty +
                      "' to a value of " +
                      aPropertyValue.getClass () +
                      " will most likely not be interpreted!");

    if (aPropertyValue != null)
      m_aProperties.put (eProperty, aPropertyValue);
    else
      m_aProperties.remove (eProperty);
    return this;
  }

  @Nonnull
  public final DOMReaderSettings setPropertyValues (@Nullable final Map <EXMLParserProperty, ?> aProperties)
  {
    if (aProperties != null)
      m_aProperties.putAll (aProperties);
    return this;
  }

  @Nonnull
  public final EChange removePropertyValue (@Nullable final EXMLParserProperty eProperty)
  {
    return EChange.valueOf (eProperty != null && m_aProperties.remove (eProperty) != null);
  }

  @Nonnull
  public final EChange removeAllPropertyValues ()
  {
    if (m_aProperties.isEmpty ())
      return EChange.UNCHANGED;
    m_aProperties.clear ();
    return EChange.CHANGED;
  }

  @Nullable
  public Locale getLocale ()
  {
    return (Locale) getPropertyValue (EXMLParserProperty.GENERAL_LOCALE);
  }

  public DOMReaderSettings setLocale (@Nullable final Locale aLocale)
  {
    return setPropertyValue (EXMLParserProperty.GENERAL_LOCALE, aLocale);
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
  public final DOMReaderSettings setFeatureValue (@Nonnull final EXMLParserFeature eFeature, final boolean bValue)
  {
    if (eFeature == null)
      throw new NullPointerException ("feature");

    m_aFeatures.put (eFeature, Boolean.valueOf (bValue));
    return this;
  }

  @Nonnull
  public final DOMReaderSettings setFeatureValue (@Nonnull final EXMLParserFeature eFeature,
                                                  @Nullable final Boolean aValue)
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
  public final DOMReaderSettings setFeatureValues (@Nullable final Map <EXMLParserFeature, Boolean> aValues)
  {
    if (aValues != null)
      m_aFeatures.putAll (aValues);
    return this;
  }

  @Nonnull
  public final EChange removeFeature (@Nullable final EXMLParserFeature eFeature)
  {
    return EChange.valueOf (eFeature != null && m_aFeatures.remove (eFeature) != null);
  }

  @Nonnull
  public final EChange removeAllFeatures ()
  {
    if (m_aFeatures.isEmpty ())
      return EChange.UNCHANGED;
    m_aFeatures.clear ();
    return EChange.CHANGED;
  }

  public boolean requiresNewXMLParser ()
  {
    // Force a new XML parser?
    if (m_bRequiresNewXMLParserExplicitly)
      return true;

    if (m_bNamespaceAware != XMLFactory.DEFAULT_DOM_NAMESPACE_AWARE ||
        m_bValidating != XMLFactory.DEFAULT_DOM_VALIDATING ||
        m_bIgnoringElementContentWhitespace != XMLFactory.DEFAULT_DOM_IGNORING_ELEMENT_CONTENT_WHITESPACE ||
        m_bExpandEntityReferences != XMLFactory.DEFAULT_DOM_EXPAND_ENTITY_REFERENCES ||
        m_bIgnoringComments != XMLFactory.DEFAULT_DOM_IGNORING_COMMENTS ||
        m_bCoalescing != XMLFactory.DEFAULT_DOM_COALESCING ||
        m_aSchema != null ||
        m_bXIncludeAware != XMLFactory.DEFAULT_DOM_XINCLUDE_AWARE ||
        !m_aProperties.isEmpty () ||
        !m_aFeatures.isEmpty ())
      return true;

    // Special case for JDK > 1.7.0_45 because of maximum entity expansion
    // See http://docs.oracle.com/javase/tutorial/jaxp/limits/limits.html
    return m_aEntityResolver != null;
  }

  @Deprecated
  public boolean requiresSeparateDocumentBuilderFactory ()
  {
    return requiresNewXMLParser ();
  }

  @Nullable
  public EntityResolver getEntityResolver ()
  {
    return m_aEntityResolver;
  }

  @Nonnull
  public final DOMReaderSettings setEntityResolver (@Nullable final EntityResolver aEntityResolver)
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
  public final DOMReaderSettings setErrorHandler (@Nullable final ErrorHandler aErrorHandler)
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
  public final DOMReaderSettings setExceptionHandler (@Nonnull final IExceptionHandler <Throwable> aExceptionHandler)
  {
    if (aExceptionHandler == null)
      throw new NullPointerException ("ExceptionHandler");

    m_aExceptionHandler = aExceptionHandler;
    return this;
  }

  public boolean isRequiresNewXMLParserExplicitly ()
  {
    return m_bRequiresNewXMLParserExplicitly;
  }

  @Nonnull
  public final DOMReaderSettings setRequiresNewXMLParserExplicitly (final boolean bRequiresNewXMLParserExplicitly)
  {
    m_bRequiresNewXMLParserExplicitly = bRequiresNewXMLParserExplicitly;
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
                                       .append ("requiresNewXMLParserExplicitly", m_bRequiresNewXMLParserExplicitly)
                                       .toString ();
  }
}
