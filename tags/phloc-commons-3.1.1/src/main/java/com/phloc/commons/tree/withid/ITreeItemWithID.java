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
package com.phloc.commons.tree.withid;

/**
 * Default tree item with ID.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        The type of the ID.
 * @param <VALUETYPE>
 *        The type of the value.
 */
public interface ITreeItemWithID <KEYTYPE, VALUETYPE> extends IBasicTreeItemWithID <KEYTYPE, VALUETYPE, ITreeItemWithID <KEYTYPE, VALUETYPE>>
{
  /* empty */
}
