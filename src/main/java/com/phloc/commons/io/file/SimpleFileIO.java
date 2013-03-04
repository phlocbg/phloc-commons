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
package com.phloc.commons.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.channels.ChannelUtils;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringHelper;

/**
 * All kind of file handling stuff.
 * 
 * @author philip
 */
@Immutable
public final class SimpleFileIO
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SimpleFileIO.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final SimpleFileIO s_aInstance = new SimpleFileIO ();

  private SimpleFileIO ()
  {}

  /**
   * Copy the content of the source file to the destination file
   * 
   * @param aSrcFile
   *        Source file. May not be <code>null</code>.
   * @param aDestFile
   *        Destination file. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess copyFile (@Nonnull final File aSrcFile, @Nonnull final File aDestFile)
  {
    final FileInputStream aFIS = FileUtils.getInputStream (aSrcFile);
    if (aFIS == null)
      return ESuccess.FAILURE;

    try
    {
      final FileOutputStream aFOS = FileUtils.getOutputStream (aDestFile);
      if (aFOS == null)
        return ESuccess.FAILURE;

      try
      {
        final FileChannel aSrcChannel = aFIS.getChannel ();
        final FileChannel aDestChannel = aFOS.getChannel ();
        FileLock aSrcLock = null;
        FileLock aDestLock = null;
        try
        {
          final long nBytesToRead = aSrcChannel.size ();

          // Shared read lock and exclusive write lock
          aSrcLock = aSrcChannel.lock (0, nBytesToRead, true);
          aDestLock = aDestChannel.lock ();

          // Main copying
          final long nBytesWritten = aSrcChannel.transferTo (0, nBytesToRead, aDestChannel);
          if (nBytesToRead != nBytesWritten)
          {
            s_aLogger.error ("Failed to copy file. Meant to read " +
                             nBytesToRead +
                             " bytes but wrote only " +
                             nBytesWritten);
            return ESuccess.FAILURE;
          }
          return ESuccess.SUCCESS;
        }
        catch (final IOException ex)
        {
          throw new IllegalStateException ("Failed to copy from " + aSrcFile + " to " + aDestFile, ex);
        }
        finally
        {
          // Unlock
          ChannelUtils.release (aDestLock);
          ChannelUtils.release (aSrcLock);
          // Close
          StreamUtils.close (aSrcChannel);
          StreamUtils.close (aDestChannel);
        }
      }
      finally
      {
        StreamUtils.close (aFOS);
      }
    }
    finally
    {
      StreamUtils.close (aFIS);
    }
  }

  /**
   * Get the content of the file as a byte array.
   * 
   * @param aFile
   *        The file to read. May be <code>null</code>.
   * @return <code>null</code> if the passed file is <code>null</code> or if the
   *         passed file does not exist.
   */
  @Nullable
  public static byte [] readFileBytes (@Nullable final File aFile)
  {
    return aFile == null ? null : StreamUtils.getAllBytes (FileUtils.getInputStream (aFile));
  }

  /**
   * Get the content of the passed file as a string using the system line
   * separator. Note: the last line does not end with the passed line separator.
   * 
   * @param aFile
   *        The file to read. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @return <code>null</code> if the file does not exist, the content
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static String readFileAsString (@Nullable final File aFile, @Nonnull final String sCharset)
  {
    return aFile == null ? null : StreamUtils.getAllBytesAsString (FileUtils.getInputStream (aFile), sCharset);
  }

  /**
   * Get the content of the passed file as a string using the system line
   * separator. Note: the last line does not end with the passed line separator.
   * 
   * @param aFile
   *        The file to read. May be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @return <code>null</code> if the file does not exist, the content
   *         otherwise.
   */
  @Nullable
  public static String readFileAsString (@Nullable final File aFile, @Nonnull final Charset aCharset)
  {
    return aFile == null ? null : StreamUtils.getAllBytesAsString (FileUtils.getInputStream (aFile), aCharset);
  }

  /**
   * Get the content of the passed file as a list of lines, whereas each line
   * does not contain a separator.
   * 
   * @param aFile
   *        The file to read. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @return <code>null</code> if the file does not exist, the content
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static List <String> readFileLines (@Nullable final File aFile, @Nonnull final String sCharset)
  {
    return aFile == null ? null : StreamUtils.readStreamLines (FileUtils.getInputStream (aFile), sCharset);
  }

  /**
   * Get the content of the passed file as a list of lines, whereas each line
   * does not contain a separator.
   * 
   * @param aFile
   *        The file to read. May be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @return <code>null</code> if the file does not exist, the content
   *         otherwise.
   */
  @Nullable
  public static List <String> readFileLines (@Nullable final File aFile, @Nonnull final Charset aCharset)
  {
    return aFile == null ? null : StreamUtils.readStreamLines (FileUtils.getInputStream (aFile), aCharset);
  }

  /**
   * Get the content of the passed file as a list of lines, whereas each line
   * does not contain a separator.
   * 
   * @param aFile
   *        The file to read. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param aTargetList
   *        The target list to be filled. May not be <code>null</code>.
   */
  @Deprecated
  public static void readFileLines (@Nullable final File aFile,
                                    @Nonnull final String sCharset,
                                    @Nonnull final List <String> aTargetList)
  {
    if (aFile != null)
      StreamUtils.readStreamLines (FileUtils.getInputStream (aFile), sCharset, aTargetList);
  }

  /**
   * Get the content of the passed file as a list of lines, whereas each line
   * does not contain a separator.
   * 
   * @param aFile
   *        The file to read. May be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @param aTargetList
   *        The target list to be filled. May not be <code>null</code>.
   */
  public static void readFileLines (@Nullable final File aFile,
                                    @Nonnull final Charset aCharset,
                                    @Nonnull final List <String> aTargetList)
  {
    if (aFile != null)
      StreamUtils.readStreamLines (FileUtils.getInputStream (aFile), aCharset, aTargetList);
  }

  @Nonnull
  public static ESuccess writeFile (@Nonnull final File aFile, @Nonnull final byte [] aContent)
  {
    final OutputStream aFOS = FileUtils.getOutputStream (aFile);
    return aFOS == null ? ESuccess.FAILURE : StreamUtils.writeStream (aFOS, aContent);
  }

  @Nonnull
  public static ESuccess writeFile (@Nonnull final File aFile,
                                    @Nonnull final byte [] aContent,
                                    @Nonnegative final int nOffset,
                                    @Nonnegative final int nLength)
  {
    final OutputStream aFOS = FileUtils.getOutputStream (aFile);
    return aFOS == null ? ESuccess.FAILURE : StreamUtils.writeStream (aFOS, aContent, nOffset, nLength);
  }

  @Nonnull
  @Deprecated
  public static ESuccess writeFile (@Nonnull final File aFile,
                                    @Nonnull final String sContent,
                                    @Nonnull final String sCharset)
  {
    final OutputStream aFOS = FileUtils.getOutputStream (aFile);
    return aFOS == null ? ESuccess.FAILURE : StreamUtils.writeStream (aFOS, sContent, sCharset);
  }

  @Nonnull
  public static ESuccess writeFile (@Nonnull final File aFile,
                                    @Nonnull final String sContent,
                                    @Nonnull final Charset aCharset)
  {
    final OutputStream aFOS = FileUtils.getOutputStream (aFile);
    return aFOS == null ? ESuccess.FAILURE : StreamUtils.writeStream (aFOS, sContent, aCharset);
  }

  @Nonnull
  @Deprecated
  public static ESuccess writeFile (@Nonnull final File aFile,
                                    @Nonnull final List <String> aContent,
                                    @Nonnull final String sCharset)
  {
    return writeFile (aFile, StringHelper.getImploded (CGlobal.LINE_SEPARATOR, aContent), sCharset);
  }

  @Nonnull
  public static ESuccess writeFile (@Nonnull final File aFile,
                                    @Nonnull final List <String> aContent,
                                    @Nonnull final Charset aCharset)
  {
    return writeFile (aFile, StringHelper.getImploded (CGlobal.LINE_SEPARATOR, aContent), aCharset);
  }
}
