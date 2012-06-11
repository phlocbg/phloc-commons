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
package com.phloc.commons.actor.experimental;

import java.math.BigInteger;

/**
 * A MapReducer that calculates the sum of squares of a list of integers.
 * 
 * @author BFEIGENB
 */
public class SumOfSquaresReducer implements MapReduceer
{
  @Override
  public void map (final Object [] values, final int start, final int end)
  {
    for (int i = start; i <= end; i++)
    {
      values[i] = ((BigInteger) values[i]).multiply ((BigInteger) values[i]);
      sleep (200); // fake taking time
    }
  }

  @Override
  public void reduce (final Object [] values, final int start, final int end, final Object [] target, final int posn)
  {
    BigInteger res = new BigInteger ("0");
    for (int i = start; i <= end; i++)
    {
      res = res.add ((BigInteger) values[i]);
      sleep (100); // fake taking time
    }
    target[posn] = res;
  }

  private void sleep (final int millis)
  {
    try
    {
      Thread.sleep (millis);
    }
    catch (final InterruptedException e)
    {}
  }
}
