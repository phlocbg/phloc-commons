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
package com.phloc.commons.io.streams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

/**
 * Test class for class {@link NonBlockingStringWriter}.
 * 
 * @author philip
 */
public final class NonBlockingStringWriterTest
{
  @Test
  public void testAll () throws IOException
  {
    final NonBlockingStringWriter ws = new NonBlockingStringWriter ();
    ws.write ('a');
    ws.write ("bc".toCharArray ());
    ws.write ("de".toCharArray (), 0, 1);
    ws.write ("ef");
    ws.write ("fgh", 1, 1);
    assertEquals ("abcdefg", ws.toString ());
    ws.append ('0').append ("12").append ("234", 1, 2);
    assertEquals ("abcdefg0123", ws.toString ());
    assertEquals ("abcdefg0123", ws.getBuffer ().toString ());
    ws.append (null).append (null, 1, 2);
    assertEquals ("abcdefg0123nullu", ws.toString ());
    ws.flush ();
    ws.close ();

    try
    {
      new NonBlockingStringWriter (-1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final char [] ca = "abc".toCharArray ();
    try
    {
      ws.write (ca, -1, 1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      ws.write (ca, 0, -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      ws.write (ca, 2, 5);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
