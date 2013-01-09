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
package com.phloc.commons.parent;

import java.util.Collection;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;

/**
 * This interface can be used to generically resolved children of a certain
 * object.
 * 
 * @author philip
 * @param <CHILDTYPE>
 *        The type of the children to retrieve.
 */
public interface IChildrenProvider <CHILDTYPE>
{
  /**
   * Check if an item has children.
   * 
   * @param aCurrent
   *        The object to determine the children of. May be <code>null</code>
   *        depending on the concrete implementation.
   * @return <code>true</code> if this item has children, <code>false</code>
   *         otherwise.
   */
  boolean hasChildren (@Nonnull (when = When.MAYBE) CHILDTYPE aCurrent);

  /**
   * @param aCurrent
   *        The object to determine the children count of. May be
   *        <code>null</code> depending on the concrete implementation.
   * @return The number of contained direct children. Always &ge; 0.
   */
  @Nonnegative
  int getChildCount (@Nonnull (when = When.MAYBE) CHILDTYPE aCurrent);

  /**
   * Get the children of the passed object.
   * 
   * @param aCurrent
   *        The object to determine the children of. May be <code>null</code>
   *        depending on the concrete implementation.
   * @return The child objects, or <code>null</code> if there are no children.
   *         If <code>null</code> is passed, the resolver is expected to return
   *         any possible top level (root) elements. This method may NOT return
   *         <code>null</code> if the call to {@link #hasChildren(Object)} with
   *         the same object returned <code>true</code>.
   */
  @Nullable
  Collection <? extends CHILDTYPE> getChildren (@Nonnull (when = When.MAYBE) CHILDTYPE aCurrent);
}
