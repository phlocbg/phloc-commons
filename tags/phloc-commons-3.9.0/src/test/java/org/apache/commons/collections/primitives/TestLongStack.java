/**
 * Copyright (C) 2006-2012 phloc systems
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
package org.apache.commons.collections.primitives;

import java.util.EmptyStackException;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the LongStack class.
 *
 * @author Apache Directory Project
 * @since Commons Primitives 1.1
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 */
public class TestLongStack extends TestCase
{
  LongStack stack = null;

  /**
   * Runs the test.
   *
   * @param args
   *        nada
   */
  public static void main (final String [] args)
  {
    junit.textui.TestRunner.run (TestLongStack.class);
  }

  public static TestSuite suite ()
  {
    return new TestSuite (TestLongStack.class);
  }

  /*
   * (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp () throws Exception
  {
    super.setUp ();
    stack = new LongStack ();
  }

  /**
   * Constructor for IntStackTest.
   *
   * @param arg0
   */
  public TestLongStack (final String arg0)
  {
    super (arg0);
  }

  public void testEmpty ()
  {
    assertTrue ("Newly created stacks should be empty", stack.isEmpty ());
    stack.push (1234223332233234322l);
    assertFalse ("Stack with item should not be empty", stack.isEmpty ());
    stack.pop ();
    assertTrue ("Stack last int popped should be empty", stack.isEmpty ());
  }

  public void testPeek ()
  {
    try
    {
      stack.peek ();
      fail ("Peek should have thrown an EmptyStackException");
    }
    catch (final EmptyStackException e)
    {
      assertNotNull ("EmptyStackException should not be null", e);
    }

    for (int ii = 0; ii < 10; ii++)
    {
      stack.push (ii);
      assertTrue (ii == stack.peek ());
    }
  }

  public void testPop ()
  {
    try
    {
      stack.pop ();
      fail ("Pop should have thrown an EmptyStackException");
    }
    catch (final EmptyStackException e)
    {
      assertNotNull ("EmptyStackException should not be null", e);
    }

    for (long ii = 0; ii < 10; ii++)
    {
      stack.push (ii);
      assertTrue (ii == stack.pop ());
    }

    for (long ii = 0; ii < 10; ii++)
    {
      stack.push (ii);
    }
    for (long ii = 10; ii < 0; ii--)
    {
      stack.push (ii);
      assertTrue (ii == stack.pop ());
    }
  }

  public void testPush ()
  {
    stack.push (0L);
    stack.push (0L);
    assertFalse (stack.isEmpty ());
    assertTrue (0L == stack.pop ());
    assertTrue (0L == stack.pop ());
  }

  public void testSearch ()
  {
    stack.push (0L);
    stack.push (1L);
    assertTrue (2L == stack.search (0L));
    stack.push (0L);
    assertTrue (1L == stack.search (0L));
    stack.push (0L);
    assertTrue (3L == stack.search (1L));
    assertTrue (-1L == stack.search (44L));
  }

  public void testArrayConstructor ()
  {
    final long [] array = { 1, 2, 3, 4 };
    stack = new LongStack (array);
    assertEquals (array.length, stack.size ());
    for (int i = array.length - 1; i >= 0; i--)
    {
      assertEquals (array[i], stack.pop ());
    }
  }

  public void testPeekN ()
  {
    final long [] array = { 1, 2, 3, 4 };
    stack = new LongStack (array);
    for (int i = array.length - 1; i >= 0; i--)
    {
      assertEquals (array[i], stack.peek ((array.length - 1) - i));
    }
  }
}
