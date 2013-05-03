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
package com.phloc.commons.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.mock.MockException;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link LogMessage}.
 * 
 * @author Philip Helger
 */
public final class LogMessageTest
{
  @Test
  public void testAll ()
  {
    final LogMessage lm = new LogMessage (EErrorLevel.WARN, "Msg", new MockException ());
    assertTrue (lm.getIssueDateTime ().getTime () <= new Date ().getTime ());
    assertSame (EErrorLevel.WARN, lm.getErrorLevel ());
    assertEquals ("Msg", lm.getMessage ());
    assertNotNull (lm.getThrowable ());
    PhlocTestUtils.testToStringImplementation (lm);

    try
    {
      new LogMessage (null, "Msg", new MockException ());
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new LogMessage (EErrorLevel.WARN, null, new MockException ());
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
