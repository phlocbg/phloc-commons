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
package com.phloc.commons.io.file.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Test;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test class for class {@link FilenameFilterEqualsIgnoreCase}.
 *
 * @author philip
 */
public final class FilenameFilterEqualsIgnoreCaseTest
{
  @Test
  @SuppressFBWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testAll ()
  {
    try
    {
      // null not allowed
      new FilenameFilterEqualsIgnoreCase (null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final FilenameFilter ff = new FilenameFilterEqualsIgnoreCase ("file.htm");
    assertNotNull (ff);
    assertTrue (ff.accept (null, "file.htm"));
    assertTrue (ff.accept (new File ("dir"), "file.htm"));
    assertTrue (ff.accept (null, "FILE.HTM"));
    assertTrue (ff.accept (new File ("dir"), "FILE.HTM"));
    assertFalse (ff.accept (null, "hello.html"));
    assertFalse (ff.accept (new File ("dir"), "hello.html"));
    assertFalse (ff.accept (null, "HELLO.HTML"));
    assertFalse (ff.accept (new File ("dir"), "HELLO.HTML"));
    assertFalse (ff.accept (null, null));
    assertFalse (ff.accept (null, ""));
  }
}
