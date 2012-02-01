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
package com.phloc.commons.error;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IResourceError} interface. The
 * implementation is immutable.
 * 
 * @author philip
 */
@Immutable
public class ResourceError implements IResourceError
{
  private final IResourceLocation m_aLocation;
  private final EErrorLevel m_eErrorLevel;
  private final String m_sErrorText;
  private final Throwable m_aLinkedException;

  public ResourceError (@Nonnull final IResourceLocation aLocation,
                        @Nonnull final EErrorLevel eErrorLevel,
                        @Nonnull final String sErrorText)
  {
    this (aLocation, eErrorLevel, sErrorText, null);
  }

  public ResourceError (@Nonnull final IResourceLocation aLocation,
                        @Nonnull final EErrorLevel eErrorLevel,
                        @Nonnull final String sErrorText,
                        @Nullable final Throwable aLinkedException)
  {
    if (aLocation == null)
      throw new NullPointerException ("location");
    if (eErrorLevel == null)
      throw new NullPointerException ("errorLevel");
    if (StringHelper.hasNoText (sErrorText))
      throw new IllegalArgumentException ("errorText may not be empty");

    m_aLocation = aLocation;
    m_eErrorLevel = eErrorLevel;
    m_sErrorText = sErrorText;
    m_aLinkedException = aLinkedException;
  }

  @Nonnull
  public final IResourceLocation getLocation ()
  {
    return m_aLocation;
  }

  @Nonnull
  public final EErrorLevel getErrorLevel ()
  {
    return m_eErrorLevel;
  }

  @Nonnull
  @OverrideOnDemand
  public String getErrorText (final Locale aContentLocale)
  {
    return m_sErrorText;
  }

  @Nullable
  public final Throwable getLinkedException ()
  {
    return m_aLinkedException;
  }

  @Nonnull
  @OverrideOnDemand
  public String getAsString ()
  {
    String ret = "[" + m_eErrorLevel.getID () + "]";
    final String sLocation = m_aLocation.getAsString ();
    if (StringHelper.hasText (sLocation))
      ret += ' ' + sLocation + ": ";
    ret += m_sErrorText;
    if (m_aLinkedException != null)
      ret += " (" + m_aLinkedException.getMessage () + ")";
    return ret;
  }

  public final boolean isSuccess ()
  {
    return m_eErrorLevel.isSuccess ();
  }

  public final boolean isFailure ()
  {
    return m_eErrorLevel.isFailure ();
  }

  public final boolean isError ()
  {
    return m_eErrorLevel.isError ();
  }

  public final boolean isNoError ()
  {
    return m_eErrorLevel.isNoError ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final ResourceError rhs = (ResourceError) o;
    // Do not include the exception, because it is not comparable
    return m_aLocation.equals (rhs.m_aLocation) &&
           m_eErrorLevel.equals (rhs.m_eErrorLevel) &&
           m_sErrorText.equals (rhs.m_sErrorText);
  }

  @Override
  public int hashCode ()
  {
    // Do not include the exception, because it is not comparable
    return new HashCodeGenerator (this).append (m_aLocation)
                                       .append (m_eErrorLevel)
                                       .append (m_sErrorText)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("location", m_aLocation)
                                       .append ("errorLevel", m_eErrorLevel)
                                       .append ("errorText", m_sErrorText)
                                       .appendIfNotNull ("linkedException", m_aLinkedException)
                                       .toString ();
  }
}
