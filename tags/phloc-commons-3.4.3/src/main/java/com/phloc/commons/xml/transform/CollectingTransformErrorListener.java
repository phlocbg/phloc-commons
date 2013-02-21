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
package com.phloc.commons.xml.transform;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.xml.transform.ErrorListener;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.error.IHasResourceErrorGroup;
import com.phloc.commons.error.IResourceError;
import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.commons.error.ResourceErrorGroup;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This implementation of {@link ErrorListener} saves all occurred
 * warnings/errors/fatals in a list for later evaluation.
 * 
 * @author philip
 */
@ThreadSafe
public final class CollectingTransformErrorListener extends AbstractTransformErrorListener implements
                                                                                          IHasResourceErrorGroup
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final ResourceErrorGroup m_aErrors = new ResourceErrorGroup ();

  public CollectingTransformErrorListener ()
  {
    this (null);
  }

  public CollectingTransformErrorListener (@Nullable final ErrorListener aWrappedErrorListener)
  {
    super (aWrappedErrorListener);
  }

  @Override
  protected void log (@Nonnull final IResourceError aResError)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aErrors.addResourceError (aResError);
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

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("errors", m_aErrors).toString ();
  }
}