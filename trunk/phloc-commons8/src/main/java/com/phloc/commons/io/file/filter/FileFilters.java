package com.phloc.commons.io.file.filter;

import java.io.File;
import java.io.FilenameFilter;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.FilenameHelper;

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
}
