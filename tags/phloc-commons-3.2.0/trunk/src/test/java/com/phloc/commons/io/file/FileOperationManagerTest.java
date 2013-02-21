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
package com.phloc.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link FileOperationManager}.
 * 
 * @author philip
 */
public final class FileOperationManagerTest
{
  private static void _expectedSuccess (final FileIOError ec)
  {
    assertEquals ("Expected no error but got " + ec.getErrorCode (), EFileIOErrorCode.NO_ERROR, ec.getErrorCode ());
  }

  private static void _expectedError (final FileIOError ec, final EFileIOErrorCode eCode)
  {
    assertEquals ("Expected error " + eCode + " but got " + ec.getErrorCode (), eCode, ec.getErrorCode ());
  }

  @Test
  public void testCtor ()
  {
    final IFileOperationManager aFOM = new FileOperationManager ();
    assertNull (aFOM.getLastError ());
    assertNull (aFOM.getLastOperation ());
    PhlocTestUtils.testToStringImplementation (aFOM);
  }

  @Test
  public void testCreateDir ()
  {
    final IFileOperationManager aFOM = new FileOperationManager (new DefaultFileOperationCallback ());
    final File aDir1 = new File ("TestDir");
    assertFalse (FileUtils.existsDir (aDir1));
    _expectedSuccess (aFOM.createDir (aDir1));
    assertEquals (EFileIOOperation.CREATE_DIR, aFOM.getLastOperation ());
    try
    {
      assertTrue (FileUtils.existsDir (aDir1));

      // directory already exists
      _expectedError (aFOM.createDir (aDir1), EFileIOErrorCode.TARGET_ALREADY_EXISTS);
      assertEquals (EFileIOOperation.CREATE_DIR, aFOM.getLastOperation ());
    }
    finally
    {
      aFOM.deleteDir (aDir1);
      assertEquals (EFileIOOperation.DELETE_DIR, aFOM.getLastOperation ());
      assertFalse (FileUtils.existsDir (aDir1));
    }
  }

  @Test
  public void testCreateDirIfNotExisting ()
  {
    final IFileOperationManager aFOM = new FileOperationManager (new DefaultFileOperationCallback ());
    final File aDir1 = new File ("TestDir");
    assertFalse (FileUtils.existsDir (aDir1));
    _expectedSuccess (aFOM.createDirIfNotExisting (aDir1));
    assertEquals (EFileIOOperation.CREATE_DIR, aFOM.getLastOperation ());
    try
    {
      assertTrue (FileUtils.existsDir (aDir1));

      // directory already exists
      _expectedSuccess (aFOM.createDirIfNotExisting (aDir1));
      assertEquals (EFileIOOperation.CREATE_DIR, aFOM.getLastOperation ());
    }
    finally
    {
      aFOM.deleteDir (aDir1);
      assertEquals (EFileIOOperation.DELETE_DIR, aFOM.getLastOperation ());
      assertFalse (FileUtils.existsDir (aDir1));
    }
  }

  @Test
  public void testCreateDirRecursive ()
  {
    final IFileOperationManager aFOM = new FileOperationManager (new DefaultFileOperationCallback ());
    final File aDir1 = new File ("TestDir");
    final File aDir11 = new File (aDir1, "TestSubDir");
    try
    {
      assertFalse (FileUtils.existsDir (aDir1));
      assertFalse (FileUtils.existsDir (aDir11));
      _expectedSuccess (aFOM.createDirRecursive (aDir11));
      assertEquals (EFileIOOperation.CREATE_DIR_RECURSIVE, aFOM.getLastOperation ());

      assertTrue (FileUtils.existsDir (aDir1));
      assertTrue (FileUtils.existsDir (aDir11));

      // directory already exists
      _expectedError (aFOM.createDirRecursive (aDir1), EFileIOErrorCode.TARGET_ALREADY_EXISTS);
      assertEquals (EFileIOOperation.CREATE_DIR_RECURSIVE, aFOM.getLastOperation ());
      _expectedError (aFOM.createDirRecursive (aDir11), EFileIOErrorCode.TARGET_ALREADY_EXISTS);
      assertEquals (EFileIOOperation.CREATE_DIR_RECURSIVE, aFOM.getLastOperation ());
    }
    finally
    {
      aFOM.deleteDirRecursive (aDir1);
      assertFalse (FileUtils.existsDir (aDir11));
      assertFalse (FileUtils.existsDir (aDir1));
    }
  }

