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
package com.phloc.commons.jaxb.utils;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.IWritableResource;
import com.phloc.commons.jaxb.JAXBContextCache;
import com.phloc.commons.jaxb.validation.AbstractValidationEventHandler;
import com.phloc.commons.jaxb.validation.CollectingLoggingValidationEventHandlerFactory;
import com.phloc.commons.jaxb.validation.CollectingValidationEventHandler;
import com.phloc.commons.jaxb.validation.IValidationEventHandlerFactory;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.xml.XMLFactory;
import com.phloc.commons.xml.schema.XMLSchemaCache;
import com.phloc.commons.xml.transform.ResourceStreamResult;
import com.phloc.commons.xml.transform.TransformSourceFactory;

/**
 * This is the abstract reader and writer base class for JAXB enabled document
 * types.
 * 
 * @author philip
 */
@NotThreadSafe
public abstract class AbstractJAXBMarshaller <JAXBTYPE>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractJAXBMarshaller.class);

  private final Class <JAXBTYPE> m_aType;
  private final IReadableResource [] m_aXSDs;
  private IValidationEventHandlerFactory m_aVEHFactory = new CollectingLoggingValidationEventHandlerFactory ();
  private ValidationEventHandler m_aLastEventHandler;

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
                                    @Nonnull @Nonempty final List <? extends IReadableResource> aXSDs)
  {
    if (aType == null)
      throw new NullPointerException ("type");
    m_aType = aType;
    m_aXSDs = ArrayHelper.newArray (aXSDs, IReadableResource.class);
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
  @OverrideOnDemand
  protected boolean useJAXContextCache ()
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
    return ArrayHelper.isEmpty (m_aXSDs) ? null : XMLSchemaCache.getInstance ().getSchema (m_aXSDs);
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
    final JAXBContext aJAXBContext = useJAXContextCache () ? JAXBContextCache.getInstance ().getFromCache (aPackage)
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

  /**
   * Read a document from the specified file.
   * 
   * @param aFile
   *        The file to read. May not be <code>null</code>.
   * @return <code>null</code> in case reading fails.
   */
  @Nullable
  public final JAXBTYPE read (@Nonnull final File aFile)
  {
    if (aFile == null)
      throw new NullPointerException ("file");

    return read (TransformSourceFactory.create (aFile));
  }

  /**
   * Read a document from the specified resource.
   * 
   * @param aResource
   *        The resource to read. May not be <code>null</code>.
   * @return <code>null</code> in case reading fails.
   */
  @Nullable
  public final JAXBTYPE read (@Nonnull final IReadableResource aResource)
  {
    if (aResource == null)
      throw new NullPointerException ("resource");

    return read (TransformSourceFactory.create (aResource));
  }

  /**
   * Read a document from the specified DOM node.
   * 
   * @param aNode
   *        The DOM node to read. May not be <code>null</code>.
   * @return <code>null</code> in case reading fails.
   */
  @Nullable
  public final JAXBTYPE read (@Nonnull final Node aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    return read (TransformSourceFactory.create (aNode));
  }

  /**
   * Read a document from the specified source.
   * 
   * @param aSource
   *        The source to read. May not be <code>null</code>.
   * @return <code>null</code> in case reading fails.
   */
  @Nullable
  public final JAXBTYPE read (@Nonnull final Source aSource)
  {
    if (aSource == null)
      throw new NullPointerException ("source");

    try
    {
      final Unmarshaller aUnmarshaller = _createUnmarshaller ();
      return aUnmarshaller.unmarshal (aSource, m_aType).getValue ();
    }
    catch (final UnmarshalException ex)
    {
      // The JAXB specification does not mandate how the JAXB provider
      // must behave when attempting to unmarshal invalid XML data. In
      // those cases, the JAXB provider is allowed to terminate the
      // call to unmarshal with an UnmarshalException.
      s_aLogger.error ("Unmarshal exception reading document", ex);
    }
    catch (final JAXBException ex)
    {
      s_aLogger.warn ("JAXB Exception reading document", ex);
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
    final JAXBContext aJAXBContext = useJAXContextCache () ? JAXBContextCache.getInstance ().getFromCache (aPackage)
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
    if (aObject == null)
      throw new NullPointerException ("object");

    final Document aDoc = XMLFactory.newDocument ();
    return write (aObject, new DOMResult (aDoc)).isSuccess () ? aDoc : null;
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
    return write (aObject, new StreamResult (aResultFile));
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
    return write (aObject, new ResourceStreamResult (aResource));
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
    if (aObject == null)
      throw new NullPointerException ("object");
    if (aResult == null)
      throw new NullPointerException ("result");

    try
    {
      final Marshaller aMarshaller = _createMarshaller ();
      final JAXBElement <JAXBTYPE> aJAXBElement = wrapObject (aObject);
      aMarshaller.marshal (aJAXBElement, aResult);
      return ESuccess.SUCCESS;
    }
    catch (final MarshalException ex)
    {
      s_aLogger.error ("Marshal exception writing object", ex);
    }
    catch (final JAXBException ex)
    {
      s_aLogger.warn ("JAXB Exception writing object", ex);
    }
    return ESuccess.FAILURE;
  }
}
