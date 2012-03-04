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
package com.phloc.test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

import com.phloc.commons.CGlobal;

public final class FuncTestJavaBigDecimal
{
  @Test
  public void testDivide ()
  {
    final BigDecimal aBD100 = CGlobal.BIGDEC_100;
    final BigDecimal aPerc = new BigDecimal ("20.0");
    try
    {
      // 100/120 -> indefinite precision
      final BigDecimal aResult = aBD100.divide (aBD100.add (aPerc));
      fail ();
      assertNull (aResult);
    }
    catch (final ArithmeticException ex)
    {}
    assertNotNull (aBD100.divide (aBD100.add (aPerc), 2, RoundingMode.HALF_UP));

    // compareTo with different scale
    assertEquals (0, new BigDecimal ("20").compareTo (new BigDecimal ("20.0")));

    // equals with different scale
    assertFalse (new BigDecimal ("20").equals (new BigDecimal ("20.0")));
    assertTrue (new BigDecimal ("20").setScale (1).equals (new BigDecimal ("20.0")));
  }
}
