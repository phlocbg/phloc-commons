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
package com.phloc.commons.io.streams;

import java.io.InputStream;

import javax.annotation.Nonnull;

/**
 * A special input stream that does not close the stream. This is e.g. helpful
 * for reading from streams within a ZIP file.
 * 
 * @author philip
 */
public class NonClosingInputStream extends WrappedInputStream
{
  public NonClosingInputStream (@Nonnull final InputStream aSourceIS)
  {
    super (aSourceIS);
  }

  @Override
  public void close ()
  {
    // do nothing
  }
}