  @Test
  public void testCreateDirRecursiveIfNotExisting ()
  {
    final IFileOperationManager aFOM = new FileOperationManager (new DefaultFileOperationCallback ());
    final File aDir1 = new File ("TestDir");
    final File aDir11 = new File (aDir1, "TestSubDir");
    try
    {
      assertFalse (FileUtils.existsDir (aDir1));
      assertFalse (FileUtils.existsDir (aDir11));
      _expectedSuccess (aFOM.createDirRecursiveIfNotExisting (aDir11));
      assertEquals (EFileIOOperation.CREATE_DIR_RECURSIVE, aFOM.getLastOperation ());

      assertTrue (FileUtils.existsDir (aDir1));
      assertTrue (FileUtils.existsDir (aDir11));

      // directory already exists
      _expectedSuccess (aFOM.createDirRecursiveIfNotExisting (aDir1));
      assertEquals (EFileIOOperation.CREATE_DIR_RECURSIVE, aFOM.getLastOperation ());
      _expectedSuccess (aFOM.createDirRecursiveIfNotExisting (aDir11));
      assertEquals (EFileIOOperation.CREATE_DIR_RECURSIVE, aFOM.getLastOperation ());
    }
    finally
    {
      aFOM.deleteDirRecursive (aDir1);
      assertFalse (FileUtils.existsDir (aDir11));
      assertFalse (FileUtils.existsDir (aDir1));
    }
  }

  @Test
  public void testRenameDir ()
  {
    final IFileOperationManager aFOM = new FileOperationManager ();
    final File aSrcDir = new File ("SourceDir");
    final File aDstDir = new File ("DestDir");
    try
    {
      assertTrue (!aSrcDir.equals (aDstDir));
      assertFalse (FileUtils.existsDir (aSrcDir));
      assertFalse (FileUtils.existsDir (aDstDir));

      // create source directory
      _expectedSuccess (aFOM.createDir (aSrcDir));
      assertEquals (EFileIOOperation.CREATE_DIR, aFOM.getLastOperation ());

      assertTrue (FileUtils.existsDir (aSrcDir));
      assertFalse (FileUtils.existsDir (aDstDir));

      // rename
      _expectedSuccess (aFOM.renameDir (aSrcDir, aDstDir));
      assertEquals (EFileIOOperation.RENAME_DIR, aFOM.getLastOperation ());
      assertFalse (FileUtils.existsDir (aSrcDir));
      assertTrue (FileUtils.existsDir (aDstDir));

      // rename again
      _expectedError (aFOM.renameDir (aSrcDir, aDstDir), EFileIOErrorCode.SOURCE_DOES_NOT_EXIST);
      assertEquals (EFileIOOperation.RENAME_DIR, aFOM.getLastOperation ());
      assertFalse (FileUtils.existsDir (aSrcDir));
      assertTrue (FileUtils.existsDir (aDstDir));

      // rename same
      _expectedError (aFOM.renameDir (aDstDir, aDstDir), EFileIOErrorCode.SOURCE_EQUALS_TARGET);
      assertEquals (EFileIOOperation.RENAME_DIR, aFOM.getLastOperation ());
      assertFalse (FileUtils.existsDir (aSrcDir));
      assertTrue (FileUtils.existsDir (aDstDir));

      // rename back
      _expectedSuccess (aFOM.renameDir (aDstDir, aSrcDir));
      assertEquals (EFileIOOperation.RENAME_DIR, aFOM.getLastOperation ());
      assertTrue (FileUtils.existsDir (aSrcDir));
      assertFalse (FileUtils.existsDir (aDstDir));

      // create destination directory
      _expectedSuccess (aFOM.createDir (aDstDir));
      assertEquals (EFileIOOperation.CREATE_DIR, aFOM.getLastOperation ());
      try
      {
        // cannot rename to existing directory
        _expectedError (aFOM.renameDir (aSrcDir, aDstDir), EFileIOErrorCode.TARGET_ALREADY_EXISTS);
        assertEquals (EFileIOOperation.RENAME_DIR, aFOM.getLastOperation ());
      }
      finally
      {
        _expectedSuccess (aFOM.deleteDir (aDstDir));
        assertEquals (EFileIOOperation.DELETE_DIR, aFOM.getLastOperation ());
      }

      // create child of src folder
      final File aSrcDir11 = new File (aSrcDir, "Another");
      // cannot rename to child directory
      _expectedError (aFOM.renameDir (aSrcDir, aSrcDir11), EFileIOErrorCode.TARGET_IS_CHILD_OF_SOURCE);
      assertEquals (EFileIOOperation.RENAME_DIR, aFOM.getLastOperation ());
    }
    finally
    {
      // Don't know where we are in case of an error -> no expected result
      aFOM.deleteDir (aSrcDir);
      aFOM.deleteDir (aDstDir);
      assertFalse (FileUtils.existsDir (aSrcDir));
      assertFalse (FileUtils.existsDir (aDstDir));
    }
  }

