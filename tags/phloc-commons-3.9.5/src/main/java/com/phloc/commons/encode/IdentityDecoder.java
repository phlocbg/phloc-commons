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
package com.phloc.commons.encode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The most simple decoder, that does not do anything
 * 
 * @author philip
 * @param <DATATYPE>
 *        The handled data type
 */
public final class IdentityDecoder <DATATYPE> implements IDecoder <DATATYPE>
{
  @Nullable
  public DATATYPE decode (@Nullable final DATATYPE aInput)
  {
    return aInput;
  }

  /**
   * Factory method for this class
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <DATATYPE> IdentityDecoder <DATATYPE> create ()
  {
    return new IdentityDecoder <DATATYPE> ();
  }
}
