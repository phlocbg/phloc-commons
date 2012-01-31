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
package com.phloc.commons.compare;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link EqualsUtils}.
 * 
 * @author philip
 */
public final class EqualsUtilsTest extends AbstractPhlocTestCase
{
  @Test
  public void testSafeEquals_Float ()
  {
    assertTrue (EqualsUtils.equals (1.1f, 1.1f));
    assertTrue (EqualsUtils.equals (Float.NaN, Float.NaN));
    assertTrue (EqualsUtils.equals (1f / 0f, Float.POSITIVE_INFINITY));
    assertTrue (EqualsUtils.equals (-1f / 0f, Float.NEGATIVE_INFINITY));
    assertTrue (EqualsUtils.equals (Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
    assertTrue (EqualsUtils.equals (Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
    assertTrue (EqualsUtils.equals (Float.MIN_VALUE, Float.MIN_VALUE));
    assertTrue (EqualsUtils.equals (Float.MAX_VALUE, Float.MAX_VALUE));
  }

  @Test
  public void testSafeEquals_Double ()
  {
    assertTrue (EqualsUtils.equals (1.1d, 1.1d));
    assertTrue (EqualsUtils.equals (Double.NaN, Double.NaN));
    assertTrue (EqualsUtils.equals (1d / 0d, Double.POSITIVE_INFINITY));
    assertTrue (EqualsUtils.equals (-1d / 0d, Double.NEGATIVE_INFINITY));
    assertTrue (EqualsUtils.equals (Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    assertTrue (EqualsUtils.equals (Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
    assertTrue (EqualsUtils.equals (Double.MIN_VALUE, Double.MIN_VALUE));
    assertTrue (EqualsUtils.equals (Double.MAX_VALUE, Double.MAX_VALUE));
  }

  @Test
  public void testSafeEquals_URL () throws MalformedURLException
  {
    final URL u1 = new URL ("http://www.phloc.com");
    final URL u2 = new URL ("http://www.mydomain.at");
    assertTrue (EqualsUtils.equals (u1, u1));
    assertTrue (EqualsUtils.equals (u1, new URL ("http://www.phloc.com")));
    assertFalse (EqualsUtils.equals (u1, u2));
  }

  @Test
  public void testSafeEquals_BigDecimal ()
  {
    final BigDecimal bd1 = new BigDecimal ("5.5");
    final BigDecimal bd2 = new BigDecimal ("5.49999");
    assertTrue (EqualsUtils.equals (bd1, bd1));
    assertTrue (EqualsUtils.equals (bd1, new BigDecimal ("5.5000")));
    assertTrue (EqualsUtils.equals (bd1, new BigDecimal ("5.50000000000000000")));
    assertFalse (EqualsUtils.equals (bd1, bd2));
  }

  public void _testNSE (final String s1, final String s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((String) null, (String) null));
  }

  public void _testNSE (final BigDecimal s1, final BigDecimal s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((Float) null, (Float) null));
  }

  public void _testNSE (final Double s1, final Double s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((Double) null, (Double) null));
  }

  public void _testNSE (final Float s1, final Float s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((Float) null, (Float) null));
  }

  public void _testNSE (final URL s1, final URL s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((Float) null, (Float) null));
  }

  public void _testNSE (final boolean [] s1, final boolean [] s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((boolean []) null, (boolean []) null));
  }

  public void _testNSE (final byte [] s1, final byte [] s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((byte []) null, (byte []) null));
  }

  public void _testNSE (final char [] s1, final char [] s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((char []) null, (char []) null));
  }

  public void _testNSE (final double [] s1, final double [] s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((double []) null, (double []) null));
  }

  public void _testNSE (final float [] s1, final float [] s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((float []) null, (float []) null));
  }

  public void _testNSE (final int [] s1, final int [] s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((int []) null, (int []) null));
  }

  public void _testNSE (final long [] s1, final long [] s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((long []) null, (long []) null));
  }

  public void _testNSE (final short [] s1, final short [] s2)
  {
    assertTrue (EqualsUtils.nullSafeEquals (s1, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, s2));
    assertFalse (EqualsUtils.nullSafeEquals (s2, s1));
    assertFalse (EqualsUtils.nullSafeEquals (s1, null));
    assertFalse (EqualsUtils.nullSafeEquals (null, s2));
    assertTrue (EqualsUtils.nullSafeEquals ((short []) null, (short []) null));
  }

  @Test
  public void testNullSafeEquals () throws MalformedURLException
  {
    _testNSE ("s1", "s2");
    _testNSE (new BigDecimal ("12562136756"), new BigDecimal ("67673455"));
    _testNSE (Double.valueOf (3.1415d), Double.valueOf (23.456d));
    _testNSE (Float.valueOf (3.1415f), Float.valueOf (23.456f));
    _testNSE (new URL ("http://www.phloc.com"), new URL ("http://www.google.com"));
    _testNSE (new boolean [] { true }, new boolean [] { false });
    _testNSE (new byte [] { 1 }, new byte [] { 2 });
    _testNSE (new char [] { 'a' }, new char [] { 'b' });
    _testNSE (new double [] { 2.1 }, new double [] { 2 });
    _testNSE (new float [] { 2.1f }, new float [] { 1.9f });
    _testNSE (new int [] { 5 }, new int [] { 6 });
    _testNSE (new long [] { 7 }, new long [] { 8 });
    _testNSE (new short [] { -9 }, new short [] { -10 });

    final String s1 = "s1";
    final String s2 = "S1";
    assertTrue (EqualsUtils.nullSafeEqualsIgnoreCase (s1, s1));
    assertTrue (EqualsUtils.nullSafeEqualsIgnoreCase (s1, s2));
    assertTrue (EqualsUtils.nullSafeEqualsIgnoreCase (s2, s1));
    assertFalse (EqualsUtils.nullSafeEqualsIgnoreCase (s1, null));
    assertFalse (EqualsUtils.nullSafeEqualsIgnoreCase (null, s2));
    assertTrue (EqualsUtils.nullSafeEqualsIgnoreCase (null, null));
  }

  @Test
  public void testEqualsTypeSpecific ()
  {
    final StringBuffer aSB1 = new StringBuffer ("Hi");
    assertTrue (EqualsUtils.equalsTypeSpecific (aSB1, new StringBuffer ("Hi")));
    assertFalse (EqualsUtils.equalsTypeSpecific (aSB1, new StringBuffer ("Hallo")));

    assertTrue (EqualsUtils.nullSafeEqualsTypeSpecific (aSB1, new StringBuffer ("Hi")));
    assertFalse (EqualsUtils.nullSafeEqualsTypeSpecific (aSB1, new StringBuffer ("Hallo")));
    assertFalse (EqualsUtils.nullSafeEqualsTypeSpecific (aSB1, null));
  }
}
