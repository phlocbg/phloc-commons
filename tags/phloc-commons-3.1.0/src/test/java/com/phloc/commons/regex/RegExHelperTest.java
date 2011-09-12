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
package com.phloc.commons.regex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

/**
 * Test class for {@link RegExHelper}.
 * 
 * @author philip
 */
public final class RegExHelperTest
{
  /**
   * Test for method split
   */
  @Test
  public void testSplitNoLimit ()
  {
    String [] x = RegExHelper.split ("abc", "b");
    assertNotNull (x);
    assertEquals (2, x.length);
    assertEquals ("a", x[0]);
    assertEquals ("c", x[1]);

    x = RegExHelper.split ("aaacbccca", "b");
    assertNotNull (x);
    assertEquals (2, x.length);
    assertEquals ("aaac", x[0]);
    assertEquals ("ccca", x[1]);

    x = RegExHelper.split ("aaa", "b");
    assertNotNull (x);
    assertEquals (1, x.length);
    assertEquals ("aaa", x[0]);

    x = RegExHelper.split ("", "b");
    assertNotNull (x);
    assertEquals (1, x.length);
    assertEquals ("", x[0]);

    x = RegExHelper.split ("ab9cd14ef", "[0-9]+");
    assertNotNull (x);
    assertEquals (3, x.length);
    assertEquals ("ab", x[0]);
    assertEquals ("cd", x[1]);
    assertEquals ("ef", x[2]);

    x = RegExHelper.split (null, "b");
    assertNotNull (x);
    assertEquals (0, x.length);

    x = RegExHelper.split (null, null);
    assertNotNull (x);
    assertEquals (0, x.length);

    try
    {
      // empty regex not allowed
      RegExHelper.split ("ab9cd14ef", null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // empty regex not allowed
      RegExHelper.split ("ab9cd14ef", "");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  /**
   * Test for method split
   */
  @Test
  public void testSplitWithLimit ()
  {
    String [] x = RegExHelper.split ("abc", "b", 2);
    assertNotNull (x);
    assertEquals (2, x.length);
    assertEquals ("a", x[0]);
    assertEquals ("c", x[1]);

    // a limit <= 0 means -> all tokens
    x = RegExHelper.split ("aaacbccca", "b", 0);
    assertNotNull (x);
    assertEquals (2, x.length);
    assertEquals ("aaac", x[0]);
    assertEquals ("ccca", x[1]);

    x = RegExHelper.split ("aaacbccca", "b", 1);
    assertNotNull (x);
    assertEquals (1, x.length);
    assertEquals ("aaacbccca", x[0]);

    x = RegExHelper.split ("aaacbccca", "b", 2);
    assertNotNull (x);
    assertEquals (2, x.length);
    assertEquals ("aaac", x[0]);
    assertEquals ("ccca", x[1]);

    x = RegExHelper.split ("aaacbccca", "b", 3);
    assertNotNull (x);
    assertEquals (2, x.length);
    assertEquals ("aaac", x[0]);
    assertEquals ("ccca", x[1]);

    x = RegExHelper.split ("aaa", "b", 2);
    assertNotNull (x);
    assertEquals (1, x.length);
    assertEquals ("aaa", x[0]);

    x = RegExHelper.split ("", "b", 2);
    assertNotNull (x);
    assertEquals (1, x.length);
    assertEquals ("", x[0]);

    x = RegExHelper.split ("ab9cd14ef", "[0-9]+", 2);
    assertNotNull (x);
    assertEquals (2, x.length);
    assertEquals ("ab", x[0]);
    assertEquals ("cd14ef", x[1]);

    x = RegExHelper.split (null, "b", 2);
    assertNotNull (x);
    assertEquals (0, x.length);

    x = RegExHelper.split (null, null, 2);
    assertNotNull (x);
    assertEquals (0, x.length);

    try
    {
      // empty regex not allowed
      RegExHelper.split ("ab9cd14ef", null, 2);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // empty regex not allowed
      RegExHelper.split ("ab9cd14ef", "", 2);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  /**
   * Test for method splitToList
   */
  @Test
  public void testSplitToListNoLimit ()
  {
    List <String> x = RegExHelper.splitToList ("abc", "b");
    assertNotNull (x);
    assertEquals (2, x.size ());
    assertEquals ("a", x.get (0));
    assertEquals ("c", x.get (1));

    x = RegExHelper.splitToList ("aaacbccca", "b");
    assertNotNull (x);
    assertEquals (2, x.size ());
    assertEquals ("aaac", x.get (0));
    assertEquals ("ccca", x.get (1));

    x = RegExHelper.splitToList ("aaa", "b");
    assertNotNull (x);
    assertEquals (1, x.size ());
    assertEquals ("aaa", x.get (0));

    x = RegExHelper.splitToList ("", "b");
    assertNotNull (x);
    assertEquals (1, x.size ());
    assertEquals ("", x.get (0));

    x = RegExHelper.splitToList ("ab9cd14ef", "[0-9]+");
    assertNotNull (x);
    assertEquals (3, x.size ());
    assertEquals ("ab", x.get (0));
    assertEquals ("cd", x.get (1));
    assertEquals ("ef", x.get (2));

    x = RegExHelper.splitToList (null, "b");
    assertNotNull (x);

    x = RegExHelper.splitToList (null, null);
    assertNotNull (x);

    try
    {
      // empty regex not allowed
      RegExHelper.splitToList ("ab9cd14ef", null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // empty regex not allowed
      RegExHelper.splitToList ("ab9cd14ef", "");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  /**
   * Test for method splitToList
   */
  @Test
  public void testSplitToListWithLimit ()
  {
    List <String> x = RegExHelper.splitToList ("abc", "b", 2);
    assertNotNull (x);
    assertEquals (2, x.size ());
    assertEquals ("a", x.get (0));
    assertEquals ("c", x.get (1));

    // a limit <= 0 means -> all tokens
    x = RegExHelper.splitToList ("aaacbccca", "b", 0);
    assertNotNull (x);
    assertEquals (2, x.size ());
    assertEquals ("aaac", x.get (0));
    assertEquals ("ccca", x.get (1));

    x = RegExHelper.splitToList ("aaacbccca", "b", 1);
    assertNotNull (x);
    assertEquals (1, x.size ());
    assertEquals ("aaacbccca", x.get (0));

    x = RegExHelper.splitToList ("aaacbccca", "b", 2);
    assertNotNull (x);
    assertEquals (2, x.size ());
    assertEquals ("aaac", x.get (0));
    assertEquals ("ccca", x.get (1));

    x = RegExHelper.splitToList ("aaacbccca", "b", 3);
    assertNotNull (x);
    assertEquals (2, x.size ());
    assertEquals ("aaac", x.get (0));
    assertEquals ("ccca", x.get (1));

    x = RegExHelper.splitToList ("aaa", "b", 2);
    assertNotNull (x);
    assertEquals (1, x.size ());
    assertEquals ("aaa", x.get (0));

    x = RegExHelper.splitToList ("", "b", 2);
    assertNotNull (x);
    assertEquals (1, x.size ());
    assertEquals ("", x.get (0));

    x = RegExHelper.splitToList ("ab9cd14ef", "[0-9]+", 2);
    assertNotNull (x);
    assertEquals (2, x.size ());
    assertEquals ("ab", x.get (0));
    assertEquals ("cd14ef", x.get (1));

    x = RegExHelper.splitToList (null, "b", 2);
    assertNotNull (x);

    x = RegExHelper.splitToList (null, null, 2);
    assertNotNull (x);

    try
    {
      // empty regex not allowed
      RegExHelper.splitToList ("ab9cd14ef", null, 2);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // empty regex not allowed
      RegExHelper.splitToList ("ab9cd14ef", "", 2);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  /**
   * Test for method makeIdentifier
   */
  @Test
  public void testMakeIdentifier ()
  {
    assertNull (RegExHelper.makeIdentifier (null));
    assertEquals ("", RegExHelper.makeIdentifier (""));
    assertEquals ("abc", RegExHelper.makeIdentifier ("abc"));
    assertEquals ("ABC", RegExHelper.makeIdentifier ("ABC"));
    assertEquals ("_0ABC", RegExHelper.makeIdentifier ("0ABC"));
    assertEquals ("_0ABC", RegExHelper.makeIdentifier ("_0ABC"));
    assertEquals ("___", RegExHelper.makeIdentifier (";;;"));
  }

  /**
   * Test for method stringMatchesPattern
   */
  @Test
  public void testStringMatchesPattern ()
  {
    assertTrue (RegExHelper.stringMatchesPattern ("[0-9]+", "1234"));
    assertTrue (RegExHelper.stringMatchesPattern ("[0-9]+", "0"));
    assertFalse (RegExHelper.stringMatchesPattern ("[0-9]+", ""));
    assertTrue (RegExHelper.stringMatchesPattern ("[0-9]*", ""));
    assertTrue (RegExHelper.stringMatchesPattern ("abc", "abc"));
    assertTrue (RegExHelper.stringMatchesPattern ("abc+", "abcccccccccccc"));

    try
    {
      // null regular expression not allowed
      RegExHelper.stringMatchesPattern (null, "any");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // empty regular expression not allowed
      RegExHelper.stringMatchesPattern ("", "any");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null text not allowed
      RegExHelper.stringMatchesPattern ("[0-9]+", null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  /**
   * Test for method stringReplacePattern
   */
  @Test
  public void testStringReplacePattern ()
  {
    assertEquals ("xy", RegExHelper.stringReplacePattern ("\\$email", "$emaily", "x"));
  }

  @Test
  public void testIsValidPattern ()
  {
    assertTrue (RegExHelper.isValidPattern (""));
    assertTrue (RegExHelper.isValidPattern ("abc"));
    assertTrue (RegExHelper.isValidPattern ("ab.+c"));
    assertFalse (RegExHelper.isValidPattern ("*-[]"));

    try
    {
      RegExHelper.isValidPattern (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
