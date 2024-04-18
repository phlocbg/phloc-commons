/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.jaxb.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.IWritableResource;
import com.phloc.commons.io.streams.NonBlockingStringWriter;
import com.phloc.commons.jaxb.JAXBContextCache;
import com.phloc.commons.jaxb.JAXBMarshallerUtils;
import com.phloc.commons.jaxb.validation.AbstractValidationEventHandler;
import com.phloc.commons.jaxb.validation.CollectingLoggingValidationEventHandlerFactory;
import com.phloc.commons.jaxb.validation.CollectingValidationEventHandler;
import com.phloc.commons.jaxb.validation.IValidationEventHandlerFactory;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.xml.EXMLParserFeature;
import com.phloc.commons.xml.XMLFactory;
import com.phloc.commons.xml.sax.InputSourceFactory;
import com.phloc.commons.xml.schema.XMLSchemaCache;
import com.phloc.commons.xml.serialize.SAXReaderFactory;
import com.phloc.commons.xml.serialize.SAXReaderSettings;
import com.phloc.commons.xml.transform.TransformResultFactory;
import com.phloc.commons.xml.transform.TransformSourceFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.MarshalException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.UnmarshalException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.ValidationEventHandler;

/**
 * This is the abstract reader and writer base class for JAXB enabled document
 * types.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractJAXBMarshaller <JAXBTYPE>
{
  /**
   * Default read secure
   */
  public static final boolean DEFAULT_READ_SECURE = true;
  /**
   * Default write formatted
   */
  public static final boolean DEFAULT_WRITE_FORMATTED = false;
  /**
   * Default Formatted
   */
  @Deprecated
  public static final boolean DEFAULT_FORMATTED = DEFAULT_WRITE_FORMATTED;

  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractJAXBMarshaller.class);

  private final Class <JAXBTYPE> m_aType;
  private final List <IReadableResource> m_aXSDs = new ArrayList <IReadableResource> ();
  private IValidationEventHandlerFactory m_aVEHFactory = new CollectingLoggingValidationEventHandlerFactory ();
  private ValidationEventHandler m_aLastEventHandler;
  private boolean m_bReadSecure = DEFAULT_READ_SECURE;
  private boolean m_bWriteFormatted = DEFAULT_WRITE_FORMATTED;

  /**
   * Constructor.
   * 
   * @param aType
   *        The class of the JAXB document implementation type. May not be
   *        <code>null</code>.
   * @param aXSD
   *        The XSD used to validate document. May be <code>null</code>
   *        indicating that no XSD check is needed.
   */
  protected AbstractJAXBMarshaller (@Nonnull final Class <JAXBTYPE> aType, @Nullable final IReadableResource aXSD)
  {
    m_aType = ValueEnforcer.notNull (aType, "Type");
    if (aXSD != null)
      m_aXSDs.add (aXSD);
  }

  /**
   * Constructor.
   * 
   * @param aType
   *        The class of the JAXB document implementation type. May not be
   *        <code>null</code>.
   * @param aXSDs
   *        The XSDs used to validate document. May be <code>null</code> or
   *        empty indicating, that no XSD check is needed.
   */
  protected AbstractJAXBMarshaller (@Nonnull final Class <JAXBTYPE> aType,
                                    @Nullable final List <? extends IReadableResource> aXSDs)
  {
    m_aType = ValueEnforcer.notNull (aType, "Type");
    if (aXSDs != null)
    {
      ValueEnforcer.notEmptyNoNullValue (aXSDs, "XSDs");
      m_aXSDs.addAll (aXSDs);
    }
  }

  /**
   * Constructor.
   * 
   * @param aType
   *        The class of the JAXB document implementation type. May not be
   *        <code>null</code>.
   * @param aXSDs
   *        The XSDs used to validate document. May be <code>null</code> or
   *        empty indicating, that no XSD check is needed.
   */
  protected AbstractJAXBMarshaller (@Nonnull final Class <JAXBTYPE> aType, @Nullable final IReadableResource... aXSDs)
  {
    m_aType = ValueEnforcer.notNull (aType, "Type");
    if (aXSDs != null)
    {
      ValueEnforcer.notEmptyNoNullValue (aXSDs, "XSDs");
      for (final IReadableResource aXSD : aXSDs)
        m_aXSDs.add (aXSD);
    }
  }

  /**
   * Set another factory to be used to create {@link ValidationEventHandler}
   * objects. By default a
   * {@link CollectingLoggingValidationEventHandlerFactory} is used.
   * 
   * @param aVEHFactory
   *        The new factory to be used. May be <code>null</code>.
   */
  public final void setValidationEventHandlerFactory (@Nullable final IValidationEventHandlerFactory aVEHFactory)
  {
    m_aVEHFactory = aVEHFactory;
  }

  /**
   * @return The currently used validation event handler factory. By default an
   *         instance of {@link CollectingLoggingValidationEventHandlerFactory}
   *         is used. May be <code>null</code> if explicitly set.
   */
  @Nullable
  public final IValidationEventHandlerFactory getValidationEventHandlerFactory ()
  {
    return m_aVEHFactory;
  }

  /**
   * Get the last created validation event handler. This may be required when
   * collecting all errors using a {@link CollectingValidationEventHandler}.
   * 
   * @return The last created validation event handler. Or <code>null</code> if
   *         none was created so far.
   */
  @Nullable
  public final ValidationEventHandler getLastValidationEventHandler ()
  {
    return m_aLastEventHandler;
  }

  /**
   * Get the last created collecting validation event handler. This may be
   * required when collecting all errors.
   * 
   * @return The last created collecting validation event handler. Or
   *         <code>null</code> if none was created so far.
   */
  @Nullable
  public final CollectingValidationEventHandler getCollectingValidationEventHandler ()
  {
    ValidationEventHandler aHandler = m_aLastEventHandler;
    while (aHandler != null)
    {
      if (aHandler instanceof CollectingValidationEventHandler)
      {
        // Take the first match!
        return (CollectingValidationEventHandler) aHandler;
      }

      if (aHandler instanceof AbstractValidationEventHandler)
      {
        // Go to the parent handler
        aHandler = ((AbstractValidationEventHandler) aHandler).getWrappedHandler ();
      }
      else
      {
        // Don't know how to descend
        aHandler = null;
      }
    }

    return null;
  }

  /**
   * Get the parsing errors from the last read/write actions. Works only if the
   * last created validation event handler is a
   * {@link CollectingValidationEventHandler} or wraps one.
   * 
   * @return All events for evaluation or <code>null</code> in case no
   *         {@link CollectingValidationEventHandler} is present.
   */
  @Nullable
  public final IResourceErrorGroup getLastValidationErrors ()
  {
    final CollectingValidationEventHandler aHandler = getCollectingValidationEventHandler ();
    return aHandler == null ? null : aHandler.getResourceErrors ();
  }

  /**
   * Clear the latest parsing errors. Works only if the last created validation
   * event handler is a {@link CollectingValidationEventHandler} or wraps one.
   * 
   * @return {@link EChange#CHANGED} if a
   *         {@link CollectingValidationEventHandler} was found, and at least
   *         one element was removed from it.
   */
  @Nonnull
  public final EChange clearLastValidationErrors ()
  {
    final CollectingValidationEventHandler aHandler = getCollectingValidationEventHandler ();
    return aHandler == null ? EChange.UNCHANGED : aHandler.clearResourceErrors ();
  }

  /**
   * Enable or disable secure reading. Secure reading means that documents are
   * checked for XXE and XML bombs (infinite entity expansions). By default
   * secure reading is enabled.
   * 
   * @param bReadSecure
   *        <code>true</code> to read secure, <code>false</code> to disable
   *        secure reading.
   * @return {@link EChange}
   */
  @Nonnull
  public final EChange setReadSecure (final boolean bReadSecure)
  {
    if (bReadSecure == m_bReadSecure)
      return EChange.UNCHANGED;
    m_bReadSecure = bReadSecure;
    return EChange.CHANGED;
  }

  /**
   * @return Whether or not secure reading is enabled
   */
  public final boolean isReadSecure ()
  {
    return m_bReadSecure;
  }

  /**
   * Change the way formatting happens when calling write.
   * 
   * @param bWriteFormatted
   *        <code>true</code> to write formatted output.
   * @return {@link EChange}
   */
  @Nonnull
  public final EChange setWriteFormatted (final boolean bWriteFormatted)
  {
    if (bWriteFormatted == m_bWriteFormatted)
      return EChange.UNCHANGED;
    m_bWriteFormatted = bWriteFormatted;
    return EChange.CHANGED;
  }

  /**
   * @return Whether or not formatted writing is enabled
   */
  public final boolean isWriteFormatted ()
  {
    return m_bWriteFormatted;
  }

  /**
   * @return A list of all XSD resources used for validation. Never
   *         <code>null</code>.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public final List <IReadableResource> getOriginalXSDs ()
  {
    return ContainerHelper.newList (m_aXSDs);
  }

  /**
   * Should the {@link JAXBContextCache} be used? Since creating the JAXB
   * context is quite cost intensive this is recommended.
   * 
   * @return <code>true</code> if the {@link JAXBContextCache} should be used,
   *         <code>false</code> otherwise. It's <code>true</code> by default.
   */
  @SuppressWarnings ("deprecation")
  @OverrideOnDemand
  protected boolean useJAXBContextCache ()
  {
    return true;
  }

  /**
   * @return The validation schema to be used. May be <code>null</code>
   *         indicating that no validation is required.
   */
  @Nullable
  @OverrideOnDemand
  protected Schema createValidationSchema ()
  {
    return m_aXSDs.isEmpty () ? null : XMLSchemaCache.getInstance ().getSchema (m_aXSDs);
  }

  /**
   * @return The JAXB unmarshaller to use. Never <code>null</code>.
   * @throws JAXBException
   *         In case the creation fails.
   */
  @Nonnull
  private Unmarshaller _createUnmarshaller () throws JAXBException
  {
    final Package aPackage = m_aType.getPackage ();
    final JAXBContext aJAXBContext = useJAXBContextCache () ? JAXBContextCache.getInstance ().getFromCache (aPackage)
                                                            : JAXBContext.newInstance (aPackage.getName ());

    // create an Unmarshaller
    final Unmarshaller aUnmarshaller = aJAXBContext.createUnmarshaller ();
    if (m_aVEHFactory != null)
    {
      // Create and set a new event handler
      m_aLastEventHandler = m_aVEHFactory.create (aUnmarshaller.getEventHandler ());
      aUnmarshaller.setEventHandler (m_aLastEventHandler);
    }
    else
      m_aLastEventHandler = null;

    // Set XSD (if any)
    final Schema aValidationSchema = createValidationSchema ();
    if (aValidationSchema != null)
      aUnmarshaller.setSchema (aValidationSchema);

    return aUnmarshaller;
  }

  @Nullable
  private JAXBTYPE _readSecurelyFromInputSource (@Nonnull final InputSource aInputSource)
  {
    // Initialize settings with defaults
    final SAXReaderSettings aSettings = new SAXReaderSettings ();
    if (m_bReadSecure)
    {
      // Apply settings that make reading more secure
      aSettings.setFeatureValues (EXMLParserFeature.AVOID_XML_ATTACKS);
    }

    // Create new XML reader
    final org.xml.sax.XMLReader aParser = SAXReaderFactory.createXMLReader ();

    // Apply settings
    aSettings.applyToSAXReader (aParser);

    return read (new SAXSource (aParser, aInputSource));
  }

  /**
   * Read a document from the specified file. The secure reading feature has
   * affect when using this method.
   * 
   * @param aFile
   *        The file to read. May not be <code>null</code>.
   * @return <code>null</code> in case reading fails.
   */
  @Nullable
  public final JAXBTYPE read (@Nonnull final File aFile)
  {
    ValueEnforcer.notNull (aFile, "File");

    return _readSecurelyFromInputSource (InputSourceFactory.create (aFile));
  }

  /**
   * Read a document from the specified resource. The secure reading feature has
   * affect when using this method.
   * 
   * @param aResource
   *        The resource to read. May not be <code>null</code>.
   * @return <code>null</code> in case reading fails.
   */
  @Nullable
  public final JAXBTYPE read (@Nonnull final IReadableResource aResource)
  {
    ValueEnforcer.notNull (aResource, "Resource");

    return _readSecurelyFromInputSource (InputSourceFactory.create (aResource));
  }

  /**
   * Read a document from the specified input stream. The secure reading feature
   * has affect when using this method.
   * 
   * @param aIS
   *        The input stream to read. May not be <code>null</code>.
   * @return <code>null</code> in case reading fails.
   */
  @Nullable
  public final JAXBTYPE read (@Nonnull final InputStream aIS)
  {
    ValueEnforcer.notNull (aIS, "InputStream");

    return _readSecurelyFromInputSource (InputSourceFactory.create (aIS));
  }

  /**
   * Read a document from the specified String. The secure reading feature has
   * affect when using this method.
   * 
   * @param sXML
   *        The XML string to read. May not be <code>null</code>.
   * @return <code>null</code> in case reading fails.
   */
  @Nullable
  public final JAXBTYPE read (@Nonnull final String sXML)
  {
    ValueEnforcer.notNull (sXML, "XML");

    return _readSecurelyFromInputSource (InputSourceFactory.create (sXML));
  }

  /**
   * Read a document from the specified DOM node. The secure reading feature has
   * <b>NO</b> affect when using this method because no parsing happens! To
   * ensure secure reading the Node must first be serialized to a String and be
   * parsed again!
   * 
   * @param aNode
   *        The DOM node to read. May not be <code>null</code>.
   * @return <code>null</code> in case reading fails.
   */
  @Nullable
  public final JAXBTYPE read (@Nonnull final Node aNode)
  {
    ValueEnforcer.notNull (aNode, "Node");

    return read (TransformSourceFactory.create (aNode));
  }

  /**
   * Customize the passed unmarshaller before unmarshalling (reading) something.
   * 
   * @param aUnmarshaller
   *        The object to customize. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void customizeUnmarshaller (@Nonnull final Unmarshaller aUnmarshaller)
  {
    // empty
  }

  /**
   * Handles a possible read exception
   * 
   * @param ex
   *        Exception
   */
  @OverrideOnDemand
  protected void handleReadException (@Nonnull final JAXBException ex)
  {
    if (ex instanceof UnmarshalException)
    {
      // The JAXB specification does not mandate how the JAXB provider
      // must behave when attempting to unmarshal invalid XML data. In
      // those cases, the JAXB provider is allowed to terminate the
      // call to unmarshal with an UnmarshalException.
      final Throwable aLinked = ((UnmarshalException) ex).getLinkedException ();
      if (aLinked instanceof SAXParseException)
        s_aLogger.error ("Failed to parse XML document: " + aLinked.getMessage ());
      else
        s_aLogger.error ("Unmarshal exception reading document", ex);
    }
    else
      s_aLogger.warn ("JAXB Exception reading document", ex);
  }

  /**
   * Read a document from the specified source. The secure reading feature has
   * <b>NO</b> affect when using this method because the parameter type is too
   * generic.
   * 
   * @param aSource
   *        The source to read. May not be <code>null</code>.
   * @return <code>null</code> in case reading fails.
   */
  @Nullable
  public final JAXBTYPE read (@Nonnull final Source aSource)
  {
    ValueEnforcer.notNull (aSource, "Source");

    try
    {
      final Unmarshaller aUnmarshaller = _createUnmarshaller ();
      customizeUnmarshaller (aUnmarshaller);
      return aUnmarshaller.unmarshal (aSource, m_aType).getValue ();
    }
    catch (final JAXBException ex)
    {
      handleReadException (ex);
    }
    return null;
  }

  /**
   * @return A marshaller for converting document to XML. Never
   *         <code>null</code>.
   * @throws JAXBException
   *         In case of an error.
   */
  @Nonnull
  private Marshaller _createMarshaller () throws JAXBException
  {
    final Package aPackage = m_aType.getPackage ();
    final JAXBContext aJAXBContext = useJAXBContextCache () ? JAXBContextCache.getInstance ().getFromCache (aPackage)
                                                            : JAXBContext.newInstance (aPackage.getName ());

    // create an Unmarshaller
    final Marshaller aMarshaller = aJAXBContext.createMarshaller ();
    if (m_aVEHFactory != null)
    {
      // Create and set the event handler
      m_aLastEventHandler = m_aVEHFactory.create (aMarshaller.getEventHandler ());
      aMarshaller.setEventHandler (m_aLastEventHandler);
    }
    else
      m_aLastEventHandler = null;

    JAXBMarshallerUtils.setFormattedOutput (aMarshaller, m_bWriteFormatted);

    // Set XSD (if any)
    final Schema aValidationSchema = createValidationSchema ();
    if (aValidationSchema != null)
      aMarshaller.setSchema (aValidationSchema);

    return aMarshaller;
  }

  /**
   * Wrap the passed domain object into a {@link JAXBElement} for marshalling.
   * This can usually be done using the respective's package ObjectFactory
   * implementation.
   * 
   * @param aObject
   *        The object to be wrapped.
   * @return The {@link JAXBElement} wrapping the document.
   */
  @Nonnull
  protected abstract JAXBElement <JAXBTYPE> wrapObject (@Nonnull JAXBTYPE aObject);

  /**
   * Convert the passed object to a new DOM document
   * 
   * @param aObject
   *        The object to be converted. May not be <code>null</code>.
   * @return <code>null</code> if converting the document failed.
   */
  @Nullable
  public final Document write (@Nonnull final JAXBTYPE aObject)
  {
    ValueEnforcer.notNull (aObject, "Object");

    final Document aDoc = XMLFactory.newDocument ();
    return write (aObject, TransformResultFactory.create (aDoc)).isSuccess () ? aDoc : null;
  }

  /**
   * Write the passed object to a {@link File}.
   * 
   * @param aObject
   *        The object to be written. May not be <code>null</code>.
   * @param aResultFile
   *        The result file to be written to. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public final ESuccess write (@Nonnull final JAXBTYPE aObject, @Nonnull final File aResultFile)
  {
    return write (aObject, TransformResultFactory.create (aResultFile));
  }

  /**
   * Write the passed object to an {@link IWritableResource}.
   * 
   * @param aObject
   *        The object to be written. May not be <code>null</code>.
   * @param aResource
   *        The result resource to be written to. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public final ESuccess write (@Nonnull final JAXBTYPE aObject, @Nonnull final IWritableResource aResource)
  {
    return write (aObject, TransformResultFactory.create (aResource));
  }

  /**
   * Customize the passed marshaller before marshalling something.
   * 
   * @param aMarshaller
   *        The object to customize. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void customizeMarshaller (@Nonnull final Marshaller aMarshaller)
  {
    // empty
  }

  /**
   * Handles a write exception
   * 
   * @param ex
   *        Exception to handle
   */
  @OverrideOnDemand
  protected void handleWriteException (@Nonnull final JAXBException ex)
  {
    if (ex instanceof MarshalException)
      s_aLogger.error ("Marshal exception writing object", ex);
    else
      s_aLogger.warn ("JAXB Exception writing object", ex);
  }

  /**
   * Convert the passed object to XML.
   * 
   * @param aObject
   *        The object to be converted. May not be <code>null</code>.
   * @param aResult
   *        The result object holder. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public final ESuccess write (@Nonnull final JAXBTYPE aObject, @Nonnull final Result aResult)
  {
    ValueEnforcer.notNull (aObject, "Object");
    ValueEnforcer.notNull (aResult, "Result");

    try
    {
      final Marshaller aMarshaller = _createMarshaller ();
      customizeMarshaller (aMarshaller);

      final JAXBElement <JAXBTYPE> aJAXBElement = wrapObject (aObject);
      aMarshaller.marshal (aJAXBElement, aResult);
      return ESuccess.SUCCESS;
    }
    catch (final JAXBException ex)
    {
      handleWriteException (ex);
    }
    return ESuccess.FAILURE;
  }

  /**
   * Utility method to directly convert the passed domain object to an XML
   * string.
   * 
   * @param aObject
   *        The domain object to be converted. May not be <code>null</code>.
   * @return <code>null</code> if the passed domain object could not be
   *         converted because of validation errors.
   */
  @Nullable
  public final String getAsXMLString (@Nonnull final JAXBTYPE aObject)
  {
    final NonBlockingStringWriter aSW = new NonBlockingStringWriter ();
    return write (aObject, new StreamResult (aSW)).isSuccess () ? aSW.getAsString () : null;
  }
}
