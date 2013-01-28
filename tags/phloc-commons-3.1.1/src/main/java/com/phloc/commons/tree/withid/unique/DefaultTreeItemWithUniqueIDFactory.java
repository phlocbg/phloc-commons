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

import javax.annotation.Nonnull;

import com.phloc.commons.tree.withid.ITreeItemWithID;
import com.phloc.commons.tree.withid.TreeItemWithID;

/**
 * A special tree item factory that maintains a unique ID over all items!
 * 
 * @author philip
 */
public class DefaultTreeItemWithUniqueIDFactory <KEYTYPE, VALUETYPE> extends
                                                                     AbstractBasicTreeItemWithUniqueIDFactory <KEYTYPE, VALUETYPE, ITreeItemWithID <KEYTYPE, VALUETYPE>>
{
  @Override
  @Nonnull
  protected ITreeItemWithID <KEYTYPE, VALUETYPE> internalCreate (@Nonnull final ITreeItemWithID <KEYTYPE, VALUETYPE> aParent,
                                                                 @Nonnull final KEYTYPE aDataID)
  {
    return new TreeItemWithID <KEYTYPE, VALUETYPE> (aParent, aDataID);
  }

  @Nonnull
  public ITreeItemWithID <KEYTYPE, VALUETYPE> createRoot ()
  {
    return new TreeItemWithID <KEYTYPE, VALUETYPE> (this);
  }
}