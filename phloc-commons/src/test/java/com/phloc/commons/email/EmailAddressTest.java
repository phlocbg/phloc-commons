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
package com.phloc.commons.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link EmailAddress}.
 * 
 * @author Philip Helger
 */
public final class EmailAddressTest
{
  @Test
  public void testBasic ()
  {
    try
    {
      new EmailAddress ((String) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      new EmailAddress ("haha-no-email-address");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    IEmailAddress aMA = new EmailAddress ("ph@example.org");
    assertEquals ("ph@example.org", aMA.getAddress ());
    assertNull (aMA.getPersonal ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aMA, new EmailAddress ("ph@example.org"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aMA, new EmailAddress ("ph@example2.org"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aMA,
                                                                        new EmailAddress ("ph@example.org", "My name"));
    PhlocTestUtils.testDefaultSerialization (new EmailAddress ("ph@example.org", "My name"));

    aMA = new EmailAddress ("ph@example.org", "Philip");
    assertEquals ("ph@example.org", aMA.getAddress ());
    assertEquals ("Philip", aMA.getPersonal ());
  }

  @Test
  public void testValidate ()
  {
    Assert.assertTrue (EmailAddressUtils.isValid ("bg4711@phloc.com"));
    Assert.assertFalse (EmailAddressUtils.isValid ("aÄbÖc@öüä.cöm"));
    Assert.assertFalse (EmailAddressUtils.isValid ("aÄbÖc@öü@ä.cöm"));

    Assert.assertTrue (EmailAddressUtils.isValid ("bg4711@phloc.com", true));
    Assert.assertTrue (EmailAddressUtils.isValid ("aÄbÖc@öüä.cöm", true));
    Assert.assertFalse (EmailAddressUtils.isValid ("aÄbÖc@öü@ä.cöm", true));

    EmailAddressUtils.setDefaultAllowInternationalized (true);
    Assert.assertTrue (EmailAddressUtils.isValid ("bg4711@phloc.com"));
    Assert.assertTrue (EmailAddressUtils.isValid ("aÄbÖc@öüä.cöm"));
    Assert.assertFalse (EmailAddressUtils.isValid ("aÄbÖc@öü@ä.cöm"));

    EmailAddressUtils.setDefaultAllowInternationalized (false);
  }

}
