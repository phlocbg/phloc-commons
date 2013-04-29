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
package com.phloc.commons.convert;


/**
 * This is a very simple type conversion interface for compile type conversions.
 * 
 * @param <SRCTYPE>
 *        source type
 * @param <DSTTYPE>
 *        destination type
 * @author Philip Helger
 */
public interface IBidirectionalConverter <SRCTYPE, DSTTYPE>
{
  /**
   * Convert from SRC to DST.
   * 
   * @param aSource
   *        The SRC object. No <code>null</code> or non- <code>null</code>
   *        constraint possible.
   * @return The DST object. No <code>null</code> or non- <code>null</code>
   *         constraint possible.
   */
  DSTTYPE convertToDst (SRCTYPE aSource);

  /**
   * Convert from DST to SRC
   * 
   * @param aDest
   *        The DST object. No <code>null</code> or non- <code>null</code>
   *        constraint possible.
   * @return The SRC object. No <code>null</code> or non- <code>null</code>
   *         constraint possible.
   */
  SRCTYPE convertToSrc (DSTTYPE aDest);
}
