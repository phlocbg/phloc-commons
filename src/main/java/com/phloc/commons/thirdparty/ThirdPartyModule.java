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
package com.phloc.commons.thirdparty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.version.Version;

/**
 * The default implementation of the {@link IThirdPartyModule} interface.
 * 
 * @author philip
 */
@Immutable
public final class ThirdPartyModule implements IThirdPartyModule
{
  /** By default a third-party module is not optional */
  public static final boolean DEFAULT_OPTIONAL = false;

  private final String m_sDisplayName;
  private final String m_sCopyrightOwner;
  private final ILicense m_aLicense;
  private final Version m_aVersion;
  private final String m_sWebSiteURL;
  private final boolean m_bOptional;

  public ThirdPartyModule (@Nonnull @Nonempty final String sDisplayName,
                           @Nonnull @Nonempty final String sCopyrightOwner,
                           @Nonnull final ILicense aLicense)
  {
    this (sDisplayName, sCopyrightOwner, aLicense, DEFAULT_OPTIONAL);
  }

  public ThirdPartyModule (@Nonnull @Nonempty final String sDisplayName,
                           @Nonnull @Nonempty final String sCopyrightOwner,
                           @Nonnull final ILicense aLicense,
                           final boolean bOptional)
  {
    this (sDisplayName, sCopyrightOwner, aLicense, (Version) null, (String) null, bOptional);
  }

  public ThirdPartyModule (@Nonnull @Nonempty final String sDisplayName,
                           @Nonnull @Nonempty final String sCopyrightOwner,
                           @Nonnull final ILicense aLicense,
                           @Nullable final Version aVersion,
                           @Nullable final String sWebsiteURL)
  {
    this (sDisplayName, sCopyrightOwner, aLicense, aVersion, sWebsiteURL, DEFAULT_OPTIONAL);
  }

  public ThirdPartyModule (@Nonnull @Nonempty final String sDisplayName,
                           @Nonnull @Nonempty final String sCopyrightOwner,
                           @Nonnull final ILicense aLicense,
                           @Nullable final Version aVersion,
                           @Nullable final String sWebsiteURL,
                           final boolean bOptional)
  {
    if (StringHelper.hasNoText (sDisplayName))
      throw new IllegalArgumentException ("displayName");
    if (StringHelper.hasNoText (sCopyrightOwner))
      throw new IllegalArgumentException ("copyrightOwner");
    if (aLicense == null)
      throw new NullPointerException ("license");

    m_sDisplayName = sDisplayName;
    m_sCopyrightOwner = sCopyrightOwner;
    m_aLicense = aLicense;
    m_aVersion = aVersion;
    m_sWebSiteURL = sWebsiteURL;
    m_bOptional = bOptional;
  }

  @Nonnull
  public String getDisplayName ()
  {
    return m_sDisplayName;
  }

  @Nonnull
  public String getCopyrightOwner ()
  {
    return m_sCopyrightOwner;
  }

  @Nonnull
  public ILicense getLicense ()
  {
    return m_aLicense;
  }

  @Nullable
  public Version getVersion ()
  {
    return m_aVersion;
  }

  @Nullable
  public String getWebSiteURL ()
  {
    return m_sWebSiteURL;
  }

  public boolean isOptional ()
  {
    return m_bOptional;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ThirdPartyModule))
      return false;
    final ThirdPartyModule rhs = (ThirdPartyModule) o;
    return m_sDisplayName.equals (rhs.m_sDisplayName) &&
           m_sCopyrightOwner.equals (rhs.m_sCopyrightOwner) &&
           m_aLicense.equals (rhs.m_aLicense) &&
           EqualsUtils.equals (m_aVersion, rhs.m_aVersion) &&
           EqualsUtils.equals (m_sWebSiteURL, rhs.m_sWebSiteURL) &&
           m_bOptional == rhs.m_bOptional;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sDisplayName)
                                       .append (m_sCopyrightOwner)
                                       .append (m_aLicense)
                                       .append (m_aVersion)
                                       .append (m_sWebSiteURL)
                                       .append (m_bOptional)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("displayName", m_sDisplayName)
                                       .append ("copyrightOwner", m_sCopyrightOwner)
                                       .append ("license", m_aLicense)
                                       .appendIfNotNull ("version", m_aVersion)
                                       .appendIfNotNull ("website", m_sWebSiteURL)
                                       .append ("optional", m_bOptional)
                                       .toString ();
  }
}
