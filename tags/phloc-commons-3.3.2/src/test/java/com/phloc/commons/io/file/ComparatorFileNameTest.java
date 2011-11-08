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
package com.phloc.commons.io.file;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link ComparatorFileName}.
 * 
 * @author philip
 */
public final class ComparatorFileNameTest extends AbstractPhlocTestCase
{
  @Test
  public void testIndexOfExtension ()
  {
    final List <File> aList = ContainerHelper.newList (new File ("abc"), new File ("dir/aaa"), new File ("ABB"));
    ContainerHelper.getSortedInline (aList, new ComparatorFileName (L_DE));
    assertEquals (3, aList.size ());
    assertEquals ("aaa", aList.get (0).getName ());
    assertEquals ("ABB", aList.get (1).getName ());
    assertEquals ("abc", aList.get (2).getName ());
  }
}
