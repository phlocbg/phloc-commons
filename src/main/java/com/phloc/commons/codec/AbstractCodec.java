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
package com.phloc.commons.codec;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.charset.CharsetManager;

/**
 * Abstract base class for codec
 * 
 * @author Philip Helger
 */
public abstract class AbstractCodec implements ICodec
{
  public AbstractCodec ()
  {}

  @Nullable
  public byte [] encode (@Nullable final String sDecoded, @Nonnull final Charset aCharset)
  {
    if (sDecoded == null)
      return null;

    return encode (CharsetManager.getAsBytes (sDecoded, aCharset));
  }

  @Nullable
  public byte [] decode (@Nullable final String sEncoded, @Nonnull final Charset aCharset)
  {
    if (sEncoded == null)
      return null;

    return decode (CharsetManager.getAsBytes (sEncoded, aCharset));
  }
}
