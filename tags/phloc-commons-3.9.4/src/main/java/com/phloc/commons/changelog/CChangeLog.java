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
package com.phloc.commons.changelog;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * This class contains constants for the use of changelogs.
 * 
 * @author philip
 */
@Immutable
public final class CChangeLog
{
  /** The namespace used in XML */
  public static final String CHANGELOG_NAMESPACE_10 = "http://www.phloc.com/ns/changelog/1.0";
  public static final String CHANGELOG_XSD_10 = "schemas/changelog-1.0.xsd";
  public static final String CHANGELOG_SCHEMALOCATION_10 = CHANGELOG_NAMESPACE_10 + " " + CHANGELOG_XSD_10;

  /** The default file name of the changelog XML */
  public static final String CHANGELOG_XML_FILENAME = "changelog.xml";

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CChangeLog s_aInstance = new CChangeLog ();

  private CChangeLog ()
  {}
}
