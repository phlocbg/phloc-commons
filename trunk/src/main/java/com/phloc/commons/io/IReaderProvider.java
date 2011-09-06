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
package com.phloc.commons.io;

import java.io.Reader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A callback interface to retrieve {@link Reader} objects based on
 * InputStreams.
 * 
 * @author philip
 */
public interface IReaderProvider extends IInputStreamProvider
{
  /**
   * Get an {@link Reader} based on this input stream provider using the given
   * charset.
   * 
   * @param sCharset
   *        The charset to use. May not be <code>null</code>.
   * @return <code>null</code> if no input stream could be retrieved.
   */
  @Nullable
  Reader getReader (@Nonnull String sCharset);
}
