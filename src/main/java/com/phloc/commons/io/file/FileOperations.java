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
package com.phloc.commons.io.file;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.channels.ChannelUtils;
import com.phloc.commons.state.ESuccess;

/**
 * Wraps file operations.
 * 
 * @author Philip Helger
 */
public final class FileOperations
{
  /** The default value for warning if we're about to delete the root directory. */
  public static final boolean DEFAULT_WARN_ON_DELETE_ROOT = true;

  private static final Logger s_aLogger = LoggerFactory.getLogger (FileOperations.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final FileOperations s_aInstance = new FileOperations ();

  private static boolean s_bWarnOnDeleteRoot = DEFAULT_WARN_ON_DELETE_ROOT;

  private FileOperations ()
  {}

  public static boolean isWarnOnDeleteRoot ()
  {
    return s_bWarnOnDeleteRoot;
  }

  public static void setWarnOnDeleteRoot (final boolean bWarnOnDeleteRoot)
  {
    s_bWarnOnDeleteRoot = bWarnOnDeleteRoot;
  }

  /**
   * Create a new directory. The direct parent directory already needs to exist.
   * 
   * @param aDir
   *        The directory to be created. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError createDir (@Nonnull final File aDir)
  {
    ValueEnforcer.notNull (aDir, "Directory");

    // Does the directory already exist?
    if (aDir.exists ())
      return EFileIOErrorCode.TARGET_ALREADY_EXISTS.getAsIOError (EFileIOOperation.CREATE_DIR, aDir);

    // Is the parent directory writable?
    final File aParentDir = aDir.getParentFile ();
    if (aParentDir != null && aParentDir.exists () && !FileUtils.canWrite (aParentDir))
      return EFileIOErrorCode.SOURCE_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.CREATE_DIR, aDir);

    try
    {
      final EFileIOErrorCode eError = aDir.mkdir () ? EFileIOErrorCode.NO_ERROR : EFileIOErrorCode.OPERATION_FAILED;
      return eError.getAsIOError (EFileIOOperation.CREATE_DIR, aDir);
    }
    catch (final SecurityException ex)
    {
      return EFileIOErrorCode.getAsIOError (EFileIOOperation.CREATE_DIR, ex);
    }
  }

  /**
   * Create a new directory if it does not exist. The direct parent directory
   * already needs to exist.
   * 
   * @param aDir
   *        The directory to be created if it does not exist. May not be
   *        <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError createDirIfNotExisting (@Nonnull final File aDir)
  {
    final FileIOError aError = createDir (aDir);
    if (aError.getErrorCode ().equals (EFileIOErrorCode.TARGET_ALREADY_EXISTS))
      return EFileIOErrorCode.NO_ERROR.getAsIOError (EFileIOOperation.CREATE_DIR, aDir);
    return aError;
  }

  /**
   * Create a new directory. The parent directories are created if they are
   * missing.
   * 
   * @param aDir
   *        The directory to be created. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError createDirRecursive (@Nonnull final File aDir)
  {
    ValueEnforcer.notNull (aDir, "Directory");

    // Does the directory already exist?
    if (aDir.exists ())
      return EFileIOErrorCode.TARGET_ALREADY_EXISTS.getAsIOError (EFileIOOperation.CREATE_DIR_RECURSIVE, aDir);

    // Is the parent directory writable?
    final File aParentDir = aDir.getParentFile ();
    if (aParentDir != null && aParentDir.exists () && !FileUtils.canWrite (aParentDir))
      return EFileIOErrorCode.SOURCE_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.CREATE_DIR_RECURSIVE, aDir);

    try
    {
      final EFileIOErrorCode eError = aDir.mkdirs () ? EFileIOErrorCode.NO_ERROR : EFileIOErrorCode.OPERATION_FAILED;
      return eError.getAsIOError (EFileIOOperation.CREATE_DIR_RECURSIVE, aDir);
    }
    catch (final SecurityException ex)
    {
      return EFileIOErrorCode.getAsIOError (EFileIOOperation.CREATE_DIR_RECURSIVE, ex);
    }
  }

  /**
   * Create a new directory if it does not exist. The direct parent directory
   * already needs to exist.
   * 
   * @param aDir
   *        The directory to be created if it does not exist. May not be
   *        <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError createDirRecursiveIfNotExisting (@Nonnull final File aDir)
  {
    final FileIOError aError = createDirRecursive (aDir);
    if (aError.getErrorCode ().equals (EFileIOErrorCode.TARGET_ALREADY_EXISTS))
      return EFileIOErrorCode.NO_ERROR.getAsIOError (EFileIOOperation.CREATE_DIR_RECURSIVE, aDir);
    return aError;
  }

  /**
   * Delete an existing directory. The directory needs to be empty before it can
   * be deleted.
   * 
   * @param aDir
   *        The directory to be deleted. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError deleteDir (@Nonnull final File aDir)
  {
    ValueEnforcer.notNull (aDir, "Directory");

    // Does the directory not exist?
    if (!FileUtils.existsDir (aDir))
      return EFileIOErrorCode.SOURCE_DOES_NOT_EXIST.getAsIOError (EFileIOOperation.DELETE_DIR, aDir);

    if (isWarnOnDeleteRoot ())
    {
      // Check that we're not deleting the complete hard drive...
      if (aDir.getAbsoluteFile ().getParent () == null)
        throw new IllegalArgumentException ("Aren't we deleting the full drive: '" + aDir.getAbsolutePath () + "'");
    }

    // Is the parent directory writable?
    final File aParentDir = aDir.getParentFile ();
    if (aParentDir != null && !FileUtils.canWrite (aParentDir))
      return EFileIOErrorCode.SOURCE_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.DELETE_DIR, aDir);

    try
    {
      // delete may return true even so it internally failed!
      final EFileIOErrorCode eError = aDir.delete () && !aDir.exists () ? EFileIOErrorCode.NO_ERROR
                                                                       : EFileIOErrorCode.OPERATION_FAILED;
      return eError.getAsIOError (EFileIOOperation.DELETE_DIR, aDir);
    }
    catch (final SecurityException ex)
    {
      return EFileIOErrorCode.getAsIOError (EFileIOOperation.DELETE_DIR, ex);
    }
  }

  /**
   * Delete an existing directory if it is existing. The directory needs to be
   * empty before it can be deleted.
   * 
   * @param aDir
   *        The directory to be deleted. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError deleteDirIfExisting (@Nonnull final File aDir)
  {
    final FileIOError aError = deleteDir (aDir);
    if (aError.getErrorCode ().equals (EFileIOErrorCode.SOURCE_DOES_NOT_EXIST))
      return EFileIOErrorCode.NO_ERROR.getAsIOError (EFileIOOperation.DELETE_DIR, aDir);
    return aError;
  }

  /**
   * Delete an existing directory including all child objects.
   * 
   * @param aDir
   *        The directory to be deleted. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError deleteDirRecursive (@Nonnull final File aDir)
  {
    ValueEnforcer.notNull (aDir, "Directory");

    // Non-existing directory?
    if (!FileUtils.existsDir (aDir))
      return EFileIOErrorCode.SOURCE_DOES_NOT_EXIST.getAsIOError (EFileIOOperation.DELETE_DIR_RECURSIVE, aDir);

    if (isWarnOnDeleteRoot ())
    {
      // Check that we're not deleting the complete hard drive...
      if (aDir.getAbsoluteFile ().getParent () == null)
        throw new IllegalArgumentException ("Aren't we deleting the full drive: '" + aDir.getAbsolutePath () + "'");
    }

    // Is the parent directory writable?
    final File aParentDir = aDir.getParentFile ();
    if (aParentDir != null && !FileUtils.canWrite (aParentDir))
      return EFileIOErrorCode.SOURCE_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.DELETE_DIR_RECURSIVE, aDir);

    // iterate directory
    for (final File aChild : FileUtils.getDirectoryContent (aDir))
    {
      // is it a file or a directory or ...
      if (aChild.isDirectory ())
      {
        // Ignore "." and ".." directory
        if (FilenameHelper.isSystemInternalDirectory (aChild.getName ()))
          continue;

        // recursive call
        final FileIOError eCode = deleteDirRecursive (aChild);
        if (eCode.isFailure ())
          return eCode;
      }
      else
        if (aChild.isFile ())
        {
          // delete file
          final FileIOError eCode = deleteFile (aChild);
          if (eCode.isFailure ())
            return eCode;
        }
        else
        {
          // Neither directory no file - don't know how to handle
          return EFileIOErrorCode.OBJECT_CANNOT_BE_HANDLED.getAsIOError (EFileIOOperation.DELETE_DIR_RECURSIVE, aChild);
        }
    }

    // Now this directory should be empty -> delete as if empty
    return deleteDir (aDir);
  }

  /**
   * Delete an existing directory including all child objects if it is existing.
   * 
   * @param aDir
   *        The directory to be deleted. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError deleteDirRecursiveIfExisting (@Nonnull final File aDir)
  {
    final FileIOError aError = deleteDirRecursive (aDir);
    if (aError.getErrorCode ().equals (EFileIOErrorCode.SOURCE_DOES_NOT_EXIST))
      return EFileIOErrorCode.NO_ERROR.getAsIOError (EFileIOOperation.DELETE_DIR_RECURSIVE, aDir);
    return aError;
  }

  /**
   * Delete an existing file.
   * 
   * @param aFile
   *        The file to be deleted. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError deleteFile (@Nonnull final File aFile)
  {
    ValueEnforcer.notNull (aFile, "File");

    if (!FileUtils.existsFile (aFile))
      return EFileIOErrorCode.SOURCE_DOES_NOT_EXIST.getAsIOError (EFileIOOperation.DELETE_FILE, aFile);

    // Is the parent directory writable?
    final File aParentDir = aFile.getParentFile ();
    if (aParentDir != null && !FileUtils.canWrite (aParentDir))
      return EFileIOErrorCode.SOURCE_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.DELETE_FILE, aFile);

    try
    {
      // delete may return true even so it internally failed!
      final EFileIOErrorCode eError = aFile.delete () && !aFile.exists () ? EFileIOErrorCode.NO_ERROR
                                                                         : EFileIOErrorCode.OPERATION_FAILED;
      return eError.getAsIOError (EFileIOOperation.DELETE_FILE, aFile);
    }
    catch (final SecurityException ex)
    {
      return EFileIOErrorCode.getAsIOError (EFileIOOperation.DELETE_FILE, ex);
    }
  }

  /**
   * Delete a file if it is existing.
   * 
   * @param aFile
   *        The file to be deleted. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError deleteFileIfExisting (@Nonnull final File aFile)
  {
    final FileIOError aError = deleteFile (aFile);
    if (aError.getErrorCode ().equals (EFileIOErrorCode.SOURCE_DOES_NOT_EXIST))
      return EFileIOErrorCode.NO_ERROR.getAsIOError (EFileIOOperation.DELETE_FILE, aFile);
    return aError;
  }

  /**
   * Rename a file.
   * 
   * @param aSourceFile
   *        The original file name. May not be <code>null</code>.
   * @param aTargetFile
   *        The destination file name. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError renameFile (@Nonnull final File aSourceFile, @Nonnull final File aTargetFile)
  {
    ValueEnforcer.notNull (aSourceFile, "SourceFile");
    ValueEnforcer.notNull (aTargetFile, "TargetFile");

    // Does the source file exist?
    if (!FileUtils.existsFile (aSourceFile))
      return EFileIOErrorCode.SOURCE_DOES_NOT_EXIST.getAsIOError (EFileIOOperation.RENAME_FILE, aSourceFile);

    // Are source and target different?
    if (EqualsUtils.equals (aSourceFile, aTargetFile))
      return EFileIOErrorCode.SOURCE_EQUALS_TARGET.getAsIOError (EFileIOOperation.RENAME_FILE, aSourceFile);

    // Does the target file already exist?
    if (aTargetFile.exists ())
      return EFileIOErrorCode.TARGET_ALREADY_EXISTS.getAsIOError (EFileIOOperation.RENAME_FILE, aTargetFile);

    // Is the source parent directory writable?
    final File aSourceParentDir = aSourceFile.getParentFile ();
    if (aSourceParentDir != null && !FileUtils.canWrite (aSourceParentDir))
      return EFileIOErrorCode.SOURCE_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.RENAME_FILE, aSourceFile);

    // Is the target parent directory writable?
    final File aTargetParentDir = aTargetFile.getParentFile ();
    if (aTargetParentDir != null && aTargetParentDir.exists () && !FileUtils.canWrite (aTargetParentDir))
      return EFileIOErrorCode.TARGET_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.RENAME_FILE, aTargetFile);

    // Ensure parent of target directory is present
    FileUtils.ensureParentDirectoryIsPresent (aTargetFile);

    try
    {
      final EFileIOErrorCode eError = aSourceFile.renameTo (aTargetFile) ? EFileIOErrorCode.NO_ERROR
                                                                        : EFileIOErrorCode.OPERATION_FAILED;
      return eError.getAsIOError (EFileIOOperation.RENAME_FILE, aSourceFile, aTargetFile);
    }
    catch (final SecurityException ex)
    {
      return EFileIOErrorCode.getAsIOError (EFileIOOperation.RENAME_FILE, ex);
    }
  }

  /**
   * Rename a directory.
   * 
   * @param aSourceDir
   *        The original directory name. May not be <code>null</code>.
   * @param aTargetDir
   *        The destination directory name. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError renameDir (@Nonnull final File aSourceDir, @Nonnull final File aTargetDir)
  {
    ValueEnforcer.notNull (aSourceDir, "SourceDirectory");
    ValueEnforcer.notNull (aTargetDir, "TargetDirectory");

    // Does the source directory exist?
    if (!FileUtils.existsDir (aSourceDir))
      return EFileIOErrorCode.SOURCE_DOES_NOT_EXIST.getAsIOError (EFileIOOperation.RENAME_DIR, aSourceDir);

    // Are source and target different?
    if (EqualsUtils.equals (aSourceDir, aTargetDir))
      return EFileIOErrorCode.SOURCE_EQUALS_TARGET.getAsIOError (EFileIOOperation.RENAME_DIR, aSourceDir);

    // Does the target directory already exist?
    if (aTargetDir.exists ())
      return EFileIOErrorCode.TARGET_ALREADY_EXISTS.getAsIOError (EFileIOOperation.RENAME_DIR, aTargetDir);

    // Is the source a parent of target?
    if (FileUtils.isParentDirectory (aSourceDir, aTargetDir))
      return EFileIOErrorCode.TARGET_IS_CHILD_OF_SOURCE.getAsIOError (EFileIOOperation.RENAME_DIR,
                                                                      aSourceDir,
                                                                      aTargetDir);

    // Is the source parent directory writable?
    final File aSourceParentDir = aSourceDir.getParentFile ();
    if (aSourceParentDir != null && !FileUtils.canWrite (aSourceParentDir))
      return EFileIOErrorCode.SOURCE_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.RENAME_DIR, aSourceDir);

    // Is the target parent directory writable?
    final File aTargetParentDir = aTargetDir.getParentFile ();
    if (aTargetParentDir != null && aTargetParentDir.exists () && !FileUtils.canWrite (aTargetParentDir))
      return EFileIOErrorCode.TARGET_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.RENAME_DIR, aTargetDir);

    // Ensure parent of target directory is present
    FileUtils.ensureParentDirectoryIsPresent (aTargetDir);

    try
    {
      final EFileIOErrorCode eError = aSourceDir.renameTo (aTargetDir) ? EFileIOErrorCode.NO_ERROR
                                                                      : EFileIOErrorCode.OPERATION_FAILED;
      return eError.getAsIOError (EFileIOOperation.RENAME_DIR, aSourceDir, aTargetDir);
    }
    catch (final SecurityException ex)
    {
      return EFileIOErrorCode.getAsIOError (EFileIOOperation.RENAME_DIR, ex);
    }
  }

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
  private static ESuccess _copyFile (@Nonnull final File aSrcFile, @Nonnull final File aDestFile)
  {
    final FileChannel aSrcChannel = FileUtils.getFileReadChannel (aSrcFile);
    if (aSrcChannel == null)
      return ESuccess.FAILURE;

    try
    {
      final FileChannel aDstChannel = FileUtils.getFileWriteChannel (aDestFile, EAppend.TRUNCATE);
      if (aDstChannel == null)
        return ESuccess.FAILURE;

      try
      {
        FileLock aSrcLock = null;
        FileLock aDestLock = null;
        try
        {
          final long nBytesToRead = aSrcChannel.size ();

          // Shared read lock and exclusive write lock
          aSrcLock = aSrcChannel.lock (0, nBytesToRead, true);
          aDestLock = aDstChannel.lock ();

          // Main copying - the loop version is much quicker than then
          // transferTo with full size!
          long nBytesWritten = 0;
          final long nChunkSize = 1 * CGlobal.BYTES_PER_MEGABYTE;
          while (nBytesWritten < nBytesToRead)
            nBytesWritten += aSrcChannel.transferTo (nBytesWritten, nChunkSize, aDstChannel);

          if (nBytesToRead != nBytesWritten)
          {
            s_aLogger.error ("Failed to copy file. Meant to read " + nBytesToRead + " bytes but wrote " + nBytesWritten);
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
        }
      }
      finally
      {
        ChannelUtils.close (aDstChannel);
      }
    }
    finally
    {
      ChannelUtils.close (aSrcChannel);
    }
  }

  /**
   * Copies the source file to the target file.
   * 
   * @param aSourceFile
   *        The source file to use. May not be <code>null</code>. Needs to be an
   *        existing file.
   * @param aTargetFile
   *        The destination files. May not be <code>null</code> and may not be
   *        an existing file.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError copyFile (@Nonnull final File aSourceFile, @Nonnull final File aTargetFile)
  {
    ValueEnforcer.notNull (aSourceFile, "SourceFile");
    ValueEnforcer.notNull (aTargetFile, "TargetFile");

    // Does the source file exist?
    if (!FileUtils.existsFile (aSourceFile))
      return EFileIOErrorCode.SOURCE_DOES_NOT_EXIST.getAsIOError (EFileIOOperation.COPY_FILE, aSourceFile);

    // Are source and target different?
    if (EqualsUtils.equals (aSourceFile, aTargetFile))
      return EFileIOErrorCode.SOURCE_EQUALS_TARGET.getAsIOError (EFileIOOperation.COPY_FILE, aSourceFile);

    // Does the target file already exist?
    if (aTargetFile.exists ())
      return EFileIOErrorCode.TARGET_ALREADY_EXISTS.getAsIOError (EFileIOOperation.COPY_FILE, aTargetFile);

    // Is the source file readable?
    if (!FileUtils.canRead (aSourceFile))
      return EFileIOErrorCode.SOURCE_NOT_READABLE.getAsIOError (EFileIOOperation.COPY_FILE, aSourceFile);

    // Is the target parent directory writable?
    final File aTargetParentDir = aTargetFile.getParentFile ();
    if (aTargetParentDir != null && aTargetParentDir.exists () && !FileUtils.canWrite (aTargetParentDir))
      return EFileIOErrorCode.TARGET_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.COPY_FILE, aTargetFile);

    // Ensure the targets parent directory is present
    FileUtils.ensureParentDirectoryIsPresent (aTargetFile);

    // Used FileChannel for better performance
    final EFileIOErrorCode eError = _copyFile (aSourceFile, aTargetFile).isSuccess () ? EFileIOErrorCode.NO_ERROR
                                                                                     : EFileIOErrorCode.OPERATION_FAILED;
    return eError.getAsIOError (EFileIOOperation.COPY_FILE, aSourceFile, aTargetFile);
  }

  /**
   * Copy a directory including all child objects.
   * 
   * @param aSourceDir
   *        The source directory to be copied. May not be <code>null</code>.
   * @param aTargetDir
   *        The destination directory where to be copied. This directory may not
   *        be existing. May not be <code>null</code>.
   * @return A non-<code>null</code> error code.
   */
  @Nonnull
  public static FileIOError copyDirRecursive (@Nonnull final File aSourceDir, @Nonnull final File aTargetDir)
  {
    ValueEnforcer.notNull (aSourceDir, "SourceDirectory");
    ValueEnforcer.notNull (aTargetDir, "TargetDirectory");

    // Does the source directory exist?
    if (!FileUtils.existsDir (aSourceDir))
      return EFileIOErrorCode.SOURCE_DOES_NOT_EXIST.getAsIOError (EFileIOOperation.COPY_DIR_RECURSIVE, aSourceDir);

    // Are source and target different?
    if (EqualsUtils.equals (aSourceDir, aTargetDir))
      return EFileIOErrorCode.SOURCE_EQUALS_TARGET.getAsIOError (EFileIOOperation.COPY_DIR_RECURSIVE, aSourceDir);

    // Is the source a parent of target?
    if (FileUtils.isParentDirectory (aSourceDir, aTargetDir))
      return EFileIOErrorCode.TARGET_IS_CHILD_OF_SOURCE.getAsIOError (EFileIOOperation.COPY_DIR_RECURSIVE,
                                                                      aSourceDir,
                                                                      aTargetDir);

    // Does the target directory already exist?
    if (aTargetDir.exists ())
      return EFileIOErrorCode.TARGET_ALREADY_EXISTS.getAsIOError (EFileIOOperation.COPY_DIR_RECURSIVE, aTargetDir);

    // Is the source directory readable?
    if (!FileUtils.canRead (aSourceDir))
      return EFileIOErrorCode.SOURCE_NOT_READABLE.getAsIOError (EFileIOOperation.COPY_DIR_RECURSIVE, aSourceDir);

    // Is the target parent directory writable?
    final File aTargetParentDir = aTargetDir.getParentFile ();
    if (aTargetParentDir != null && aTargetParentDir.exists () && !FileUtils.canWrite (aTargetParentDir))
      return EFileIOErrorCode.TARGET_PARENT_NOT_WRITABLE.getAsIOError (EFileIOOperation.COPY_DIR_RECURSIVE, aTargetDir);

    FileIOError eCode;

    // Ensure the targets parent directory is present
    eCode = createDirRecursive (aTargetDir);
    if (eCode.isFailure ())
      return eCode;

    for (final File aChild : FileUtils.getDirectoryContent (aSourceDir))
    {
      if (aChild.isDirectory ())
      {
        // Skip "." and ".."
        if (FilenameHelper.isSystemInternalDirectory (aChild.getName ()))
          continue;

        // Copy directory
        eCode = copyDirRecursive (aChild, new File (aTargetDir, aChild.getName ()));
        if (eCode.isFailure ())
          return eCode;
      }
      else
        if (aChild.isFile ())
        {
          // Copy a file
          eCode = copyFile (aChild, new File (aTargetDir, aChild.getName ()));
          if (eCode.isFailure ())
            return eCode;
        }
        else
        {
          // Neither directory not file - don't know how to handle
          return EFileIOErrorCode.OBJECT_CANNOT_BE_HANDLED.getAsIOError (EFileIOOperation.COPY_DIR_RECURSIVE, aChild);
        }
    }

    // Done
    return EFileIOErrorCode.NO_ERROR.getAsIOError (EFileIOOperation.COPY_DIR_RECURSIVE, aSourceDir, aTargetDir);
  }
}
