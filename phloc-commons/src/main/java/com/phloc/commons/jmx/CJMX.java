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
package com.phloc.commons.jmx;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Constants for JMX usage
 * 
 * @author Philip Helger
 */
@Immutable
public final class CJMX
{
  /** JMX domain for phloc products */
  public static final String PHLOC_JMX_DOMAIN = "com.phloc";

  /** Standard JMX property */
  public static final String PROPERTY_TYPE = "type";

  /** Standard JMX property */
  public static final String PROPERTY_NAME = "name";

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CJMX s_aInstance = new CJMX ();

  private CJMX ()
  {}
}
