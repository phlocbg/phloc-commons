/**
 * Copyright (C) 2006-2015 phloc systems
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

import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CharsetManager;

/**
 * An input stream based on an input String.
 * 
 * @author Philip Helger
 */
public class StringInputStream extends NonBlockingByteArrayInputStream
{
  @Deprecated
  public StringInputStream (@Nonnull final String sInput, @Nonnull @Nonempty final String sCharset)
  {
    super (CharsetManager.getAsBytes (sInput, sCharset));
  }

  public StringInputStream (@Nonnull final String sInput, @Nonnull final Charset aCharset)
  {
    super (CharsetManager.getAsBytes (sInput, aCharset));
  }
}
