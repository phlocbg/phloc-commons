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
package com.phloc.commons.url;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link ReadonlySimpleURL}.
 * 
 * @author philip
 */
public final class ReadonlySimpleURLTest
{
  private static void _checkAsString (final String sHref)
  {
    assertEquals (sHref, new ReadonlySimpleURL (sHref).getAsString ());
  }

  @Test
  public void testAsString ()
  {
    _checkAsString ("http://www.phloc.com");
    _checkAsString ("http://www.phloc.com/directory");
    _checkAsString ("http://www.phloc.com/#anchor");
    _checkAsString ("http://www.phloc.com/?x=y");
    _checkAsString ("http://www.phloc.com/?x=y#anchor");
    _checkAsString ("http://www.phloc.com/?x=y&ab=cd");
    _checkAsString ("/?x=y&ab=cd");
    _checkAsString ("http://www.phloc.com/?this&that&thatalso");
    _checkAsString ("?this&that&thatalso");
    _checkAsString ("http://www.phloc.com/?upper=LOWER&äöü=aou");
    _checkAsString ("http://www.phloc.com/?upper=LOWER&äöü=aou#anchor");
    _checkAsString ("?upper=LOWER&äöü=aou");
    _checkAsString ("http://www.phloc.com/;jsessionid=1234");
    _checkAsString ("http://www.phloc.com/folder/;jsessionid=1234");
    _checkAsString ("http://www.phloc.com/folder/;jsessionid=1234?x=y&z=z");

    // asString results in slightly different but semantically equivalent URLs
    assertEquals ("http://www.phloc.com/", new ReadonlySimpleURL ("http://www.phloc.com/?").getAsString ());
    assertEquals ("http://www.phloc.com/#anchor",
                  new ReadonlySimpleURL ("http://www.phloc.com/?#anchor").getAsString ());
  }

  private static void _checkAsEncodedString (final String sHref)
  {
    _checkAsEncodedString (sHref, sHref);
  }

  private static void _checkAsEncodedString (final String sHref, final String sEncodedHref)
  {
    assertEquals (sEncodedHref, new ReadonlySimpleURL (sHref).getAsStringWithEncodedParameters ());
  }

  @Test
  public void testAsEncodedString ()
  {
    _checkAsEncodedString ("http://www.phloc.com");
    _checkAsEncodedString ("http://www.phloc.com/directory");
    _checkAsEncodedString ("http://www.phloc.com/#anchor");
    _checkAsEncodedString ("http://www.phloc.com/?#anchor", "http://www.phloc.com/#anchor");
    _checkAsEncodedString ("http://www.phloc.com/?x=y");
    _checkAsEncodedString ("http://www.phloc.com/?x=y#anchor");
    _checkAsEncodedString ("http://www.phloc.com/?x=y&ab=cd");
    _checkAsEncodedString ("/?x=y&ab=cd");
    _checkAsEncodedString ("http://www.phloc.com/?this&that&thatalso");
    _checkAsEncodedString ("?this&that&thatalso");
    _checkAsEncodedString ("http://www.phloc.com/?upper=LOWER&äöü=aou",
                           "http://www.phloc.com/?upper=LOWER&%C3%A4%C3%B6%C3%BC=aou");
    _checkAsEncodedString ("http://www.phloc.com/?upper=LOWER&äöü=aou#anchor",
                           "http://www.phloc.com/?upper=LOWER&%C3%A4%C3%B6%C3%BC=aou#anchor");
    _checkAsEncodedString ("?upper=LOWER&äöü=aou", "?upper=LOWER&%C3%A4%C3%B6%C3%BC=aou");
    _checkAsEncodedString ("http://www.phloc.com/;jsessionid=1234");
    _checkAsEncodedString ("http://www.phloc.com/folder/;jsessionid=1234");
    _checkAsEncodedString ("http://www.phloc.com/folder/;jsessionid=1234?x=y&z=z");

    // asString results in slightly different but semantically equivalent URLs
    assertEquals ("http://www.phloc.com/",
                  new ReadonlySimpleURL ("http://www.phloc.com/?").getAsStringWithEncodedParameters ());
  }

  @Test
  public void testCtor ()
  {
    // only href
    ISimpleURL aURL = new ReadonlySimpleURL ("http://www.phloc.com");
    assertEquals ("http://www.phloc.com", aURL.getAsString ());

    // params
    // 1. default
    aURL = new ReadonlySimpleURL ("http://www.phloc.com", new SMap ("a", "b"));
    assertEquals ("http://www.phloc.com?a=b", aURL.getAsString ());
    // 2. plus params in href
    aURL = new ReadonlySimpleURL ("http://www.phloc.com?x=y", new SMap ("a", "b"));
    assertEquals ("http://www.phloc.com?x=y&a=b", aURL.getAsString ());
    // 3. overwrite parameter in href
    aURL = new ReadonlySimpleURL ("http://www.phloc.com?a=a", new SMap ("a", "b"));
    assertEquals ("http://www.phloc.com?a=b", aURL.getAsString ());

    // anchor
    // 1. default
    aURL = new ReadonlySimpleURL ("http://www.phloc.com", new SMap ("a", "b"), "root");
    assertEquals ("http://www.phloc.com?a=b#root", aURL.getAsString ());
    // 2. overwrite anchor
    aURL = new ReadonlySimpleURL ("http://www.phloc.com#main", new SMap ("a", "b"), "root");
    assertEquals ("http://www.phloc.com?a=b#root", aURL.getAsString ());
    // 3. only anchor in href
    aURL = new ReadonlySimpleURL ("http://www.phloc.com#main", new SMap ("a", "b"));
    assertEquals ("http://www.phloc.com?a=b#main", aURL.getAsString ());
  }
}
