/**
 * Copyright (C) 2006-2015 phloc systems
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

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.version.Version;

/**
 * Represents a custom license.
 * 
 * @author Boris Gregorcic
 * @author Philip Helger
 */
@Immutable
public final class CustomLicense implements ILicense
{
  private final String m_sID;
  private final String m_sName;
  private final Version m_aVersion;
  private final String m_sWebSiteURL;

  /**
   * Create a custom license.
   * 
   * @param sID
   *        The ID of the license.
   * @param sName
   *        The name of the license.
   * @param aVersion
   *        The version of the license.
   * @param sURL
   *        The URL of the license.
   */
  public CustomLicense (@Nonnull @Nonempty final String sID,
                        @Nonnull @Nonempty final String sName,
                        @Nullable final Version aVersion,
                        @Nullable final String sURL)
  {
    this.m_sID = ValueEnforcer.notEmpty (sID, "ID"); //$NON-NLS-1$
    this.m_sName = ValueEnforcer.notEmpty (sName, "Name"); //$NON-NLS-1$
    this.m_aVersion = aVersion;
    this.m_sWebSiteURL = sURL;
  }

  @Override
  @Nonnull
  @Nonempty
  public String getID ()
  {
    return this.m_sID;
  }

  @Override
  @Nonnull
  @Nonempty
  public String getDisplayName ()
  {
    return this.m_sName;
  }

  @Override
  @Nullable
  public Version getVersion ()
  {
    return this.m_aVersion;
  }

  @Override
  @Nullable
  public String getURL ()
  {
    return this.m_sWebSiteURL;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof CustomLicense))
      return false;
    final CustomLicense rhs = (CustomLicense) o;
    return this.m_sID.equals (rhs.m_sID) &&
           this.m_sName.equals (rhs.m_sName) &&
           EqualsUtils.equals (this.m_aVersion, rhs.m_aVersion) &&
           EqualsUtils.equals (this.m_sWebSiteURL, rhs.m_sWebSiteURL);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_sID)
                                       .append (this.m_sName)
                                       .append (this.m_aVersion)
                                       .append (this.m_sWebSiteURL)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", this.m_sID) //$NON-NLS-1$
                                       .append ("name", this.m_sName) //$NON-NLS-1$
                                       .appendIfNotNull ("version", this.m_aVersion) //$NON-NLS-1$
                                       .appendIfNotNull ("website", this.m_sWebSiteURL) //$NON-NLS-1$
                                       .toString ();
  }
}
