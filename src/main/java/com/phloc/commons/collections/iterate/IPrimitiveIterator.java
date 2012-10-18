/**
 * Copyright (C) 2006-2012 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the Linterfacecense.
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
package com.phloc.commons.collections.iterate;

/**
 * Base interface for all primitive iterator interfaces
 * 
 * @author philip
 */
public interface IPrimitiveIterator
{
  /**
   * Returns <code>true</code> if the iteration has more elements. (In other
   * words, returns <code>true</code> if <code>next</code> would return an
   * element rather than throwing an exception.)
   * 
   * @return <code>true</code> if the iterator has more elements.
   */
  boolean hasNext ();
}
