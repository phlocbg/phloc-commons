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
package com.phloc.commons.io.streams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;

/**
 * Test class for class {@link CountingOutputStream}.
 * 
 * @author Philip Helger
 */
public final class CountingOutputStreamTest
{
  @Test
  public void testAll () throws IOException
  {
    final String sTestString = "test 123 - This counts!";
    final CountingOutputStream aCOS = new CountingOutputStream (new NonBlockingByteArrayOutputStream ());
    StreamUtils.copyInputStreamToOutputStream (new NonBlockingByteArrayInputStream (CharsetManager.getAsBytes (sTestString,
                                                                                                               CCharset.CHARSET_ISO_8859_1_OBJ)),
                                               aCOS);
    assertEquals (sTestString.length (), aCOS.getBytesWritten ());
    aCOS.write (5);
    aCOS.write (new byte [] { 1, 2, 3 });
    assertNotNull (aCOS.toString ());
  }
}
