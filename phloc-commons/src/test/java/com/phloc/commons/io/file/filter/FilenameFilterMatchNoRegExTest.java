/**
 * Copyright (C) 2006-2014 phloc systems
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
 * Test class for class {@link FilenameFilterMatchNoRegEx}.
 *
 * @author Philip Helger
 */
public final class FilenameFilterMatchNoRegExTest
{
  @Test
  @SuppressFBWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testAll ()
  {
    try
    {
      // null not allowed
      new FilenameFilterMatchNoRegEx ((String []) null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final FilenameFilter ff = new FilenameFilterMatchNoRegEx (".*html$");
    assertNotNull (ff);
    assertTrue (ff.accept (null, "file.htm"));
    assertTrue (ff.accept (new File ("dir"), "file.htm"));
    assertFalse (ff.accept (null, "file.html"));
    assertFalse (ff.accept (new File ("dir"), "file.html"));
    assertFalse (ff.accept (null, null));
    assertTrue (ff.accept (null, ""));
  }
}
