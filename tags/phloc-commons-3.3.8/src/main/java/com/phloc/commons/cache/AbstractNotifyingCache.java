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
package com.phloc.commons.cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.state.EChange;

/**
 * A caching class that has the ability to fill itself with the abstract
 * getValueToCache(Object) method.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        Cache key type
 * @param <VALUETYPE>
 *        Cache value type
 */
@ThreadSafe
public abstract class AbstractNotifyingCache <KEYTYPE, VALUETYPE> extends AbstractCache <KEYTYPE, VALUETYPE>
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  public AbstractNotifyingCache (@Nonnull final String sCacheName)
  {
    super (sCacheName);
  }

  /**
   * This abstract method is invoked, once a new value needs to be put into the
   * cache. This method is invoked within a locked section.
   * 
   * @param aKey
   *        The key for which the value to cache is required.
   * @return The value to be cached. May not be <code>null</code>.
   */
  @Nonnull
  protected abstract VALUETYPE getValueToCache (@Nullable KEYTYPE aKey);

  @Override
  public final VALUETYPE getFromCache (@Nullable final KEYTYPE aKey)
  {
    VALUETYPE aValue;

    // read existing value
    m_aRWLock.readLock ().lock ();
    try
    {
      aValue = super.getFromCache (aKey);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
    if (aValue == null)
    {
      // put in new value!
      m_aRWLock.writeLock ().lock ();
      try
      {
        // Read again, in case the value was set between the two locking
        // sections
        aValue = super.getFromCache (aKey);
        if (aValue == null)
        {
          aValue = getValueToCache (aKey);
          // Just a consistency check
          if (aValue == null)
            throw new IllegalStateException ("The value to cache was null for key " + aKey);
          putInCache (aKey, aValue);
        }
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }
    }
    return aValue;
  }

  @Override
  @Nonnull
  public final EChange removeFromCache (@Nullable final KEYTYPE aKey)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return super.removeFromCache (aKey);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  public final EChange clearCache ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return super.clearCache ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }
}
