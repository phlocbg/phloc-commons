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
package com.phloc.commons.io.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.IReadWriteResource;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.IWritableResource;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Implementation of the {@link IReadableResource} and {@link IWritableResource}
 * interfaces for file system objects.
 * 
 * @author philip
 */
@Immutable
public final class FileSystemResource implements IReadWriteResource
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FileSystemResource.class);

  private final File m_aFile;
  private final String m_sPath;
  private Integer m_aHashCode;

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
    if (aFile == null)
      throw new NullPointerException ("file");

    // Make absolute
    File aAbsFile = aFile.getAbsoluteFile ();
    try
    {
      // Try to remove all ".." etc paths
      aAbsFile = aAbsFile.getCanonicalFile ();
    }
    catch (final IOException ex)
    {
      // Something obviously went wrong
      s_aLogger.warn ("Failed to canonicalize file '" +
                      aAbsFile.toString () +
                      "': " +
                      ex.getClass ().getName () +
                      " - " +
                      ex.getMessage ());
    }

    m_aFile = aAbsFile;
    // Cache absolute path for performance reasons
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
  public InputStream getInputStream ()
  {
    return FileUtils.getInputStream (m_aFile);
  }

  @Nullable
  public Reader getReader (@Nonnull final String sCharset)
  {
    return StreamUtils.createReader (getInputStream (), sCharset);
  }

  @Nullable
  public OutputStream getOutputStream (@Nonnull final EAppend eAppend)
  {
    return FileUtils.getOutputStream (m_aFile, eAppend);
  }

  @Nullable
  public Writer getWriter (@Nonnull final String sCharset, @Nonnull final EAppend eAppend)
  {
    return StreamUtils.createWriter (getOutputStream (eAppend), sCharset);
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
