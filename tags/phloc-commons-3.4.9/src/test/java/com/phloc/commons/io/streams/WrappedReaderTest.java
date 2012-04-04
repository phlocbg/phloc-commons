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
package com.phloc.commons.io.streams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.string.StringHelper;

/**
 * Test class for class {@link WrappedReader}.
 *
 * @author philip
 */
public final class WrappedReaderTest
{
  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testAll () throws IOException
  {
    final StringReader baos = new StringReader (StringHelper.getRepeated ('a', 100));
    final WrappedReader ws = new WrappedReader (baos);
    assertTrue (ws.markSupported ());
    assertTrue (ws.ready ());
    ws.mark (0);
    ws.read ();
    assertEquals (4, ws.read (new char [4]));
    assertEquals (1, ws.read (new char [5], 1, 1));
    ws.read (CharBuffer.allocate (1));
    assertEquals (4, ws.skip (4));
    assertEquals (89, ws.skip (100));
    ws.reset ();
    assertEquals (100, ws.skip (100));
    ws.close ();
    PhlocTestUtils.testToStringImplementation (ws);

    WrappedReader aReader = null;
    try
    {
      aReader = new WrappedReader (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    finally
    {
      if (aReader != null)
        aReader.close ();
    }
  }
}
