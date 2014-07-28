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
package com.phloc.commons.io.file.filter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.regex.RegExHelper;

/**
 * This is the new main class for all file filter handling.
 * 
 * @author Philip Helger
 */
public final class FileFilters
{
  private FileFilters ()
  {}

  @Nonnull
  public static IFileFilter getFromFilenameFilter (@Nonnull final FilenameFilter aFilenameFilter)
  {
    ValueEnforcer.notNull (aFilenameFilter, "FilenameFilter");
    return aFile -> aFile != null &&
                    FileUtils.existsFile (aFile) &&
                    aFilenameFilter.accept (aFile.getParentFile (), aFile.getName ());
  }

  @Nonnull
  public static IFileFilter getDirectoryFromFilenameFilter (@Nonnull final FilenameFilter aFilenameFilter)
  {
    ValueEnforcer.notNull (aFilenameFilter, "FilenameFilter");
    return aFile -> aFile != null &&
        aFile.isDirectory () &&
        aFilenameFilter.accept (aFile.getParentFile (), aFile.getName ());
  }

  @Nonnull
  public static IFileFilter getDirectoryOnly ()
  {
    return aFile -> aFile != null && aFile.isDirectory ();
  }

  @Nonnull
  public static IFileFilter getDirectoryPublic ()
  {
    return aFile -> aFile != null && aFile.isDirectory () && !FilenameHelper.isHiddenFilename (aFile);
  }

  @Nonnull
  public static IFileFilter getFileOnly ()
  {
    return aFile -> aFile != null && FileUtils.existsFile (aFile);
  }

  @Nonnull
  public static IFileFilter getParentDirectoryPublic ()
  {
    return aFile -> {
      final File aParentFile = aFile != null ? aFile.getAbsoluteFile ().getParentFile () : null;
      return aParentFile != null && !FilenameHelper.isHiddenFilename (aParentFile);
    };
  }

  @Nonnull
  public static IFileFilter getNameStartsWith (@Nonnull @Nonempty final String sPrefix)
  {
    ValueEnforcer.notEmpty (sPrefix, "Prefix");
    return aFile -> {
      final String sRealName = FilenameHelper.getSecureFilename (aFile.getName ());
      return sRealName != null && sRealName.startsWith (sPrefix);
    };
  }

  @Nonnull
  public static IFileFilter getNameEndsWith (@Nonnull @Nonempty final String sSuffix)
  {
    ValueEnforcer.notEmpty (sSuffix, "Suffix");
    return aFile -> {
      final String sRealName = FilenameHelper.getSecureFilename (aFile.getName ());
      return sRealName != null && sRealName.endsWith (sSuffix);
    };
  }

  @Nonnull
  public static IFileFilter getNameEquals (@Nonnull @Nonempty final String sFilename)
  {
    ValueEnforcer.notEmpty (sFilename, "Filename");
    return aFile -> {
      final String sRealName = FilenameHelper.getSecureFilename (aFile.getName ());
      return sRealName != null && sRealName.equals (sFilename);
    };
  }

  @Nonnull
  public static IFileFilter getNameEqualsIgnoreCase (@Nonnull @Nonempty final String sFilename)
  {
    ValueEnforcer.notEmpty (sFilename, "Filename");
    return aFile -> {
      final String sRealName = FilenameHelper.getSecureFilename (aFile.getName ());
      return sRealName != null && sRealName.equalsIgnoreCase (sFilename);
    };
  }

  @Nonnull
  public static IFileFilter getNameNotEquals (@Nonnull @Nonempty final String sFilename)
  {
    ValueEnforcer.notEmpty (sFilename, "Filename");
    return aFile -> {
      final String sRealName = FilenameHelper.getSecureFilename (aFile.getName ());
      return sRealName != null && !sRealName.equals (sFilename);
    };
  }

  @Nonnull
  public static IFileFilter getNameNotEqualsIgnoreCase (@Nonnull @Nonempty final String sFilename)
  {
    ValueEnforcer.notEmpty (sFilename, "Filename");
    return aFile -> {
      final String sRealName = FilenameHelper.getSecureFilename (aFile.getName ());
      return sRealName != null && !sRealName.equalsIgnoreCase (sFilename);
    };
  }

  @Nonnull
  public static IFileFilter getNameMatchAnyRegEx (@Nonnull @Nonempty final String... aRegExs)
  {
    final List <String> aRegExList = ContainerHelper.newList (ValueEnforcer.notEmpty (aRegExs, "RegularExpressions"));
    return aFile -> {
      final String sRealName = FilenameHelper.getSecureFilename (aFile.getName ());
      return sRealName != null &&
          aRegExList.stream ()
          .filter (p -> RegExHelper.stringMatchesPattern (p, sRealName))
          .findFirst ()
          .isPresent ();
    };
  }

  @Nonnull
  public static IFileFilter getNameMatchNoRegEx (@Nonnull @Nonempty final String... aRegExs)
  {
    final List <String> aRegExList = ContainerHelper.newList (ValueEnforcer.notEmpty (aRegExs, "RegularExpressions"));
    return aFile -> {
      final String sRealName = FilenameHelper.getSecureFilename (aFile.getName ());
      return sRealName != null &&
          !aRegExList.stream ()
                        .filter (p -> RegExHelper.stringMatchesPattern (p, sRealName))
                        .findFirst ()
                        .isPresent ();
    };
  }
}
