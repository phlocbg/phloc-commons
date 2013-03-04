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
package com.phloc.commons.tree.withid.folder;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.combine.ICombinator;

/**
 * The default folder tree item factory implementation.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        Key type
 * @param <VALUETYPE>
 *        Value type
 * @param <COLLTYPE>
 *        Collection type consisting of value elements
 */
@NotThreadSafe
public class DefaultFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE extends Collection <VALUETYPE>> extends
                                                                                                        AbstractFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE, DefaultFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE>>
{
  public DefaultFolderTreeItemFactory (@Nullable final ICombinator <KEYTYPE> aKeyCombinator)
  {
    super (aKeyCombinator);
  }

  @Override
  protected final DefaultFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> internalCreateRoot ()
  {
    return new DefaultFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> (this);
  }

  @Override
  @Nonnull
  protected DefaultFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> internalCreate (@Nonnull final DefaultFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> aParent,
                                                                                 @Nonnull final KEYTYPE aDataID)
  {
    return new DefaultFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> (aParent, aDataID);
  }
}