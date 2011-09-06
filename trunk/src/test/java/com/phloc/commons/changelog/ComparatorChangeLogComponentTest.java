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
package com.phloc.commons.changelog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.version.Version;

/**
 * Test class for class {@link ComparatorChangeLogComponent}.
 * 
 * @author philip
 */
public final class ComparatorChangeLogComponentTest
{
  @Test
  public void testAll ()
  {
    final List <ChangeLog> aEntries = ContainerHelper.newList (ChangeLogSerializer.readAllChangeLogs ().values ());
    aEntries.add (new ChangeLog (new Version (1), "aaa-first"));
    aEntries.add (new ChangeLog (new Version (1), "zzz-last"));
    assertSame (aEntries, ContainerHelper.getSortedInline (aEntries, new ComparatorChangeLogComponent ()));
    assertEquals ("aaa-first", aEntries.get (0).getComponent ());
    assertEquals ("phloc-commons", aEntries.get (1).getComponent ());
    assertEquals ("zzz-last", aEntries.get (2).getComponent ());
  }
}
