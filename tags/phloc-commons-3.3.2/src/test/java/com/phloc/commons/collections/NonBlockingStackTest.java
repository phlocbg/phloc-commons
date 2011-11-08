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
package com.phloc.commons.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.EmptyStackException;
import java.util.HashSet;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link NonBlockingStack}
 * 
 * @author philip
 */
public final class NonBlockingStackTest
{
  @Test
  public void testCtor ()
  {
    NonBlockingStack <String> st = NonBlockingStack.create ();
    assertTrue (st.isEmpty ());
    assertFalse (st.iterator ().hasNext ());
    st = NonBlockingStack.create ("s", "t");
    assertEquals (2, st.size ());
    assertEquals ("s", st.firstElement ());
    assertTrue (st.iterator ().hasNext ());
    st = NonBlockingStack.create ((String []) null);
    assertTrue (st.isEmpty ());
    assertFalse (st.iterator ().hasNext ());
    st = NonBlockingStack.create (ContainerHelper.newList ("s", "t"));
    assertEquals (2, st.size ());
    assertEquals ("s", st.firstElement ());
    assertTrue (st.iterator ().hasNext ());
    st = NonBlockingStack.create (new HashSet <String> ());
    assertTrue (st.isEmpty ());
    assertFalse (st.iterator ().hasNext ());
    st = NonBlockingStack.create (NonBlockingStack.create ("s", "t"));
    assertEquals (2, st.size ());
    assertEquals ("s", st.firstElement ());
    assertTrue (st.iterator ().hasNext ());
  }

  @Test
  public void testMethods ()
  {
    final NonBlockingStack <String> aStack = new NonBlockingStack <String> ();
    assertEquals (0, aStack.size ());
    assertTrue (aStack.isEmpty ());
    aStack.push ("a");
    assertEquals (1, aStack.size ());
    assertFalse (aStack.isEmpty ());
    assertFalse (aStack.contains ("b"));
    aStack.push ("b");
    assertTrue (aStack.contains ("b"));
    aStack.pop ();
    assertFalse (aStack.contains ("b"));
    assertEquals (1, aStack.size ());
    assertFalse (aStack.isEmpty ());
    aStack.pop ();
    assertEquals (0, aStack.size ());
    assertTrue (aStack.isEmpty ());
    aStack.clear ();
    aStack.push ("a");
    assertEquals ("a", aStack.firstElement ());
    assertFalse (aStack.isEmpty ());
    aStack.clear ();
    assertTrue (aStack.isEmpty ());
    aStack.push ("a");
    assertEquals ("a", aStack.peek ());
    assertEquals ("a", aStack.pop ());

    try
    {
      aStack.peek ();
      fail ();
    }
    catch (final EmptyStackException ex)
    {}
    try
    {
      aStack.firstElement ();
      fail ();
    }
    catch (final EmptyStackException ex)
    {}

    aStack.addAll (ContainerHelper.newList ("g", "h"));
    assertEquals (2, aStack.size ());
    assertEquals ("g", aStack.firstElement ());
    assertEquals ("h", aStack.peek ());
    assertEquals ("h", aStack.pop ());
    assertEquals ("g", aStack.pop ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new NonBlockingStack <String> (),
                                                                    new NonBlockingStack <String> ());
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new NonBlockingStack <String> ("a", "b"),
                                                                    new NonBlockingStack <String> ("a", "b"));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new NonBlockingStack <String> (ContainerHelper.newList ("a",
                                                                                                                            "b")),
                                                                    new NonBlockingStack <String> (ContainerHelper.newList ("a",
                                                                                                                            "b")));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new NonBlockingStack <String> (ContainerHelper.newList ("a",
                                                                                                                                "b")),
                                                                        new NonBlockingStack <String> (ContainerHelper.newList ("a")));
    PhlocTestUtils.testGetClone (new NonBlockingStack <String> ("a", "b"));
    PhlocTestUtils.testGetClone (new NonBlockingStack <String> ());
  }
}
