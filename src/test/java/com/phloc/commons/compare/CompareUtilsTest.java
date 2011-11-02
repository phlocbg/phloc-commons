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
package com.phloc.commons.compare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;

import org.junit.Test;

import com.phloc.commons.locale.ComparatorLocale;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link CompareUtils}.
 * 
 * @author philip
 */
public final class CompareUtilsTest extends AbstractPhlocTestCase
{
  @Test
  public void testSafeEquals_Float ()
  {
    assertTrue (CompareUtils.safeEquals (1.1f, 1.1f));
    assertTrue (CompareUtils.safeEquals (Float.NaN, Float.NaN));
    assertTrue (CompareUtils.safeEquals (1f / 0f, Float.POSITIVE_INFINITY));
    assertTrue (CompareUtils.safeEquals (-1f / 0f, Float.NEGATIVE_INFINITY));
    assertTrue (CompareUtils.safeEquals (Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
    assertTrue (CompareUtils.safeEquals (Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
    assertTrue (CompareUtils.safeEquals (Float.MIN_VALUE, Float.MIN_VALUE));
    assertTrue (CompareUtils.safeEquals (Float.MAX_VALUE, Float.MAX_VALUE));
  }

  @Test
  public void testSafeEquals_Double ()
  {
    assertTrue (CompareUtils.safeEquals (1.1d, 1.1d));
    assertTrue (CompareUtils.safeEquals (Double.NaN, Double.NaN));
    assertTrue (CompareUtils.safeEquals (1d / 0d, Double.POSITIVE_INFINITY));
    assertTrue (CompareUtils.safeEquals (-1d / 0d, Double.NEGATIVE_INFINITY));
    assertTrue (CompareUtils.safeEquals (Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    assertTrue (CompareUtils.safeEquals (Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
    assertTrue (CompareUtils.safeEquals (Double.MIN_VALUE, Double.MIN_VALUE));
    assertTrue (CompareUtils.safeEquals (Double.MAX_VALUE, Double.MAX_VALUE));
  }

  @Test
  public void testSafeEquals_URL () throws MalformedURLException
  {
    final URL u1 = new URL ("http://www.phloc.com");
    final URL u2 = new URL ("http://www.mydomain.at");
    assertTrue (CompareUtils.safeEquals (u1, u1));
    assertTrue (CompareUtils.safeEquals (u1, new URL ("http://www.phloc.com")));
    assertFalse (CompareUtils.safeEquals (u1, u2));
  }

  @Test
  public void testSafeEquals_BigDecimal ()
  {
    final BigDecimal bd1 = new BigDecimal ("5.5");
    final BigDecimal bd2 = new BigDecimal ("5.49999");
    assertTrue (CompareUtils.safeEquals (bd1, bd1));
    assertTrue (CompareUtils.safeEquals (bd1, new BigDecimal ("5.5000")));
    assertTrue (CompareUtils.safeEquals (bd1, new BigDecimal ("5.50000000000000000")));
    assertFalse (CompareUtils.safeEquals (bd1, bd2));
  }

  @Test
  public void testNullSafeCompare ()
  {
    final String s1 = "A";
    final String s2 = "B";
    assertEquals (-1, CompareUtils.nullSafeCompare (s1, s2));
    assertEquals (+1, CompareUtils.nullSafeCompare (s2, s1));
    assertEquals (0, CompareUtils.nullSafeCompare (s1, s1));
    assertEquals (0, CompareUtils.nullSafeCompare (s2, s2));

    assertEquals (+1, CompareUtils.nullSafeCompare (s1, null));
    assertEquals (+1, CompareUtils.nullSafeCompare (s2, null));
    assertEquals (-1, CompareUtils.nullSafeCompare (null, s1));
    assertEquals (-1, CompareUtils.nullSafeCompare (null, s2));
    assertEquals (0, CompareUtils.nullSafeCompare ((String) null, null));

    // Using our collator
    assertEquals (-1, CompareUtils.nullSafeCompare ("1.1 a", "1.1.1 a", L_DE));
    assertEquals (-1, CompareUtils.nullSafeCompare ("1.1 a", "1.1.1 a", CollatorUtils.getCollatorSpaceBeforeDot (L_DE)));
    // Using the system collaor
    assertEquals (+1, CompareUtils.nullSafeCompare ("1.1 a", "1.1.1 a", Collator.getInstance (L_DE)));

    assertEquals (-1, CompareUtils.nullSafeCompare (L_DE, L_EN, new ComparatorLocale ()));
    assertEquals (+1, CompareUtils.nullSafeCompare (L_DE, null, new ComparatorLocale ()));
    assertEquals (-1, CompareUtils.nullSafeCompare (null, L_EN, new ComparatorLocale ()));
  }

  public void _testNSE (final String s1, final String s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((String) null, (String) null));
  }

  public void _testNSE (final Float s1, final Float s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((Float) null, (Float) null));
  }

  public void _testNSE (final Double s1, final Double s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((Double) null, (Double) null));
  }

  public void _testNSE (final Object [] s1, final Object [] s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((Object []) null, (Object []) null));
  }

  public void _testNSE (final boolean [] s1, final boolean [] s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((boolean []) null, (boolean []) null));
  }

  public void _testNSE (final byte [] s1, final byte [] s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((byte []) null, (byte []) null));
  }

  public void _testNSE (final char [] s1, final char [] s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((char []) null, (char []) null));
  }

  public void _testNSE (final double [] s1, final double [] s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((double []) null, (double []) null));
  }

  public void _testNSE (final float [] s1, final float [] s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((float []) null, (float []) null));
  }

  public void _testNSE (final int [] s1, final int [] s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((int []) null, (int []) null));
  }

  public void _testNSE (final long [] s1, final long [] s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((long []) null, (long []) null));
  }

  public void _testNSE (final short [] s1, final short [] s2)
  {
    assertTrue (CompareUtils.nullSafeEquals (s1, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, s2));
    assertFalse (CompareUtils.nullSafeEquals (s2, s1));
    assertFalse (CompareUtils.nullSafeEquals (s1, null));
    assertFalse (CompareUtils.nullSafeEquals (null, s2));
    assertTrue (CompareUtils.nullSafeEquals ((short []) null, (short []) null));
  }

  @Test
  public void testNullSafeEquals ()
  {
    _testNSE ("s1", "s2");
    _testNSE (Float.valueOf (3.1415f), Float.valueOf (23.456f));
    _testNSE (Double.valueOf (3.1415d), Double.valueOf (23.456d));
    _testNSE (new Object [] { "a" }, new Object [] { Integer.valueOf (4) });
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
    assertTrue (CompareUtils.nullSafeEqualsIgnoreCase (s1, s1));
    assertTrue (CompareUtils.nullSafeEqualsIgnoreCase (s1, s2));
    assertTrue (CompareUtils.nullSafeEqualsIgnoreCase (s2, s1));
    assertFalse (CompareUtils.nullSafeEqualsIgnoreCase (s1, null));
    assertFalse (CompareUtils.nullSafeEqualsIgnoreCase (null, s2));
    assertTrue (CompareUtils.nullSafeEqualsIgnoreCase (null, null));
  }
}
