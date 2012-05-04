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
package com.phloc.commons.collections.attrs;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;

/**
 * Base class for all kind of string-object mapping container. This
 * implementation is a thread-safe wrapper around
 * {@link MapBasedAttributeContainer}!
 * 
 * @author philip
 */
@ThreadSafe
public class MapBasedAttributeContainerThreadSafe extends MapBasedAttributeContainer
{
  protected final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  public MapBasedAttributeContainerThreadSafe ()
  {
    super ();
  }

  public MapBasedAttributeContainerThreadSafe (@Nonnull final Map <String, Object> aMap)
  {
    super (aMap);
  }

  public MapBasedAttributeContainerThreadSafe (@Nonnull final IReadonlyAttributeContainer aCont)
  {
    if (aCont == null)
      throw new NullPointerException ("cont");
    m_aAttrs.putAll (aCont.getAllAttributes ());
  }

  @Override
  public boolean containsAttribute (@Nullable final String sName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.containsAttribute (sName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public Map <String, Object> getAllAttributes ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newMap (m_aAttrs);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nullable
  public Object getAttributeObject (@Nullable final String sName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.getAttributeObject (sName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  public EChange setAttribute (@Nonnull final String sName, @Nullable final Object aValue)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return super.setAttribute (sName, aValue);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  public EChange removeAttribute (@Nullable final String sName)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return super.removeAttribute (sName);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public Enumeration <String> getAttributeNames ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.getEnumeration (ContainerHelper.newSet (m_aAttrs.keySet ()));
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllAttributeNames ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (m_aAttrs.keySet ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public Collection <Object> getAllAttributeValues ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aAttrs.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nonnegative
  public int size ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public boolean isEmpty ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.isEmpty ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  public EChange clear ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return super.clear ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public boolean equals (final Object o)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.equals (o);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public int hashCode ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.hashCode ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.toString ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
