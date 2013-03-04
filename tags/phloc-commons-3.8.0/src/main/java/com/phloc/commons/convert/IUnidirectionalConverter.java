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
package com.phloc.commons.convert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;

/**
 * This is a very simple type conversion interface for compile type conversions.
 * 
 * @param <SRCTYPE>
 *        source type
 * @param <DSTTYPE>
 *        destination type
 * @author philip
 */
public interface IUnidirectionalConverter <SRCTYPE, DSTTYPE>
{
  /**
   * Convert the passed source object to the destination type.
   * 
   * @param aSource
   *        The source object to be converted.
   * @return The converted value.
   */
  @Nullable
  DSTTYPE convert (@Nonnull (when = When.MAYBE) SRCTYPE aSource);
}