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
package com.phloc.commons.xml.ls;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;

/**
 * A class that collects all requested resources.
 * 
 * @author philip
 */
@ThreadSafe
public final class CollectingLSResourceResolver implements LSResourceResolver
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final List <LSResourceData> m_aList = new ArrayList <LSResourceData> ();
  private final LSResourceResolver m_aParentResolver;

  public CollectingLSResourceResolver ()
  {
    this (null);
  }

  public CollectingLSResourceResolver (@Nullable final LSResourceResolver aParentResolver)
  {
    m_aParentResolver = aParentResolver;
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <LSResourceData> getAllRequestedResources ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.makeUnmodifiable (m_aList);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public LSInput resolveResource (final String sType,
                                  final String sNamespaceURI,
                                  final String sPublicId,
                                  final String sSystemId,
                                  final String sBaseURI)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aList.add (new LSResourceData (sType, sNamespaceURI, sPublicId, sSystemId, sBaseURI));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    // Pass to parent (if available)
    return m_aParentResolver == null ? null : m_aParentResolver.resolveResource (sType,
                                                                                 sNamespaceURI,
                                                                                 sPublicId,
                                                                                 sSystemId,
                                                                                 sBaseURI);
  }
}
