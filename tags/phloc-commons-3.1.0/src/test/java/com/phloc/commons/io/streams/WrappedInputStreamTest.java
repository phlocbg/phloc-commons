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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link WrappedInputStream}.
 * 
 * @author philip
 */
public final class WrappedInputStreamTest
{
  @Test
  public void testAll () throws IOException
  {
    final NonBlockingByteArrayInputStream baos = new NonBlockingByteArrayInputStream (new byte [100]);
    final WrappedInputStream ws = new WrappedInputStream (baos);
    assertTrue (ws.markSupported ());
    assertEquals (100, ws.available ());
    ws.mark (0);
    ws.read ();
    ws.read (new byte [4]);
    ws.read (new byte [5], 1, 1);
    ws.skip (4);
    assertEquals (90, ws.available ());
    ws.reset ();
    assertEquals (100, ws.available ());
    ws.close ();
    PhlocTestUtils.testToStringImplementation (ws);

    try
    {
      new WrappedInputStream (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
