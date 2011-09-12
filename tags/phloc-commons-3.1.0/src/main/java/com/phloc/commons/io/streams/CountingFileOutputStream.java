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
package com.phloc.commons.io.streams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.io.EAppend;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.IStatisticsHandlerSize;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A special {@link FileOutputStream} sub class that keeps track of all written
 * bytes for the statistics handler.
 *
 * @author philip
 */
public class CountingFileOutputStream extends FileOutputStream
{
  public static final EAppend DEFAULT_APPEND = EAppend.DEFAULT;
  private static final IStatisticsHandlerSize s_aWriteSizeHdl = StatisticsManager.getSizeHandler (CountingFileOutputStream.class.getName () +
                                                                                                  "$write.bytes");
  private static final IStatisticsHandlerCounter s_aWriteFilesHdl = StatisticsManager.getCounterHandler (CountingFileOutputStream.class.getName () +
                                                                                                         "$write.files");
  private long m_nBytesWritten = 0;

  public CountingFileOutputStream (@Nonnull final File aFile) throws FileNotFoundException
  {
    this (aFile, DEFAULT_APPEND);
  }

  public CountingFileOutputStream (@Nonnull final File aFile, @Nonnull final EAppend eAppend) throws FileNotFoundException
  {
    super (aFile, eAppend.isAppend ());
    s_aWriteFilesHdl.increment ();
  }

  public CountingFileOutputStream (@Nonnull final String sFilename) throws FileNotFoundException
  {
    this (sFilename, DEFAULT_APPEND);
  }

  public CountingFileOutputStream (@Nonnull final String sFilename, @Nonnull final EAppend eAppend) throws FileNotFoundException
  {
    super (sFilename, eAppend.isAppend ());
    s_aWriteFilesHdl.increment ();
  }

  @Override
  public void write (final int b) throws IOException
  {
    super.write (b);
    s_aWriteSizeHdl.addSize (1L);
    m_nBytesWritten++;
  }

  @Override
  public void write (final byte [] b) throws IOException
  {
    super.write (b);
    s_aWriteSizeHdl.addSize (b.length);
    m_nBytesWritten += b.length;
  }

  @Override
  public void write (final byte [] b, final int nOffset, final int nLength) throws IOException
  {
    super.write (b, nOffset, nLength);
    s_aWriteSizeHdl.addSize (nLength);
    m_nBytesWritten += nLength;
  }

  @Nonnegative
  public long getBytesWritten ()
  {
    return m_nBytesWritten;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("bytesWritten", m_nBytesWritten).toString ();
  }
}
