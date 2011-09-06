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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public final class FuncTestJavaBigDecimal
{
  @Test
  public void testDivide ()
  {
    final BigDecimal aBD100 = new BigDecimal (100);
    final BigDecimal aPerc = new BigDecimal ("20.0");
    try
    {
      aBD100.divide (aBD100.add (aPerc));
      fail ();
    }
    catch (final ArithmeticException ex)
    {}
    assertNotNull (aBD100.divide (aBD100.add (aPerc), 2, RoundingMode.HALF_UP));
  }
}
