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

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link FileFilterFromFilenameFilter}.
 * 
 * @author Philip Helger
 */
public final class FileFilterFromFilenameFilterTest
{
  @Test
  public void testGetFileNameFilter ()
  {
    final FilenameFilter ff = new FilenameFilterMatchAnyRegEx (".*\\.xml$", ".*\\.htm$", "src");
    final FileFilter aFilter = new FileFilterFromFilenameFilter (ff);
    assertNotNull (aFilter);

    // file
    assertTrue (aFilter.accept (new File ("pom.xml")));
    // not existing file
    assertTrue (aFilter.accept (new File ("file.htm")));
    // not existing file not matching regex
    assertFalse (aFilter.accept (new File ("file.html")));
    // directory
    assertTrue (aFilter.accept (new File ("src")));
    // null
    assertFalse (aFilter.accept (null));

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aFilter, new FileFilterFromFilenameFilter (ff));
  }
}
