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
package com.phloc.commons.io.streamprovider;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.OutputStream;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.streams.NonBlockingStringReader;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.io.streams.StringInputStream;

/**
 * Test class for class {@link ByteArrayOutputStreamProvider}.
 * 
 * @author Philip Helger
 */
public final class ByteArrayOutputStreamProviderTest
{
  @Test
  public void testSimple ()
  {
    final ByteArrayOutputStreamProvider aOSP = new ByteArrayOutputStreamProvider ();
    final OutputStream aOS = aOSP.getOutputStream (EAppend.DEFAULT);
    assertNotNull (aOS);
    StreamUtils.copyInputStreamToOutputStreamAndCloseOS (new StringInputStream ("Hiho", CCharset.CHARSET_ISO_8859_1_OBJ),
                                                         aOS);
    assertEquals ("Hiho", aOSP.getAsString (CCharset.CHARSET_ISO_8859_1_OBJ));
    assertArrayEquals (CharsetManager.getAsBytes ("Hiho", CCharset.CHARSET_ISO_8859_1_OBJ), aOSP.getBytes ());
    // Close the underlying OS
    StreamUtils.close (aOSP.getWriter (CCharset.CHARSET_UTF_8_OBJ, EAppend.DEFAULT));

    // Reader/Writer
    StreamUtils.copyReaderToWriterAndCloseWriter (new NonBlockingStringReader ("Hiho"),
                                                  aOSP.getWriter (CCharset.CHARSET_UTF_8_OBJ, EAppend.DEFAULT));
    assertEquals ("Hiho", aOSP.getAsString (CCharset.CHARSET_UTF_8_OBJ));
  }
}
