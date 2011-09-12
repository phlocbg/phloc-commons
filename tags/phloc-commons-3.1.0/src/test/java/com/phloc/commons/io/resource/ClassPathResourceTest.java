/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.io.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link ClassPathResource}.
 * 
 * @author philip
 */
public final class ClassPathResourceTest
{
  @Test
  public void testValidRoot () throws IOException
  {
    assertTrue (ClassPathResource.isExplicitClassPathResource ("cp:x"));
    assertTrue (ClassPathResource.isExplicitClassPathResource ("classpath:x"));
    assertFalse (ClassPathResource.isExplicitClassPathResource ("cpx"));
    assertFalse (ClassPathResource.isExplicitClassPathResource ("classpathx"));
    assertFalse (ClassPathResource.isExplicitClassPathResource ("wtf"));
    assertFalse (ClassPathResource.isExplicitClassPathResource (null));

    final ClassPathResource aCPISP = new ClassPathResource ("/test1.txt");
    assertTrue (aCPISP.exists ());
    assertTrue (aCPISP.existsNoCacheUsage ());
    assertTrue (aCPISP.canRead ());
    assertNotNull (aCPISP.getReadableCloneForPath ("folder/test3-surely not existing.txt"));
    assertNotNull (aCPISP.getAsFile ());
    assertNotNull (aCPISP.getAsURL ());
    assertEquals ("/test1.txt", aCPISP.getPath ());
    assertNotNull (aCPISP.getResourceID ());

    final InputStream aIS1 = aCPISP.getInputStream ();
    assertNotNull (aIS1);
    try
    {
      final InputStream aIS2 = aCPISP.getInputStream ();
      assertNotNull (aIS2);
      try
      {
        assertNotSame (aIS1, aIS2);
      }
      finally
      {
        StreamUtils.close (aIS2);
      }
    }
    finally
    {
      StreamUtils.close (aIS1);
    }

    aCPISP.getReader (CCharset.CHARSET_ISO_8859_1).close ();
    assertNull (new ClassPathResource ("/test1 not existing.txt").getReader (CCharset.CHARSET_ISO_8859_1));
    assertFalse (new ClassPathResource ("/test1 not existing.txt").existsNoCacheUsage ());
    assertFalse (new ClassPathResource ("/test1 not existing.txt").canRead ());
    assertNotNull (ClassPathResource.getAsFile ("/test1.txt"));
    assertNotNull (ClassPathResource.getAsFile ("/test1 not existing.txt"));
    assertTrue (ClassPathResource.canRead ("/test1.txt"));
    assertFalse (ClassPathResource.canRead ("/test1 not existing.txt"));
  }

  @Test
  public void testValidRootWithPrefixLong ()
  {
    final ClassPathResource aCPISP = new ClassPathResource ("classpath:test1.txt");
    assertTrue (aCPISP.exists ());
    assertNotNull (aCPISP.getReadableCloneForPath ("folder/test3-surely not existing.txt"));
    assertNotNull (aCPISP.getAsFile ());
    assertNotNull (aCPISP.getAsURL ());
    assertEquals ("test1.txt", aCPISP.getPath ());
    assertNotNull (aCPISP.getResourceID ());

    final InputStream aIS1 = aCPISP.getInputStream ();
    assertNotNull (aIS1);
    try
    {
      final InputStream aIS2 = aCPISP.getInputStream ();
      assertNotNull (aIS2);
      try
      {
        assertNotSame (aIS1, aIS2);
      }
      finally
      {
        StreamUtils.close (aIS2);
      }
    }
    finally
    {
      StreamUtils.close (aIS1);
    }
  }

  @Test
  public void testValidRootWithPrefixShort ()
  {
    final ClassPathResource aCPISP = new ClassPathResource ("cp:test1.txt");
    assertTrue (aCPISP.exists ());
    assertNotNull (aCPISP.getReadableCloneForPath ("folder/test3-surely not existing.txt"));
    assertNotNull (aCPISP.getAsFile ());
    assertNotNull (aCPISP.getAsURL ());
    assertEquals ("test1.txt", aCPISP.getPath ());
    assertNotNull (aCPISP.getResourceID ());

    final InputStream aIS1 = aCPISP.getInputStream ();
    assertNotNull (aIS1);
    try
    {
      final InputStream aIS2 = aCPISP.getInputStream ();
      assertNotNull (aIS2);
      try
      {
        assertNotSame (aIS1, aIS2);
      }
      finally
      {
        StreamUtils.close (aIS2);
      }
    }
    finally
    {
      StreamUtils.close (aIS1);
    }
  }

  @Test
  public void testValidFolder ()
  {
    final ClassPathResource aCPISP = new ClassPathResource ("folder/test2.txt");
    assertTrue (aCPISP.exists ());
    assertNotNull (aCPISP.getReadableCloneForPath ("folder/test3-surely not existing.txt"));
    assertNotNull (aCPISP.getAsFile ());
    assertNotNull (aCPISP.getAsURL ());
    assertEquals ("folder/test2.txt", aCPISP.getPath ());
    assertNotNull (aCPISP.getResourceID ());
    final InputStream aIS1 = aCPISP.getInputStream ();
    assertNotNull (aIS1);
    try
    {
      final InputStream aIS2 = aCPISP.getInputStream ();
      assertNotNull (aIS2);
      try
      {
        assertNotSame (aIS1, aIS2);
      }
      finally
      {
        StreamUtils.close (aIS2);
      }
    }
    finally
    {
      StreamUtils.close (aIS1);
    }
  }

  @Test
  public void testInvalid ()
  {
    final ClassPathResource aCPISP = new ClassPathResource ("test1-not-existing.txt");
    assertFalse (aCPISP.exists ());
    assertNull (aCPISP.getInputStream ());
    assertNull (aCPISP.getAsURL ());
    assertNotNull (aCPISP.getAsFile ());
    assertFalse (aCPISP.getAsFile ().exists ());

    try
    {
      new ClassPathResource (null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new ClassPathResource ("");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new ClassPathResource (ClassPathResource.CLASSPATH_PREFIX_LONG);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new ClassPathResource (ClassPathResource.CLASSPATH_PREFIX_SHORT);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testDefault ()
  {
    final ClassPathResource aCPISP1a = new ClassPathResource ("folder/test2.txt");
    final ClassPathResource aCPISP1b = new ClassPathResource ("folder/test2.txt");
    final ClassPathResource aCPISP2 = new ClassPathResource ("folder/test1.txt");
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aCPISP1a, aCPISP1b);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aCPISP1a,
                                                                    new ClassPathResource ("cp:folder/test2.txt"));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aCPISP1a,
                                                                    new ClassPathResource ("classpath:folder/test2.txt"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aCPISP1a, aCPISP2);
  }
}
