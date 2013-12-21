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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import javax.xml.validation.Schema;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import com.phloc.commons.callback.IExceptionHandler;
import com.phloc.commons.callback.LoggingExceptionHandler;

/**
 * DOM reader settings
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class DOMReaderSettings
{
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // Default exception handler
  @GuardedBy ("s_aRWLock")
  private static IExceptionHandler <Throwable> s_aDefaultExceptionHandler = new XMLLoggingExceptionHandler ();

  private EntityResolver m_aEntityResolver;
  private Schema m_aSchema;
  private ErrorHandler m_aErrorHandler;
  private IExceptionHandler <Throwable> m_aExceptionHandler;

  public DOMReaderSettings ()
  {
    // Set default values
    setExceptionHandler (getDefaultExceptionHandler ());
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
