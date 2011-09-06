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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

import org.junit.Test;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for {@link RegExPool}.
 * 
 * @author philip
 */
public final class RegExPoolTest extends AbstractPhlocTestCase
{
  /**
   * Test method getPattern
   */
  @Test
  public void testGetPattern ()
  {
    assertNotNull (RegExPool.getPattern ("xy"));
    assertSame (RegExPool.getPattern ("xy"), RegExPool.getPattern ("xy"));
    assertNotSame (RegExPool.getPattern ("xy"), RegExPool.getPattern ("yy"));

    try
    {
      // empty string not allowed
      RegExPool.getPattern ("");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null not allowed
      RegExPool.getPattern (null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // Illegal regular expression
      RegExPool.getPattern ("[a-z+");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {
      assertNotNull (ex.getCause ());
      assertTrue (ex.getCause () instanceof PatternSyntaxException);
    }

    if (GlobalDebug.isDebugMode ())
    {
      assertNotNull (RegExPool.getPattern ("any\\$here"));
      try
      {
        // RegEx contains unquoted $!
        RegExPool.getPattern ("any$here");
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }
  }

  /**
   * Test method getMatcher
   */
  @Test
  public void testGetMatcher ()
  {
    final Matcher m = RegExHelper.getMatcher ("Hallo (\\d+)\\.(\\d+) Welt", "This is Hallo 2.3 Welt Text");
    assertNotNull (m);
    assertEquals (2, m.groupCount ());

    // start over
    m.reset ();
    final StringBuffer sb = new StringBuffer ();
    while (m.find ())
    {
      // Get the match result
      String replaceStr = m.group ();
      assertEquals ("Hallo 2.3 Welt", replaceStr);

      // Convert to uppercase
      replaceStr = "<$1.$2>";

      // Insert replacement
      m.appendReplacement (sb, replaceStr);
    }
    m.appendTail (sb);
    assertEquals ("This is <2.3> Text", sb.toString ());

    // easy cheesy
    assertEquals ("This is <2.3> Text", m.replaceAll ("<$1.$2>"));
  }
}
