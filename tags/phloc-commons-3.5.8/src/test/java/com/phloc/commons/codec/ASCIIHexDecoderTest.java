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
package com.phloc.commons.codec;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;

/**
 * Test class for class {@link ASCIIHexDecoder}
 * 
 * @author philip
 */
public final class ASCIIHexDecoderTest
{
  @Test
  public void testDecode ()
  {
    final String sEncoded = "616263\n" + "414243>";
    final byte [] aDecoded = new ASCIIHexDecoder ().decode (sEncoded.getBytes (CCharset.CHARSET_US_ASCII_OBJ));
    final String sDecoded = CharsetManager.getAsString (aDecoded, CCharset.CHARSET_US_ASCII_OBJ);
    assertEquals ("abcABC", sDecoded);
  }
}