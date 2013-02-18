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
package com.phloc.commons.collections.iterate;

import java.util.Iterator;

/**
 * This is a merged interface of {@link Iterator} and {@link Iterable} for
 * simpler usage of iterators in the new Java 1.5 "for" constructs.
 * 
 * @author philip
 * @param <ELEMENTTYPE>
 *        The type of object to iterate
 */
public interface IIterableIterator <ELEMENTTYPE> extends Iterable <ELEMENTTYPE>, Iterator <ELEMENTTYPE>
{
  /**
   * {@inheritDoc}
   * 
   * @return The next element. No constraint whether the element can be
   *         <code>null</code> or non-<code>null</code>.
   */
  @Override
  ELEMENTTYPE next ();
}
