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
package com.phloc.commons.xml.sax;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.error.IResourceError;
import com.phloc.commons.error.IResourceLocation;
import com.phloc.commons.error.ResourceError;
import com.phloc.commons.error.ResourceLocation;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * java.xml error handler base class.
 * 
 * @author Philip Helger
 */
public abstract class AbstractSAXErrorHandler implements ErrorHandler
{
  private final ErrorHandler m_aWrappedErrorHandler;

  /**
   * Constructor without parent error handler.
   */
  protected AbstractSAXErrorHandler ()
  {
    this (null);
  }

  /**
   * Constructor that takes a parent error handler to be called.
   * 
   * @param aWrappedErrorHandler
   *        The parent error handler. May be <code>null</code>.
   */
  public AbstractSAXErrorHandler (@Nullable final ErrorHandler aWrappedErrorHandler)
  {
    m_aWrappedErrorHandler = aWrappedErrorHandler;
  }

  /**
   * @return The wrapped error handler. May be <code>null</code>.
   */
  @Nullable
  public ErrorHandler getWrappedErrorHandler ()
  {
    return m_aWrappedErrorHandler;
  }

  /**
   * Utility method to convert a {@link SAXParseException} into a readable
   * string.
   * 
   * @param eErrorLevel
   *        The occurred error level. May not be <code>null</code>.
   * @param ex
   *        The exception to convert. May not be <code>null</code>.
   * @return The String representation.
   */
  @Nonnull
  protected static final IResourceError getSaxParseError (@Nonnull final EErrorLevel eErrorLevel,
                                                          @Nonnull final SAXParseException ex)
  {
    final String sResourceID = StringHelper.getConcatenatedOnDemand (ex.getPublicId (), "/", ex.getSystemId ());
    final IResourceLocation aLocation = new ResourceLocation (sResourceID, ex.getLineNumber (), ex.getColumnNumber ());
    return new ResourceError (aLocation, eErrorLevel, ex.getMessage ());
  }

  protected abstract void internalLog (@Nonnull EErrorLevel eErrorLevel, @Nonnull SAXParseException aException);

  public final void warning (final SAXParseException ex) throws SAXException
  {
    internalLog (EErrorLevel.WARN, ex);

    // Call parent error handler if available
    final ErrorHandler aWrappedErrorHandler = getWrappedErrorHandler ();
    if (aWrappedErrorHandler != null)
      aWrappedErrorHandler.warning (ex);
  }

  public final void error (final SAXParseException ex) throws SAXException
  {
    internalLog (EErrorLevel.ERROR, ex);

    // Call parent error handler if available
    final ErrorHandler aWrappedErrorHandler = getWrappedErrorHandler ();
    if (aWrappedErrorHandler != null)
      aWrappedErrorHandler.error (ex);
  }

  public final void fatalError (final SAXParseException ex) throws SAXException
  {
    internalLog (EErrorLevel.FATAL_ERROR, ex);

    // Call parent error handler if available
    final ErrorHandler aWrappedErrorHandler = getWrappedErrorHandler ();
    if (aWrappedErrorHandler != null)
      aWrappedErrorHandler.fatalError (ex);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("wrappedErrorHandler", m_aWrappedErrorHandler).toString ();
  }
}
