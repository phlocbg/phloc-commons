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
package com.phloc.commons.parent.impl;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.parent.IChildrenProviderWithID;

/**
 * An {@link IChildrenProviderWithID} with ID that returns the children in
 * {@link #getChildren(Object)} sorted.
 * 
 * @author Philip Helger
 * @param <KEYTYPE>
 *        The key type.
 * @param <CHILDTYPE>
 *        The data type of the child objects.
 */
public class ChildrenProviderSortingWithID <KEYTYPE, CHILDTYPE> extends ChildrenProviderSorting <CHILDTYPE> implements
                                                                                                           IChildrenProviderWithID <KEYTYPE, CHILDTYPE>
{
  public ChildrenProviderSortingWithID (@Nonnull final IChildrenProviderWithID <KEYTYPE, CHILDTYPE> aCP,
                                        @Nonnull final Comparator <? super CHILDTYPE> aComparator)
  {
    super (aCP, aComparator);
  }

  @SuppressWarnings ("unchecked")
  @Nullable
  public CHILDTYPE getChildWithID (@Nullable final CHILDTYPE aCurrent, @Nullable final KEYTYPE aID)
  {
    return ((IChildrenProviderWithID <KEYTYPE, CHILDTYPE>) m_aCR).getChildWithID (aCurrent, aID);
  }
}
