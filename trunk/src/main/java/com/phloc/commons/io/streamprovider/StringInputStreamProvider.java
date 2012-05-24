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
package com.phloc.commons.io.streamprovider;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import com.phloc.commons.charset.CharsetManager;

/**
 * An input stream provider based on a String.
 * 
 * @author philip
 */
public class StringInputStreamProvider extends ByteArrayInputStreamProvider
{
  public StringInputStreamProvider (@Nonnull final char [] aChars, @Nonnull final String sCharset)
  {
    this (new String (aChars), sCharset);
  }

  public StringInputStreamProvider (@Nonnull final char [] aChars, @Nonnull final Charset aCharset)
  {
    this (new String (aChars), aCharset);
  }

  public StringInputStreamProvider (@Nonnull final CharSequence aData, @Nonnull final String sCharset)
  {
    this (aData.toString (), sCharset);
  }

  public StringInputStreamProvider (@Nonnull final CharSequence aData, @Nonnull final Charset aCharset)
  {
    this (aData.toString (), aCharset);
  }

  public StringInputStreamProvider (@Nonnull final String sData, @Nonnull final String sCharset)
  {
    super (CharsetManager.getAsBytes (sData, sCharset));
  }

  public StringInputStreamProvider (@Nonnull final String sData, @Nonnull final Charset aCharset)
  {
    super (CharsetManager.getAsBytes (sData, aCharset));
  }
}
