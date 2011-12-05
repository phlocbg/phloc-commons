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
package com.phloc.commons.id;

import javax.annotation.Nonnull;

import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.ESortOrder;

/**
 * This is a collation {@link java.util.Comparator} for objects that implement
 * the {@link IHasSimpleIntID} interface.
 * 
 * @author philip
 * @param <DATATYPE>
 *        The type of elements to be compared.
 */
public class ComparatorHasSimpleIntID <DATATYPE extends IHasSimpleIntID> extends AbstractComparator <DATATYPE>
{
  public ComparatorHasSimpleIntID ()
  {}

  public ComparatorHasSimpleIntID (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  @Override
  protected final int mainCompare (final DATATYPE aElement1, final DATATYPE aElement2)
  {
    final int nID1 = aElement1.getID ();
    final int nID2 = aElement2.getID ();
    return nID1 < nID2 ? -1 : nID2 < nID1 ? +1 : 0;
  }
}
