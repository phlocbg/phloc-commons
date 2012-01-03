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
package com.phloc.commons.tree;

import javax.annotation.Nonnull;

/**
 * Base interface for a tree.
 * 
 * @param <VALUETYPE>
 *        tree item value type
 * @param <ITEMTYPE>
 *        tree item implementation type
 * @author philip
 */
public interface IBasicTree <VALUETYPE, ITEMTYPE extends IBasicTreeItem <VALUETYPE, ITEMTYPE>>
{
  /**
   * @return The root item of the tree. Never <code>null</code>. The root item
   *         should not be displayed but it is the parent node of all displayed
   *         root items.
   */
  @Nonnull
  ITEMTYPE getRootItem ();
}
