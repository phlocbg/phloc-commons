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

import org.junit.Test;

import com.phloc.commons.filter.FilterChainAND;
import com.phloc.commons.filter.FilterChainOR;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.mock.PhlocTestUtils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test class for class {@link FileFilterToIFilterAdapter}.
 *
 * @author Philip Helger
 */
public final class FileFilterToIFilterAdapterTest
{
  @Test
  @SuppressFBWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testAll ()
  {
    IFilter <File> aFilter = FileFilters.getParentDirectoryPublic ();

    // file
    assertTrue (aFilter.matchesFilter (new File ("pom.xml")));
    // not existing file
    assertTrue (aFilter.matchesFilter (new File ("file.htm")));
    // directory
    assertTrue (aFilter.matchesFilter (new File ("src")));
    // null
    assertFalse (aFilter.matchesFilter (null));

    PhlocTestUtils.testToStringImplementation (aFilter);

    aFilter = new FilterChainAND <> (FileFilters.getParentDirectoryPublic (), FileFilters.getFileOnly ());
    assertNotNull (aFilter);

    // file
    assertTrue (aFilter.matchesFilter (new File ("pom.xml")));
    // not existing file
    assertFalse (aFilter.matchesFilter (new File ("file.htm")));
    // directory
    assertFalse (aFilter.matchesFilter (new File ("src")));
    // null
    assertFalse (aFilter.matchesFilter (null));

    PhlocTestUtils.testToStringImplementation (aFilter);

    aFilter = new FilterChainOR <> (FileFilters.getParentDirectoryPublic (), FileFilters.getFileOnly ());
    assertNotNull (aFilter);

    // file
    assertTrue (aFilter.matchesFilter (new File ("pom.xml")));
    // not existing file
    assertTrue (aFilter.matchesFilter (new File ("file.htm")));
    // directory
    assertTrue (aFilter.matchesFilter (new File ("src")));
    // null
    assertFalse (aFilter.matchesFilter (null));

    PhlocTestUtils.testToStringImplementation (aFilter);
  }
}
