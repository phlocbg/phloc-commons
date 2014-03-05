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
package com.phloc.commons.io.resolver;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.IInputStreamResolver;
import com.phloc.commons.io.IOutputStreamResolver;
import com.phloc.commons.io.IReaderResolver;
import com.phloc.commons.io.IWriterResolver;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Implementation of the {@link IInputStreamResolver} and
 * {@link IOutputStreamResolver} interfaces for {@link File} objects.
 * 
 * @author Philip Helger
 */
@Immutable
public final class FileSystemCharStreamResolver implements IReaderResolver, IWriterResolver
{
  private final FileSystemByteStreamResolver m_aByteStreamResolver;
  private final Charset m_aCharset;

  public FileSystemCharStreamResolver (@Nonnull final String sBasePath, @Nonnull final Charset aCharset)
  {
    this (new File (sBasePath), aCharset);
  }

  public FileSystemCharStreamResolver (@Nonnull final File aBasePath, @Nonnull final Charset aCharset)
  {
    if (aBasePath == null)
      throw new NullPointerException ("basePath");
    if (!aBasePath.exists ())
      throw new IllegalArgumentException ("Base path does not exist: " + aBasePath);
    if (!aBasePath.isDirectory ())
      throw new IllegalArgumentException ("Only directories are allowed as base path: " + aBasePath);
    if (aCharset == null)
      throw new NullPointerException ("charset");
    m_aByteStreamResolver = new FileSystemByteStreamResolver (aBasePath);
    m_aCharset = aCharset;
  }

  @Nonnull
  public File getBasePath ()
  {
    return m_aByteStreamResolver.getBasePath ();
  }

  @Nonnull
  public Charset getCharset ()
  {
    return m_aCharset;
  }

  @Nullable
  public Reader getReader (@Nonnull final String sName)
  {
    return StreamUtils.createReader (m_aByteStreamResolver.getInputStream (sName), m_aCharset);
  }

  @Nullable
  public Writer getWriter (@Nonnull final String sName, @Nonnull final EAppend eAppend)
  {
    return StreamUtils.createWriter (m_aByteStreamResolver.getOutputStream (sName, eAppend), m_aCharset);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FileSystemCharStreamResolver))
      return false;
    final FileSystemCharStreamResolver rhs = (FileSystemCharStreamResolver) o;
    return m_aByteStreamResolver.equals (rhs.m_aByteStreamResolver) && m_aCharset.equals (rhs.m_aCharset);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aByteStreamResolver).append (m_aCharset).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("byteStreamResolver", m_aByteStreamResolver)
                                       .append ("charset", m_aCharset)
                                       .toString ();
  }
}
