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
package com.phloc.test.java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;

public final class FuncTestJavaCharToLowercase extends AbstractPhlocTestCase
{
  @Test
  public void testAllCharsets ()
  {
    for (int c = Character.MIN_VALUE; c <= Character.MAX_VALUE; ++c)
    {
      final char cUp = Character.toString ((char) c).toUpperCase (L_DE).charAt (0);
      if (c == 'a')
        assertEquals ('A', cUp);

      if (cUp != c)
        if (false)
          m_aLogger.info ("c(" + (char) c + ") ==> (" + cUp + ")");
    }
  }
}
