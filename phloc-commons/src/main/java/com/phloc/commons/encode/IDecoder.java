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
package com.phloc.commons.encode;

import javax.annotation.Nullable;

/**
 * The most basic decoding interface
 * 
 * @param <DATATYPE>
 *        data type
 * @author Philip Helger
 */
public interface IDecoder <DATATYPE>
{
  /**
   * Decode the passed source object
   * 
   * @param aInput
   *        The source object to be decoded
   * @return The decoded value.
   */
  @Nullable
  DATATYPE decode (@Nullable DATATYPE aInput);
}
