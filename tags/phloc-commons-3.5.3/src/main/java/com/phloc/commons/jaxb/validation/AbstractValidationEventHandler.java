/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.commons.jaxb.validation;

import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.error.IResourceError;
import com.phloc.commons.error.IResourceLocation;
import com.phloc.commons.error.ResourceError;
import com.phloc.commons.error.ResourceLocation;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.xml.serialize.XMLWriter;

/**
 * An abstract implementation of the JAXB {@link ValidationEventHandler}
 * interface.
 * 
 * @author philip
 */
@NotThreadSafe
public abstract class AbstractValidationEventHandler implements ValidationEventHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractValidationEventHandler.class);

  private final ValidationEventHandler m_aOrigHandler;

  /**
   * Constructor not encapsulating any existing handler.
   */
  public AbstractValidationEventHandler ()
  {
    this (null);
  }

  /**
   * Constructor.
   * 
   * @param aOrigHandler
   *        Optional validation event handler to be invoked after this handler.
   *        May be <code>null</code>.
   */
  public AbstractValidationEventHandler (@Nullable final ValidationEventHandler aOrigHandler)
  {
    m_aOrigHandler = aOrigHandler;
  }

  /**
   * @return The original validation event handler passed in the constructor.
   */
  @Nullable
  public ValidationEventHandler getOriginalHandler ()
  {
    return m_aOrigHandler;
  }

  /**
   * Get the error level matching the passed JAXB severity.
   * 
   * @param nSeverity
   *        The JAXB severity.
   * @return The matching {@link EErrorLevel}. Never <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected EErrorLevel getErrorLevel (final int nSeverity)
  {
    switch (nSeverity)
    {
      case ValidationEvent.WARNING:
        return EErrorLevel.WARN;
      case ValidationEvent.ERROR:
        return EErrorLevel.ERROR;
      case ValidationEvent.FATAL_ERROR:
        return EErrorLevel.FATAL_ERROR;
      default:
        s_aLogger.warn ("Unknown JAXB validation severity: " + nSeverity + "; defaulting to error");
        return EErrorLevel.ERROR;
    }
  }

  @Nullable
  private static String _getLocationResourceID (@Nullable final ValidationEventLocator aLocator)
  {
    if (aLocator != null)
    {
      // Source file found?
      final URL aURL = aLocator.getURL ();
      if (aURL != null)
        return aURL.toString ();

      // Source object found?
      final Object aObj = aLocator.getObject ();
      if (aObj != null)
        return "obj: " + aObj.toString ();

      // Source node found?
      final Node aNode = aLocator.getNode ();
      if (aNode != null)
        return XMLWriter.getXMLString (aNode);
    }
    return null;
  }

  protected abstract void onEvent (@Nonnull IResourceError aEvent);

  /**
   * Should the processing be continued? By default it is always continued, as
   * long as no fatal error occurs. This method is only invoked, if no original
   * handler is present.
   * 
   * @param eErrorLevel
   *        The error level to be checked.
   * @return <code>true</code> if processing should be continued,
   *         <code>false</code> if processing should stop.
   */
  @OverrideOnDemand
  protected boolean continueProcessing (@Nonnull final EErrorLevel eErrorLevel)
  {
    // Continue as long as it is no fatal error. On Fatal error stop!
    return eErrorLevel.isLessSevereThan (EErrorLevel.FATAL_ERROR);
  }

  public final boolean handleEvent (@Nonnull final ValidationEvent aEvent)
  {
    final EErrorLevel eErrorLevel = getErrorLevel (aEvent.getSeverity ());

    // call our callback
    final ValidationEventLocator aLocator = aEvent.getLocator ();
    final IResourceLocation aLocation = new ResourceLocation (_getLocationResourceID (aLocator),
                                                              aLocator != null ? aLocator.getLineNumber ()
                                                                              : IResourceLocation.ILLEGAL_NUMBER,
                                                              aLocator != null ? aLocator.getColumnNumber ()
                                                                              : IResourceLocation.ILLEGAL_NUMBER,
                                                              null);
    // Message may be null in some cases (e.g. when a linked exception is
    // present), but is not allowed to be null!
    String sMsg = aEvent.getMessage ();
    if (sMsg == null)
    {
      if (aEvent.getLinkedException () != null)
      {
        sMsg = aEvent.getLinkedException ().getMessage ();
        if (sMsg == null)
          sMsg = "Exception";
      }
      else
      {
        // Does this ever happen????
        sMsg = "Validation event";
      }
    }
    onEvent (new ResourceError (aLocation, eErrorLevel, sMsg, aEvent.getLinkedException ()));

    if (m_aOrigHandler != null)
    {
      // call original handler
      return m_aOrigHandler.handleEvent (aEvent);
    }

    // Continue processing?
    return continueProcessing (eErrorLevel);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("origHandler", m_aOrigHandler).toString ();
  }
}
