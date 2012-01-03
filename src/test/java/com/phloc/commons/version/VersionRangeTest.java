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
package com.phloc.commons.version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * JUnit test for class {@link VersionRange}.
 * 
 * @author philip
 */
public final class VersionRangeTest
{
  @Test
  public void testVersionRange ()
  {
    VersionRange vr = new VersionRange ("[1.2.3, 4.5.6)");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertFalse (vr.includeCeil ());
    assertEquals (vr.getCeilVersion ().getAsString (), "4.5.6");
    assertNotNull (vr.toString ());

    vr = new VersionRange ("   [1.2.3, 4.5.6)   ");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertFalse (vr.includeCeil ());
    assertEquals (vr.getCeilVersion ().getAsString (), "4.5.6");

    vr = new VersionRange ("[1.2.3, 4.5.6]");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertTrue (vr.includeCeil ());
    assertEquals (vr.getCeilVersion ().getAsString (), "4.5.6");

    vr = new VersionRange ("(1.2.3, 4.5.6]");
    assertFalse (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertTrue (vr.includeCeil ());
    assertEquals (vr.getCeilVersion ().getAsString (), "4.5.6");

    vr = new VersionRange ("(1.2.3, 4.5.6)");
    assertFalse (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertFalse (vr.includeCeil ());
    assertEquals (vr.getCeilVersion ().getAsString (), "4.5.6");

    vr = new VersionRange ("1.2.3");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertFalse (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    vr = new VersionRange ("[1.2.3");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertFalse (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    vr = new VersionRange ("(1.2.3");
    assertFalse (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertFalse (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    vr = new VersionRange ("1.2.3]");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertTrue (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    vr = new VersionRange ("1.2.3)");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertFalse (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    vr = new VersionRange ("[1.2.3]");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertTrue (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    vr = new VersionRange ("(1.2.3]");
    assertFalse (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "1.2.3");
    assertTrue (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    vr = new VersionRange ("5");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "5");
    assertFalse (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    vr = new VersionRange ("5,6");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), "5");
    assertFalse (vr.includeCeil ());
    assertEquals (vr.getCeilVersion ().getAsString (), "6");

    vr = new VersionRange (",6");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), Version.DEFAULT_VERSION_STRING);
    assertFalse (vr.includeCeil ());
    assertEquals (vr.getCeilVersion ().getAsString (), "6");

    vr = new VersionRange ("(,6]");
    assertFalse (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), Version.DEFAULT_VERSION_STRING);
    assertTrue (vr.includeCeil ());
    assertEquals (vr.getCeilVersion ().getAsString (), "6");

    vr = new VersionRange ("(]");
    assertFalse (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), Version.DEFAULT_VERSION_STRING);
    assertTrue (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    // empty string: okay
    vr = new VersionRange ("");
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), Version.DEFAULT_VERSION_STRING);
    assertFalse (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    // null: okay
    vr = new VersionRange (null);
    assertTrue (vr.includeFloor ());
    assertEquals (vr.getFloorVersion ().getAsString (), Version.DEFAULT_VERSION_STRING);
    assertFalse (vr.includeCeil ());
    assertNull (vr.getCeilVersion ());

    // check floor > ceil
    try
    {
      new VersionRange ("1.2.3,0.0.1");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {
      // okay
    }
  }

  @Test
  public void testCtor1 ()
  {
    VersionRange vr1 = new VersionRange (new Version ("1.0"), true, new Version ("2.0"), false);
    assertEquals ("[1,2)", vr1.getAsString ());

    // ceiling version null is OK
    vr1 = new VersionRange (new Version ("1.0"), true, null, false);
    assertEquals ("[1)", vr1.getAsString ());

    try
    {
      // null floor not allowed
      new VersionRange (null, true, new Version ("2.0"), false);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // floor > ceil
      new VersionRange (new Version ("2.1"), true, new Version ("2.0"), false);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testCtor2 ()
  {
    new VersionRange (new Version ("1.0"), new Version ("2.0"));
    try
    {
      // null floor not allowed
      new VersionRange (null, new Version ("2.0"));
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // floor > ceil
      new VersionRange (new Version ("2.1"), new Version ("2.0"));
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testVersionMatches ()
  {
    VersionRange vr = new VersionRange ("[1.2.3, 4.5.6]");
    assertTrue (vr.versionMatches (new Version (2)));
    assertTrue (vr.versionMatches (new Version (2, 99)));
    assertFalse (vr.versionMatches (new Version (1, 2)));
    assertTrue (vr.versionMatches (new Version (4, 5)));
    // check borders
    assertTrue (vr.versionMatches (new Version (1, 2, 3)));
    assertTrue (vr.versionMatches (new Version (1, 2, 3, "alpha")));
    assertTrue (vr.versionMatches (new Version (4, 5, 6)));
    assertFalse (vr.versionMatches (new Version (4, 5, 6, "beta")));

    vr = new VersionRange ("[1.2.3, 4.5.6)");
    assertTrue (vr.versionMatches (new Version (2)));
    assertTrue (vr.versionMatches (new Version (2, 99)));
    assertFalse (vr.versionMatches (new Version (1, 2)));
    assertTrue (vr.versionMatches (new Version (4, 5)));
    // check borders
    assertTrue (vr.versionMatches (new Version (1, 2, 3)));
    assertTrue (vr.versionMatches (new Version (1, 2, 3, "alpha")));
    assertFalse (vr.versionMatches (new Version (4, 5, 6)));
    assertFalse (vr.versionMatches (new Version (4, 5, 6, "beta")));

    vr = new VersionRange ("(1.2.3, 4.5.6]");
    assertTrue (vr.versionMatches (new Version (2)));
    assertTrue (vr.versionMatches (new Version (2, 99)));
    assertFalse (vr.versionMatches (new Version (1, 2)));
    assertTrue (vr.versionMatches (new Version (4, 5)));
    // check borders
    assertFalse (vr.versionMatches (new Version (1, 2, 3)));
    assertTrue (vr.versionMatches (new Version (1, 2, 3, "alpha")));
    assertTrue (vr.versionMatches (new Version (4, 5, 6)));
    assertFalse (vr.versionMatches (new Version (4, 5, 6, "beta")));

    vr = new VersionRange ("(1.2.3, 4.5.6)");
    assertTrue (vr.versionMatches (new Version (2)));
    assertTrue (vr.versionMatches (new Version (2, 99)));
    assertFalse (vr.versionMatches (new Version (1, 2)));
    assertTrue (vr.versionMatches (new Version (4, 5)));
    // check borders
    assertFalse (vr.versionMatches (new Version (1, 2, 3)));
    assertTrue (vr.versionMatches (new Version (1, 2, 3, "alpha")));
    assertFalse (vr.versionMatches (new Version (4, 5, 6)));
    assertFalse (vr.versionMatches (new Version (4, 5, 6, "beta")));
  }

  @Test
  public void testGetAsString ()
  {
    final String [] aTrueTests = new String [] { "[1.2.3]",
                                                "(1.2.3)",
                                                "[1.2)",
                                                "(3.4]",
                                                "[12,12]",
                                                "(1,4]",
                                                "[1.2.3,4.5.6)",
                                                "(47.11.0.alpha,58]",
                                                "[1.2.3,1.2.3]",
                                                VersionRange.DEFAULT_VERSION_RANGE_STRING };
    for (final String element : aTrueTests)
    {
      final VersionRange vr = new VersionRange (element);
      assertEquals (vr.getAsString (), element);
    }
    final String [] aFalseTests = new String [] { " 1", "1.2.3", "[1.2.3, 4.5]", "00" };
    for (final String element : aFalseTests)
    {
      final VersionRange vr = new VersionRange (element);
      assertFalse (vr.getAsString ().equals (element));
    }
  }

  @Test
  public void testGetAsStringPrintZeroElements ()
  {
    final String [] aTrueTests = new String [] { "[1.2.3]",
                                                "(1.2.3)",
                                                "[1.2.0)",
                                                "(3.4.0]",
                                                "[12.0.0,12.0.0]",
                                                "(1.0.0,4.0.0]",
                                                "[1.2.3,4.5.6)",
                                                "(47.11.0.alpha,58.0.0]",
                                                "[1.2.3,1.2.3]",
                                                "[0.0.0)" };
    for (final String element : aTrueTests)
    {
      final VersionRange vr = new VersionRange (element);
      assertEquals (vr.getAsString (true), element);
    }
    final String [] aFalseTests = new String [] { " 1.0.0", "1.2.3", "[1.2.3, 4.5.0]", "00.0.0" };
    for (final String element : aFalseTests)
    {
      final VersionRange vr = new VersionRange (element);
      assertFalse (vr.getAsString (true).equals (element));
    }
  }

  private static int _compare (final String sStr1, final String sStr2)
  {
    return new VersionRange (sStr1).compareTo (new VersionRange (sStr2));
  }

  @Test
  public void testCompareTo ()
  {
    assertEquals (_compare ("[1.2,2.0]", "[1.2,2.0]"), 0);
    assertEquals (_compare ("[1.2,2.0]", "[1.2,2.0)"), +1);
    assertEquals (_compare ("[1.2,2.0]", "(1.2,2.0]"), -1);
    assertEquals (_compare ("[1.2,2.0]", "(1.2,2.0)"), -1);

    assertEquals (_compare ("[1.2,2.0)", "[1.2,2.0]"), -1);
    assertEquals (_compare ("[1.2,2.0)", "[1.2,2.0)"), 0);
    assertEquals (_compare ("[1.2,2.0)", "(1.2,2.0]"), -1);
    assertEquals (_compare ("[1.2,2.0)", "(1.2,2.0)"), -1);

    assertEquals (_compare ("(1.2,2.0]", "[1.2,2.0]"), +1);
    assertEquals (_compare ("(1.2,2.0]", "[1.2,2.0)"), +1);
    assertEquals (_compare ("(1.2,2.0]", "(1.2,2.0]"), 0);
    assertEquals (_compare ("(1.2,2.0]", "(1.2,2.0)"), +1);

    assertEquals (_compare ("(1.2,2.0)", "[1.2,2.0]"), +1);
    assertEquals (_compare ("(1.2,2.0)", "[1.2,2.0)"), +1);
    assertEquals (_compare ("(1.2,2.0)", "(1.2,2.0]"), -1);
    assertEquals (_compare ("(1.2,2.0)", "(1.2,2.0)"), 0);

    assertEquals (_compare ("(1.2,)", "[1.2,]"), +1);
    assertEquals (_compare ("(1.2,)", "[1.2,)"), +1);
    assertEquals (_compare ("(1.2,)", "(1.2,]"), -1);
    assertEquals (_compare ("(1.2,)", "(1.2,)"), 0);

    assertEquals (_compare ("(,2.0)", "[,2.0]"), +1);
    assertEquals (_compare ("(,2.0)", "[,2.0)"), +1);
    assertEquals (_compare ("(,2.0)", "(,2.0]"), -1);
    assertEquals (_compare ("(,2.0)", "(,2.0)"), 0);

    assertEquals (_compare ("(1.2,)", "[1.2,2.0]"), +1);
    assertEquals (_compare ("(1.2,)", "[1.2,2.0)"), +1);
    assertEquals (_compare ("(1.2,)", "(1.2,2.0]"), +1);
    assertEquals (_compare ("(1.2,)", "(1.2,2.0)"), +1);

    assertEquals (_compare ("[1.2,2.0]", "(1.2,)"), -1);
    assertEquals (_compare ("[1.2,2.0)", "(1.2,)"), -1);
    assertEquals (_compare ("(1.2,2.0]", "(1.2,)"), -1);
    assertEquals (_compare ("(1.2,2.0)", "(1.2,)"), -1);

    assertEquals (_compare ("(,2.0)", "[1.2,2.0]"), -1);
    assertEquals (_compare ("(,2.0)", "[1.2,2.0)"), -1);
    assertEquals (_compare ("(,2.0)", "(1.2,2.0]"), -1);
    assertEquals (_compare ("(,2.0)", "(1.2,2.0)"), -1);

    // test single versions
    assertEquals (_compare ("1.2", "1.1"), +1);
    assertEquals (_compare ("1.2", "1.1.9"), +1);
    assertEquals (_compare ("1.2", "1.2"), 0);
    assertEquals (_compare ("1.2", "1.2.0"), 0);
    assertEquals (_compare ("1.2", "1.2.0.alpha"), -1);
    assertEquals (_compare ("1.2", "1.2.1"), -1);
    assertEquals (_compare ("1.2", "1.3"), -1);

    // test qualifier stuff
    assertEquals (_compare ("1.2.0.beta", "1.2.0.alpha"), +1);
    assertEquals (_compare ("1.2.0.beta", "1.2.0.beta"), 0);
    assertEquals (_compare ("1.2.0.beta", "1.2.0.beta1"), -1);
    assertEquals (_compare ("1.2.0.beta", "1.2.0.gamma"), -1);
  }

  private static boolean _eq (final VersionRange vr1, final VersionRange vr2)
  {
    final boolean bEquals = vr1.equals (vr2);
    final boolean bHashcodeEquals = vr1.hashCode () == vr2.hashCode ();
    assertTrue (bEquals == bHashcodeEquals);
    return bEquals;
  }

  @Test
  public void testEquals ()
  {
    final VersionRange vr1 = new VersionRange ("(1.2,2.0]");
    assertTrue (vr1.equals (vr1));
    assertFalse (vr1.equals (null));
    assertFalse (vr1.equals ("Not a VersionRange"));
    assertTrue (_eq (vr1, new VersionRange ("(1.2,2.0]")));
    assertFalse (_eq (vr1, new VersionRange ("[1.2,2.0]")));
    assertFalse (_eq (vr1, new VersionRange ("(1.2,2.0)")));
    assertFalse (_eq (vr1, new VersionRange ("[1.2,2.0)")));
    assertFalse (_eq (vr1, new VersionRange ("(1.2,]")));
    assertFalse (_eq (vr1, new VersionRange ("(,2.0]")));
    assertFalse (_eq (vr1, new VersionRange ("(1.3,2.0]")));
    assertFalse (_eq (vr1, new VersionRange ("(1.2,2.1]")));
    assertFalse (_eq (new VersionRange ("(,2.0]"), new VersionRange ("(,2.1]")));
    assertFalse (_eq (new VersionRange ("(1.2]"), new VersionRange ("(1.3]")));
    assertFalse (_eq (new VersionRange ("(1.2,]"), new VersionRange ("(,2.0]")));
    assertFalse (_eq (new VersionRange ("(,2.0]"), new VersionRange ("(1.2,]")));

    assertTrue (_eq (new VersionRange ("(1.2]"), new VersionRange ("(1.2]")));
    assertFalse (_eq (new VersionRange ("(1.2]"), new VersionRange ("(1.2)")));
  }

  @Test
  public void testSerialization () throws Exception
  {
    PhlocTestUtils.testDefaultSerialization (new VersionRange ("(1.2]"));
    PhlocTestUtils.testDefaultSerialization (new VersionRange ("(0)"));
  }
}
