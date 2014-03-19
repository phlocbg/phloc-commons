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
package com.phloc.commons.io.resource;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.IReadWriteResource;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Implementation of the {@link com.phloc.commons.io.IReadableResource} and
 * {@link com.phloc.commons.io.IWritableResource} interfaces for file system
 * objects.
 *
 * @author Philip Helger
 */
@Immutable
public class FileSystemResource implements IReadWriteResource
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FileSystemResource.class);

  private final File m_aFile;
  private final String m_sPath;
  private Integer m_aHashCode;

  public FileSystemResource (@Nonnull final URI aURI)
  {
    this (new File (aURI));
  }

  public FileSystemResource (@Nonnull final String sParentPath, @Nonnull final String sChildPath)
  {
    this (new File (sParentPath, sChildPath));
  }

  public FileSystemResource (@Nonnull final String sFilename)
  {
    this (new File (sFilename));
  }

  public FileSystemResource (@Nonnull final File aParentFile, final String sChildPath)
  {
    this (new File (aParentFile, sChildPath));
  }

  public FileSystemResource (@Nonnull final File aFile)
  {
    ValueEnforcer.notNull (aFile, "File");

    // Make absolute and try to remove all ".." etc paths
    // Note: using getCleanPath with String is much faster compared to
    // getCleanPath with a File parameter, as on Unix the
    // UnixFileSystem.canonicalize method is a bottleneck
    final String sPath = FilenameHelper.getCleanPath (aFile.getAbsolutePath ());
    m_aFile = new File (sPath);

    // Note: cache absolute path for performance reasons
    // Note: this path always uses the platform dependent path separator
    m_sPath = m_aFile.getAbsolutePath ();
  }

  @Nonnull
  public String getResourceID ()
  {
    return getPath ();
  }

  @Nonnull
  public String getPath ()
  {
    return m_sPath;
  }

  @Nullable
  public static InputStream getInputStream (@Nonnull final File aFile)
  {
    ValueEnforcer.notNull (aFile, "File");

    return FileUtils.getInputStream (aFile);
  }

  @Nullable
  public static Reader getReader (@Nonnull final File aFile, @Nonnull final Charset aCharset)
  {
    ValueEnforcer.notNull (aFile, "File");
    ValueEnforcer.notNull (aCharset, "Charset");

    return StreamUtils.createReader (getInputStream (aFile), aCharset);
  }

  @Nullable
  public InputStream getInputStream ()
  {
    return getInputStream (m_aFile);
  }

  @Nullable
  public Reader getReader (@Nonnull final Charset aCharset)
  {
    return getReader (m_aFile, aCharset);
  }

  @Nullable
  public static OutputStream getOutputStream (@Nonnull final File aFile, @Nonnull final EAppend eAppend)
  {
    ValueEnforcer.notNull (aFile, "File");
    ValueEnforcer.notNull (eAppend, "Append");

    return FileUtils.getOutputStream (aFile, eAppend);
  }

  @Nullable
  public static Writer getWriter (@Nonnull final File aFile,
                                  @Nonnull final Charset aCharset,
                                  @Nonnull final EAppend eAppend)
  {
    ValueEnforcer.notNull (aFile, "File");
    ValueEnforcer.notNull (aCharset, "Charset");
    ValueEnforcer.notNull (eAppend, "Append");

    return StreamUtils.createWriter (getOutputStream (aFile, eAppend), aCharset);
  }

  @Nullable
  public OutputStream getOutputStream (@Nonnull final EAppend eAppend)
  {
    return getOutputStream (m_aFile, eAppend);
  }

  @Nullable
  public Writer getWriter (@Nonnull final Charset aCharset, @Nonnull final EAppend eAppend)
  {
    return getWriter (m_aFile, aCharset, eAppend);
  }

  public boolean exists ()
  {
    return m_aFile.exists ();
  }

  @Nullable
  public static URL getAsURL (final File aFile)
  {
    try
    {
      return aFile.toURI ().toURL ();
    }
    catch (final MalformedURLException ex)
    {
      s_aLogger.warn ("Failed to convert file to URL: " + aFile, ex);
      return null;
    }
  }

  @Nullable
  public URL getAsURL ()
  {
    return getAsURL (m_aFile);
  }

  @Nonnull
  public File getAsFile ()
  {
    return m_aFile;
  }

  @Nonnull
  public FileSystemResource getReadableCloneForPath (@Nonnull final String sPath)
  {
    return new FileSystemResource (sPath);
  }

  @Nonnull
  public FileSystemResource getWritableCloneForPath (@Nonnull final String sPath)
  {
    return new FileSystemResource (sPath);
  }

  /**
   * Tests whether the application can read the file denoted by this abstract
   * pathname.
   *
   * @return <code>true</code> if and only if the file specified by this
   *         abstract pathname exists <em>and</em> can be read by the
   *         application; <code>false</code> otherwise
   */
  public boolean canRead ()
  {
    return FileUtils.canRead (m_aFile);
  }

  /**
   * Tests whether the application can modify the file denoted by this abstract
   * pathname.
   *
   * @return <code>true</code> if and only if the file system actually contains
   *         a file denoted by this abstract pathname <em>and</em> the
   *         application is allowed to write to the file; <code>false</code>
   *         otherwise.
   */
  public boolean canWrite ()
  {
    return FileUtils.canWrite (m_aFile);
  }

  /**
   * Tests whether the application can execute the file denoted by this abstract
   * pathname.
   *
   * @return <code>true</code> if and only if the abstract pathname exists
   *         <em>and</em> the application is allowed to execute the file
   */
  public boolean canExecute ()
  {
    return FileUtils.canExecute (m_aFile);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FileSystemResource))
      return false;
    final FileSystemResource rhs = (FileSystemResource) o;
    return m_aFile.equals (rhs.m_aFile);
  }

  @Override
  public int hashCode ()
  {
    // We need a cached one!
    if (m_aHashCode == null)
      m_aHashCode = new HashCodeGenerator (this).append (m_aFile).getHashCodeObj ();
    return m_aHashCode.intValue ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("file", m_aFile).toString ();
  }
}
