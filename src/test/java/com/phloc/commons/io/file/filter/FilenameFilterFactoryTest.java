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
package com.phloc.commons.io.file.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Test;

import com.phloc.commons.io.file.FileOperations;

/**
 * Test class for class {@link FilenameFilterFactory}.
 *
 * @author philip
 */
public final class FilenameFilterFactoryTest
{
  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testGetEndsWithFilter ()
  {
    try
    {
      // null not allowed
      FilenameFilterFactory.getEndsWithFilter (null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final FilenameFilter ff = FilenameFilterFactory.getEndsWithFilter (".htm");
    assertNotNull (ff);
    assertTrue (ff.accept (null, "file.htm"));
    assertTrue (ff.accept (new File ("dir"), "file.htm"));
    assertFalse (ff.accept (null, "file.html"));
    assertFalse (ff.accept (new File ("dir"), "file.html"));
    assertFalse (ff.accept (null, null));
    assertFalse (ff.accept (null, ""));
  }

  @Test
  public void testGetPublicDirectoryFilter ()
  {
    final FilenameFilter ff = FilenameFilterFactory.getPublicDirectoryFilter ();
    assertNotNull (ff);

    // null directory
    assertFalse (ff.accept (null, "file.html"));

    try
    {
      // null file
      ff.accept (new File ("dir"), null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    final File aDir = new File ("directory");
    FileOperations.createDir (aDir);
    try
    {
      // subdir does not exist
      assertFalse (ff.accept (aDir, "subdir"));

      // create public subdir
      File aSubDir = new File (aDir, "subdir");
      FileOperations.createDir (aSubDir);
      try
      {
        // subdir exists
        assertTrue (ff.accept (aDir, "subdir"));
        assertFalse (ff.accept (aDir, "subdir2"));
      }
      finally
      {
        FileOperations.deleteDir (aSubDir);
      }

      // create hidden subdir
      aSubDir = new File (aDir, ".subdir");
      FileOperations.createDir (aSubDir);
      try
      {
        // subdir is hidden
        assertFalse (ff.accept (aDir, ".subdir"));
      }
      finally
      {
        FileOperations.deleteDir (aSubDir);
      }
    }
    finally
    {
      FileOperations.deleteDir (aDir);
    }
  }

  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testGetMatchRegExFilter ()
  {
    try
    {
      // null not allowed
      FilenameFilterFactory.getMatchRegExFilter ((String []) null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final FilenameFilter ff = FilenameFilterFactory.getMatchRegExFilter (".*htm$");
    assertNotNull (ff);
    assertTrue (ff.accept (null, "file.htm"));
    assertTrue (ff.accept (new File ("dir"), "file.htm"));
    assertFalse (ff.accept (null, "file.html"));
    assertFalse (ff.accept (new File ("dir"), "file.html"));
    assertFalse (ff.accept (null, null));
    assertFalse (ff.accept (null, ""));
  }

  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testGetIgnoreMatchRegExFilter ()
  {
    try
    {
      // null not allowed
      FilenameFilterFactory.getIgnoreMatchRegExFilter ((String []) null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final FilenameFilter ff = FilenameFilterFactory.getIgnoreMatchRegExFilter (".*html$");
    assertNotNull (ff);
    assertTrue (ff.accept (null, "file.htm"));
    assertTrue (ff.accept (new File ("dir"), "file.htm"));
    assertFalse (ff.accept (null, "file.html"));
    assertFalse (ff.accept (new File ("dir"), "file.html"));
    assertFalse (ff.accept (null, null));
    assertTrue (ff.accept (null, ""));
  }
}
