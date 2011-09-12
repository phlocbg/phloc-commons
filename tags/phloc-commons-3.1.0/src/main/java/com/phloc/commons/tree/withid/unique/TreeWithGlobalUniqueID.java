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
package com.phloc.commons.tree.withid.unique;

import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.tree.withid.ITreeItemWithID;

/**
 * A managed tree is a specialized version of the tree, where each item is
 * required to have a unique ID so that item searching can be performed quite
 * easily.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        The type of the key elements for the tree.
 * @param <VALUETYPE>
 *        The type of the elements contained in the tree
 */
@NotThreadSafe
public class TreeWithGlobalUniqueID <KEYTYPE, VALUETYPE> extends
                                                         BasicTreeWithGlobalUniqueID <KEYTYPE, VALUETYPE, ITreeItemWithID <KEYTYPE, VALUETYPE>>
{
  public TreeWithGlobalUniqueID ()
  {
    this (new DefaultTreeItemWithUniqueIDFactory <KEYTYPE, VALUETYPE> ());
  }

  public TreeWithGlobalUniqueID (final ITreeItemWithUniqueIDFactory <KEYTYPE, VALUETYPE, ITreeItemWithID <KEYTYPE, VALUETYPE>> aFactory)
  {
    super (aFactory);
  }
}
