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
package com.phloc.commons.io.streams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.phloc.commons.random.VerySecureRandom;

/**
 * Test class for class {@link NonBlockingByteArrayInputStream}.
 * 
 * @author philip
 */
public final class NonBlockingByteArrayInputStreamTest
{
  @Test
  public void testAll () throws IOException
  {
    final byte [] buf = new byte [100];
    VerySecureRandom.getInstance ().nextBytes (buf);
    try
    {
      new NonBlockingByteArrayInputStream (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new NonBlockingByteArrayInputStream (null, 0, 100);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new NonBlockingByteArrayInputStream (buf, -1, 100);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new NonBlockingByteArrayInputStream (buf, 0, 101);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new NonBlockingByteArrayInputStream (buf, 0, -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new NonBlockingByteArrayInputStream (buf, 90, 20);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final NonBlockingByteArrayInputStream bais = new NonBlockingByteArrayInputStream (buf);
    assertEquals (100, bais.available ());
    bais.read ();
    assertEquals (1, bais.skip (1));
    assertEquals (98, bais.available ());
    bais.read (new byte [9]);
    bais.read (new byte [0]);
    bais.read (new byte [91]);
    assertEquals (-1, bais.read ());
    assertEquals (0, bais.skip (10));

    try
    {
      bais.read ((byte []) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      bais.read ((byte []) null, 0, 10);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    final byte [] x = new byte [100];
    try
    {
      bais.read (x, -1, 1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      bais.read (x, 1, -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      bais.read (x, 90, 20);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    assertTrue (bais.markSupported ());
    bais.mark (0);
    bais.reset ();
  }
}
