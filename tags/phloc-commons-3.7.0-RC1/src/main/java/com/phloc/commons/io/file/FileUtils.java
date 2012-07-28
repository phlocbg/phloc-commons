/**
 * Copyright (C) 2006-2012 phloc systems
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
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.SystemProperties;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.misc.SizeHelper;
import com.phloc.commons.io.streams.CountingFileInputStream;
import com.phloc.commons.io.streams.CountingFileOutputStream;
import com.phloc.commons.state.EChange;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Miscellaneous file utility methods.
 * 
 * @author philip
 */
@Immutable
public final class FileUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FileUtils.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final FileUtils s_aInstance = new FileUtils ();

  private FileUtils ()
  {}

  /**
   * Check if the passed file exists. Must be existing and a file.
   * 
   * @param aFile
   *        The file to be checked for existence. May not be <code>null</code> .
   * @return <code>true</code> if the File is a file and exists,
   *         <code>false</code> otherwise.
   */
  public static boolean existsFile (@Nonnull final File aFile)
  {
    if (aFile == null)
      throw new NullPointerException ("file");

    return aFile.isFile () && aFile.exists ();
  }

  /**
   * Check if the passed directory exists. Must be existing and must be a
   * directory!
   * 
   * @param aDir
   *        The directory to be checked for existence. May not be
   *        <code>null</code>.
   * @return <code>true</code> if the directory exists, <code>false</code>
   *         otherwise.
   */
  public static boolean existsDir (@Nonnull final File aDir)
  {
    if (aDir == null)
      throw new NullPointerException ("directory");

    // returns true if it exists() AND is a directory!
    return aDir.isDirectory ();
  }

  /**
   * Tests whether the application can read the file denoted by this abstract
   * pathname.
   * 
   * @param aFile
   *        The file to be checked. May not be <code>null</code>.
   * @return <code>true</code> if and only if the file specified by this
   *         abstract pathname exists <em>and</em> can be read by the
   *         application; <code>false</code> otherwise
   */
  public static boolean canRead (@Nonnull final File aFile)
  {
    if (aFile == null)
      throw new NullPointerException ("file");
    return aFile.canRead ();
  }

  /**
   * Tests whether the application can modify the file denoted by this abstract
   * pathname.
   * 
   * @param aFile
   *        The file to be checked. May not be <code>null</code>.
   * @return <code>true</code> if and only if the file system actually contains
   *         a file denoted by this abstract pathname <em>and</em> the
   *         application is allowed to write to the file; <code>false</code>
   *         otherwise.
   */
  public static boolean canWrite (@Nonnull final File aFile)
  {
    if (aFile == null)
      throw new NullPointerException ("file");
    return aFile.canWrite ();
  }

  /**
   * Tests whether the application can execute the file denoted by this abstract
   * pathname.
   * 
   * @param aFile
   *        The file to be checked. May not be <code>null</code>.
   * @return <code>true</code> if and only if the abstract pathname exists
   *         <em>and</em> the application is allowed to execute the file
   */
  public static boolean canExecute (@Nonnull final File aFile)
  {
    if (aFile == null)
      throw new NullPointerException ("file");
    return aFile.canExecute ();
  }

  @Nonnull
  public static EChange ensureParentDirectoryIsPresent (@Nonnull final File aFileObject)
  {
    if (aFileObject == null)
      throw new NullPointerException ("file");

    // If the file has no parent, it is located in the root...
    final File aParent = aFileObject.getParentFile ();
    if (aParent == null || aParent.exists ())
    {
      if (aParent != null && !aParent.isDirectory ())
        s_aLogger.warn ("Parent object specified is not a directory: '" + aParent + "'");
      return EChange.UNCHANGED;
    }

    // Now try to create the directory
    final FileIOError aError = FileOperations.createDirRecursive (aParent);
    if (aError.isFailure ())
      throw new IllegalStateException ("Failed to create parent of " + aFileObject.getAbsolutePath () + ": " + aError);

    // Check again if it exists, to be 100% sure :)
    if (!aParent.exists ())
      throw new IllegalStateException ("Parent of " + aFileObject.getAbsolutePath () + " is still not existing!");
    return EChange.CHANGED;
  }

  @Nullable
  public static File getCanonicalFile (@Nullable final File aFile) throws IOException
  {
    return aFile == null ? null : aFile.getCanonicalFile ();
  }

  /**
   * Check if the searched directory is a parent object of the start directory
   * 
   * @param aSearchDirectory
   *        The directory to be searched. May not be <code>null</code>.
   * @param aStartDirectory
   *        The directory where the search starts. May not be <code>null</code>.
   * @return <code>true</code> if the search directory is a parent of the start
   *         directory, <code>false</code> otherwise.
   */
  @SuppressFBWarnings ("IL_INFINITE_LOOP")
  public static boolean isParentDirectory (@Nonnull final File aSearchDirectory, @Nonnull final File aStartDirectory)
  {
    if (aSearchDirectory == null)
      throw new NullPointerException ("search directory");
    if (aStartDirectory == null)
      throw new NullPointerException ("start directory");

    File aRealSearchDirectory = aSearchDirectory.getAbsoluteFile ();
    File aRealStartDirectory = aStartDirectory.getAbsoluteFile ();
    try
    {
      aRealSearchDirectory = getCanonicalFile (aRealSearchDirectory);
      aRealStartDirectory = getCanonicalFile (aRealStartDirectory);
    }
    catch (final IOException ex)
    {
      // ignore
    }
    if (!aRealSearchDirectory.isDirectory ())
      return false;

    File aParent = aRealStartDirectory;
    while (aParent != null)
    {
      if (aParent.equals (aRealSearchDirectory))
        return true;
      aParent = aParent.getParentFile ();
    }
    return false;
  }

  @Nullable
  public static FileInputStream getInputStream (@Nonnull final String sFilename)
  {
    return getInputStream (new File (sFilename));
  }

  @Nullable
  public static FileInputStream getInputStream (@Nonnull final File aFile)
  {
    if (aFile == null)
      throw new NullPointerException ("file");

    try
    {
      return new CountingFileInputStream (aFile);
    }
    catch (final FileNotFoundException ex)
    {
      return null;
    }
  }

  /**
   * Get an output stream for writing to a file. Any existing file is
   * overwritten.
   * 
   * @param sFilename
   *        The name of the file to write to. May not be <code>null</code>.
   * @return <code>null</code> if the file could not be opened
   */
  @Nullable
  public static FileOutputStream getOutputStream (@Nonnull final String sFilename)
  {
    return getOutputStream (sFilename, EAppend.DEFAULT);
  }

  /**
   * Get an output stream for writing to a file.
   * 
   * @param sFilename
   *        The name of the file to write to. May not be <code>null</code>.
   * @param eAppend
   *        Appending mode. May not be <code>null</code>.
   * @return <code>null</code> if the file could not be opened
   */
  @Nullable
  public static FileOutputStream getOutputStream (@Nonnull final String sFilename, @Nonnull final EAppend eAppend)
  {
    return getOutputStream (new File (sFilename), eAppend);
  }

  /**
   * Get an output stream for writing to a file.
   * 
   * @param aFile
   *        The file to write to. May not be <code>null</code>.
   * @return <code>null</code> if the file could not be opened
   */
  @Nullable
  public static FileOutputStream getOutputStream (@Nonnull final File aFile)
  {
    return getOutputStream (aFile, EAppend.DEFAULT);
  }

  /**
   * Get an output stream for writing to a file.
   * 
   * @param aFile
   *        The file to write to. May not be <code>null</code>.
   * @param eAppend
   *        Appending mode. May not be <code>null</code>.
   * @return <code>null</code> if the file could not be opened
   */
  @Nullable
  public static FileOutputStream getOutputStream (@Nonnull final File aFile, @Nonnull final EAppend eAppend)
  {
    if (aFile == null)
      throw new NullPointerException ("file");

    try
    {
      ensureParentDirectoryIsPresent (aFile);
    }
    catch (final IllegalStateException ex)
    {
      // Happens e.g. when the parent directory is "  "
      s_aLogger.warn ("Failed to create parent directory of '" + aFile + "'", ex);
      return null;
    }

    // Check if parent directory is writable, to avoid catching the
    // FileNotFoundException with "permission denied" afterwards
    final File aParentDir = aFile.getParentFile ();
    if (aParentDir != null && !canWrite (aParentDir))
    {
      s_aLogger.warn ("Parent directory '" +
                      aParentDir +
                      "' of '" +
                      aFile +
                      "' is not writable for current user '" +
                      SystemProperties.getUserName () +
                      "'");
      return null;
    }

    // OK, parent is present
    try
    {
      return new CountingFileOutputStream (aFile, eAppend);
    }
    catch (final FileNotFoundException ex)
    {
      s_aLogger.warn ("Failed to create output stream for '" +
                      aFile +
                      "'; append: " +
                      eAppend +
                      ": " +
                      ex.getClass ().getName () +
                      " - " +
                      ex.getMessage ());
      return null;
    }
  }

  /**
   * Returns <code>true</code> if the first file is newer than the second file.
   * Returns <code>true</code> if the first file exists and the second file does
   * not exist. Returns <code>false</code> if the first file is older than the
   * second file. Returns <code>false</code> if the first file does not exists
   * but the second does. Returns <code>false</code> if none of the files exist.
   * 
   * @param aFile1
   *        First file. May not be <code>null</code>.
   * @param aFile2
   *        Second file. May not be <code>null</code>.
   * @return <code>true</code> if the first file is newer than the second file,
   *         <code>false</code> otherwise.
   */
  public static boolean isFileNewer (@Nonnull final File aFile1, @Nonnull final File aFile2)
  {
    if (aFile1 == null)
      throw new NullPointerException ("firstFile");
    if (aFile2 == null)
      throw new NullPointerException ("secondFile");

    // Compare with the same file?
    if (aFile1.equals (aFile2))
      return false;

    // if the first file does not exists, always false
    if (!aFile1.exists ())
      return false;

    // first file exists, but second file does not
    if (!aFile2.exists ())
      return true;

    // both exist, compare file times
    return aFile1.lastModified () > aFile2.lastModified ();
  }

  @Nonnull
  public static String getFileSizeDisplay (@Nullable final File aFile)
  {
    return getFileSizeDisplay (aFile, 0);
  }

  @Nonnull
  public static String getFileSizeDisplay (@Nullable final File aFile, @Nonnegative final int nDecimals)
  {
    if (aFile != null && aFile.exists ())
      return getFileSizeDisplay (aFile.length (), nDecimals);
    return "";
  }

  @Nonnull
  public static String getFileSizeDisplay (final long nFileSize)
  {
    return getFileSizeDisplay (nFileSize, 0);
  }

  @Nonnull
  public static String getFileSizeDisplay (@Nonnegative final long nFileSize, @Nonnegative final int nDecimals)
  {
    if (nFileSize < 0)
      throw new IllegalArgumentException ("File size may not be negative!");
    if (nDecimals < 0)
      throw new IllegalArgumentException ("Decimals may not be negative!");

    return SizeHelper.getSizeHelperOfLocale (CGlobal.LOCALE_FIXED_NUMBER_FORMAT).getAsMatching (nFileSize, nDecimals);
  }

  /**
   * Get a secure {@link File} object based on the passed file object. First all
   * relative paths ("." and "..") are resolved and all eventually contained
   * '\0' characters are eliminated. Than all file names are checked for
   * validity (so that no special characters are contained).
   * 
   * @param aFile
   *        The file to be secured.
   * @return <code>null</code> if the passed file is <code>null</code>.
   */
  @Nullable
  public static File getSecureFile (@Nullable final File aFile)
  {
    if (aFile == null)
      return null;

    String sRet = null;
    // Go through all directories and secure them
    File aWork = new File (FilenameHelper.getCleanPath (aFile));
    while (aWork != null)
    {
      String sSecuredName;
      if (aWork.getParent () == null)
      {
        // For the root directory
        sSecuredName = aWork.getPath ();
      }
      else
      {
        // Secure path element name
        sSecuredName = FilenameHelper.getAsSecureValidFilename (aWork.getName ());
      }
      sRet = sRet == null ? sSecuredName : sSecuredName + "/" + sRet;
      aWork = aWork.getParentFile ();
    }
    return new File (sRet);
  }

  /**
   * Returns the number of files and directories contained in the passed
   * directory excluding the system internal directories.
   * 
   * @param aDirectory
   *        The directory to check. May not be <code>null</code> and must be a
   *        directory.
   * @return A non-negative number of objects in that directory.
   * @see FilenameHelper#isSystemInternalDirectory(CharSequence)
   */
  @Nonnegative
  public static int getDirectoryObjectCount (@Nonnull final File aDirectory)
  {
    if (aDirectory == null)
      throw new NullPointerException ("directory");
    if (!aDirectory.isDirectory ())
      throw new IllegalArgumentException ("Passed object is not a directory: " + aDirectory);

    int ret = 0;
    for (final File aChild : getDirectoryContent (aDirectory))
      if (!FilenameHelper.isSystemInternalDirectory (aChild.getName ()))
        ret++;
    return ret;
  }

  /**
   * Check if the passed 2 files are equal using the unified (unix separator),
   * absolute and cleaned (no "." or "..") path.
   * 
   * @param f1
   *        First file. May be <code>null</code>.
   * @param f2
   *        Second file. May be <code>null</code>.
   * @return <code>true</code> if both are null or equal.
   */
  public static boolean safeEquals (@Nullable final File f1, @Nullable final File f2)
  {
    if (f1 == f2)
      return true;
    if (f1 == null || f2 == null)
      return false;
    return FilenameHelper.getCleanPath (f1.getAbsoluteFile ())
                         .equals (FilenameHelper.getCleanPath (f2.getAbsoluteFile ()));
  }

  @Nonnull
  @ReturnsMutableCopy
  private static List <File> _getDirectoryContent (@Nonnull final File aDirectory,
                                                   @Nullable final File [] aSelectedContent)
  {
    if (!canExecute (aDirectory))
    {
      // If this happens, the resulting File objects are neither files nor
      // directories (isFile() and isDirectory() both return false!!)
      s_aLogger.warn ("Directory is missing the listing permission: " + aDirectory.getAbsolutePath ());
    }

    if (aSelectedContent == null)
    {
      // No content returned
      if (!aDirectory.isDirectory ())
        s_aLogger.warn ("Cannot list non-directory: " + aDirectory.getAbsolutePath ());
      else
        if (!canRead (aDirectory))
          s_aLogger.warn ("Cannot list directory because of no read-rights: " + aDirectory.getAbsolutePath ());
        else
          s_aLogger.warn ("Directory listing failed because of IO error: " + aDirectory.getAbsolutePath ());
    }
    return ContainerHelper.newList (aSelectedContent);
  }

  /**
   * This is a replacement for <code>File.listFiles()</code> doing some
   * additional checks on permissions. The order of the returned files is
   * defined by the underlying {@link File#listFiles()} method.
   * 
   * @param aDirectory
   *        The directory to be listed. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static List <File> getDirectoryContent (@Nonnull final File aDirectory)
  {
    if (aDirectory == null)
      throw new NullPointerException ("directory");

    return _getDirectoryContent (aDirectory, aDirectory.listFiles ());
  }

  /**
   * This is a replacement for <code>File.listFiles(FilenameFilter)</code> doing
   * some additional checks on permissions. The order of the returned files is
   * defined by the underlying {@link File#listFiles(FilenameFilter)} method.
   * 
   * @param aDirectory
   *        The directory to be listed. May not be <code>null</code>.
   * @param aFilenameFilter
   *        The filename filter to be used. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static List <File> getDirectoryContent (@Nonnull final File aDirectory,
                                                 @Nonnull final FilenameFilter aFilenameFilter)
  {
    if (aDirectory == null)
      throw new NullPointerException ("directory");
    if (aFilenameFilter == null)
      throw new NullPointerException ("filenameFilter");

    return _getDirectoryContent (aDirectory, aDirectory.listFiles (aFilenameFilter));
  }

  /**
   * This is a replacement for <code>File.listFiles(FileFilter)</code> doing
   * some additional checks on permissions. The order of the returned files is
   * defined by the underlying {@link File#listFiles(FileFilter)} method.
   * 
   * @param aDirectory
   *        The directory to be listed. May not be <code>null</code>.
   * @param aFileFilter
   *        The file filter to be used. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static List <File> getDirectoryContent (@Nonnull final File aDirectory, @Nonnull final FileFilter aFileFilter)
  {
    if (aDirectory == null)
      throw new NullPointerException ("directory");
    if (aFileFilter == null)
      throw new NullPointerException ("fileFilter");

    return _getDirectoryContent (aDirectory, aDirectory.listFiles (aFileFilter));
  }
}
