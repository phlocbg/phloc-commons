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

import java.util.NoSuchElementException;

/**
 * Interface for a primitive iterator iterating over int elements
 * 
 * @author philip
 */
public interface IPrimitiveIteratorInt extends IPrimitiveIterator
{
  /**
   * Returns the next element in the iteration.
   * 
   * @return the next element in the iteration.
   * @exception NoSuchElementException
   *            iteration has no more elements.
   */
  int next ();
}
