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
package com.phloc.commons.url;

import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IURLData} interface.
 * 
 * @author philip
 */
@Immutable
public final class URLData implements IURLData
{
  private final String m_sPath;
  private final Map <String, String> m_aParams;
  private final String m_sAnchor;

  public URLData (@Nonnull final String sPath)
  {
    this (sPath, null, null);
  }

  public URLData (@Nonnull final String sPath, @Nullable final Map <String, String> aParams)
  {
    this (sPath, aParams, null);
  }

  public URLData (@Nonnull final String sPath,
                  @Nullable final Map <String, String> aParams,
                  @Nullable final String sAnchor)
  {
    if (sPath == null)
      throw new NullPointerException ("href may not be null");
    m_sPath = sPath;
    m_aParams = aParams;
    m_sAnchor = sAnchor;
  }

  @Nullable
  public IURLProtocol getProtocol ()
  {
    return URLProtocolRegistry.getProtocol (m_sPath);
  }

  public boolean hasKnownProtocol ()
  {
    return URLProtocolRegistry.hasKnownProtocol (m_sPath);
  }

  @Nonnull
  public String getPath ()
  {
    return m_sPath;
  }

  public boolean hasParams ()
  {
    return ContainerHelper.isNotEmpty (m_aParams);
  }

  @Nonnegative
  public int getParamCount ()
  {
    return ContainerHelper.getSize (m_aParams);
  }

  @Nullable
  @ReturnsMutableObject (reason = "Performance reasons")
  public Map <String, String> directGetParams ()
  {
    return m_aParams;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAllParams ()
  {
    return ContainerHelper.newOrderedMap (m_aParams);
  }

  public boolean hasAnchor ()
  {
    return StringHelper.hasText (m_sAnchor);
  }

  @Nullable
  public String getAnchor ()
  {
    return m_sAnchor;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof URLData))
      return false;
    final URLData rhs = (URLData) o;
    return m_sPath.equals (rhs.m_sPath) &&
           EqualsUtils.equals (m_aParams, rhs.m_aParams) &&
           EqualsUtils.equals (m_sAnchor, rhs.m_sAnchor);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sPath).append (m_aParams).append (m_sAnchor).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("path", m_sPath)
                                       .appendIfNotNull ("params", m_aParams)
                                       .appendIfNotNull ("anchor", m_sAnchor)
                                       .toString ();
  }
}
