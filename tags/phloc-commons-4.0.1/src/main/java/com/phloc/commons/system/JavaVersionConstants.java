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
package com.phloc.commons.system;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.SystemProperties;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringParser;

/**
 * Helper class that holds the current class version. Must be a separate class
 * to maintain the correct initialization order.
 * 
 * @author philip
 */
@Immutable
final class JavaVersionConstants
{
  /** The global Java class version as a double value. */
  public static final double CLASS_VERSION = StringParser.parseDouble (SystemProperties.getJavaClassVersion (),
                                                                       CGlobal.ILLEGAL_DOUBLE);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final JavaVersionConstants s_aInstance = new JavaVersionConstants ();

  private JavaVersionConstants ()
  {}
}
