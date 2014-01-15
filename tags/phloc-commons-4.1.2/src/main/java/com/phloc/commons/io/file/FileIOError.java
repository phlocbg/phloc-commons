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
package com.phloc.commons.io.file;

import java.io.File;
import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents an error with an additional error object.
 * 
 * @author Philip Helger
 */
@Immutable
public final class FileIOError implements ISuccessIndicator, Serializable
{
  private final EFileIOOperation m_eOperation;
  private final EFileIOErrorCode m_eCode;
  private final File m_aFile1;
  private final File m_aFile2;
  private final Exception m_aException;

  public FileIOError (@Nonnull final EFileIOOperation eOperation, @Nonnull final EFileIOErrorCode eCode)
  {
    if (eOperation == null)
      throw new NullPointerException ("operation");
    if (eCode == null)
      throw new NullPointerException ("errorCode");
    m_eOperation = eOperation;
    m_eCode = eCode;
    m_aFile1 = null;
    m_aFile2 = null;
    m_aException = null;
  }

  public FileIOError (@Nonnull final EFileIOOperation eOperation,
                      @Nonnull final EFileIOErrorCode eCode,
                      @Nonnull final File aFile1)
  {
    if (eOperation == null)
      throw new NullPointerException ("operation");
    if (eCode == null)
      throw new NullPointerException ("errorCode");
    if (aFile1 == null)
      throw new NullPointerException ("file1");
    m_eOperation = eOperation;
    m_eCode = eCode;
    m_aFile1 = aFile1;
    m_aFile2 = null;
    m_aException = null;
  }

  public FileIOError (@Nonnull final EFileIOOperation eOperation,
                      @Nonnull final EFileIOErrorCode eCode,
                      @Nonnull final File aFile1,
                      @Nonnull final File aFile2)
  {
    if (eOperation == null)
      throw new NullPointerException ("operation");
    if (eCode == null)
      throw new NullPointerException ("errorCode");
    if (aFile1 == null)
      throw new NullPointerException ("file1");
    if (aFile2 == null)
      throw new NullPointerException ("file2");
    m_eOperation = eOperation;
    m_eCode = eCode;
    m_aFile1 = aFile1;
    m_aFile2 = aFile2;
    m_aException = null;
  }

  public FileIOError (@Nonnull final EFileIOOperation eOperation,
                      @Nonnull final EFileIOErrorCode eCode,
                      @Nonnull final Exception aException)
  {
    if (eOperation == null)
      throw new NullPointerException ("operation");
    if (eCode == null)
      throw new NullPointerException ("errorCode");
    if (aException == null)
      throw new NullPointerException ("exception");
    m_eOperation = eOperation;
    m_eCode = eCode;
    m_aFile1 = null;
    m_aFile2 = null;
    m_aException = aException;
  }

  @Nonnull
  public EFileIOOperation getOperation ()
  {
    return m_eOperation;
  }

  @Nonnull
  public EFileIOErrorCode getErrorCode ()
  {
    return m_eCode;
  }

  public boolean isSuccess ()
  {
    return m_eCode.isSuccess ();
  }

  public boolean isFailure ()
  {
    return m_eCode.isFailure ();
  }

  @Nullable
  public File getFile1 ()
  {
    return m_aFile1;
  }

  @Nullable
  public File getFile2 ()
  {
    return m_aFile2;
  }

  @Nullable
  public Exception getException ()
  {
    return m_aException;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FileIOError))
      return false;
    final FileIOError rhs = (FileIOError) o;
    return m_eOperation == rhs.m_eOperation &&
           m_eCode == rhs.m_eCode &&
           EqualsUtils.equals (m_aFile1, rhs.m_aFile1) &&
           EqualsUtils.equals (m_aFile2, rhs.m_aFile2) &&
           EqualsUtils.equals (m_aException, rhs.m_aException);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eOperation)
                                       .append (m_eCode)
                                       .append (m_aFile1)
                                       .append (m_aFile2)
                                       .append (m_aException)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("operation", m_eOperation)
                                       .append ("ecode", m_eCode)
                                       .appendIfNotNull ("file1", m_aFile1)
                                       .appendIfNotNull ("file2", m_aFile2)
                                       .appendIfNotNull ("exception", m_aException)
                                       .toString ();
  }
}
