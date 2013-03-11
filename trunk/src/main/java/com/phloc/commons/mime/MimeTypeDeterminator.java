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
package com.phloc.commons.mime;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.reader.XMLMapHandler;
import com.phloc.commons.regex.RegExHelper;

/**
 * Contains a basic set of MimeType determination method.
 * 
 * @author philip
 */
@Immutable
public final class MimeTypeDeterminator
{
  // Maps file extension to MIME type
  private static final Map <String, String> s_aFileExtMap = new HashMap <String, String> ();
  private static final Set <MimeTypeContent> s_aContents = new HashSet <MimeTypeContent> ();

  private static final byte [] MIME_ID_GIF87A = new byte [] { 'G', 'I', 'F', '8', '7', 'a' };
  private static final byte [] MIME_ID_GIF89A = new byte [] { 'G', 'I', 'F', '8', '9', 'a' };
  private static final byte [] MIME_ID_JPG = new byte [] { (byte) 0xff, (byte) 0xd8 };
  private static final byte [] MIME_ID_PNG = new byte [] { (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a };
  private static final byte [] MIME_ID_TIFF_MOTOROLLA = new byte [] { 'M', 'M' };
  private static final byte [] MIME_ID_TIFF_INTEL = new byte [] { 'I', 'I' };
  private static final byte [] MIME_ID_PSD = new byte [] { '8', 'B', 'P', 'S' };
  private static final byte [] MIME_ID_XML = new byte [] { '<', '?', 'x', 'm', 'l' };
  private static final byte [] MIME_ID_PDF = new byte [] { '%', 'P', 'D', 'F' };

  static
  {
    // Key: extension (without dot), value (MIME type)
    if (XMLMapHandler.readMap (new ClassPathResource ("codelists/fileext-mimetype-mapping.xml"), s_aFileExtMap)
                     .isFailure ())
      throw new InitializationException ("Failed to init file extension to mimetype mapping file");

    // Validate all file extensions
    if (GlobalDebug.isDebugMode ())
      for (final Map.Entry <String, String> aEntry : s_aFileExtMap.entrySet ())
      {
        final String sFileExt = aEntry.getKey ();
        if (!RegExHelper.stringMatchesPattern ("(|[a-zA-Z0-9]+(\\.[a-z0-9]+)*)", sFileExt))
          throw new InitializationException ("MIME file extension '" + sFileExt + "' is invalid!");
        if (aEntry.getValue ().contains (" "))
          throw new InitializationException ("MIME type '" + aEntry.getValue () + "' is invalid!");
      }

    s_aContents.add (new MimeTypeContent (MIME_ID_GIF87A, CMimeType.IMAGE_GIF));
    s_aContents.add (new MimeTypeContent (MIME_ID_GIF89A, CMimeType.IMAGE_GIF));
    s_aContents.add (new MimeTypeContent (MIME_ID_JPG, CMimeType.IMAGE_JPG));
    s_aContents.add (new MimeTypeContent (MIME_ID_PNG, CMimeType.IMAGE_PNG));
    s_aContents.add (new MimeTypeContent (MIME_ID_TIFF_MOTOROLLA, CMimeType.IMAGE_TIFF));
    s_aContents.add (new MimeTypeContent (MIME_ID_TIFF_INTEL, CMimeType.IMAGE_TIFF));
    s_aContents.add (new MimeTypeContent (MIME_ID_PSD, CMimeType.IMAGE_PSD));
    s_aContents.add (new MimeTypeContent (MIME_ID_XML, CMimeType.TEXT_XML));
    s_aContents.add (new MimeTypeContent (MIME_ID_PDF, CMimeType.APPLICATION_PDF));
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final MimeTypeDeterminator s_aInstance = new MimeTypeDeterminator ();

  private MimeTypeDeterminator ()
  {}

  @Nonnull
  @Deprecated
  public static IMimeType getMimeTypeFromString (@Nullable final String s, @Nonnull @Nonempty final String sCharsetName)
  {
    return getMimeTypeFromBytes (s == null ? null : CharsetManager.getAsBytes (s, sCharsetName));
  }

  @Nonnull
  public static IMimeType getMimeTypeFromString (@Nullable final String s, @Nonnull final Charset aCharset)
  {
    return getMimeTypeFromBytes (s == null ? null : CharsetManager.getAsBytes (s, aCharset));
  }

  /**
   * Try to determine the MIME type from the given byte array. The array should
   * have at least 8 characters to perform all known tests.
   * 
   * @param b
   *        The byte array. to parse.
   * @return {@link CMimeType#APPLICATION_OCTET_STREAM} if no specific MIME type
   *         was found
   */
  @Nonnull
  public static IMimeType getMimeTypeFromBytes (@Nullable final byte [] b)
  {
    if (b != null)
    {
      for (final MimeTypeContent aMTC : s_aContents)
        if (aMTC.matchesBeginning (b))
          return aMTC.getMimeType ();
    }

    // default fallback
    return CMimeType.APPLICATION_OCTET_STREAM;
  }

  @Nullable
  public static String getMimeTypeFromFilename (@Nonnull final String sFilename)
  {
    final String sExt = FilenameHelper.getExtension (sFilename);
    return getMimeTypeFromExtension (sExt);
  }

  @Nullable
  public static IMimeType getMimeTypeObjectFromFilename (@Nonnull final String sExtension)
  {
    final String sMimeType = getMimeTypeFromFilename (sExtension);
    return MimeType.parseFromStringWithoutEncoding (sMimeType);
  }

  @Nullable
  public static String getMimeTypeFromExtension (@Nonnull final String sExtension)
  {
    String ret = s_aFileExtMap.get (sExtension);
    if (ret == null)
    {
      // Especially on Windows, sometimes file extensions like "JPG" can be
      // found. Therefore also test for the lowercase version of the extension.
      ret = s_aFileExtMap.get (sExtension.toLowerCase (Locale.US));
    }
    return ret;
  }

  @Nullable
  public static IMimeType getMimeTypeObjectFromExtension (@Nonnull final String sExtension)
  {
    final String sMimeType = getMimeTypeFromExtension (sExtension);
    return MimeType.parseFromStringWithoutEncoding (sMimeType);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Collection <String> getAllKnownMimeTypes ()
  {
    return ContainerHelper.newList (s_aFileExtMap.values ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Map <String, String> getAllKnownMimeTypeFilenameMappings ()
  {
    return ContainerHelper.newMap (s_aFileExtMap);
  }
}
