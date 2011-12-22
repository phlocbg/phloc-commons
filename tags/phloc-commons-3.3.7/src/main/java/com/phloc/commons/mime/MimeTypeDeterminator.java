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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsImmutableObject;
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

  private static final byte [] MIME_ID_GIF87A = "GIF87a".getBytes ();
  private static final byte [] MIME_ID_GIF89A = "GIF89a".getBytes ();
  private static final byte [] MIME_ID_JPG = new byte [] { (byte) 0xff, (byte) 0xd8 };
  private static final byte [] MIME_ID_PNG = new byte [] { (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a };
  private static final byte [] MIME_ID_TIFF_MOTOROLLA = "MM".getBytes ();
  private static final byte [] MIME_ID_TIFF_INTEL = "II".getBytes ();
  private static final byte [] MIME_ID_PSD = "8BPS".getBytes ();
  private static final byte [] MIME_ID_XML = "<?xml".getBytes ();
  private static final byte [] MIME_ID_PDF = "%PDF".getBytes ();

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
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final MimeTypeDeterminator s_aInstance = new MimeTypeDeterminator ();

  private MimeTypeDeterminator ()
  {}

  private static boolean _match (@Nonnull final byte [] aBytes, final int nOffset, @Nonnull final byte [] aCmpBytes)
  {
    final int nEnd = aCmpBytes.length;
    for (int i = 0; i < nEnd; ++i)
      if (aCmpBytes[i] != aBytes[nOffset + i])
        return false;
    return true;
  }

  private static boolean _match (@Nonnull final byte [] b, @Nonnull final byte [] aCmp)
  {
    return b.length > aCmp.length && _match (b, 0, aCmp);
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
      if (_match (b, MIME_ID_GIF87A) || _match (b, MIME_ID_GIF89A))
        return CMimeType.IMAGE_GIF;
      if (_match (b, MIME_ID_JPG))
        return CMimeType.IMAGE_JPG;
      if (_match (b, MIME_ID_PNG))
        return CMimeType.IMAGE_PNG;
      if (_match (b, MIME_ID_TIFF_INTEL) || _match (b, MIME_ID_TIFF_MOTOROLLA))
        return CMimeType.IMAGE_TIFF;
      if (_match (b, MIME_ID_PSD))
        return CMimeType.IMAGE_PSD;
      if (_match (b, MIME_ID_XML))
        return CMimeType.TEXT_XML;
      if (_match (b, MIME_ID_PDF))
        return CMimeType.APPLICATION_PDF;
    }

    // default fallback
    return CMimeType.APPLICATION_OCTET_STREAM;
  }

  @Nullable
  public static String getMimeTypeFromFilename (@Nonnull final String sFilename)
  {
    return getMimeTypeFromExtension (FilenameHelper.getExtension (sFilename));
  }

  @Nullable
  public static IMimeType getMimeTypeObjectFromFilename (@Nonnull final String sExtension)
  {
    return MimeType.parseFromStringWithoutEncoding (getMimeTypeFromFilename (sExtension));
  }

  @Nullable
  public static String getMimeTypeFromExtension (@Nonnull final String sExtension)
  {
    String ret = s_aFileExtMap.get (sExtension);
    if (ret == null)
    {
      // Especially on Windows, sometimes file extensions like "JPG" can be
      // found. Therefore also test for the lowercase version of the extension.
      ret = s_aFileExtMap.get (sExtension.toLowerCase ());
    }
    return ret;
  }

  @Nullable
  public static IMimeType getMimeTypeObjectFromExtension (@Nonnull final String sExtension)
  {
    return MimeType.parseFromStringWithoutEncoding (getMimeTypeFromExtension (sExtension));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Collection <String> getAllKnownMimeTypes ()
  {
    return ContainerHelper.makeUnmodifiable (s_aFileExtMap.values ());
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Map <String, String> getAllKnownMimeTypeFilenameMappings ()
  {
    return ContainerHelper.makeUnmodifiable (s_aFileExtMap);
  }
}
