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
package com.phloc.commons.xml.transform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.error.IResourceError;
import com.phloc.commons.error.IResourceLocation;
import com.phloc.commons.error.ResourceError;
import com.phloc.commons.error.ResourceLocation;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstract implementation of a transformation {@link ErrorListener}.
 * 
 * @author philip
 */
public abstract class AbstractTransformErrorListener implements ErrorListener
{
  private final ErrorListener m_aWrappedErrorListener;

  public AbstractTransformErrorListener (@Nullable final ErrorListener aWrappedErrorListener)
  {
    m_aWrappedErrorListener = aWrappedErrorListener;
  }

  @Nonnull
  private static IResourceError _buildError (final TransformerException ex,
                                             final EErrorLevel eErrorLevel,
                                             final String sPrefix)
  {
    final SourceLocator aLocator = ex.getLocator ();
    final String sResourceID = aLocator == null ? null : StringHelper.concatenateOnDemand (aLocator.getPublicId (),
                                                                                           "/",
                                                                                           aLocator.getSystemId ());
    final IResourceLocation aLocation = new ResourceLocation (sResourceID,
                                                              aLocator != null ? aLocator.getLineNumber ()
                                                                              : IResourceLocation.ILLEGAL_NUMBER,
                                                              aLocator != null ? aLocator.getColumnNumber ()
                                                                              : IResourceLocation.ILLEGAL_NUMBER,
                                                              null);
    return new ResourceError (aLocation, eErrorLevel, sPrefix, ex);
  }

  /**
   * Handle the passed resource error.
   * 
   * @param aResError
   *        The resource error to be handled. Never <code>null</code>.
   */
  protected abstract void log (@Nonnull final IResourceError aResError);

  public final void warning (final TransformerException ex) throws TransformerException
  {
    log (_buildError (ex, EErrorLevel.WARN, "Transformation warning"));

    if (m_aWrappedErrorListener != null)
      m_aWrappedErrorListener.warning (ex);
  }

  public final void error (final TransformerException ex) throws TransformerException
  {
    log (_buildError (ex, EErrorLevel.ERROR, "Transformation error"));

    if (m_aWrappedErrorListener != null)
      m_aWrappedErrorListener.error (ex);
  }

  public final void fatalError (final TransformerException ex) throws TransformerException
  {
    log (_buildError (ex, EErrorLevel.FATAL_ERROR, "Transformation fatal error"));

    if (m_aWrappedErrorListener != null)
      m_aWrappedErrorListener.fatalError (ex);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("wrappedListener", m_aWrappedErrorListener).toString ();
  }
}
