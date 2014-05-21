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
package com.phloc.commons.io.file.iterate;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import javax.annotation.Nullable;

import org.junit.Test;

import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.filter.FilenameFilterEndsWith;
import com.phloc.commons.tree.utils.walk.TreeWalker;
import com.phloc.commons.tree.withid.folder.DefaultFolderTreeItem;

/**
 * Test class for class {@link FileSystemFolderTree}.
 * 
 * @author Philip Helger
 */
public final class FileSystemFolderTreeTest
{
  @Test
  public void testCreate ()
  {
    try
    {
      new FileSystemFolderTree ((File) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      new FileSystemFolderTree ((String) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      new FileSystemFolderTree (new File ("gibts-ned"));
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      new FileSystemFolderTree ("gibts-ned");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    FileSystemFolderTree aTree = new FileSystemFolderTree (new File (".").getAbsoluteFile (),
                                                           null,
                                                           new FilenameFilterEndsWith (".java"));
    TreeWalker.walkSubTree (aTree.getRootItem (),
                            new DefaultHierarchyWalkerCallback <DefaultFolderTreeItem <String, File, List <File>>> ()
                            {
                              @Override
                              public void onItemBeforeChildren (@Nullable final DefaultFolderTreeItem <String, File, List <File>> aFolder)
                              {
                                if (aFolder != null)
                                  for (final File aFile : aFolder.getData ())
                                    assertTrue (FileUtils.existsFile (aFile));
                              }
                            });

    // Only dir filter
    aTree = new FileSystemFolderTree (new File (".").getAbsoluteFile (), new FilenameFilterEndsWith ("src"), null);
    TreeWalker.walkSubTree (aTree.getRootItem (),
                            new DefaultHierarchyWalkerCallback <DefaultFolderTreeItem <String, File, List <File>>> ());

    // No filter
    aTree = new FileSystemFolderTree (new File (".").getAbsoluteFile ());
    TreeWalker.walkSubTree (aTree.getRootItem (),
                            new DefaultHierarchyWalkerCallback <DefaultFolderTreeItem <String, File, List <File>>> ());
  }
}
