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
package com.phloc.test.java;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FuncTestJavaListAllFonts
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FuncTestJavaListAllFonts.class);

  @Test
  public void testGetAllFonts ()
  {
    for (final Font aFont : GraphicsEnvironment.getLocalGraphicsEnvironment ().getAllFonts ())
      s_aLogger.info (aFont.toString ());
  }
}
