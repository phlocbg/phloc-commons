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
package com.phloc.commons.parent.impl;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.parent.IChildrenProvider;

/**
 * An {@link IChildrenProvider} that returns the children in
 * {@link #getChildren(Object)} sorted.
 * 
 * @author philip
 * @param <CHILDTYPE>
 *        The data type of the child objects.
 */
@Immutable
public class ChildrenProviderSorting <CHILDTYPE> implements IChildrenProvider <CHILDTYPE>
{
  protected final IChildrenProvider <CHILDTYPE> m_aCR;
  private final Comparator <? super CHILDTYPE> m_aComparator;

  public ChildrenProviderSorting (@Nonnull final IChildrenProvider <CHILDTYPE> aCR,
                                  @Nonnull final Comparator <? super CHILDTYPE> aComparator)
  {
    if (aCR == null)
      throw new NullPointerException ("childrenProvider");
    if (aComparator == null)
      throw new NullPointerException ("comparator");
    m_aCR = aCR;
    m_aComparator = aComparator;
  }

  public final boolean hasChildren (@Nullable final CHILDTYPE aCurrent)
  {
    // Just pass on to the original children resolver
    return m_aCR.hasChildren (aCurrent);
  }

  public final int getChildCount (@Nullable final CHILDTYPE aCurrent)
  {
    // Just pass on to the original children resolver
    return m_aCR.getChildCount (aCurrent);
  }

  @Nullable
  public List <? extends CHILDTYPE> getChildren (@Nullable final CHILDTYPE aCurrent)
  {
    final Collection <? extends CHILDTYPE> ret = m_aCR.getChildren (aCurrent);
    return ret == null ? null : ContainerHelper.getSorted (ret, m_aComparator);
  }
}
