/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.commons.idfactory;

import java.io.File;
import java.nio.charset.Charset;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.string.ToStringGenerator;

/**
 * {@link File} based persisting {@link IIntIDFactory} implementation.
 * 
 * @author philip
 */
@ThreadSafe
public class FileIntIDFactory extends AbstractPersistingIntIDFactory
{
  /** The charset to use for writing the file */
  @Nonnull
  public static final Charset CHARSET_TO_USE = CCharset.CHARSET_ISO_8859_1_OBJ;
  /** The default number of values to reserve with a single IO action */
  @Nonnegative
  public static final int DEFAULT_RESERVE_COUNT = 20;

  @Nonnull
  private final File m_aFile;

  public FileIntIDFactory (@Nonnull final File aFile)
  {
    this (aFile, DEFAULT_RESERVE_COUNT);
  }

  public FileIntIDFactory (@Nonnull final File aFile, @Nonnegative final int nReserveCount)
  {
    super (nReserveCount);
    if (aFile == null)
      throw new NullPointerException ("file");
    if (!FileUtils.canReadAndWriteFile (aFile))
      throw new IllegalArgumentException ("Cannot read and/or write the file " + aFile + "!");
    m_aFile = aFile;
  }

  /*
   * Note: this method must only be called from within a locked section!
   */
  @Override
  protected final int readAndUpdateIDCounter (@Nonnegative final int nReserveCount)
  {
    final String sContent = SimpleFileIO.readFileAsString (m_aFile, CHARSET_TO_USE);
    final int nRead = sContent != null ? StringParser.parseInt (sContent.trim (), 0) : 0;
    SimpleFileIO.writeFile (m_aFile, Integer.toString (nRead + nReserveCount), CHARSET_TO_USE);
    return nRead;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (!super.equals (o))
      return false;
    final FileIntIDFactory rhs = (FileIntIDFactory) o;
    return m_aFile.equals (rhs.m_aFile);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aFile).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("file", m_aFile).toString ();
  }
}
