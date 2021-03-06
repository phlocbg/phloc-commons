/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.parent;

import java.util.Collection;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

/**
 * A simple interface, indicating that an item has direct children.
 * 
 * @author Philip Helger
 * @param <CHILDTYPE>
 *        The type of the children.
 */
public interface IHasChildren <CHILDTYPE>
{
  /**
   * @return <code>true</code> if this item has direct children,
   *         <code>false</code> otherwise.
   */
  boolean hasChildren ();

  /**
   * @return The number of contained direct children. Always &ge; 0.
   */
  @Nonnegative
  int getChildCount ();

  /**
   * @return A collection of all direct child elements. May be <code>null</code>
   *         .
   */
  @Nullable
  Collection <? extends CHILDTYPE> getChildren ();
}
