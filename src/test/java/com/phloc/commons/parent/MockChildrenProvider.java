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

import javax.annotation.Nullable;

public class MockChildrenProvider implements IChildrenProvider <MockHasChildren>
{
  private final MockHasChildren m_aRootItem;

  public MockChildrenProvider ()
  {
    this (null);
  }

  public MockChildrenProvider (@Nullable final MockHasChildren aRootItem)
  {
    m_aRootItem = aRootItem;
  }

  public boolean hasChildren (final MockHasChildren aCurrent)
  {
    return aCurrent != null ? aCurrent.hasChildren () : m_aRootItem != null ? m_aRootItem.hasChildren () : false;
  }

  public int getChildCount (final MockHasChildren aCurrent)
  {
    return aCurrent != null ? aCurrent.getChildCount () : m_aRootItem != null ? m_aRootItem.getChildCount () : 0;
  }

  public Collection <? extends MockHasChildren> getChildren (final MockHasChildren aCurrent)
  {
    return aCurrent != null ? aCurrent.getChildren () : m_aRootItem != null ? m_aRootItem.getChildren () : null;
  }
}
