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


/**
 * Assorted utility functions (sort of a kitchen sink). Some methods used but
 * core to Actor implementation. Additional unused methods may be included and
 * should be ignored.
 * 
 * @author bfeigenb
 */
public class Utils
{
  /** Safely implement sleep(). */
  public static void sleep (final long millis)
  {
    if (millis >= 0)
    {
      try
      {
        Thread.sleep (millis);
      }
      catch (final InterruptedException e)
      {
        // e.printStackTrace(System.out);
      }
    }
  }
}
