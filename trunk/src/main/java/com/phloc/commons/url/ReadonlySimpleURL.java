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

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstraction of the string parts of a URL but much simpler (and faster) than
 * {@link java.net.URL}.
 * 
 * @author philip
 */
public final class ReadonlySimpleURL implements ISimpleURL
{
  /** Empty URL */
  public static final ReadonlySimpleURL EMPTY_URL = new ReadonlySimpleURL (EMPTY_URL_STRING);

  private final String m_sPath;
  private Map <String, String> m_aParams;
  private String m_sAnchor;

  public ReadonlySimpleURL (@Nonnull final String sHref)
  {
    this (URLUtils.getAsURLData (sHref));
  }

  public ReadonlySimpleURL (@Nonnull final String sHref, @Nullable final Map <String, String> aParams)
  {
    this (sHref);
    if (!ContainerHelper.isEmpty (aParams))
    {
      // m_aParams may already be non-null
      if (m_aParams == null)
        m_aParams = new LinkedHashMap <String, String> ();
      m_aParams.putAll (aParams);
    }
  }

  public ReadonlySimpleURL (@Nonnull final String sHref,
                            @Nullable final Map <String, String> aParams,
                            @Nullable final String sAnchor)
  {
    this (sHref, aParams);
    if (sAnchor != null)
      m_sAnchor = sAnchor;
  }

  public ReadonlySimpleURL (@Nonnull final IURLData aURL)
  {
    m_sPath = aURL.getPath ();
    if (aURL.directGetParams () != null)
      m_aParams = new LinkedHashMap <String, String> (aURL.directGetParams ());
    m_sAnchor = aURL.getAnchor ();
  }

  @Nonnull
  public String getPath ()
  {
    return m_sPath;
  }

  @Nonnull
  @ReturnsImmutableObject
  public Map <String, String> directGetParams ()
  {
    return ContainerHelper.makeUnmodifiableNotNull (m_aParams);
  }

  @Nullable
  public String getAnchor ()
  {
    return m_sAnchor;
  }

  @Nullable
  public String getParam (@Nullable final String sKey)
  {
    return m_aParams == null ? null : m_aParams.get (sKey);
  }

  public boolean hasKnownProtocol ()
  {
    return EURLProtocol.hasKnownProtocol (m_sPath);
  }

  @Nonnull
  public String getAsString ()
  {
    return URLUtils.getURLString (this, (String) null);
  }

  @Nonnull
  public String getAsStringWithEncodedParameters ()
  {
    return getAsStringWithEncodedParameters (URLUtils.CHARSET_URL);
  }

  @Nonnull
  public String getAsStringWithEncodedParameters (@Nonnull final String sParameterCharset)
  {
    if (sParameterCharset == null)
      throw new NullPointerException ("parameterCharset");
    return URLUtils.getURLString (this, sParameterCharset);
  }

  @Nonnull
  public String getAsStringWithEncodedParameters (@Nonnull final Charset aParameterCharset)
  {
    if (aParameterCharset == null)
      throw new NullPointerException ("parameterCharset");
    return URLUtils.getURLString (this, aParameterCharset);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ReadonlySimpleURL))
      return false;
    final ReadonlySimpleURL rhs = (ReadonlySimpleURL) o;
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
    return new ToStringGenerator (null).append ("path", m_sPath)
                                       .appendIfNotNull ("params", m_aParams)
                                       .appendIfNotNull ("anchor", m_sAnchor)
                                       .toString ();
  }
}
