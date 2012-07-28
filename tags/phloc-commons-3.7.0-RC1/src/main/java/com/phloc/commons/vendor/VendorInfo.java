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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
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
  public static final String DEFAULT_VENDOR_NAME = "phloc systems";
  @Deprecated
  public static final String VENDOR_NAME = DEFAULT_VENDOR_NAME;
  public static final String DEFAULT_VENDOR_URL_NO_HTTP = "www.phloc.com";
  @Deprecated
  public static final String VENDOR_URL_NO_HTTP = DEFAULT_VENDOR_URL_NO_HTTP;
  public static final String DEFAULT_VENDOR_URL = "http://" + DEFAULT_VENDOR_URL_NO_HTTP;
  @Deprecated
  public static final String VENDOR_URL = DEFAULT_VENDOR_URL;
  public static final String VENDOR_PERSON_BORIS = "Boris Gregorcic";
  public static final String VENDOR_PERSON_PHILIP = "Philip Helger";
  public static final String DEFAULT_VENDOR_EMAIL_SUFFIX = "@phloc.com";
  @Deprecated
  public static final String VENDOR_EMAIL_SUFFIX = DEFAULT_VENDOR_EMAIL_SUFFIX;
  public static final String DEFAULT_VENDOR_EMAIL = "office" + DEFAULT_VENDOR_EMAIL_SUFFIX;
  @Deprecated
  public static final String VENDOR_EMAIL = DEFAULT_VENDOR_EMAIL;
  public static final int DEFAULT_INCEPTION_YEAR = 2004;

  /** These are the lines that should used for prefixing generated files */
  @Deprecated
  public static final List <String> FILE_HEADER_LINES = ContainerHelper.newUnmodifiableList ("THIS FILE IS GENERATED - DO NOT EDIT",
                                                                                             "",
                                                                                             "Copyright",
                                                                                             "",
                                                                                             "Copyright (c) " +
                                                                                                 DEFAULT_VENDOR_NAME +
                                                                                                 " " +
                                                                                                 DEFAULT_INCEPTION_YEAR +
                                                                                                 " - " +
                                                                                                 CGlobal.CURRENT_YEAR,
                                                                                             DEFAULT_VENDOR_URL,
                                                                                             "",
                                                                                             "All Rights Reserved",
                                                                                             "Use, duplication or disclosure restricted by " +
                                                                                                 DEFAULT_VENDOR_NAME,
                                                                                             "",
                                                                                             "Vienna, " +
                                                                                                 DEFAULT_INCEPTION_YEAR +
                                                                                                 " - " +
                                                                                                 CGlobal.CURRENT_YEAR);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final VendorInfo s_aInstance = new VendorInfo ();

  private static int s_nInceptionYear = DEFAULT_INCEPTION_YEAR;
  private static String s_sVendorName = DEFAULT_VENDOR_NAME;
  private static String s_sVendorURL = DEFAULT_VENDOR_URL;
  private static String s_sVendorEmail = DEFAULT_VENDOR_EMAIL;

  private VendorInfo ()
  {}

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
                                    "Vienna, " + getInceptionYear () + " - " + CGlobal.CURRENT_YEAR);
  }
}
