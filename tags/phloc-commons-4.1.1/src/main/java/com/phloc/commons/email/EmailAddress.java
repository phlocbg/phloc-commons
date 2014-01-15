/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.email;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class handles a single email address. It is split into an address part
 * and an optional name. The personal name is optional and may be
 * <code>null</code>.
 * 
 * @author Philip Helger
 */
@Immutable
public final class EmailAddress implements IEmailAddress
{
  private final String m_sAddress;
  private final String m_sPersonal;

  public EmailAddress (@Nonnull final IEmailAddress aAddress)
  {
    this (aAddress.getAddress (), aAddress.getPersonal ());
  }

  public EmailAddress (@Nonnull final String sAddress)
  {
    this (sAddress, null);
  }

  public EmailAddress (@Nonnull final String sAddress, @Nullable final String sPersonal)
  {
    if (sAddress == null)
      throw new NullPointerException ("emailAddress");
    if (!EmailAddressUtils.isValid (sAddress))
      throw new IllegalArgumentException ("The passed email address '" + sAddress + "' is illegal!");
    m_sAddress = EmailAddressUtils.getUnifiedEmailAddress (sAddress);
    m_sPersonal = StringHelper.hasNoText (sPersonal) ? null : sPersonal;
  }

  @Nonnull
  public String getAddress ()
  {
    return m_sAddress;
  }

  @Nullable
  public String getPersonal ()
  {
    return m_sPersonal;
  }

  @Nonnull
  public String getDisplayName ()
  {
    if (StringHelper.hasText (m_sPersonal))
      return m_sPersonal + " <" + m_sAddress + ">";
    return m_sAddress;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof EmailAddress))
      return false;
    final EmailAddress rhs = (EmailAddress) o;
    return m_sAddress.equals (rhs.m_sAddress) && EqualsUtils.equals (m_sPersonal, rhs.m_sPersonal);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sAddress).append (m_sPersonal).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("address", m_sAddress)
                                       .appendIfNotNull ("personal", m_sPersonal)
                                       .toString ();
  }
}
