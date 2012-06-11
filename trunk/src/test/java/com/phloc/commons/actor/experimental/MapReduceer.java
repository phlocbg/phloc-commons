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
package com.phloc.commons.actor.experimental;

public interface MapReduceer
{
  /**
   * Map (in place) the elements of an array.
   * 
   * @param values
   *        elements to map
   * @param start
   *        start position in values
   * @param end
   *        end position in values
   */
  void map (Object [] values, int start, int end);

  /**
   * Reduce the elements of an array.
   * 
   * @param values
   *        elements to reduce
   * @param start
   *        start position in values
   * @param end
   *        end position in values
   * @param target
   *        place to set reduced value
   * @param posn
   *        position in target to place the value
   */
  void reduce (Object [] values, int start, int end, Object [] target, int posn);
}
