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

import java.text.MessageFormat;

import org.junit.Test;

public final class FuncTestJavaMessageFormat
{
  @Test
  public void testBasicFormat ()
  {
    assertEquals ("Hallo Welt!", MessageFormat.format ("Hallo {0}!", "Welt"));
    assertEquals ("Hallo Welt!", MessageFormat.format ("{1} {0}!", "Welt", "Hallo"));
    assertEquals ("Hallo 17!", MessageFormat.format ("Hallo {0}!", Integer.valueOf (17)));
  }
}
