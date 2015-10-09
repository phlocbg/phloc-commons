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
package com.phloc.commons.stats;

/**
 * Modifying interface of {@link StatisticsHandlerCounterMBean}
 * 
 * @author Philip Helger
 */
public interface IStatisticsHandlerCounter extends StatisticsHandlerCounterMBean
{
  /**
   * Increment the counter by 1
   */
  void increment ();

  /**
   * Increment the counter by an arbitrary number
   * 
   * @param nByHowMany
   *        The number to be added. May be negative as well.
   */
  void increment (long nByHowMany);
}
