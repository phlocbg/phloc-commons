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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.combine.CombinatorStringWithSeparator;
import com.phloc.commons.combine.ICombinator;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link FolderTree}.
 * 
 * @author philip
 */
public final class FolderTreeTest extends AbstractPhlocTestCase
{
  @Test
  public void testBasic ()
  {
    final ICombinator <String> aCombinator = new CombinatorStringWithSeparator ("/");
    final FolderTree <String, Integer, Set <Integer>> ft = FolderTree.<String, Integer> createForSet (aCombinator);
    assertNotNull (ft.getRootItem ());

    final FolderTreeItem <String, Integer, Set <Integer>> i1 = ft.getRootItem ()
                                                                 .createChildItem ("id1",
                                                                                   ContainerHelper.newSet (I1, I2, I3));
    assertNotNull (i1);

    final FolderTreeItem <String, Integer, Set <Integer>> i2 = ft.getRootItem ()
                                                                 .createChildItem ("id1",
                                                                                   ContainerHelper.newSet (I1, I2, I3));
    assertNotNull (i2);
    assertSame (i1, i2);

    final FolderTreeItem <String, Integer, Set <Integer>> i3 = ft.getRootItem ()
                                                                 .createChildItem ("id3",
                                                                                   ContainerHelper.newSet (I1, I3));
    assertNotNull (i3);
    assertTrue (i1 != i3);

    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (i1, i3);
  }
}
