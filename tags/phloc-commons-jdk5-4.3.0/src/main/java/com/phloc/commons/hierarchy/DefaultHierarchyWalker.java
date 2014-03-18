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
package com.phloc.commons.hierarchy;

import javax.annotation.Nonnegative;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;

/**
 * The default implementation of the {@link IBaseHierarchyWalker} interface.
 * 
 * @author Philip Helger
 */
public class DefaultHierarchyWalker implements IBaseHierarchyWalker
{
  private int m_nLevel;

  public DefaultHierarchyWalker ()
  {
    this (0);
  }

  public DefaultHierarchyWalker (final int nInitialLevel)
  {
    m_nLevel = nInitialLevel;
  }

  @OverrideOnDemand
  public void begin ()
  {}

  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  public void onLevelDown ()
  {
    ++m_nLevel;
  }

  @Nonnegative
  public int getLevel ()
  {
    return m_nLevel;
  }

  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  public void onLevelUp ()
  {
    --m_nLevel;
  }

  @OverrideOnDemand
  public void end ()
  {}
}
