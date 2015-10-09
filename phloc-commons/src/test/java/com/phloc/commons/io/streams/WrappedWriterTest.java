/**
 * Copyright (C) 2006-2015 phloc systems
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

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link WrappedWriter}.
 * 
 * @author Philip Helger
 */
public final class WrappedWriterTest
{
  @Test
  public void testAll () throws IOException
  {
    final NonBlockingStringWriter aSW = new NonBlockingStringWriter ();
    final WrappedWriter ws = new WrappedWriter (aSW);
    ws.write ('a');
    ws.write ("bc".toCharArray ());
    ws.write ("de".toCharArray (), 0, 1);
    ws.write ("ef");
    ws.write ("fgh", 1, 1);
    assertEquals ("abcdefg", aSW.getAsString ());
    ws.append ('0').append ("12").append ("234", 1, 2);
    assertEquals ("abcdefg0123", aSW.getAsString ());
    ws.flush ();
    ws.close ();
    PhlocTestUtils.testToStringImplementation (ws);

    try
    {
      new WrappedWriter (null).close ();
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
