/**
 * Copyright (C) 2006-2014 phloc systems
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

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link WrappedOutputStream}.
 * 
 * @author Philip Helger
 */
public final class WrappedOutputStreamTest
{
  @Test
  public void testAll () throws IOException
  {
    final NonBlockingByteArrayOutputStream baos = new NonBlockingByteArrayOutputStream ();
    final WrappedOutputStream ws = new WrappedOutputStream (baos);
    ws.write ('a');
    ws.write (CharsetManager.getAsBytes ("bc", CCharset.CHARSET_ISO_8859_1_OBJ));
    ws.write (CharsetManager.getAsBytes ("cde", CCharset.CHARSET_ISO_8859_1_OBJ), 1, 1);
    ws.flush ();
    ws.close ();
    assertEquals ("abcd", baos.getAsString (CCharset.CHARSET_ISO_8859_1_OBJ));
    PhlocTestUtils.testToStringImplementation (ws);

    try
    {
      new WrappedOutputStream (null).close ();
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
