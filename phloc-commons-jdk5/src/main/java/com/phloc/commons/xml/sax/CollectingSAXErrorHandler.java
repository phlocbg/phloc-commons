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
package com.phloc.commons.xml.sax;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.error.IHasResourceErrorGroup;
import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.commons.error.ResourceErrorGroup;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * An error handler implementation that stores all warnings, errors and fatal
 * errors.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class CollectingSAXErrorHandler extends AbstractSAXErrorHandler implements IHasResourceErrorGroup
{
  protected final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final ResourceErrorGroup m_aErrors = new ResourceErrorGroup ();

  public CollectingSAXErrorHandler ()
  {}

  public CollectingSAXErrorHandler (@Nullable final ErrorHandler aWrappedErrorHandler)
  {
    super (aWrappedErrorHandler);
  }

  @Override
  protected void internalLog (@Nonnull final EErrorLevel eErrorLevel, final SAXParseException aException)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aErrors.addResourceError (getSaxParseError (eErrorLevel, aException));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public IResourceErrorGroup getResourceErrors ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aErrors.getClone ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean containsAtLeastOneError ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aErrors.containsAtLeastOneError ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Clear all currently stored errors.
   * 
   * @return {@link EChange#CHANGED} if at least one item was cleared.
   */
  @Nonnull
  public EChange clearResourceErrors ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return m_aErrors.clear ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ToStringGenerator.getDerived (super.toString ()).append ("errors", m_aErrors).toString ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
