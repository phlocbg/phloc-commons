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
package org.apache.commons.collections.primitives;

import java.util.EmptyStackException;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.phloc.commons.mock.PhlocAssert;

/**
 * Tests the FloatStack class.
 * 
 * @author Apache Directory Project
 * @since Commons Primitives 1.1
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 */
public class TestFloatStack extends TestCase
{
  FloatStack stack = null;

  /**
   * Runs the test.
   * 
   * @param args
   *        nada
   */
  public static void main (final String [] args)
  {
    junit.textui.TestRunner.run (TestFloatStack.class);
  }

  public static TestSuite suite ()
  {
    return new TestSuite (TestFloatStack.class);
  }

  /*
   * (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp () throws Exception
  {
    super.setUp ();
    stack = new FloatStack ();
  }

  public TestFloatStack (final String arg0)
  {
    super (arg0);
  }

  public void testEmpty ()
  {
    assertTrue ("Newly created stacks should be empty", stack.isEmpty ());
    stack.push (1.4f);
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

    for (float ii = 0; ii < 10; ii++)
    {
      stack.push (ii);
      PhlocAssert.assertEquals (ii, stack.peek ());
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

    for (float ii = 0; ii < 10; ii++)
    {
      stack.push (ii);
      PhlocAssert.assertEquals (ii, stack.pop ());
    }

    for (float ii = 0; ii < 10; ii++)
    {
      stack.push (ii);
    }
    for (float ii = 10; ii < 0; ii--)
    {
      stack.push (ii);
      PhlocAssert.assertEquals (ii, stack.pop ());
    }
  }

  public void testPush ()
  {
    stack.push (1.4f);
    stack.push (2.67f);
    assertFalse (stack.isEmpty ());
    PhlocAssert.assertEquals (2.67f, stack.pop ());
    PhlocAssert.assertEquals (1.4f, stack.pop ());
  }

  public void testSearch ()
  {
    stack.push (0);
    stack.push (1);
    assertTrue (2 == stack.search (0));
    stack.push (0);
    assertTrue (1 == stack.search (0));
    stack.push (0);
    assertTrue (3 == stack.search (1));
    assertTrue (-1 == stack.search (44));
  }

  public void testArrayConstructor ()
  {
    final float [] array = { 1.0f, 2.0f, 3.0f, 4.0f };
    stack = new FloatStack (array);
    assertEquals (array.length, stack.size ());
    for (int i = array.length - 1; i >= 0; i--)
    {
      assertEquals (array[i], stack.pop (), 0.0f);
    }
  }

  public void testPeekN ()
  {
    final float [] array = { 1.0f, 2.0f, 3.0f, 4.0f };
    stack = new FloatStack (array);
    for (int i = array.length - 1; i >= 0; i--)
    {
      assertEquals (array[i], stack.peek ((array.length - 1) - i), 0.0f);
    }
  }

}
