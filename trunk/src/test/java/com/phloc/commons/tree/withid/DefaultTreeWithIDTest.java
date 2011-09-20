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
package com.phloc.commons.tree.withid;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link DefaultTreeWithID}.
 * 
 * @author philip
 */
public final class DefaultTreeWithIDTest
{
  @Test
  public void testBasic ()
  {
    final DefaultTreeWithID <String, String> t = new DefaultTreeWithID <String, String> ();
    assertNotNull (t.getRootItem ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (t, new DefaultTreeWithID <String, String> ());
    final DefaultTreeWithID <String, String> t2 = new DefaultTreeWithID <String, String> ();
    t2.getRootItem ().createChildItem ("dataid", "data");
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (t, t2);

    try
    {
      new DefaultTreeWithID <String, String> (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // factory creating a null root
      new DefaultTreeWithID <String, String> (new DefaultTreeItemWithIDFactory <String, String> ()
      {
        @Override
        public DefaultTreeItemWithID <String, String> createRoot ()
        {
          return null;
        }
      });
      fail ();
    }
    catch (final IllegalStateException ex)
    {}

    try
    {
      // factory creating a root item with a parent
      new DefaultTreeWithID <String, String> (new DefaultTreeItemWithIDFactory <String, String> ()
      {
        @Override
        public DefaultTreeItemWithID <String, String> createRoot ()
        {
          return new DefaultTreeItemWithID <String, String> (super.createRoot (), "DataIDroot");
        }
      });
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
  }
}
