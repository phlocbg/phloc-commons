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
package com.phloc.commons;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link CGlobal}.
 * 
 * @author philip
 */
public final class CGlobalTest
{
  @Test
  public void testConstants ()
  {
    assertEquals (1024L, CGlobal.BYTES_PER_KILOBYTE);
    assertEquals (1024L * 1024, CGlobal.BYTES_PER_MEGABYTE);
    assertEquals (1024L * 1024 * 1024, CGlobal.BYTES_PER_GIGABYTE);
    assertEquals (1024L * 1024 * 1024 * 1024, CGlobal.BYTES_PER_TERABYTE);
    assertEquals (1024L * 1024 * 1024 * 1024 * 1024, CGlobal.BYTES_PER_PETABYTE);
  }
}