  @Test
  public void testRenameFile ()
  {
    final IFileOperationManager aFOM = new FileOperationManager ();
    final File aFile = new File ("renfile.test");
    final File aFile2 = new File ("renfile.renamed");
    try
    {
      assertFalse (FileUtils.existsFile (aFile));
      _expectedError (aFOM.renameFile (aFile, aFile2), EFileIOErrorCode.SOURCE_DOES_NOT_EXIST);
      assertEquals (EFileIOOperation.RENAME_FILE, aFOM.getLastOperation ());

      SimpleFileIO.writeFile (aFile, "hhsad".getBytes ());
      assertTrue (FileUtils.existsFile (aFile));
      assertFalse (FileUtils.existsFile (aFile2));
      _expectedSuccess (aFOM.renameFile (aFile, aFile2));
      assertEquals (EFileIOOperation.RENAME_FILE, aFOM.getLastOperation ());

      assertFalse (FileUtils.existsFile (aFile));
      assertTrue (FileUtils.existsFile (aFile2));
    }
    finally
    {
      aFOM.deleteFile (aFile);
      assertEquals (EFileIOOperation.DELETE_FILE, aFOM.getLastOperation ());
      aFOM.deleteFile (aFile2);
      assertEquals (EFileIOOperation.DELETE_FILE, aFOM.getLastOperation ());
    }
  }

  @Test
  public void testDeleteFile ()
  {
    final IFileOperationManager aFOM = new FileOperationManager ();
    final File aFile = new File ("delfile.test");
    try
    {
      assertFalse (FileUtils.existsFile (aFile));
      _expectedError (aFOM.deleteFile (aFile), EFileIOErrorCode.SOURCE_DOES_NOT_EXIST);
      assertEquals (EFileIOOperation.DELETE_FILE, aFOM.getLastOperation ());

      SimpleFileIO.writeFile (aFile, "xxx".getBytes ());
      _expectedSuccess (aFOM.deleteFile (aFile));
      assertEquals (EFileIOOperation.DELETE_FILE, aFOM.getLastOperation ());
      assertFalse (FileUtils.existsFile (aFile));
    }
    finally
    {
      aFOM.deleteFile (aFile);
      assertEquals (EFileIOOperation.DELETE_FILE, aFOM.getLastOperation ());
    }
  }

