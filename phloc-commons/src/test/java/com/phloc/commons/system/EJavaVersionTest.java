/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link EJavaVersion}.,
 * 
 * @author Philip Helger
 */
public final class EJavaVersionTest
{
  @Test
  public void testBasic ()
  {
    for (final EJavaVersion e : EJavaVersion.values ())
      assertSame (e, EJavaVersion.valueOf (e.name ()));

    assertEquals (EJavaVersion.UNKNOWN, EJavaVersion.getFromVersionNumber (44.0));
    assertEquals (EJavaVersion.JDK_15, EJavaVersion.getFromMajorAndMinor (49, 0));
    assertEquals (EJavaVersion.JDK_15, EJavaVersion.getFromVersionNumber (49.0));
    assertEquals (EJavaVersion.JDK_16, EJavaVersion.getFromMajorAndMinor (50, 0));
    assertEquals (EJavaVersion.JDK_16, EJavaVersion.getFromVersionNumber (50.0));
    assertEquals (EJavaVersion.JDK_17, EJavaVersion.getFromVersionNumber (51.0));
    assertEquals (EJavaVersion.JDK_18, EJavaVersion.getFromVersionNumber (52.0));
    assertEquals (EJavaVersion.JDK17, EJavaVersion.getFromVersionNumber (61.0));

    assertTrue (EJavaVersion.JDK_11.isSupportedVersion ());
    assertTrue (EJavaVersion.JDK_12.isSupportedVersion ());
    assertTrue (EJavaVersion.JDK_13.isSupportedVersion ());
    assertTrue (EJavaVersion.JDK_14.isSupportedVersion ());
    assertTrue (EJavaVersion.JDK_15.isSupportedVersion ());
  }

  @Test
  public void testIsNewerOrEqualsThan ()
  {
    assertTrue (EJavaVersion.JDK_11.isNewerOrEqualsThan (EJavaVersion.JDK_11));
    assertFalse (EJavaVersion.JDK_11.isNewerOrEqualsThan (EJavaVersion.JDK_12));
    assertFalse (EJavaVersion.JDK_11.isNewerOrEqualsThan (EJavaVersion.JDK_17));

    assertTrue (EJavaVersion.JDK_17.isNewerOrEqualsThan (EJavaVersion.JDK_11));
    assertTrue (EJavaVersion.JDK_17.isNewerOrEqualsThan (EJavaVersion.JDK_16));
    assertTrue (EJavaVersion.JDK_17.isNewerOrEqualsThan (EJavaVersion.JDK_17));
  }
}
