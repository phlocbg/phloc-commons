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

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.id.IHasID;

public final class MockHasChildren implements IHasChildren <MockHasChildren>, IHasID <String>
{
  private final String m_sID;
  private final List <MockHasChildren> m_aList;

  public MockHasChildren (@Nonnull final String sID, @Nullable final MockHasChildren... aList)
  {
    m_sID = sID;
    m_aList = ContainerHelper.newList (aList);
  }

  public String getID ()
  {
    return m_sID;
  }

  public boolean hasChildren ()
  {
    return !m_aList.isEmpty ();
  }

  public int getChildCount ()
  {
    return m_aList.size ();
  }

  public Collection <? extends MockHasChildren> getChildren ()
  {
    return ContainerHelper.makeUnmodifiable (m_aList);
  }

  public MockHasChildren getChildWithID (final String sID)
  {
    for (final MockHasChildren aChild : m_aList)
      if (EqualsUtils.equals (aChild.m_sID, sID))
        return aChild;
    return null;
  }
}
