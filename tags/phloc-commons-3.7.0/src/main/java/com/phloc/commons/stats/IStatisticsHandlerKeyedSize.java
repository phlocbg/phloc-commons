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
package com.phloc.commons.stats;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

/**
 * Modifying interface of {@link StatisticsHandlerKeyedSizeMBean}
 * 
 * @author philip
 */
public interface IStatisticsHandlerKeyedSize extends StatisticsHandlerKeyedSizeMBean
{
  /**
   * Add a new size
   * 
   * @param sKey
   *        The key to be incremented.
   * @param nSize
   *        The size to be added. Must be &ge; 0 as sizes cannot be negative.
   */
  void addSize (@Nullable String sKey, @Nonnegative long nSize);
}
