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
package com.phloc.commons.io.file.iterate;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.tree.withid.BasicTreeItemWithID;
import com.phloc.commons.tree.withid.IBasicTreeItemWithID;
import com.phloc.commons.tree.withid.IBasicTreeItemWithIDFactory;
import com.phloc.commons.tree.withid.unique.AbstractBasicTreeItemWithUniqueIDFactory;
import com.phloc.commons.tree.withid.unique.BasicTreeWithGlobalUniqueID;

interface IFileItem extends IBasicTreeItemWithID <String, List <File>, IFileItem>
{}

final class FileItem extends BasicTreeItemWithID <String, List <File>, IFileItem> implements IFileItem
{
  public FileItem (final IBasicTreeItemWithIDFactory <String, List <File>, IFileItem> aFactory)
  {
    super (aFactory);
  }

  public FileItem (final IFileItem aParent, final String sName)
  {
    super (aParent, sName);
  }
}

final class FileItemFactory extends AbstractBasicTreeItemWithUniqueIDFactory <String, List <File>, IFileItem>
{
  public FileItemFactory ()// NOPMD
  {}

  @Override
  protected IFileItem internalCreate (@Nonnull final IFileItem aParent, @Nonnull final String sName)
  {
    return new FileItem (aParent, sName);
  }

  public IFileItem createRoot ()
  {
    return new FileItem (this);
  }
}

@NotThreadSafe
public class FileSystemFolderTree extends BasicTreeWithGlobalUniqueID <String, List <File>, IFileItem>
{
  private static void _iterate (@Nonnull final IFileItem aTreeItem,
                                @Nonnull final File aDir,
                                @Nullable final FileFilter aDirFilter,
                                @Nullable final FileFilter aFileFilter)
  {
    if (aDir != null)
      for (final File aChild : aDir.listFiles ())
      {
        if (aChild.isFile ())
        {
          // file
          if (aFileFilter == null || aFileFilter.accept (aChild))
            aTreeItem.getData ().add (aChild);
        }
        else
          if (aChild.isDirectory ())
          {
            // directory
            if (!FilenameHelper.isSystemInternalDirectory (aChild))
            {
              // Check against the optional filter
              if (aDirFilter == null || aDirFilter.accept (aChild))
              {
                // create item and recursively descend
                final IFileItem aChildItem = aTreeItem.createChildItem (FilenameHelper.getCleanPath (aChild),
                                                                        new ArrayList <File> ());
                _iterate (aChildItem, aChild, aDirFilter, aFileFilter);
              }
            }
          }
      }
  }

  public FileSystemFolderTree (@Nonnull final File aStartDir)
  {
    this (aStartDir, null, null);
  }

  public FileSystemFolderTree (@Nonnull final File aStartDir,
                               @Nullable final FileFilter aDirFilter,
                               @Nullable final FileFilter aFileFilter)
  {
    super (new FileItemFactory ());
    if (aStartDir == null)
      throw new NullPointerException ("startDir");
    if (!aStartDir.isDirectory ())
      throw new IllegalArgumentException ("Start directory is not a directory!");

    final IFileItem aStart = getRootItem ().createChildItem (aStartDir.getName (), new ArrayList <File> ());
    _iterate (aStart, aStartDir, aDirFilter, aFileFilter);
  }
}
