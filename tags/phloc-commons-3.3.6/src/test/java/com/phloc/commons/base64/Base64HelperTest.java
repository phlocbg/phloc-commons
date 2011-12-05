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
package com.phloc.commons.base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;

/**
 * Test class for class {@link Base64Helper}.
 * 
 * @author philip
 */
public final class Base64HelperTest
{
  @Test
  public void testAll ()
  {
    final String sSource = "dgMP$";
    final String sEncoded = Base64Helper.safeEncode (sSource, CCharset.CHARSET_ISO_8859_1);
    assertTrue (Arrays.equals (sSource.getBytes (), Base64Helper.safeDecode (sEncoded)));
    assertTrue (Arrays.equals (sSource.getBytes (), Base64Helper.safeDecode (sEncoded.getBytes ())));
    assertEquals (sSource, Base64Helper.safeDecodeAsString (sEncoded, CCharset.CHARSET_ISO_8859_1));
    assertEquals (sSource, Base64Helper.safeDecodeAsString (sEncoded.getBytes (), CCharset.CHARSET_ISO_8859_1));

    // Invalid input
    assertNull (Base64Helper.safeDecode ("xyz"));
  }
}
