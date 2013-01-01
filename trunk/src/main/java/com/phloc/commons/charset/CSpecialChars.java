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
package com.phloc.commons.charset;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * These constants only exist to work around the common file encoding problem
 * with Cp1252/UTF-8. This file should only be stored as UTF-8!
 * 
 * @author philip
 */
@Immutable
public final class CSpecialChars
{
  public static final char AUML_LC = '\u00E4';
  public static final String AUML_LC_STR = Character.toString (AUML_LC);
  public static final char AUML_UC = '\u00C4';
  public static final String AUML_UC_STR = Character.toString (AUML_UC);
  public static final char OUML_LC = '\u00F6';
  public static final String OUML_LC_STR = Character.toString (OUML_LC);
  public static final char OUML_UC = '\u00D6';
  public static final String OUML_UC_STR = Character.toString (OUML_UC);
  public static final char UUML_LC = '\u00FC';
  public static final String UUML_LC_STR = Character.toString (UUML_LC);
  public static final char UUML_UC = '\u00DC';
  public static final String UUML_UC_STR = Character.toString (UUML_UC);
  public static final char SZLIG = '\u00DF';
  public static final String SZLIG_STR = Character.toString (SZLIG);
  public static final char EURO = '\u20ac';
  public static final String EURO_STR = Character.toString (EURO);
  public static final char COPYRIGHT = '\u00a9';
  public static final String COPYRIGHT_STR = Character.toString (COPYRIGHT);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CSpecialChars s_aInstance = new CSpecialChars ();

  private CSpecialChars ()
  {}
}
