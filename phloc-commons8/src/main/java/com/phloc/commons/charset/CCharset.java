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
package com.phloc.commons.charset;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Charset constants.
 *
 * @author Philip Helger
 */
@Immutable
public final class CCharset
{
  /**
   * The ISO-8859-1 charset object.
   */
  @Nonnull
  public static final Charset CHARSET_ISO_8859_1_OBJ = StandardCharsets.ISO_8859_1;

  /**
   * The US-ASCII charset object.
   */
  @Nonnull
  public static final Charset CHARSET_US_ASCII_OBJ = StandardCharsets.US_ASCII;

  /**
   * The UTF-8 charset object.
   */
  @Nonnull
  public static final Charset CHARSET_UTF_8_OBJ = StandardCharsets.UTF_8;

  /**
   * The UTF-16 charset object.
   */
  @Nonnull
  public static final Charset CHARSET_UTF_16_OBJ = StandardCharsets.UTF_16;

  /**
   * The UTF-16BE charset object.
   */
  @Nonnull
  public static final Charset CHARSET_UTF_16BE_OBJ = StandardCharsets.UTF_16BE;

  /**
   * The UTF-16LE charset object.
   */
  @Nonnull
  public static final Charset CHARSET_UTF_16LE_OBJ = StandardCharsets.UTF_16LE;

  /**
   * The default charset object. Currently this is UTF-8.
   */
  @Nonnull
  public static final Charset DEFAULT_CHARSET_OBJ = CHARSET_UTF_8_OBJ;

  /**
   * The non-standard Windows 1252 charset object.
   */
  @Nonnull
  public static final Charset CHARSET_WINDOWS_1252_OBJ = CharsetManager.getCharsetFromName ("windows-1252");

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CCharset s_aInstance = new CCharset ();

  private CCharset ()
  {}
}
