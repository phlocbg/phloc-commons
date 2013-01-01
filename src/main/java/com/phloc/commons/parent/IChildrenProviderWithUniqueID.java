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

import javax.annotation.Nullable;

/**
 * This interface can be used to generically resolved children of a certain
 * object.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        The key type
 * @param <CHILDTYPE>
 *        the value type
 */
public interface IChildrenProviderWithUniqueID <KEYTYPE, CHILDTYPE> extends IChildrenProvider <CHILDTYPE>
{
  /**
   * Retrieve the objects with the passed ID or <code>null</code> if no such
   * object.
   * 
   * @param aID
   *        the ID of the object in question
   * @return the object with the passed ID or <code>null</code>
   */
  @Nullable
  CHILDTYPE getItemWithID (@Nullable KEYTYPE aID);
}
