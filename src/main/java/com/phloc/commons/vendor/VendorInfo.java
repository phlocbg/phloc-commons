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
package com.phloc.commons.vendor;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.email.EmailAddressUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.EURLProtocol;
import com.phloc.commons.url.IURLProtocol;
import com.phloc.commons.url.URLProtocolRegistry;

/**
 * Contains some general vendor specific information. This is mainly for keeping
 * the CI in all applications.
 * 
 * @author philip
 */
@NotThreadSafe
public final class VendorInfo
{
  /** Where the vendor is located by default */
  public static final String DEFAULT_VENDOR_LOCATION = "Vienna, Austria, Europe, World, Milky Way, Universe";
  /** Default vendor name */
  public static final String DEFAULT_VENDOR_NAME = "phloc systems";
  /** Default vendor URL without http:// prefix */
  public static final String DEFAULT_VENDOR_URL_WITHOUT_PROTOCOL = "www.phloc.com";
  /** Complete vendor web URL */
  public static final String DEFAULT_VENDOR_URL = "http://" + DEFAULT_VENDOR_URL_WITHOUT_PROTOCOL;
  /** Boris */
  public static final String VENDOR_PERSON_BORIS = "Boris Gregorcic";
  /** Philip */
  public static final String VENDOR_PERSON_PHILIP = "Philip Helger";
  /** Vendor email suffix starting from '@' */
  public static final String DEFAULT_VENDOR_EMAIL_SUFFIX = "@phloc.com";
  /** Default vendor email */
  public static final String DEFAULT_VENDOR_EMAIL = "office" + DEFAULT_VENDOR_EMAIL_SUFFIX;
  /** Default inception year */
  public static final int DEFAULT_INCEPTION_YEAR = 2004;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final VendorInfo s_aInstance = new VendorInfo ();

  private static String s_sVendorLocation = DEFAULT_VENDOR_LOCATION;
  private static int s_nInceptionYear = DEFAULT_INCEPTION_YEAR;
  private static String s_sVendorName = DEFAULT_VENDOR_NAME;
  private static String s_sVendorURLWithoutProtocol = DEFAULT_VENDOR_URL_WITHOUT_PROTOCOL;
  private static String s_sVendorURL = DEFAULT_VENDOR_URL;
  private static String s_sVendorEmailSuffix = DEFAULT_VENDOR_EMAIL_SUFFIX;
  private static String s_sVendorEmail = DEFAULT_VENDOR_EMAIL;

  private VendorInfo ()
  {}

  @Nonnull
  public static String getVendorLocation ()
  {
    return s_sVendorLocation;
  }

  public static void setVendorLocation (@Nonnull @Nonempty final String sVendorLocation)
  {
    if (StringHelper.hasNoText (sVendorLocation))
      throw new IllegalArgumentException ("vendorLocation");
    s_sVendorLocation = sVendorLocation;
  }

  @Nonnegative
  public static int getInceptionYear ()
  {
    return s_nInceptionYear;
  }

  public static void setInceptionYear (@Nonnegative final int nInceptionYear)
  {
    if (nInceptionYear < 0)
      throw new IllegalArgumentException ("inceptionYear may not be negative");
    s_nInceptionYear = nInceptionYear;
  }

  @Nonnull
  public static String getVendorName ()
  {
    return s_sVendorName;
  }

  public static void setVendorName (@Nonnull @Nonempty final String sVendorName)
  {
    if (StringHelper.hasNoText (sVendorName))
      throw new IllegalArgumentException ("vendorName");
    s_sVendorName = sVendorName;
  }

  @Nonnull
  public static String getVendorURLWithoutProtocol ()
  {
    return s_sVendorURLWithoutProtocol;
  }

  @Nonnull
  public static String getVendorURL ()
  {
    return s_sVendorURL;
  }

  public static void setVendorURL (@Nonnull @Nonempty final String sVendorURL)
  {
    if (StringHelper.hasNoText (sVendorURL))
      throw new IllegalArgumentException ("vendorURL");

    final IURLProtocol aProtocol = URLProtocolRegistry.getProtocol (sVendorURL);
    if (aProtocol == null)
    {
      // No protocol present - assume HTTP
      s_sVendorURLWithoutProtocol = sVendorURL;
      s_sVendorURL = EURLProtocol.HTTP.getWithProtocol (sVendorURL);
    }
    else
    {
      // Strip leading protocol
      s_sVendorURLWithoutProtocol = sVendorURL.substring (aProtocol.getProtocol ().length ());
      s_sVendorURL = sVendorURL;
    }
  }

  @Nonnull
  public static String getVendorEmail ()
  {
    return s_sVendorEmail;
  }

  @Nonnull
  public static String getVendorEmailSuffix ()
  {
    return s_sVendorEmailSuffix;
  }

  public static void setVendorEmail (@Nonnull @Nonempty final String sVendorEmail)
  {
    if (StringHelper.hasNoText (sVendorEmail))
      throw new IllegalArgumentException ("vendorEmail");
    if (!EmailAddressUtils.isValid (sVendorEmail))
      throw new IllegalArgumentException ("Illegal vendor email: " + sVendorEmail);
    s_sVendorEmail = sVendorEmail;
    s_sVendorEmailSuffix = StringHelper.getFromFirstIncl (sVendorEmail, '@');
  }

  /**
   * @return These are the lines that should used for prefixing generated files.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static List <String> getFileHeaderLines ()
  {
    return ContainerHelper.newList ("THIS FILE IS GENERATED - DO NOT EDIT",
                                    "",
                                    "Copyright",
                                    "",
                                    "Copyright (c) " +
                                        getVendorName () +
                                        " " +
                                        getInceptionYear () +
                                        " - " +
                                        CGlobal.CURRENT_YEAR,
                                    getVendorURL (),
                                    "",
                                    "All Rights Reserved",
                                    "Use, duplication or disclosure restricted by " + getVendorName (),
                                    "",
                                    getVendorLocation () + ", " + getInceptionYear () + " - " + CGlobal.CURRENT_YEAR);
  }
}
