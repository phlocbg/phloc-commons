/**
 * Copyright (C) 2006-2013 phloc systems
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

import java.util.List;

import org.junit.Test;

import com.phloc.commons.combine.CombinatorStringWithSeparator;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link DefaultFolderTreeItemFactory}.
 * 
 * @author Philip Helger
 */
public final class DefaultFolderTreeItemFactoryTest
{
  @Test
  public void testBasic ()
  {
    final DefaultFolderTreeItemFactory <String, String, List <String>> ftif = new DefaultFolderTreeItemFactory <String, String, List <String>> (new CombinatorStringWithSeparator ("/"));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (ftif,
                                                                    new DefaultFolderTreeItemFactory <String, String, List <String>> (new CombinatorStringWithSeparator ("/")));
    assertNotNull (ftif.createRoot ());
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (ftif,
                                                                        new DefaultFolderTreeItemFactory <String, String, List <String>> (new CombinatorStringWithSeparator ("/")));
  }
}
