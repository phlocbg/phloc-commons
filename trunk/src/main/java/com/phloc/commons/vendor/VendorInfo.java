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
package com.phloc.commons.vendor;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;

/**
 * Contains some general vendor specific information. This is mainly for keeping
 * the CI in all applications.
 * 
 * @author philip
 */
@NotThreadSafe
public final class VendorInfo
{
  public static final String VENDOR_NAME = "phloc systems";
  public static final String VENDOR_URL_NO_HTTP = "www.phloc.com";
  public static final String VENDOR_URL = "http://" + VENDOR_URL_NO_HTTP;
  public static final String VENDOR_PERSON_BORIS = "Boris Gregorcic";
  public static final String VENDOR_PERSON_PHILIP = "Philip Helger";
  public static final String VENDOR_EMAIL_SUFFIX = "@phloc.com";
  public static final String VENDOR_EMAIL = "office" + VENDOR_EMAIL_SUFFIX;

  // These are the lines that should used for prefixing generated files
  public static final List <String> FILE_HEADER_LINES = ContainerHelper.newUnmodifiableList ("THIS FILE IS GENERATED - DO NOT EDIT",
                                                                                             "",
                                                                                             "Copyright",
                                                                                             "",
                                                                                             "Copyright (c) " +
                                                                                                 VENDOR_NAME +
                                                                                                 " 2004 - " +
                                                                                                 CGlobal.CURRENT_YEAR,
                                                                                             VENDOR_URL,
                                                                                             "",
                                                                                             "All Rights Reserved",
                                                                                             "Use, duplication or disclosure restricted by " +
                                                                                                 VENDOR_NAME,
                                                                                             "",
                                                                                             "Vienna, 2004 - " +
                                                                                                 CGlobal.CURRENT_YEAR);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final VendorInfo s_aInstance = new VendorInfo ();

  private static String s_sVendorName = VENDOR_NAME;
  private static String s_sVendorURL = VENDOR_URL;
  private static String s_sVendorEmail = VENDOR_EMAIL;

  private VendorInfo ()
  {}

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
  public static String getVendorURL ()
  {
    return s_sVendorURL;
  }

  public static void setVendorURL (@Nonnull @Nonempty final String sVendorURL)
  {
    if (StringHelper.hasNoText (sVendorURL))
      throw new IllegalArgumentException ("vendorURL");
    s_sVendorURL = sVendorURL;
  }

  @Nonnull
  public static String getVendorEmail ()
  {
    return s_sVendorEmail;
  }

  public static void setVendorEmail (@Nonnull @Nonempty final String sVendorEmail)
  {
    if (StringHelper.hasNoText (sVendorEmail))
      throw new IllegalArgumentException ("vendorEmail");
    s_sVendorEmail = sVendorEmail;
  }
}
