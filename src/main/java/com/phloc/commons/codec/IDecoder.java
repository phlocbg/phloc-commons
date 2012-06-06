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
package com.phloc.commons.codec;

import javax.annotation.Nonnull;

/**
 * Interface for a single decoder.
 * 
 * @author philip
 */
public interface IDecoder
{
  /**
   * Decode stream bytes.
   * 
   * @param aEncodedBuffer
   *        The byte array to be decoded. May not be <code>null</code>.
   * @return The decoded byte array. Never <code>null</code>.
   * @throws DecoderException
   *         in case something goes wrong
   */
  @Nonnull
  byte [] decode (@Nonnull byte [] aEncodedBuffer);
}
