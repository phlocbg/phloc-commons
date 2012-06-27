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

import com.phloc.commons.ICloneable;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.protocol.URLProtocolRegistry;

/**
 * Abstraction of the string parts of a URL but much simpler (and faster) than
 * {@link java.net.URL}.
 * 
 * @author philip
 */
public final class SimpleURL implements ISimpleURL, ICloneable <SimpleURL>
{
  private final String m_sPath;
  private Map <String, String> m_aParams;
  private String m_sAnchor;

  public SimpleURL ()
  {
    this (EMPTY_URL_STRING, null, null);
  }

  public SimpleURL (@Nonnull final String sHref)
  {
    this (URLUtils.getAsURLData (sHref));
  }

  public SimpleURL (@Nonnull final String sHref, @Nullable final Map <String, String> aParams)
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

  public SimpleURL (@Nonnull final String sHref,
                    @Nullable final Map <String, String> aParams,
                    @Nullable final String sAnchor)
  {
    this (sHref, aParams);
    if (sAnchor != null)
      m_sAnchor = sAnchor;
  }

  public SimpleURL (@Nonnull final IURLData aURL)
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

  public boolean hasKnownProtocol ()
  {
    return URLProtocolRegistry.hasKnownProtocol (m_sPath);
  }

  @Nonnull
  public SimpleURL add (@Nonnull final String sKey, @Nonnull final String sValue)
  {
    if (StringHelper.hasNoText (sKey))
      throw new IllegalArgumentException ("key may not be empty!");
    if (sValue == null)
      throw new NullPointerException ("value");

    if (m_aParams == null)
      m_aParams = new LinkedHashMap <String, String> ();
    m_aParams.put (sKey, sValue);
    return this;
  }

  @Nonnull
  public SimpleURL add (@Nonnull final String sKey, final int nValue)
  {
    return add (sKey, Integer.toString (nValue));
  }

  @Nonnull
  public SimpleURL addIfNonNull (@Nonnull final String sKey, @Nullable final String sValue)
  {
    if (sValue != null)
      add (sKey, sValue);
    return this;
  }

  @Nonnull
  public SimpleURL addAll (@Nullable final Map <String, String> aParams)
  {
    if (!ContainerHelper.isEmpty (aParams))
    {
      if (m_aParams == null)
        m_aParams = new LinkedHashMap <String, String> ();
      m_aParams.putAll (aParams);
    }
    return this;
  }

  /**
   * Remove the parameter with the given key.
   * 
   * @param sKey
   *        The key to remove
   * @return this
   */
  @Nonnull
  public SimpleURL remove (@Nullable final String sKey)
  {
    if (m_aParams != null)
      m_aParams.remove (sKey);
    return this;
  }

  public int getParamCount ()
  {
    return ContainerHelper.getSize (m_aParams);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> directGetParams ()
  {
    return ContainerHelper.newOrderedMap (m_aParams);
  }

  @Nonnull
  public SimpleURL setAnchor (@Nullable final String sAnchor)
  {
    m_sAnchor = sAnchor;
    return this;
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

  @Nonnull
  public SimpleURL getClone ()
  {
    return new SimpleURL (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SimpleURL))
      return false;
    final SimpleURL rhs = (SimpleURL) o;
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
