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
package com.phloc.commons.parent.utils;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.hierarchy.DefaultHierarchyWalkerDynamicCallback;
import com.phloc.commons.parent.IChildrenProvider;
import com.phloc.commons.parent.MockChildrenProvider;
import com.phloc.commons.parent.MockHasChildren;

/**
 * Test class for class {@link ChildrenProviderWalkerDynamic}.
 * 
 * @author Philip Helger
 */
public final class ChildrenProviderWalkerDynamicTest
{
  @Test
  public void testAll ()
  {
    final MockHasChildren hca = new MockHasChildren ("a");
    final MockHasChildren hcb = new MockHasChildren ("b");
    final MockHasChildren hc1 = new MockHasChildren ("1", hca, hcb);
    final IChildrenProvider <MockHasChildren> cp = new MockChildrenProvider (hc1);

    // Having children
    ChildrenProviderWalkerDynamic.walkProvider (cp, new DefaultHierarchyWalkerDynamicCallback <MockHasChildren> ());

    // Not having children
    ChildrenProviderWalkerDynamic.walkProvider (new MockChildrenProvider (hca),
                                                new DefaultHierarchyWalkerDynamicCallback <MockHasChildren> ());

    // Start explicitly at object
    ChildrenProviderWalkerDynamic.walkSubProvider (hc1,
                                                   cp,
                                                   new DefaultHierarchyWalkerDynamicCallback <MockHasChildren> ());
    // no provider
    try
    {
      ChildrenProviderWalkerDynamic.walkProvider (null, new DefaultHierarchyWalkerDynamicCallback <MockHasChildren> ());
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    // no callback
    try
    {
      ChildrenProviderWalkerDynamic.walkProvider (cp, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    // Nothing to walk
    try
    {
      ChildrenProviderWalkerDynamic.walkSubProvider (null,
                                                     cp,
                                                     new DefaultHierarchyWalkerDynamicCallback <MockHasChildren> ());
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
