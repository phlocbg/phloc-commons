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
package com.phloc.commons.io.streamprovider;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.io.IInputStreamAndReaderProvider;
import com.phloc.commons.io.IReaderProvider;
import com.phloc.commons.io.streams.NonBlockingStringReader;
import com.phloc.commons.io.streams.StringInputStream;
import com.phloc.commons.string.ToStringGenerator;

/**
 * An {@link InputStream} provider based on a {@link String}.
 * 
 * @author philip
 */
public class StringInputStreamProvider implements IInputStreamAndReaderProvider, IReaderProvider
{
  private final String m_sData;
  private final Charset m_aCharset;

  public StringInputStreamProvider (@Nonnull final char [] aChars, @Nonnull @Nonempty final String sCharset)
  {
    this (new String (aChars), sCharset);
  }

  public StringInputStreamProvider (@Nonnull final char [] aChars, @Nonnull final Charset aCharset)
  {
    this (new String (aChars), aCharset);
  }

  public StringInputStreamProvider (@Nonnull final char [] aChars,
                                    @Nonnegative final int nOfs,
                                    @Nonnegative final int nLen,
                                    @Nonnull @Nonempty final String sCharset)
  {
    this (new String (aChars, nOfs, nLen), sCharset);
  }

  public StringInputStreamProvider (@Nonnull final char [] aChars,
                                    @Nonnegative final int nOfs,
                                    @Nonnegative final int nLen,
                                    @Nonnull final Charset aCharset)
  {
    this (new String (aChars, nOfs, nLen), aCharset);
  }

  public StringInputStreamProvider (@Nonnull final CharSequence aData, @Nonnull @Nonempty final String sCharset)
  {
    this (aData.toString (), sCharset);
  }

  public StringInputStreamProvider (@Nonnull final CharSequence aData, @Nonnull final Charset aCharset)
  {
    this (aData.toString (), aCharset);
  }

  public StringInputStreamProvider (@Nonnull final String sData, @Nonnull @Nonempty final String sCharset)
  {
    this (sData, CharsetManager.getCharsetFromName (sCharset));
  }

  public StringInputStreamProvider (@Nonnull final String sData, @Nonnull final Charset aCharset)
  {
    if (sData == null)
      throw new NullPointerException ("data");
    if (aCharset == null)
      throw new NullPointerException ("charset");
    m_sData = sData;
    m_aCharset = aCharset;
  }

  @Nonnull
  public String getData ()
  {
    return m_sData;
  }

  @Nonnull
  public Charset getCharset ()
  {
    return m_aCharset;
  }

  @Nonnull
  public final StringInputStream getInputStream ()
  {
    return new StringInputStream (m_sData, m_aCharset);
  }

  @Nonnull
  public final NonBlockingStringReader getReader (@Nonnull final String sCharset)
  {
    return new NonBlockingStringReader (m_sData);
  }

  @Nonnull
  public final NonBlockingStringReader getReader (@Nonnull final Charset aCharset)
  {
    return new NonBlockingStringReader (m_sData);
  }

  @Nonnull
  public final NonBlockingStringReader getReader ()
  {
    return new NonBlockingStringReader (m_sData);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("data", m_sData).append ("charset", m_aCharset).toString ();
  }
}
