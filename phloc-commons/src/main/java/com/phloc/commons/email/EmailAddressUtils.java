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
package com.phloc.commons.email;

import java.util.Locale;
import java.util.regex.Pattern;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.regex.RegExPool;
import com.phloc.commons.string.StringHelper;

/**
 * Perform simple email address validation based on a regular expression.
 * 
 * @author Boris Gregorcic
 */
@Immutable
public final class EmailAddressUtils
{
  /** This is the email RegEx :) */

  // RFC 6530/6531/6532
  public static final String EMAIL_ADDRESS_PATTERN_INT = "[a-z0-9äöüß!#\\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9äöüß!#\\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9äöüß](?:[a-z0-9äöüß-]*[a-z0-9äöüß])?\\.)+[a-z0-9äöüß](?:[a-z0-9äöüß-]*[a-z0-9äöüß])?"; //$NON-NLS-1$
  public static final String EMAIL_ADDRESS_PATTERN = "[a-z0-9!#\\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"; //$NON-NLS-1$

  /** Compile this little pattern only once */
  private static final Pattern s_aPatternInt = RegExPool.getPattern (EMAIL_ADDRESS_PATTERN_INT);
  private static Pattern s_aCustomPatternInt = null;
  private static final Pattern s_aPattern = RegExPool.getPattern (EMAIL_ADDRESS_PATTERN);
  private static boolean s_bDefaultAllowInternationalized = false;

  private EmailAddressUtils ()
  {}

  /**
   * Get the unified version of an email address. It trims leading and trailing
   * spaces and lower-cases the email address.
   * 
   * @param sEmailAddress
   *        The email address to unify. May be <code>null</code>.
   * @return The unified email address or <code>null</code> if the input address
   *         is <code>null</code>.
   */
  @Nullable
  public static String getUnifiedEmailAddress (@Nullable final String sEmailAddress)
  {
    return sEmailAddress == null ? null : sEmailAddress.trim ().toLowerCase (Locale.US);
  }

  /**
   * Checks if a value is a valid e-mail address according to a certain regular
   * expression.
   * 
   * @param sEmailAddress
   *        The value validation is being performed on. A <code>null</code>
   *        value is considered invalid.
   * @return <code>true</code> if the email address is valid, <code>false</code>
   *         otherwise.
   */
  public static boolean isValid (@Nullable final String sEmailAddress)
  {
    return isValid (sEmailAddress, s_bDefaultAllowInternationalized);
  }

  /**
   * Checks if a value is a valid e-mail address according to a certain regular
   * expression.
   * 
   * @param sEmailAddress
   *        The value validation is being performed on. A <code>null</code>
   *        value is considered invalid.
   * @param bAllowInternationalizedAddresses
   *        Whether or not to allow internationalized addresses (RFC
   *        6530/6531/6532)
   * @return <code>true</code> if the email address is valid, <code>false</code>
   *         otherwise.
   */
  public static boolean isValid (@Nullable final String sEmailAddress, final boolean bAllowInternationalizedAddresses)
  {
    if (sEmailAddress == null)
      return false;

    // Unify (lowercase)
    final String sUnifiedEmail = getUnifiedEmailAddress (sEmailAddress);

    // Pattern matching
    if (bAllowInternationalizedAddresses)
    {
      if (s_aCustomPatternInt == null)
      {
        return s_aPatternInt.matcher (sUnifiedEmail).matches ();
      }
      return s_aCustomPatternInt.matcher (sUnifiedEmail).matches ();
    }
    return s_aPattern.matcher (sUnifiedEmail).matches ();
  }

  /**
   * Sets a custom regular expression to check for valid mail address according
   * to RFC 6530/6531/6532. The current default pattern only considers German
   * Umlauts. <br>
   * <b>Default:</b> {@value #EMAIL_ADDRESS_PATTERN_INT}
   * 
   * @param sRegExp
   *        The regular expression pattern to set
   */
  public static void setCustomPatternforInternationalized (@Nullable final String sRegExp)
  {
    s_aCustomPatternInt = StringHelper.hasText (sRegExp) ? RegExPool.getPattern (sRegExp) : null;
  }

  /**
   * Changes the default behaviour or the mail address validation to allow or
   * disallow internationalized addresses.<br>
   * <b>Default:</b> false.<br>
   * Note that enabling internationalized addresses, you need also a mail API
   * (e.g. Java Mail 1.6) that can handle it.
   * 
   * @param bAllow
   *        Whether or not internationalized mail addresses should be used by
   *        default (default = false)
   */
  public static void setDefaultAllowInternationalized (final boolean bAllow)
  {
    s_bDefaultAllowInternationalized = bAllow;
  }
}