  @Test
  public void testDeleteDir ()
  {
    final IFileOperationManager aFOM = new FileOperationManager ();
    final File aDir = new File ("deldir.test");
    try
    {
      assertFalse (FileUtils.existsDir (aDir));
      _expectedError (aFOM.deleteDir (aDir), EFileIOErrorCode.SOURCE_DOES_NOT_EXIST);
      assertEquals (EFileIOOperation.DELETE_DIR, aFOM.getLastOperation ());
      _expectedSuccess (aFOM.createDir (aDir));
      assertEquals (EFileIOOperation.CREATE_DIR, aFOM.getLastOperation ());
      _expectedError (aFOM.deleteFile (aDir), EFileIOErrorCode.SOURCE_DOES_NOT_EXIST);
      assertEquals (EFileIOOperation.DELETE_FILE, aFOM.getLastOperation ());
      _expectedSuccess (aFOM.deleteDir (aDir));
      assertEquals (EFileIOOperation.DELETE_DIR, aFOM.getLastOperation ());
    }
    finally
    {
      aFOM.deleteDir (aDir);
      assertEquals (EFileIOOperation.DELETE_DIR, aFOM.getLastOperation ());
    }
  }

  @Test
  public void testCopyDirRecursive ()
  {
    final IFileOperationManager aFOM = new FileOperationManager ();
    final File aDir = new File ("copydir.test");
    final File aDir2 = new File ("copydir.copied");
    try
    {
      assertFalse (FileUtils.existsDir (aDir));
      _expectedError (aFOM.deleteDir (aDir), EFileIOErrorCode.SOURCE_DOES_NOT_EXIST);
      assertEquals (EFileIOOperation.DELETE_DIR, aFOM.getLastOperation ());
      _expectedSuccess (aFOM.createDir (aDir));
      assertEquals (EFileIOOperation.CREATE_DIR, aFOM.getLastOperation ());
      _expectedError (aFOM.copyFile (aDir, aDir2), EFileIOErrorCode.SOURCE_DOES_NOT_EXIST);
      assertEquals (EFileIOOperation.COPY_FILE, aFOM.getLastOperation ());
      assertTrue (FileUtils.existsDir (aDir));
      assertFalse (FileUtils.existsDir (aDir2));
      _expectedSuccess (aFOM.copyDirRecursive (aDir, aDir2));
      assertEquals (EFileIOOperation.COPY_DIR_RECURSIVE, aFOM.getLastOperation ());
      assertTrue (FileUtils.existsDir (aDir));
      assertTrue (FileUtils.existsDir (aDir2));
    }
    finally
    {
      aFOM.deleteDir (aDir);
      assertEquals (EFileIOOperation.DELETE_DIR, aFOM.getLastOperation ());
      aFOM.deleteDir (aDir2);
      assertEquals (EFileIOOperation.DELETE_DIR, aFOM.getLastOperation ());
    }
  }

  @Test
  public void testCopyFile ()
  {
    final IFileOperationManager aFOM = new FileOperationManager ();
    final File aFile = new File ("copyfile.test");
    final File aFile2 = new File ("copyfile.copied");
    try
    {
      assertFalse (FileUtils.existsFile (aFile));
      _expectedError (aFOM.deleteFile (aFile), EFileIOErrorCode.SOURCE_DOES_NOT_EXIST);
      assertEquals (EFileIOOperation.DELETE_FILE, aFOM.getLastOperation ());
      SimpleFileIO.writeFile (aFile, "hudriwudri".getBytes ());
      _expectedError (aFOM.copyDirRecursive (aFile, aFile2), EFileIOErrorCode.SOURCE_DOES_NOT_EXIST);
      assertEquals (EFileIOOperation.COPY_DIR_RECURSIVE, aFOM.getLastOperation ());
      assertTrue (FileUtils.existsFile (aFile));
      assertFalse (FileUtils.existsFile (aFile2));
      _expectedSuccess (aFOM.copyFile (aFile, aFile2));
      assertEquals (EFileIOOperation.COPY_FILE, aFOM.getLastOperation ());
      assertTrue (FileUtils.existsFile (aFile));
      assertTrue (FileUtils.existsFile (aFile2));
    }
    finally
    {
      aFOM.deleteFile (aFile);
      assertEquals (EFileIOOperation.DELETE_FILE, aFOM.getLastOperation ());
      aFOM.deleteFile (aFile2);
      assertEquals (EFileIOOperation.DELETE_FILE, aFOM.getLastOperation ());
    }
  }
}