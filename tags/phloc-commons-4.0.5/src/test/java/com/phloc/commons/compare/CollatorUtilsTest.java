/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.commons.compare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.phloc.commons.concurrent.ManagedExecutorService;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link CollatorUtils}.
 * 
 * @author Philip Helger
 */
public final class CollatorUtilsTest extends AbstractPhlocTestCase
{
  @Test
  public void testGetCollatorSpaceBeforeDot ()
  {
    final Collator c = CollatorUtils.getCollatorSpaceBeforeDot (L_DE);
    assertNotNull (c);
    final Collator c2 = CollatorUtils.getCollatorSpaceBeforeDot (L_DE);
    assertNotNull (c2);
    assertTrue (c != c2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (c, c2);

    // Unknown locale
    assertNotNull (CollatorUtils.getCollatorSpaceBeforeDot (new Locale ("xy", "87")));

    final List <Collator> res = new Vector <Collator> ();
    final int nMax = 100;
    final ExecutorService aES = Executors.newFixedThreadPool (10);
    for (int i = 0; i < nMax; ++i)
      aES.submit (new Runnable ()
      {
        public void run ()
        {
          res.add (CollatorUtils.getCollatorSpaceBeforeDot (L_EN));
        }
      });
    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (aES);
    assertEquals (100, res.size ());
    for (int i = 1; i < nMax; ++i)
      PhlocTestUtils.testDefaultImplementationWithEqualContentObject (res.get (0), res.get (i));
  }

  @Test
  public void testSort ()
  {
    final int nMax = 10000;
    final ExecutorService aES = Executors.newFixedThreadPool (10);
    for (int i = 0; i < nMax; ++i)
    {
      final int nIndex = i;
      aES.submit (new Runnable ()
      {
        public void run ()
        {
          final Collator c = CollatorUtils.getCollatorSpaceBeforeDot ((nIndex % 2) == 0 ? L_DE : L_EN);
          assertEquals (-1, CompareUtils.nullSafeCompare ("1.1 a", "1.1.1 a", c));
        }
      });
    }
    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (aES);
  }
}
