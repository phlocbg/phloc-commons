/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.tree.simple;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class DefaultTreeItem <VALUETYPE> extends BasicTreeItem <VALUETYPE, DefaultTreeItem <VALUETYPE>>
{
  /**
   * Constructor for root object.
   */
  public DefaultTreeItem (@Nonnull final IBasicTreeItemFactory <VALUETYPE, DefaultTreeItem <VALUETYPE>> aFactory)
  {
    super (aFactory);
  }

  /**
   * Constructor for normal elements.
   * 
   * @param aParent
   *        Parent item to use. May never be <code>null</code> since only the
   *        root has no parent and for the root item a special no-argument
   *        constructor is present.
   */
  public DefaultTreeItem (@Nonnull final DefaultTreeItem <VALUETYPE> aParent)
  {
    super (aParent);
  }
}
