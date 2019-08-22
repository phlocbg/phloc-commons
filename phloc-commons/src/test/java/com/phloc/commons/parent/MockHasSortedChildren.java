/**
 * Copyright (C) 2006-2015 phloc systems
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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.id.ComparatorHasIDString;
import com.phloc.commons.id.IHasID;

public final class MockHasSortedChildren implements IHasChildrenSorted <MockHasSortedChildren>, IHasID <String>
{
  private final String m_sID;
  private final List <MockHasSortedChildren> m_aList;

  public MockHasSortedChildren (@Nonnull final String sID, @Nullable final MockHasSortedChildren... aList)
  {
    this.m_sID = sID;
    this.m_aList = ContainerHelper.getSorted (aList, new ComparatorHasIDString <IHasID <String>> ());
  }

  @Override
  public String getID ()
  {
    return this.m_sID;
  }

  @Override
  public boolean hasChildren ()
  {
    return !this.m_aList.isEmpty ();
  }

  @Override
  public int getChildCount ()
  {
    return this.m_aList.size ();
  }

  @Override
  public List <MockHasSortedChildren> getChildren ()
  {
    return ContainerHelper.makeUnmodifiable (this.m_aList);
  }

  @Override
  public MockHasSortedChildren getChildAtIndex (final int nIndex)
  {
    return this.m_aList.get (nIndex);
  }

  @Override
  @Nullable
  public MockHasSortedChildren getFirstChild ()
  {
    return ContainerHelper.getFirstElement (this.m_aList);
  }

  @Override
  @Nullable
  public MockHasSortedChildren getLastChild ()
  {
    return ContainerHelper.getLastElement (this.m_aList);
  }
}
