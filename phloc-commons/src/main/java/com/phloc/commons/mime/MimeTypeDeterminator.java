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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.MustBeLocked;
import com.phloc.commons.annotations.MustBeLocked.ELockType;
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
import com.phloc.commons.state.EChange;

/**
 * Contains a basic set of MimeType determination method.
 * 
 * @author philip
 */
@ThreadSafe
public final class MimeTypeDeterminator
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MimeTypeDeterminator.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // Maps file extension to MIME type
  private static final Map <String, String> s_aFileExtMap = new HashMap <String, String> ();

  // Contains all byte[] to mime type mappings
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

    _registerDefaultMimeTypeContents ();
  }

  @MustBeLocked (ELockType.WRITE)
  private static void _registerDefaultMimeTypeContents ()
  {
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
  public static EChange registerMimeTypeContent (@Nonnull final MimeTypeContent aMimeTypeContent)
  {
    if (aMimeTypeContent == null)
      throw new NullPointerException ("MimeTypeContent");

    s_aRWLock.writeLock ().lock ();
    try
    {
      return EChange.valueOf (s_aContents.add (aMimeTypeContent));
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public static EChange unregisterMimeTypeContent (@Nullable final MimeTypeContent aMimeTypeContent)
  {
    if (aMimeTypeContent == null)
      return EChange.UNCHANGED;

    s_aRWLock.writeLock ().lock ();
    try
    {
      return EChange.valueOf (s_aContents.remove (aMimeTypeContent));
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @Deprecated
  public static IMimeType getMimeTypeFromString (@Nullable final String s, @Nonnull @Nonempty final String sCharsetName)
  {
    return getMimeTypeFromBytes (s == null ? null : CharsetManager.getAsBytes (s, sCharsetName));
  }

  @Nonnull
  public static IMimeType getMimeTypeFromString (@Nullable final String s, @Nonnull final Charset aCharset)
  {
    return getMimeTypeFromString (s, aCharset, CMimeType.APPLICATION_OCTET_STREAM);
  }

  @Nullable
  public static IMimeType getMimeTypeFromString (@Nullable final String s,
                                                 @Nonnull final Charset aCharset,
                                                 @Nullable final IMimeType aDefault)
  {
    return getMimeTypeFromBytes (s == null ? null : CharsetManager.getAsBytes (s, aCharset), aDefault);
  }

  /**
   * Try to determine the MIME type from the given byte array.
   * 
   * @param b
   *        The byte array. to parse.
   * @return {@link CMimeType#APPLICATION_OCTET_STREAM} if no specific MIME type
   *         was found
   */
  @Nonnull
  public static IMimeType getMimeTypeFromBytes (@Nullable final byte [] b)
  {
    return getMimeTypeFromBytes (b, CMimeType.APPLICATION_OCTET_STREAM);
  }

  /**
   * Try to determine the MIME type from the given byte array.
   * 
   * @param b
   *        The byte array to parse. May be <code>null</code>.
   * @param aDefault
   *        The default MIME type to be returned, if no matching MIME type was
   *        found. May be <code>null</code>.
   * @returnThe supplied default value, if no matching MIME type was found
   */
  @Nullable
  public static IMimeType getMimeTypeFromBytes (@Nullable final byte [] b, @Nullable final IMimeType aDefault)
  {
    if (b != null)
    {
      s_aRWLock.readLock ().lock ();
      try
      {
        for (final MimeTypeContent aMTC : s_aContents)
          if (aMTC.matchesBeginning (b))
            return aMTC.getMimeType ();
      }
      finally
      {
        s_aRWLock.readLock ().unlock ();
      }
    }

    // default fallback
    return aDefault;
  }

  /**
   * @return A copy of all registered {@link MimeTypeContent} objects.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static List <MimeTypeContent> getAllMimeTypeContents ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (s_aContents);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
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

  /**
   * Reset the MimeTypeContent cache to the initial state.
   */
  public static void resetCache ()
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aContents.clear ();
      _registerDefaultMimeTypeContents ();
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Cache was reset: " + MimeTypeDeterminator.class.getName ());
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }
}
