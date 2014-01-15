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

import javax.annotation.Nullable;

/**
 * This helper interface is required to build a tree. It should be used like
 * this:<br>
 * <code>class A implements IHasParent &lt;A&gt;
 * {
 *   A getParent () { ... }
 * }</code>
 * 
 * @author Philip
 * @param <PARENTTYPE>
 *        The type of object to get a parent from
 */
public interface IHasParent <PARENTTYPE>
{
  /**
   * Get the parent object of this object.
   * 
   * @return The parent object or <code>null</code> if this object has no
   *         parent.
   */
  @Nullable
  PARENTTYPE getParent ();
}
