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
package com.phloc.commons.supplementary.test.java;

import java.nio.charset.Charset;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FuncTestJavaCharset
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FuncTestJavaCharset.class);

  @Test
  public void testAllCharsets ()
  {
    for (final Map.Entry <String, Charset> aEntry : Charset.availableCharsets ().entrySet ())
      s_aLogger.info (aEntry.getKey () + " " + aEntry.getValue ().aliases ());
  }

  @Test
  public void testAllCharsetNamess ()
  {
    s_aLogger.info (Charset.availableCharsets ().keySet ().toString ());
  }
}
