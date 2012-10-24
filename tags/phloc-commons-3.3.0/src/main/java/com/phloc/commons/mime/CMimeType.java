/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.mime;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Contains a collection of well-known constant MIME types.
 * 
 * @author philip
 */
@Immutable
public final class CMimeType
{
  /** Word document. */
  public static final IMimeType APPLICATION_MS_WORD = EMimeContentType.APPLICATION.buildMimeType ("msword");

  /** Word 2007 document */
  public static final IMimeType APPLICATION_MS_WORD_2007 = EMimeContentType.APPLICATION.buildMimeType ("vnd.openxmlformats-officedocument.wordprocessingml.document");

  /** Excel document. */
  public static final IMimeType APPLICATION_MS_EXCEL = EMimeContentType.APPLICATION.buildMimeType ("vnd.ms-excel");

  /** Excel 2007 document */
  public static final IMimeType APPLICATION_MS_EXCEL_2007 = EMimeContentType.APPLICATION.buildMimeType ("vnd.openxmlformats-officedocument.spreadsheetml.sheet");

  /** PowerPoint document. */
  public static final IMimeType APPLICATION_MS_POWERPOINT = EMimeContentType.APPLICATION.buildMimeType ("vnd.ms-powerpoint");

  /** PowerPoint 2007 document. */
  public static final IMimeType APPLICATION_MS_POWERPOINT_2007 = EMimeContentType.APPLICATION.buildMimeType ("vnd.openxmlformats-officedocument.presentationml.presentation");

  /** Any byte stream. */
  public static final IMimeType APPLICATION_OCTET_STREAM = EMimeContentType.APPLICATION.buildMimeType ("octet-stream");

  /** PDF document. */
  public static final IMimeType APPLICATION_PDF = EMimeContentType.APPLICATION.buildMimeType ("pdf");

  /** Downloadable document. */
  public static final IMimeType APPLICATION_FORCE_DOWNLOAD = EMimeContentType.APPLICATION.buildMimeType ("force-download");

  /** ZIP document. */
  public static final IMimeType APPLICATION_ZIP = EMimeContentType.APPLICATION.buildMimeType ("zip");

  /** JSON document. */
  public static final IMimeType APPLICATION_JSON = EMimeContentType.APPLICATION.buildMimeType ("json");

  /** Atom XML feed. */
  public static final IMimeType APPLICATION_ATOM_XML = EMimeContentType.APPLICATION.buildMimeType ("atom+xml");

  /** RSS XML feed. */
  public static final IMimeType APPLICATION_RSS_XML = EMimeContentType.APPLICATION.buildMimeType ("rss+xml");

  /** Shockwave/Flash */
  public static final IMimeType APPLICATION_SHOCKWAVE_FLASH = EMimeContentType.APPLICATION.buildMimeType ("x-shockwave-flash");

  /** Java applet */
  public static final IMimeType APPLICATION_JAVA_APPLET = EMimeContentType.APPLICATION.buildMimeType ("x-java-applet");

  /** XHTML document. */
  public static final IMimeType APPLICATION_XHTML_XML = EMimeContentType.APPLICATION.buildMimeType ("xhtml+xml");

  /** For MP3 files */
  public static final IMimeType AUDIO_MP3 = EMimeContentType.AUDIO.buildMimeType ("mpeg");

  /** Bitmap image. */
  public static final IMimeType IMAGE_BMP = EMimeContentType.IMAGE.buildMimeType ("bmp");

  /** GIF image. */
  public static final IMimeType IMAGE_GIF = EMimeContentType.IMAGE.buildMimeType ("gif");

  /** Icon image. */
  public static final IMimeType IMAGE_ICON = EMimeContentType.IMAGE.buildMimeType ("icon");

  /** JPEG image. */
  public static final IMimeType IMAGE_JPG = EMimeContentType.IMAGE.buildMimeType ("jpeg");

  /** PNG image. */
  public static final IMimeType IMAGE_PNG = EMimeContentType.IMAGE.buildMimeType ("png");

  /** TIFF image. */
  public static final IMimeType IMAGE_TIFF = EMimeContentType.IMAGE.buildMimeType ("tiff");

  /** Photoshop image. */
  public static final IMimeType IMAGE_PSD = EMimeContentType.IMAGE.buildMimeType ("vnd.adobe.photoshop");

  /** Icon image. */
  public static final IMimeType IMAGE_X_ICON = EMimeContentType.IMAGE.buildMimeType ("x-icon");

  /** For HTML upload forms */
  public static final IMimeType MULTIPART_FORMDATA = EMimeContentType.MULTIPART.buildMimeType ("form-data");

  /** CSV document. */
  public static final IMimeType TEXT_CSV = EMimeContentType.TEXT.buildMimeType ("csv");

  /** HTML document. */
  public static final IMimeType TEXT_HTML = EMimeContentType.TEXT.buildMimeType ("html");

  /** JavaScript document. */
  public static final IMimeType TEXT_JAVASCRIPT = EMimeContentType.TEXT.buildMimeType ("javascript");

  /** Plain text document. */
  public static final IMimeType TEXT_PLAIN = EMimeContentType.TEXT.buildMimeType ("plain");

  /** XHTML document. */
  public static final IMimeType TEXT_XHTML_XML = EMimeContentType.TEXT.buildMimeType ("xhtml+xml");

  /** XML document. */
  public static final IMimeType TEXT_XML = EMimeContentType.TEXT.buildMimeType ("xml");

  /** CSS style-sheet document. */
  public static final IMimeType TEXT_CSS = EMimeContentType.TEXT.buildMimeType ("css");

  /** Mozilla CSP */
  public static final IMimeType TEXT_CONTENT_SECURITY_POLICY = EMimeContentType.TEXT.buildMimeType ("x-content-security-policy");

  /** The character used to separate content type and sub type. */
  public static final char CONTENTTYPE_SUBTYPE_SEPARATOR = '/';

  /** For appending a charset e.g. to HTML */
  public static final String CHARSET_PREFIX = "charset=";

  /** For appending a charset e.g. to HTML */
  public static final String CHARSET_PARAM = "; " + CHARSET_PREFIX;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CMimeType s_aInstance = new CMimeType ();

  private CMimeType ()
  {}
}