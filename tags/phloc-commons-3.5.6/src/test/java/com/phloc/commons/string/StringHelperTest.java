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
package com.phloc.commons.string;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.phloc.commons.CGlobal;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.streams.NonBlockingStringWriter;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocAssert;
import com.phloc.commons.mutable.MutableByte;
import com.phloc.commons.mutable.MutableInt;
import com.phloc.commons.mutable.MutableLong;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * Test class for class {@link StringHelper}.
 * 
 * @author philip
 */
public final class StringHelperTest extends AbstractPhlocTestCase
{
  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "DM_BOOLEAN_CTOR")
  public void testParseBool ()
  {
    assertTrue (StringHelper.parseBool ("true"));
    assertTrue (StringHelper.parseBool ("TRUE"));
    assertTrue (StringHelper.parseBool (Boolean.TRUE.toString ()));
    assertFalse (StringHelper.parseBool ("false"));
    assertFalse (StringHelper.parseBool ("FALSE"));
    assertFalse (StringHelper.parseBool (Boolean.FALSE.toString ()));
    assertFalse (StringHelper.parseBool ("anything"));
    assertFalse (StringHelper.parseBool ((String) null));

    assertTrue (StringHelper.parseBool ((Object) "true"));
    assertFalse (StringHelper.parseBool ((Object) "false"));
    assertFalse (StringHelper.parseBool ((Object) "anything"));
    assertTrue (StringHelper.parseBool (Boolean.TRUE));
    assertFalse (StringHelper.parseBool (Boolean.FALSE));
    assertTrue (StringHelper.parseBool (new Boolean (true)));
    assertFalse (StringHelper.parseBool (new Boolean (false)));
    assertFalse (StringHelper.parseBool ((Object) null));
    assertFalse (StringHelper.parseBool (Integer.valueOf (0)));
    assertFalse (StringHelper.parseBool (Integer.valueOf (1)));
    assertTrue (StringHelper.parseBool (Integer.valueOf (0), true));
    assertTrue (StringHelper.parseBool (Integer.valueOf (1), true));
  }

  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "DM_BOOLEAN_CTOR")
  public void testParseBoolObj ()
  {
    assertEquals (Boolean.TRUE, StringHelper.parseBoolObj ("true"));
    assertEquals (Boolean.TRUE, StringHelper.parseBoolObj ("TRUE"));
    assertEquals (Boolean.TRUE, StringHelper.parseBoolObj (Boolean.TRUE.toString ()));
    assertEquals (Boolean.FALSE, StringHelper.parseBoolObj ("false"));
    assertEquals (Boolean.FALSE, StringHelper.parseBoolObj ("FALSE"));
    assertEquals (Boolean.FALSE, StringHelper.parseBoolObj (Boolean.FALSE.toString ()));
    assertEquals (Boolean.FALSE, StringHelper.parseBoolObj ("anything"));
    assertEquals (Boolean.FALSE, StringHelper.parseBoolObj ((String) null));

    assertEquals (Boolean.TRUE, StringHelper.parseBoolObj ((Object) "true"));
    assertEquals (Boolean.FALSE, StringHelper.parseBoolObj ((Object) "false"));
    assertEquals (Boolean.FALSE, StringHelper.parseBoolObj ((Object) "anything"));
    assertEquals (Boolean.TRUE, StringHelper.parseBoolObj (Boolean.TRUE));
    assertEquals (Boolean.FALSE, StringHelper.parseBoolObj (Boolean.FALSE));
    assertEquals (Boolean.TRUE, StringHelper.parseBoolObj (new Boolean (true)));
    assertEquals (Boolean.FALSE, StringHelper.parseBoolObj (new Boolean (false)));
    assertNull (StringHelper.parseBoolObj ((Object) null));
    assertNull (StringHelper.parseBoolObj (Integer.valueOf (0)));
    assertNull (StringHelper.parseBoolObj (Integer.valueOf (1)));
    assertEquals (Boolean.TRUE, StringHelper.parseBoolObj (Integer.valueOf (0), Boolean.TRUE));
    assertEquals (Boolean.TRUE, StringHelper.parseBoolObj (Integer.valueOf (1), Boolean.TRUE));
    assertNull (StringHelper.parseBoolObj (Integer.valueOf (0), null));
  }

  @Test
  public void testParseByte ()
  {
    // Object
    assertEquals (1, StringHelper.parseByte ((Object) "1", (byte) 0));
    assertEquals (0, StringHelper.parseByte ((Object) "1.2", (byte) 0));
    assertEquals (0, StringHelper.parseByte ((Object) "0", (byte) 0));
    assertEquals (0, StringHelper.parseByte ((Object) "0000", (byte) 0));
    assertEquals (0, StringHelper.parseByte ((Object) "-129", (byte) 0));
    assertEquals (-128, StringHelper.parseByte ((Object) "-128", (byte) 0));
    assertEquals (-1, StringHelper.parseByte ((Object) "-1", (byte) 0));
    assertEquals (44, StringHelper.parseByte ((Object) "44", (byte) 0));
    assertEquals (127, StringHelper.parseByte ((Object) "127", (byte) 0));
    assertEquals (0, StringHelper.parseByte ((Object) "128", (byte) 0));
    assertEquals (0, StringHelper.parseByte ((Object) "257", (byte) 0));
    assertEquals (0, StringHelper.parseByte ((Object) "445566abcdef", (byte) 0));
    assertEquals (0, StringHelper.parseByte ((Object) "abcdef445566", (byte) 0));
    assertEquals (0, StringHelper.parseByte ((Object) "", (byte) 0));
    assertEquals (0, StringHelper.parseByte ((Object) null, (byte) 0));

    assertEquals (0, StringHelper.parseByte (new MutableByte (5), (byte) 0));

    assertEquals (1, StringHelper.parseByte (Byte.valueOf ((byte) 1), (byte) 0));
    assertEquals (1, StringHelper.parseByte (Double.valueOf (1), (byte) 0));
    assertEquals (1, StringHelper.parseByte (BigDecimal.ONE, (byte) 0));

    // String
    assertEquals (1, StringHelper.parseByte ("1", (byte) 0));
    assertEquals (0, StringHelper.parseByte ("1.2", (byte) 0));
    assertEquals (0, StringHelper.parseByte ("0", (byte) 0));
    assertEquals (0, StringHelper.parseByte ("0000", (byte) 0));
    assertEquals (0, StringHelper.parseByte ("-129", (byte) 0));
    assertEquals (-128, StringHelper.parseByte ("-128", (byte) 0));
    assertEquals (-1, StringHelper.parseByte ("-1", (byte) 0));
    assertEquals (44, StringHelper.parseByte ("44", (byte) 0));
    assertEquals (127, StringHelper.parseByte ("127", (byte) 0));
    assertEquals (0, StringHelper.parseByte ("128", (byte) 0));
    assertEquals (0, StringHelper.parseByte ("257", (byte) 0));
    assertEquals (0, StringHelper.parseByte ("445566", (byte) 0));
    assertEquals (0, StringHelper.parseByte ("445566abcdef", (byte) 0));
    assertEquals (0, StringHelper.parseByte ("abcdef445566", (byte) 0));
    assertEquals (0, StringHelper.parseByte ("", (byte) 0));
    assertEquals (0, StringHelper.parseByte (null, (byte) 0));

    final byte nDefault = 17;
    assertEquals (1, StringHelper.parseByte ("1", nDefault));
    assertEquals (nDefault, StringHelper.parseByte ("1.2", nDefault));
    assertEquals (0, StringHelper.parseByte ("0", nDefault));
    assertEquals (0, StringHelper.parseByte ("0000", nDefault));
    assertEquals (-1, StringHelper.parseByte ("-1", nDefault));
    assertEquals (nDefault, StringHelper.parseByte ("445566", nDefault));
    assertEquals (nDefault, StringHelper.parseByte ("445566abcdef", nDefault));
    assertEquals (nDefault, StringHelper.parseByte ("abcdef445566", nDefault));
    assertEquals (nDefault, StringHelper.parseByte ("", nDefault));
    assertEquals (nDefault, StringHelper.parseByte (null, nDefault));
  }

  @Test
  public void testParseByteObj ()
  {
    final Byte b_1 = Byte.valueOf ((byte) -1);
    final Byte b0 = Byte.valueOf ((byte) 0);
    final Byte b1 = Byte.valueOf ((byte) 1);

    // Object
    assertEquals (b1, StringHelper.parseByteObj ((Object) "1"));
    assertNull (StringHelper.parseByteObj ((Object) "abc"));
    assertEquals (b1, StringHelper.parseByteObj ((Object) "1", b0));
    assertEquals (b0, StringHelper.parseByteObj ((Object) "1.2", b0));
    assertEquals (b0, StringHelper.parseByteObj ((Object) "0", b0));
    assertEquals (b0, StringHelper.parseByteObj ((Object) "0000", b0));
    assertEquals (b_1, StringHelper.parseByteObj ((Object) "-1", b0));
    assertEquals (Byte.valueOf ((byte) 44), StringHelper.parseByteObj ((Object) "44", b0));
    assertEquals (b0, StringHelper.parseByteObj ((Object) "445566abcdef", b0));
    assertEquals (b0, StringHelper.parseByteObj ((Object) "abcdef445566", b0));
    assertEquals (b0, StringHelper.parseByteObj ((Object) "", b0));
    assertEquals (b0, StringHelper.parseByteObj ((Object) null, b0));

    assertEquals (b0, StringHelper.parseByteObj (new MutableByte (5), b0));

    assertEquals (b1, StringHelper.parseByteObj (Byte.valueOf ((byte) 1), b0));
    assertEquals (b1, StringHelper.parseByteObj (Double.valueOf (1), b0));
    assertEquals (b1, StringHelper.parseByteObj (BigDecimal.ONE, b0));

    // String
    assertEquals (b1, StringHelper.parseByteObj ("1"));
    assertNull (StringHelper.parseByteObj ("abc"));
    assertEquals (b1, StringHelper.parseByteObj ("1", b0));
    assertEquals (b0, StringHelper.parseByteObj ("1.2", b0));
    assertEquals (b0, StringHelper.parseByteObj ("0", b0));
    assertEquals (b0, StringHelper.parseByteObj ("0000", b0));
    assertEquals (b_1, StringHelper.parseByteObj ("-1", b0));
    assertEquals (Byte.valueOf ((byte) 44), StringHelper.parseByteObj ("44", b0));
    assertEquals (b0, StringHelper.parseByteObj ("445566abcdef", b0));
    assertEquals (b0, StringHelper.parseByteObj ("abcdef445566", b0));
    assertEquals (b0, StringHelper.parseByteObj ("", b0));
    assertEquals (b0, StringHelper.parseByteObj (null, b0));

    final Byte aDefault = Byte.valueOf ((byte) 17);
    assertEquals (b1, StringHelper.parseByteObj ("1", aDefault));
    assertEquals (aDefault, StringHelper.parseByteObj ("1.2", aDefault));
    assertEquals (b0, StringHelper.parseByteObj ("0", aDefault));
    assertEquals (b0, StringHelper.parseByteObj ("0000", aDefault));
    assertEquals (b_1, StringHelper.parseByteObj ("-1", aDefault));
    assertEquals (Byte.valueOf ((byte) 44), StringHelper.parseByteObj ("44", aDefault));
    assertEquals (aDefault, StringHelper.parseByteObj ("445566abcdef", aDefault));
    assertEquals (aDefault, StringHelper.parseByteObj ("abcdef445566", aDefault));
    assertEquals (aDefault, StringHelper.parseByteObj ("", aDefault));
    assertEquals (aDefault, StringHelper.parseByteObj (null, aDefault));
  }

  @Test
  public void testParseInt ()
  {
    // Object
    assertEquals (1, StringHelper.parseInt ((Object) "1", 0));
    assertEquals (0, StringHelper.parseInt ((Object) "1.2", 0));
    assertEquals (0, StringHelper.parseInt ((Object) "0", 0));
    assertEquals (0, StringHelper.parseInt ((Object) "0000", 0));
    assertEquals (-1, StringHelper.parseInt ((Object) "-1", 0));
    assertEquals (445566, StringHelper.parseInt ((Object) "445566", 0));
    assertEquals (0, StringHelper.parseInt ((Object) "445566abcdef", 0));
    assertEquals (0, StringHelper.parseInt ((Object) "abcdef445566", 0));
    assertEquals (0, StringHelper.parseInt ((Object) "", 0));
    assertEquals (0, StringHelper.parseInt ((Object) null, 0));

    assertEquals (0, StringHelper.parseInt (new MutableInt (5), 0));

    assertEquals (1, StringHelper.parseInt (Integer.valueOf (1), 0));
    assertEquals (1, StringHelper.parseInt (Double.valueOf (1), 0));
    assertEquals (1, StringHelper.parseInt (BigDecimal.ONE, 0));

    // String
    assertEquals (1, StringHelper.parseInt ("1", 0));
    assertEquals (0, StringHelper.parseInt ("1.2", 0));
    assertEquals (0, StringHelper.parseInt ("0", 0));
    assertEquals (0, StringHelper.parseInt ("0000", 0));
    assertEquals (-1, StringHelper.parseInt ("-1", 0));
    assertEquals (445566, StringHelper.parseInt ("445566", 0));
    assertEquals (0, StringHelper.parseInt ("445566abcdef", 0));
    assertEquals (0, StringHelper.parseInt ("abcdef445566", 0));
    assertEquals (0, StringHelper.parseInt ("", 0));
    assertEquals (0, StringHelper.parseInt (null, 0));

    final int nDefault = 17;
    assertEquals (1, StringHelper.parseInt ("1", nDefault));
    assertEquals (nDefault, StringHelper.parseInt ("1.2", nDefault));
    assertEquals (0, StringHelper.parseInt ("0", nDefault));
    assertEquals (0, StringHelper.parseInt ("0000", nDefault));
    assertEquals (-1, StringHelper.parseInt ("-1", nDefault));
    assertEquals (445566, StringHelper.parseInt ("445566", nDefault));
    assertEquals (nDefault, StringHelper.parseInt ("445566abcdef", nDefault));
    assertEquals (nDefault, StringHelper.parseInt ("abcdef445566", nDefault));
    assertEquals (nDefault, StringHelper.parseInt ("", nDefault));
    assertEquals (nDefault, StringHelper.parseInt (null, nDefault));
  }

  @Test
  public void testParseIntObj ()
  {
    // Object
    assertEquals (I1, StringHelper.parseIntObj ((Object) "1"));
    assertNull (StringHelper.parseIntObj ((Object) "abc"));
    assertEquals (I1, StringHelper.parseIntObj ((Object) "1", I0));
    assertEquals (I0, StringHelper.parseIntObj ((Object) "1.2", I0));
    assertEquals (I0, StringHelper.parseIntObj ((Object) "0", I0));
    assertEquals (I0, StringHelper.parseIntObj ((Object) "0000", I0));
    assertEquals (I_1, StringHelper.parseIntObj ((Object) "-1", I0));
    assertEquals (Integer.valueOf (445566), StringHelper.parseIntObj ((Object) "445566", I0));
    assertEquals (I0, StringHelper.parseIntObj ((Object) "445566abcdef", I0));
    assertEquals (I0, StringHelper.parseIntObj ((Object) "abcdef445566", I0));
    assertEquals (I0, StringHelper.parseIntObj ((Object) "", I0));
    assertEquals (I0, StringHelper.parseIntObj ((Object) null, I0));

    assertEquals (I0, StringHelper.parseIntObj (new MutableInt (5), I0));

    assertEquals (I1, StringHelper.parseIntObj (Integer.valueOf (1), I0));
    assertEquals (I1, StringHelper.parseIntObj (Double.valueOf (1), I0));
    assertEquals (I1, StringHelper.parseIntObj (BigDecimal.ONE, I0));

    // String
    assertEquals (I1, StringHelper.parseIntObj ("1"));
    assertNull (StringHelper.parseIntObj ("abc"));
    assertEquals (I1, StringHelper.parseIntObj ("1", I0));
    assertEquals (I0, StringHelper.parseIntObj ("1.2", I0));
    assertEquals (I0, StringHelper.parseIntObj ("0", I0));
    assertEquals (I0, StringHelper.parseIntObj ("0000", I0));
    assertEquals (I_1, StringHelper.parseIntObj ("-1", I0));
    assertEquals (Integer.valueOf (445566), StringHelper.parseIntObj ("445566", I0));
    assertEquals (I0, StringHelper.parseIntObj ("445566abcdef", I0));
    assertEquals (I0, StringHelper.parseIntObj ("abcdef445566", I0));
    assertEquals (I0, StringHelper.parseIntObj ("", I0));
    assertEquals (I0, StringHelper.parseIntObj (null, I0));

    final Integer aDefault = Integer.valueOf (17);
    assertEquals (I1, StringHelper.parseIntObj ("1", aDefault));
    assertEquals (aDefault, StringHelper.parseIntObj ("1.2", aDefault));
    assertEquals (I0, StringHelper.parseIntObj ("0", aDefault));
    assertEquals (I0, StringHelper.parseIntObj ("0000", aDefault));
    assertEquals (I_1, StringHelper.parseIntObj ("-1", aDefault));
    assertEquals (Integer.valueOf (445566), StringHelper.parseIntObj ("445566", aDefault));
    assertEquals (aDefault, StringHelper.parseIntObj ("445566abcdef", aDefault));
    assertEquals (aDefault, StringHelper.parseIntObj ("abcdef445566", aDefault));
    assertEquals (aDefault, StringHelper.parseIntObj ("", aDefault));
    assertEquals (aDefault, StringHelper.parseIntObj (null, aDefault));
  }

  @Test
  public void testParseLong ()
  {
    // Object
    assertEquals (1L, StringHelper.parseLong ((Object) "1", 0));
    assertEquals (0L, StringHelper.parseLong ((Object) "1.2", 0));
    assertEquals (0L, StringHelper.parseLong ((Object) "0", 0));
    assertEquals (0L, StringHelper.parseLong ((Object) "0000", 0));
    assertEquals (-1L, StringHelper.parseLong ((Object) "-1", 0));
    assertEquals (445566L, StringHelper.parseLong ((Object) "445566", 0));
    assertEquals (0L, StringHelper.parseLong ((Object) "445566abcdef", 0));
    assertEquals (0L, StringHelper.parseLong ((Object) "abcdef445566", 0));
    assertEquals (0L, StringHelper.parseLong ((Object) "", 0));
    assertEquals (0L, StringHelper.parseLong ((Object) null, 0));

    assertEquals (0L, StringHelper.parseLong (new MutableLong (5), 0));

    assertEquals (1L, StringHelper.parseLong (Integer.valueOf (1), 0));
    assertEquals (1L, StringHelper.parseLong (Double.valueOf (1), 0));
    assertEquals (1L, StringHelper.parseLong (BigDecimal.ONE, 0));

    // String
    assertEquals (1L, StringHelper.parseLong ("1", 0));
    assertEquals (0L, StringHelper.parseLong ("1.2", 0));
    assertEquals (0L, StringHelper.parseLong ("0", 0));
    assertEquals (0L, StringHelper.parseLong ("0000", 0));
    assertEquals (-1L, StringHelper.parseLong ("-1", 0));
    assertEquals (445566L, StringHelper.parseLong ("445566", 0));
    assertEquals (445566445566L, StringHelper.parseLong ("445566445566", 0));
    assertEquals (445566445566445566L, StringHelper.parseLong ("445566445566445566", 0));
    assertEquals (0L, StringHelper.parseLong ("445566abcdef", 0));
    assertEquals (0L, StringHelper.parseLong ("abcdef445566", 0));
    assertEquals (0L, StringHelper.parseLong ("", 0));
    assertEquals (0L, StringHelper.parseLong (null, 0));

    final long nDefault = 171819171819171819L;
    assertEquals (1L, StringHelper.parseLong ("1", nDefault));
    assertEquals (nDefault, StringHelper.parseLong ("1.2", nDefault));
    assertEquals (0L, StringHelper.parseLong ("0", nDefault));
    assertEquals (0L, StringHelper.parseLong ("0000", nDefault));
    assertEquals (-1L, StringHelper.parseLong ("-1", nDefault));
    assertEquals (445566L, StringHelper.parseLong ("445566", nDefault));
    assertEquals (445566445566L, StringHelper.parseLong ("445566445566", nDefault));
    assertEquals (445566445566445566L, StringHelper.parseLong ("445566445566445566", nDefault));
    assertEquals (nDefault, StringHelper.parseLong ("445566abcdef", nDefault));
    assertEquals (nDefault, StringHelper.parseLong ("abcdef445566", nDefault));
    assertEquals (nDefault, StringHelper.parseLong ("", nDefault));
    assertEquals (nDefault, StringHelper.parseLong (null, nDefault));
  }

  @Test
  public void testParseLongObj ()
  {
    // Object
    assertEquals (L1, StringHelper.parseLongObj ((Object) "1"));
    assertNull (StringHelper.parseLongObj ((Object) "abc"));
    assertEquals (L1, StringHelper.parseLongObj ((Object) "1", L0));
    assertEquals (L0, StringHelper.parseLongObj ((Object) "1.2", L0));
    assertEquals (L0, StringHelper.parseLongObj ((Object) "0", L0));
    assertEquals (L0, StringHelper.parseLongObj ((Object) "0000", L0));
    assertEquals (L_1, StringHelper.parseLongObj ((Object) "-1", L0));
    assertEquals (Long.valueOf (445566), StringHelper.parseLongObj ((Object) "445566", L0));
    assertEquals (L0, StringHelper.parseLongObj ((Object) "445566abcdef", L0));
    assertEquals (L0, StringHelper.parseLongObj ((Object) "abcdef445566", L0));
    assertEquals (L0, StringHelper.parseLongObj ((Object) "", L0));
    assertEquals (L0, StringHelper.parseLongObj ((Object) null, L0));

    assertEquals (L0, StringHelper.parseLongObj (new MutableInt (5), L0));

    assertEquals (L1, StringHelper.parseLongObj (Integer.valueOf (1), L0));
    assertEquals (L1, StringHelper.parseLongObj (Double.valueOf (1), L0));
    assertEquals (L1, StringHelper.parseLongObj (BigDecimal.ONE, L0));

    // String
    assertEquals (L1, StringHelper.parseLongObj ("1"));
    assertNull (StringHelper.parseLongObj ("abc"));
    assertEquals (L1, StringHelper.parseLongObj ("1", L0));
    assertEquals (L0, StringHelper.parseLongObj ("1.2", L0));
    assertEquals (L0, StringHelper.parseLongObj ("0", L0));
    assertEquals (L0, StringHelper.parseLongObj ("0000", L0));
    assertEquals (L_1, StringHelper.parseLongObj ("-1", L0));
    assertEquals (Long.valueOf (445566), StringHelper.parseLongObj ("445566", L0));
    assertEquals (L0, StringHelper.parseLongObj ("445566abcdef", L0));
    assertEquals (L0, StringHelper.parseLongObj ("abcdef445566", L0));
    assertEquals (L0, StringHelper.parseLongObj ("", L0));
    assertEquals (L0, StringHelper.parseLongObj (null, L0));

    final Long aDefault = Long.valueOf (-173267823468L);
    assertEquals (L1, StringHelper.parseLongObj ("1", aDefault));
    assertEquals (aDefault, StringHelper.parseLongObj ("1.2", aDefault));
    assertEquals (L0, StringHelper.parseLongObj ("0", aDefault));
    assertEquals (L0, StringHelper.parseLongObj ("0000", aDefault));
    assertEquals (L_1, StringHelper.parseLongObj ("-1", aDefault));
    assertEquals (Long.valueOf (445566), StringHelper.parseLongObj ("445566", aDefault));
    assertEquals (aDefault, StringHelper.parseLongObj ("445566abcdef", aDefault));
    assertEquals (aDefault, StringHelper.parseLongObj ("abcdef445566", aDefault));
    assertEquals (aDefault, StringHelper.parseLongObj ("", aDefault));
    assertEquals (aDefault, StringHelper.parseLongObj (null, aDefault));
  }

  @Test
  public void testParseDouble ()
  {
    final double dDefault = 3.145667;

    // Object
    PhlocAssert.assertEquals (dDefault, StringHelper.parseDouble ((Object) null, dDefault));
    PhlocAssert.assertEquals (1, StringHelper.parseDouble (BigDecimal.ONE, dDefault));
    PhlocAssert.assertEquals (dDefault, StringHelper.parseDouble (new MutableInt (5), dDefault));

    // String
    PhlocAssert.assertEquals (dDefault, StringHelper.parseDouble ((String) null, dDefault));
    PhlocAssert.assertEquals (dDefault, StringHelper.parseDouble ("", dDefault));
    PhlocAssert.assertEquals (1.2, StringHelper.parseDouble ("1.2", dDefault));
    PhlocAssert.assertEquals (-1.23456, StringHelper.parseDouble ("-1.23456", dDefault));
    PhlocAssert.assertEquals (dDefault, StringHelper.parseDouble ("bla", dDefault));
  }

  @Test
  public void testParseDoubleObj ()
  {
    final Double aDefault = Double.valueOf (3.145667);

    // Object
    assertNull (StringHelper.parseDoubleObj ((Object) null));
    assertEquals (aDefault, StringHelper.parseDoubleObj ((Object) null, aDefault));
    PhlocAssert.assertEquals (1, StringHelper.parseDoubleObj (BigDecimal.ONE, aDefault));
    assertEquals (aDefault, StringHelper.parseDoubleObj (new MutableInt (5), aDefault));

    // String
    assertNull (StringHelper.parseDoubleObj ("foo"));
    assertEquals (aDefault, StringHelper.parseDoubleObj ((String) null, aDefault));
    assertEquals (aDefault, StringHelper.parseDoubleObj ("", aDefault));
    PhlocAssert.assertEquals (1.2, StringHelper.parseDoubleObj ("1.2", aDefault));
    PhlocAssert.assertEquals (-1.23456, StringHelper.parseDoubleObj ("-1.23456", aDefault));
    assertEquals (aDefault, StringHelper.parseDoubleObj ("bla", aDefault));
  }

  @Test
  public void testParseFloat ()
  {
    final float fDefault = 3.145667f;

    // Object
    PhlocAssert.assertEquals (fDefault, StringHelper.parseFloat ((Object) null, fDefault));
    PhlocAssert.assertEquals (1, StringHelper.parseFloat (BigDecimal.ONE, fDefault));
    PhlocAssert.assertEquals (fDefault, StringHelper.parseFloat (new MutableInt (5), fDefault));

    // String
    PhlocAssert.assertEquals (fDefault, StringHelper.parseFloat ((String) null, fDefault));
    PhlocAssert.assertEquals (fDefault, StringHelper.parseFloat ("", fDefault));
    PhlocAssert.assertEquals (1.2, StringHelper.parseFloat ("1.2", fDefault));
    PhlocAssert.assertEquals (-1.23456, StringHelper.parseFloat ("-1.23456", fDefault));
    PhlocAssert.assertEquals (fDefault, StringHelper.parseFloat ("bla", fDefault));
  }

  @Test
  public void testParseFloatObj ()
  {
    final Float aDefault = Float.valueOf (3.145667f);

    // Object
    assertNull (StringHelper.parseFloatObj ((Object) null));
    assertEquals (aDefault, StringHelper.parseFloatObj ((Object) null, aDefault));
    PhlocAssert.assertEquals (1, StringHelper.parseFloatObj (BigDecimal.ONE, aDefault));
    assertEquals (aDefault, StringHelper.parseFloatObj (new MutableInt (5), aDefault));

    // String
    assertNull (StringHelper.parseFloatObj ("foo"));
    assertEquals (aDefault, StringHelper.parseFloatObj ((String) null, aDefault));
    assertEquals (aDefault, StringHelper.parseFloatObj ("", aDefault));
    PhlocAssert.assertEquals (1.2f, StringHelper.parseFloatObj ("1.2", aDefault));
    PhlocAssert.assertEquals (-1.23456f, StringHelper.parseFloatObj ("-1.23456", aDefault));
    assertEquals (aDefault, StringHelper.parseFloatObj ("bla", aDefault));
  }

  @Test
  public void testParseBigInteger ()
  {
    final BigInteger aDefault = new BigInteger ("123462786432798234676875657234709");

    // String
    assertEquals (BigInteger.ONE, StringHelper.parseBigInteger ("1"));
    assertEquals (new BigInteger ("10"), StringHelper.parseBigInteger ("10", 10));
    assertEquals (new BigInteger ("16"), StringHelper.parseBigInteger ("10", 16));
    assertNull (StringHelper.parseBigInteger ("abc"));
    assertEquals (BigInteger.ONE, StringHelper.parseBigInteger ("1", aDefault));
    assertEquals (new BigInteger ("46278643279823467687565723"),
                  StringHelper.parseBigInteger ("46278643279823467687565723"));
    assertEquals (aDefault, StringHelper.parseBigInteger ("abc", aDefault));
    assertEquals (aDefault, StringHelper.parseBigInteger ((String) null, aDefault));
    assertEquals (aDefault, StringHelper.parseBigInteger ("", aDefault));
    assertEquals (aDefault, StringHelper.parseBigInteger ("1.2", aDefault));
    assertEquals (aDefault, StringHelper.parseBigInteger ("-1.23456", aDefault));
    assertEquals (aDefault, StringHelper.parseBigInteger ("bla", aDefault));
  }

  @Test
  public void testParseBigDecimal ()
  {
    final BigDecimal aDefault = new BigDecimal ("1234627864327.98234676875657234709");

    // String
    assertEquals (BigDecimal.ONE, StringHelper.parseBigDecimal ("1"));
    assertNull (StringHelper.parseBigDecimal ("abc"));
    assertEquals (BigDecimal.ONE, StringHelper.parseBigDecimal ("1", aDefault));
    assertEquals (new BigDecimal ("46278643279.823467687565723"),
                  StringHelper.parseBigDecimal ("46278643279.823467687565723"));
    assertEquals (aDefault, StringHelper.parseBigDecimal ("abc", aDefault));
    assertEquals (aDefault, StringHelper.parseBigDecimal ((String) null, aDefault));
    assertEquals (aDefault, StringHelper.parseBigDecimal ("", aDefault));
    assertEquals (new BigDecimal ("1.2"), StringHelper.parseBigDecimal ("1.2", aDefault));
    assertEquals (new BigDecimal ("-1.23456"), StringHelper.parseBigDecimal ("-1.23456", aDefault));
    assertEquals (aDefault, StringHelper.parseBigDecimal ("bla", aDefault));
  }

  @Test
  public void testIsInt ()
  {
    assertTrue (StringHelper.isInt ("1"));
    assertFalse (StringHelper.isInt ("1.2"));
    assertTrue (StringHelper.isInt ("0"));
    assertTrue (StringHelper.isInt ("0000"));
    assertTrue (StringHelper.isInt ("-1"));
    assertTrue (StringHelper.isInt ("445566"));
    assertFalse (StringHelper.isInt ("445566 "));
    assertFalse (StringHelper.isInt (" 445566"));
    assertFalse (StringHelper.isInt ("445566445566"));
    assertFalse (StringHelper.isInt ("445566445566445566"));
    assertFalse (StringHelper.isInt ("445566abcdef"));
    assertFalse (StringHelper.isInt ("abcdef445566"));
    assertFalse (StringHelper.isInt (""));
    assertFalse (StringHelper.isInt (null));
  }

  @Test
  public void testIsLong ()
  {
    assertTrue (StringHelper.isLong ("1"));
    assertFalse (StringHelper.isLong ("1.2"));
    assertTrue (StringHelper.isLong ("0"));
    assertTrue (StringHelper.isLong ("0000"));
    assertTrue (StringHelper.isLong ("-1"));
    assertTrue (StringHelper.isLong ("445566"));
    assertTrue (StringHelper.isLong ("445566445566"));
    assertTrue (StringHelper.isLong ("445566445566445566"));
    assertFalse (StringHelper.isLong ("445566445566445566 "));
    assertFalse (StringHelper.isLong (" 445566445566445566"));
    assertFalse (StringHelper.isLong ("445566abcdef"));
    assertFalse (StringHelper.isLong ("abcdef445566"));
    assertFalse (StringHelper.isLong (""));
    assertFalse (StringHelper.isLong (null));
  }

  @Test
  public void testIsUnsignedInt ()
  {
    assertTrue (StringHelper.isUnsignedInt ("1"));
    assertFalse (StringHelper.isUnsignedInt ("1.2"));
    assertTrue (StringHelper.isUnsignedInt ("0"));
    assertTrue (StringHelper.isUnsignedInt ("0000"));
    assertFalse (StringHelper.isUnsignedInt ("-1"));
    assertTrue (StringHelper.isUnsignedInt ("445566"));
    assertFalse (StringHelper.isUnsignedInt ("445566445566"));
    assertFalse (StringHelper.isUnsignedInt ("445566445566445566"));
    assertFalse (StringHelper.isUnsignedInt ("445566abcdef"));
    assertFalse (StringHelper.isUnsignedInt ("abcdef445566"));
    assertFalse (StringHelper.isUnsignedInt (""));
    assertFalse (StringHelper.isUnsignedInt (null));
  }

  @Test
  public void testIsUnsignedLong ()
  {
    assertTrue (StringHelper.isUnsignedLong ("1"));
    assertFalse (StringHelper.isUnsignedLong ("1.2"));
    assertTrue (StringHelper.isUnsignedLong ("0"));
    assertTrue (StringHelper.isUnsignedLong ("0000"));
    assertFalse (StringHelper.isUnsignedLong ("-1"));
    assertTrue (StringHelper.isUnsignedLong ("445566"));
    assertTrue (StringHelper.isUnsignedLong ("445566445566"));
    assertTrue (StringHelper.isUnsignedLong ("445566445566445566"));
    assertFalse (StringHelper.isUnsignedLong ("445566abcdef"));
    assertFalse (StringHelper.isUnsignedLong ("abcdef445566"));
    assertFalse (StringHelper.isUnsignedLong (""));
    assertFalse (StringHelper.isUnsignedLong (null));
  }

  @Test
  public void testIsDouble ()
  {
    assertTrue (StringHelper.isDouble ("1"));
    assertTrue (StringHelper.isDouble ("1.2"));
    assertTrue (StringHelper.isDouble ("1,2"));
    assertTrue (StringHelper.isDouble ("0"));
    assertTrue (StringHelper.isDouble ("0000"));
    assertTrue (StringHelper.isDouble ("-1"));
    assertTrue (StringHelper.isDouble ("445566"));
    assertTrue (StringHelper.isDouble ("445566445566"));
    assertTrue (StringHelper.isDouble ("445566445566445566"));
    assertFalse (StringHelper.isDouble ("445566abcdef"));
    assertFalse (StringHelper.isDouble ("abcdef445566"));
    assertFalse (StringHelper.isDouble (""));
    assertFalse (StringHelper.isDouble (null));
  }

  @Test
  public void testHasTextAndHasNoText ()
  {
    assertTrue (StringHelper.hasText ("any"));
    assertTrue (StringHelper.hasText (" "));
    assertFalse (StringHelper.hasText (""));
    assertFalse (StringHelper.hasText (null));

    assertTrue (StringHelper.hasTextAfterTrim ("any"));
    assertFalse (StringHelper.hasTextAfterTrim (" "));
    assertFalse (StringHelper.hasTextAfterTrim (""));
    assertFalse (StringHelper.hasTextAfterTrim (null));

    assertFalse (StringHelper.hasNoText ("any"));
    assertFalse (StringHelper.hasNoText (" "));
    assertTrue (StringHelper.hasNoText (""));
    assertTrue (StringHelper.hasNoText (null));

    assertFalse (StringHelper.hasNoTextAfterTrim ("any"));
    assertTrue (StringHelper.hasNoTextAfterTrim (" "));
    assertTrue (StringHelper.hasNoTextAfterTrim (""));
    assertTrue (StringHelper.hasNoTextAfterTrim (null));
  }

  @Test
  public void testLeadingZero ()
  {
    assertEquals ("005", StringHelper.leadingZero (5, 3));
    assertEquals ("0005", StringHelper.leadingZero (5, 4));
    assertEquals ("5", StringHelper.leadingZero (5, 1));
    assertEquals ("56", StringHelper.leadingZero (56, 1));
    assertEquals ("56", StringHelper.leadingZero (56, 2));
    assertEquals ("056", StringHelper.leadingZero (56, 3));
    assertEquals ("0000056", StringHelper.leadingZero (56, 7));
    assertEquals ("0005678", StringHelper.leadingZero (5678, 7));
    assertEquals ("-5", StringHelper.leadingZero (-5, 1));
    assertEquals ("-05", StringHelper.leadingZero (-5, 2));
    assertEquals ("-005", StringHelper.leadingZero (-5, 3));

    assertEquals ("005", StringHelper.leadingZero (5L, 3));
    assertEquals ("0005", StringHelper.leadingZero (5L, 4));
    assertEquals ("5", StringHelper.leadingZero (5L, 1));
    assertEquals ("56", StringHelper.leadingZero (56L, 1));
    assertEquals ("56", StringHelper.leadingZero (56L, 2));
    assertEquals ("056", StringHelper.leadingZero (56L, 3));
    assertEquals ("0000056", StringHelper.leadingZero (56L, 7));
    assertEquals ("0005678", StringHelper.leadingZero (5678L, 7));
    assertEquals ("-5", StringHelper.leadingZero (-5L, 1));
    assertEquals ("-05", StringHelper.leadingZero (-5L, 2));
    assertEquals ("-005", StringHelper.leadingZero (-5L, 3));

    assertNull (StringHelper.leadingZero ((Byte) null, 5));
    assertEquals ("00013", StringHelper.leadingZero (Byte.valueOf ((byte) 13), 5));
    assertNull (StringHelper.leadingZero ((Integer) null, 5));
    assertEquals ("00013", StringHelper.leadingZero (Integer.valueOf (13), 5));
    assertNull (StringHelper.leadingZero ((Long) null, 5));
    assertEquals ("00013", StringHelper.leadingZero (Long.valueOf (13), 5));
    assertNull (StringHelper.leadingZero ((Short) null, 5));
    assertEquals ("00013", StringHelper.leadingZero (Short.valueOf ((short) 13), 5));
  }

  @Test
  public void testHexEncode ()
  {
    try
    {
      // null not allowed
      StringHelper.hexEncode (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      // null not allowed
      StringHelper.hexEncode (null, 0, 5);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      StringHelper.hexEncode (new byte [0], -1, 5);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      StringHelper.hexEncode (new byte [0], 0, -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      StringHelper.hexEncode (new byte [0], 0, 1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    assertEquals (StringHelper.hexEncode (new byte [] {}), "");
    assertEquals (StringHelper.hexEncode (new byte [] { 1 }), "01");
    assertEquals (StringHelper.hexEncode (new byte [] { 1, 10 }), "010a");
    assertEquals (StringHelper.hexEncode (new byte [] { 0, 1, 10, (byte) 255 }), "00010aff");

    // Byte offset
    assertEquals (StringHelper.hexEncode (new byte [] { 1, 10 }, 0, 2), "010a");
    assertEquals (StringHelper.hexEncode (new byte [] { 1, 10 }, 0, 1), "01");
    assertEquals (StringHelper.hexEncode (new byte [] { 1, 10 }, 1, 1), "0a");
    try
    {
      // length is too large
      StringHelper.hexEncode (new byte [] { 1, 10 }, 1, 2);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testGetHexValue ()
  {
    assertEquals (0, StringHelper.getHexValue ('0'));
    assertEquals (9, StringHelper.getHexValue ('9'));
    assertEquals (10, StringHelper.getHexValue ('a'));
    assertEquals (10, StringHelper.getHexValue ('A'));
    assertEquals (15, StringHelper.getHexValue ('f'));
    assertEquals (15, StringHelper.getHexValue ('F'));
    assertEquals (-1, StringHelper.getHexValue ('g'));
    assertEquals (-1, StringHelper.getHexValue ('z'));
  }

  @Test
  public void testGetHexChar ()
  {
    assertEquals ('0', StringHelper.getHexChar (0));
    assertEquals ('9', StringHelper.getHexChar (9));
    assertEquals ('a', StringHelper.getHexChar (10));
    assertEquals ('f', StringHelper.getHexChar (15));
    assertEquals ('\0', StringHelper.getHexChar (-1));
    assertEquals ('\0', StringHelper.getHexChar (16));
    assertEquals ('\0', StringHelper.getHexChar (999));
  }

  @Test
  public void testHexDecode ()
  {
    try
    {
      // null not allowed
      StringHelper.hexDecode (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      // odd length
      StringHelper.hexDecode ("000");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      // No valid hex char 'g'
      StringHelper.hexDecode ("0g");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      // No valid hex char 'g'
      StringHelper.hexDecode ("g0");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    for (final String sString : new String [] { "Super", "Hallo", "", "Welt!", "fff" })
      assertEquals (sString,
                    new String (StringHelper.hexDecode (StringHelper.hexEncode (sString.getBytes (CCharset.CHARSET_ISO_8859_1_OBJ))),
                                CCharset.CHARSET_ISO_8859_1_OBJ));

    assertArrayEquals (new byte [] { 0 }, StringHelper.hexDecode ("00"));
    assertArrayEquals (new byte [] { 0, 1 }, StringHelper.hexDecode ("0001"));
  }

  @Test
  public void testHexStringByte ()
  {
    assertEquals ("0", StringHelper.hexString ((byte) 0));
    assertEquals ("9", StringHelper.hexString ((byte) 9));
    assertEquals ("a", StringHelper.hexString ((byte) 10));
    assertEquals ("ff", StringHelper.hexString ((byte) 255));
  }

  @Test
  public void testHexStringLeadingZeroByte ()
  {
    assertEquals ("0000", StringHelper.hexStringLeadingZero ((byte) 0, 4));
    assertEquals ("0009", StringHelper.hexStringLeadingZero ((byte) 9, 4));
    assertEquals ("000a", StringHelper.hexStringLeadingZero ((byte) 10, 4));
    assertEquals ("00ff", StringHelper.hexStringLeadingZero ((byte) 255, 4));
  }

  @Test
  public void testHexStringInt ()
  {
    assertEquals ("9", StringHelper.hexString (9));
    assertEquals ("a", StringHelper.hexString (10));
    assertEquals ("ff", StringHelper.hexString (255));
    assertEquals ("ffff", StringHelper.hexString (65535));
  }

  @Test
  public void testHexStringLeadingZeroInt ()
  {
    assertEquals ("09", StringHelper.hexStringLeadingZero (9, 2));
    assertEquals ("00a", StringHelper.hexStringLeadingZero (10, 3));
    assertEquals ("00ff", StringHelper.hexStringLeadingZero (255, 4));
    assertEquals ("ffff", StringHelper.hexStringLeadingZero (65535, 4));
    assertEquals ("ffff", StringHelper.hexStringLeadingZero (65535, 0));
  }

  @Test
  public void testHexStringLong ()
  {
    assertEquals ("9", StringHelper.hexString (9L));
    assertEquals ("a", StringHelper.hexString (10L));
    assertEquals ("ff", StringHelper.hexString (255L));
    assertEquals ("ffff", StringHelper.hexString (65535L));
    assertEquals ("ffff0000", StringHelper.hexString (65536L * 65535L));
  }

  @Test
  public void testHexStringLeadingZeroLong ()
  {
    assertEquals ("09", StringHelper.hexStringLeadingZero (9L, 2));
    assertEquals ("00a", StringHelper.hexStringLeadingZero (10L, 3));
    assertEquals ("00ff", StringHelper.hexStringLeadingZero (255L, 4));
    assertEquals ("ffff", StringHelper.hexStringLeadingZero (65535L, 4));
    assertEquals ("0000ffff", StringHelper.hexStringLeadingZero (65535L, 8));
    assertEquals ("ffff0000", StringHelper.hexStringLeadingZero (65536L * 65535L, 5));
  }

  @Test
  public void testHexStringShort ()
  {
    assertEquals ("0", StringHelper.hexString ((short) 0));
    assertEquals ("9", StringHelper.hexString ((short) 9));
    assertEquals ("a", StringHelper.hexString ((short) 10));
    assertEquals ("ff", StringHelper.hexString ((short) 255));
  }

  @Test
  public void testHexStringLeadingZeroShort ()
  {
    assertEquals ("0000", StringHelper.hexStringLeadingZero ((short) 0, 4));
    assertEquals ("0009", StringHelper.hexStringLeadingZero ((short) 9, 4));
    assertEquals ("000a", StringHelper.hexStringLeadingZero ((short) 10, 4));
    assertEquals ("00ff", StringHelper.hexStringLeadingZero ((short) 255, 4));
  }

  @Test
  public void testGetLeadingWhitespaceCount ()
  {
    assertEquals (0, StringHelper.getLeadingWhitespaceCount ("Hallo Welt"));
    assertEquals (1, StringHelper.getLeadingWhitespaceCount (" Hallo Welt"));
    assertEquals (2, StringHelper.getLeadingWhitespaceCount ("  Hallo Welt"));
    assertEquals (2, StringHelper.getLeadingWhitespaceCount ("\t\tHallo Welt"));
    assertEquals (2, StringHelper.getLeadingWhitespaceCount ("  "));
    assertEquals (0, StringHelper.getLeadingWhitespaceCount (""));
    assertEquals (0, StringHelper.getLeadingWhitespaceCount (null));
  }

  @Test
  public void testGetTrailingWhitespaceCount ()
  {
    assertEquals (0, StringHelper.getTrailingWhitespaceCount ("Hallo Welt"));
    assertEquals (1, StringHelper.getTrailingWhitespaceCount (" Hallo Welt "));
    assertEquals (2, StringHelper.getTrailingWhitespaceCount ("  Hallo Welt  "));
    assertEquals (2, StringHelper.getTrailingWhitespaceCount ("\t\tHallo Welt\t\t"));
    assertEquals (2, StringHelper.getTrailingWhitespaceCount ("  "));
    assertEquals (0, StringHelper.getTrailingWhitespaceCount (""));
    assertEquals (0, StringHelper.getTrailingWhitespaceCount (null));
  }

  @Test
  public void testGetLeadingCharCount ()
  {
    assertEquals (0, StringHelper.getLeadingCharCount ("Hallo Welt", 'x'));
    assertEquals (1, StringHelper.getLeadingCharCount ("xHallo Welt", 'x'));
    assertEquals (2, StringHelper.getLeadingCharCount ("xxHallo Welt", 'x'));
    assertEquals (2, StringHelper.getLeadingCharCount ("xx", 'x'));
    assertEquals (0, StringHelper.getLeadingCharCount ("", 'x'));
    assertEquals (0, StringHelper.getLeadingCharCount (null, 'x'));
  }

  @Test
  public void testGetTrailingCharCount ()
  {
    assertEquals (0, StringHelper.getTrailingCharCount ("Hallo Welt", 'x'));
    assertEquals (1, StringHelper.getTrailingCharCount (" Hallo Weltx", 'x'));
    assertEquals (2, StringHelper.getTrailingCharCount ("  Hallo Weltxx", 'x'));
    assertEquals (2, StringHelper.getTrailingCharCount ("xx", 'x'));
    assertEquals (0, StringHelper.getTrailingCharCount ("", 'x'));
    assertEquals (0, StringHelper.getTrailingCharCount (null, 'x'));
  }

  @Test
  public void testImplodeIterable ()
  {
    final List <String> aList = ContainerHelper.newList ("a", "b", "c");
    assertEquals ("", StringHelper.implode (".", (String []) null));
    assertEquals ("", StringHelper.implode (".", (List <String>) null));
    assertEquals ("a.b.c", StringHelper.implode (".", aList));
    assertEquals ("abc", StringHelper.implode ("", aList));
    assertEquals ("a.b.c", StringHelper.implode (".", aList.toArray (new String [3])));
    assertEquals ("abc", StringHelper.implode ("", aList.toArray (new String [3])));

    try
    {
      StringHelper.implode (null, aList);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  @SuppressWarnings ("TQ_NEVER_VALUE_USED_WHERE_ALWAYS_REQUIRED")
  public void testImplodeArray ()
  {
    final String [] aArray = new String [] { "a", "b", "c" };
    assertEquals ("a.b", StringHelper.implode (".", aArray, 0, 2));
    assertEquals ("b.c", StringHelper.implode (".", aArray, 1, 2));
    assertEquals ("", StringHelper.implode (".", aArray, 0, 0));
    assertEquals ("", StringHelper.implode (".", aArray, 2, 0));
    assertEquals ("", StringHelper.implode (".", null, 2, 0));

    try
    {
      StringHelper.implode (null, aArray, 2, 2);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      StringHelper.implode (".", aArray, -1, 2);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      StringHelper.implode (".", aArray, 0, -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      // too long
      StringHelper.implode (".", aArray, 2, 2);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      // too long
      StringHelper.implode (".", aArray, 0, 4);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      StringHelper.implode (null, aArray);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testImplodeMap ()
  {
    final Map <String, String> aMap = ContainerHelper.newOrderedMap ("a", "true", "b", "true", "c", "false");
    assertEquals ("atruebtruecfalse", StringHelper.implode ("", "", aMap));
    assertEquals ("atrue,btrue,cfalse", StringHelper.implode (",", "", aMap));
    assertEquals ("a,trueb,truec,false", StringHelper.implode ("", ",", aMap));
    assertEquals ("a,true,b,true,c,false", StringHelper.implode (",", ",", aMap));
    assertEquals ("a:true,b:true,c:false", StringHelper.implode (",", ":", aMap));

    try
    {
      StringHelper.implode (null, ":", aMap);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      StringHelper.implode (",", null, aMap);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testExplodeToList ()
  {
    List <String> ret = StringHelper.explode ("@", "a@b@@c");
    assertEquals (ContainerHelper.newList ("a", "b", "", "c"), ret);
    ret = StringHelper.explode ("uu", "auubuuuuuuc");
    assertEquals (ContainerHelper.newList ("a", "b", "", "", "c"), ret);
    ret = StringHelper.explode (".", "a.b...c");
    assertEquals (ContainerHelper.newList ("a", "b", "", "", "c"), ret);
    ret = StringHelper.explode ("o", "boo:and:foo");
    assertEquals (ContainerHelper.newList ("b", "", ":and:f", "", ""), ret);
    ret = StringHelper.explode ("@", "@a@b@@c");
    assertEquals (ContainerHelper.newList ("", "a", "b", "", "c"), ret);
    ret = StringHelper.explode ("@", "a@b@@c@");
    assertEquals (ContainerHelper.newList ("a", "b", "", "c", ""), ret);
    ret = StringHelper.explode ("@", "@a@b@@c@");
    assertEquals (ContainerHelper.newList ("", "a", "b", "", "c", ""), ret);
    assertTrue (StringHelper.explode ("@", null).isEmpty ());

    try
    {
      StringHelper.explode (null, "@a@b@@c@");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testExplodeToSet ()
  {
    Set <String> ret = StringHelper.explodeToSet ("@", "a@b@@c");
    assertEquals (ContainerHelper.newSet ("a", "b", "", "c"), ret);
    ret = StringHelper.explodeToSet ("uu", "auubuuuuuuc");
    assertEquals (ContainerHelper.newSet ("a", "b", "", "", "c"), ret);
    ret = StringHelper.explodeToSet (".", "a.b...c");
    assertEquals (ContainerHelper.newSet ("a", "b", "", "", "c"), ret);
    ret = StringHelper.explodeToSet ("o", "boo:and:foo");
    assertEquals (ContainerHelper.newSet ("b", "", ":and:f", "", ""), ret);
    ret = StringHelper.explodeToSet ("@", "@a@b@@c");
    assertEquals (ContainerHelper.newSet ("", "a", "b", "", "c"), ret);
    ret = StringHelper.explodeToSet ("@", "a@b@@c@");
    assertEquals (ContainerHelper.newSet ("a", "b", "", "c", ""), ret);
    ret = StringHelper.explodeToSet ("@", "@a@b@@c@");
    assertEquals (ContainerHelper.newSet ("", "a", "b", "", "c", ""), ret);
    assertTrue (StringHelper.explodeToSet ("@", null).isEmpty ());

    try
    {
      StringHelper.explodeToSet (null, "@a@b@@c@");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testExplodeToOrderedSet ()
  {
    Set <String> ret = StringHelper.explodeToOrderedSet ("@", "a@b@@c");
    assertEquals (ContainerHelper.newSet ("a", "b", "", "c"), ret);
    ret = StringHelper.explodeToOrderedSet ("uu", "auubuuuuuuc");
    assertEquals (ContainerHelper.newSet ("a", "b", "", "", "c"), ret);
    ret = StringHelper.explodeToOrderedSet (".", "a.b...c");
    assertEquals (ContainerHelper.newSet ("a", "b", "", "", "c"), ret);
    ret = StringHelper.explodeToOrderedSet ("o", "boo:and:foo");
    assertEquals (ContainerHelper.newSet ("b", "", ":and:f", "", ""), ret);
    ret = StringHelper.explodeToOrderedSet ("@", "@a@b@@c");
    assertEquals (ContainerHelper.newSet ("", "a", "b", "", "c"), ret);
    ret = StringHelper.explodeToOrderedSet ("@", "a@b@@c@");
    assertEquals (ContainerHelper.newSet ("a", "b", "", "c", ""), ret);
    ret = StringHelper.explodeToOrderedSet ("@", "@a@b@@c@");
    assertEquals (ContainerHelper.newSet ("", "a", "b", "", "c", ""), ret);
    assertTrue (StringHelper.explodeToOrderedSet ("@", null).isEmpty ());

    try
    {
      StringHelper.explodeToOrderedSet (null, "@a@b@@c@");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGetRepeated ()
  {
    assertEquals ("", StringHelper.getRepeated ('a', 0));
    assertEquals ("a", StringHelper.getRepeated ('a', 1));
    assertEquals ("aaa", StringHelper.getRepeated ('a', 3));
    assertEquals ("  ", StringHelper.getRepeated (' ', 2));
    try
    {
      StringHelper.getRepeated (' ', -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    assertEquals ("", StringHelper.getRepeated ("a", 0));
    assertEquals ("a", StringHelper.getRepeated ("a", 1));
    assertEquals ("aaa", StringHelper.getRepeated ("a", 3));
    assertEquals ("ababab", StringHelper.getRepeated ("ab", 3));
    assertEquals ("  ", StringHelper.getRepeated (" ", 2));
    try
    {
      StringHelper.getRepeated (null, 5);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      StringHelper.getRepeated (" ", -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testConcatenateOnDemand ()
  {
    assertEquals ("abc", StringHelper.concatenateOnDemand ("a", "b", "c"));
    assertEquals ("a", StringHelper.concatenateOnDemand ("a", "b", null));
    assertEquals ("a", StringHelper.concatenateOnDemand ("a", "b", ""));
    assertEquals ("c", StringHelper.concatenateOnDemand (null, "b", "c"));
    assertEquals ("c", StringHelper.concatenateOnDemand ("", "b", "c"));
    assertEquals ("ac", StringHelper.concatenateOnDemand ("a", "", "c"));
    assertEquals ("ac", StringHelper.concatenateOnDemand ("a", null, "c"));
    assertEquals ("bc", StringHelper.concatenateOnDemand (null, null, "bc"));
    assertEquals ("", StringHelper.concatenateOnDemand (null, null, null));
    assertEquals ("", StringHelper.concatenateOnDemand ("", "", ""));

    assertEquals ("a", StringHelper.concatenateOnDemand ("a", null));
    assertEquals ("a", StringHelper.concatenateOnDemand ("a", ""));
    assertEquals ("b", StringHelper.concatenateOnDemand (null, "b"));
    assertEquals ("b", StringHelper.concatenateOnDemand ("", "b"));
    assertEquals ("ab", StringHelper.concatenateOnDemand ("a", "b"));
    assertEquals ("", StringHelper.concatenateOnDemand (null, null));
    assertEquals ("", StringHelper.concatenateOnDemand ("", ""));
  }

  @Test
  public void testStartsWithChar ()
  {
    assertTrue (StringHelper.startsWith ("abc", 'a'));
    assertFalse (StringHelper.startsWith ("abc", 'b'));
    assertTrue (StringHelper.startsWith ("a", 'a'));
    assertFalse (StringHelper.startsWith ("", 'a'));
    assertFalse (StringHelper.startsWith (null, 'a'));

    final char [] aStart = new char [] { 'a', 'b', 'c' };
    assertTrue (StringHelper.startsWithAny ("abc", aStart));
    assertTrue (StringHelper.startsWithAny ("bbc", aStart));
    assertTrue (StringHelper.startsWithAny ("ccc", aStart));
    assertFalse (StringHelper.startsWithAny ("def", aStart));
    assertFalse (StringHelper.startsWithAny ("daabbcc", aStart));
    assertTrue (StringHelper.startsWithAny ("a", aStart));
    assertFalse (StringHelper.startsWithAny ("", aStart));
    assertFalse (StringHelper.startsWithAny (null, aStart));
    assertFalse (StringHelper.startsWithAny ("a", (char []) null));
    assertFalse (StringHelper.startsWithAny ("a", new char [0]));

    assertTrue (StringHelper.startsWithIgnoreCase ("abc", 'a'));
    assertFalse (StringHelper.startsWithIgnoreCase ("abc", 'b'));
    assertTrue (StringHelper.startsWithIgnoreCase ("a", 'a'));
    assertFalse (StringHelper.startsWithIgnoreCase ("", 'a'));
    assertFalse (StringHelper.startsWithIgnoreCase (null, 'a'));

    assertTrue (StringHelper.startsWithIgnoreCase ("ABC", 'a'));
    assertFalse (StringHelper.startsWithIgnoreCase ("ABC", 'b'));
    assertTrue (StringHelper.startsWithIgnoreCase ("A", 'a'));
    assertFalse (StringHelper.startsWithIgnoreCase ("", 'a'));
    assertFalse (StringHelper.startsWithIgnoreCase (null, 'a'));

    assertTrue (StringHelper.startsWithIgnoreCase ("abc", 'A'));
    assertFalse (StringHelper.startsWithIgnoreCase ("abc", 'B'));
    assertTrue (StringHelper.startsWithIgnoreCase ("a", 'A'));
    assertFalse (StringHelper.startsWithIgnoreCase ("", 'A'));
    assertFalse (StringHelper.startsWithIgnoreCase (null, 'A'));
  }

  @Test
  public void testStartsWithString ()
  {
    assertTrue (StringHelper.startsWith ("abc", "a"));
    assertTrue (StringHelper.startsWith ("abc", "ab"));
    assertTrue (StringHelper.startsWith ("abc", "abc"));
    assertFalse (StringHelper.startsWith ("abc", "b"));
    assertTrue (StringHelper.startsWith ("a", "a"));
    assertFalse (StringHelper.startsWith ("", "a"));
    assertFalse (StringHelper.startsWith (null, "a"));
    assertFalse (StringHelper.startsWith ("a", null));

    assertFalse (StringHelper.startsWith (null, null));
    assertTrue (StringHelper.startsWith ("", ""));

    assertFalse (StringHelper.startsWithIgnoreCase (null, null));
    assertTrue (StringHelper.startsWithIgnoreCase ("", ""));

    assertTrue (StringHelper.startsWithIgnoreCase ("abc", "a"));
    assertTrue (StringHelper.startsWithIgnoreCase ("abc", "ab"));
    assertTrue (StringHelper.startsWithIgnoreCase ("abc", "abc"));
    assertFalse (StringHelper.startsWithIgnoreCase ("abc", "b"));
    assertTrue (StringHelper.startsWithIgnoreCase ("a", "a"));
    assertFalse (StringHelper.startsWithIgnoreCase ("", "a"));
    assertFalse (StringHelper.startsWithIgnoreCase (null, "a"));
    assertFalse (StringHelper.startsWithIgnoreCase ("a", null));

    assertTrue (StringHelper.startsWithIgnoreCase ("ABC", "a"));
    assertTrue (StringHelper.startsWithIgnoreCase ("ABC", "ab"));
    assertTrue (StringHelper.startsWithIgnoreCase ("ABC", "abc"));
    assertFalse (StringHelper.startsWithIgnoreCase ("ABC", "b"));
    assertTrue (StringHelper.startsWithIgnoreCase ("A", "a"));
    assertFalse (StringHelper.startsWithIgnoreCase ("", "a"));
    assertFalse (StringHelper.startsWithIgnoreCase (null, "a"));
    assertFalse (StringHelper.startsWithIgnoreCase ("A", null));

    assertTrue (StringHelper.startsWithIgnoreCase ("abc", "A"));
    assertTrue (StringHelper.startsWithIgnoreCase ("abc", "AB"));
    assertTrue (StringHelper.startsWithIgnoreCase ("abc", "ABC"));
    assertFalse (StringHelper.startsWithIgnoreCase ("abc", "B"));
    assertTrue (StringHelper.startsWithIgnoreCase ("a", "A"));
    assertFalse (StringHelper.startsWithIgnoreCase ("", "A"));
    assertFalse (StringHelper.startsWithIgnoreCase (null, "A"));
    assertFalse (StringHelper.startsWithIgnoreCase ("a", null));
  }

  @Test
  public void testEndsWithChar ()
  {
    assertTrue (StringHelper.endsWith ("abc", 'c'));
    assertFalse (StringHelper.endsWith ("abc", 'b'));
    assertTrue (StringHelper.endsWith ("a", 'a'));
    assertFalse (StringHelper.endsWith ("", 'a'));
    assertFalse (StringHelper.endsWith (null, 'a'));
    assertFalse (StringHelper.endsWith (null, null));
    assertTrue (StringHelper.endsWith ("", ""));

    final char [] aEnd = new char [] { 'a', 'b', 'c' };
    assertTrue (StringHelper.endsWithAny ("abc", aEnd));
    assertTrue (StringHelper.endsWithAny ("aab", aEnd));
    assertTrue (StringHelper.endsWithAny ("aaa", aEnd));
    assertFalse (StringHelper.endsWithAny ("aad", aEnd));
    assertTrue (StringHelper.endsWithAny ("a", aEnd));
    assertFalse (StringHelper.endsWithAny ("", aEnd));
    assertFalse (StringHelper.endsWithAny (null, aEnd));
    assertFalse (StringHelper.endsWithAny ("a", (char []) null));
    assertFalse (StringHelper.endsWithAny ("a", new char [0]));

    assertFalse (StringHelper.endsWithIgnoreCase (null, null));
    assertTrue (StringHelper.endsWithIgnoreCase ("", ""));

    assertTrue (StringHelper.endsWithIgnoreCase ("abc", 'c'));
    assertFalse (StringHelper.endsWithIgnoreCase ("abc", 'b'));
    assertTrue (StringHelper.endsWithIgnoreCase ("a", 'a'));
    assertFalse (StringHelper.endsWithIgnoreCase ("", 'a'));
    assertFalse (StringHelper.endsWithIgnoreCase (null, 'a'));

    assertTrue (StringHelper.endsWithIgnoreCase ("ABC", 'c'));
    assertFalse (StringHelper.endsWithIgnoreCase ("ABC", 'b'));
    assertTrue (StringHelper.endsWithIgnoreCase ("A", 'a'));
    assertFalse (StringHelper.endsWithIgnoreCase ("", 'a'));
    assertFalse (StringHelper.endsWithIgnoreCase (null, 'a'));

    assertTrue (StringHelper.endsWithIgnoreCase ("abc", 'C'));
    assertFalse (StringHelper.endsWithIgnoreCase ("abc", 'B'));
    assertTrue (StringHelper.endsWithIgnoreCase ("a", 'A'));
    assertFalse (StringHelper.endsWithIgnoreCase ("", 'A'));
    assertFalse (StringHelper.endsWithIgnoreCase (null, 'A'));
  }

  @Test
  public void testEndsWithString ()
  {
    assertTrue (StringHelper.endsWith ("abc", "c"));
    assertTrue (StringHelper.endsWith ("abc", "bc"));
    assertTrue (StringHelper.endsWith ("abc", "abc"));
    assertFalse (StringHelper.endsWith ("abc", "b"));
    assertTrue (StringHelper.endsWith ("a", "a"));
    assertFalse (StringHelper.endsWith ("", "a"));
    assertFalse (StringHelper.endsWith (null, "a"));
    assertFalse (StringHelper.endsWith ("a", null));

    assertTrue (StringHelper.endsWithIgnoreCase ("abc", "c"));
    assertTrue (StringHelper.endsWithIgnoreCase ("abc", "bc"));
    assertTrue (StringHelper.endsWithIgnoreCase ("abc", "abc"));
    assertFalse (StringHelper.endsWithIgnoreCase ("abc", "b"));
    assertTrue (StringHelper.endsWithIgnoreCase ("a", "a"));
    assertFalse (StringHelper.endsWithIgnoreCase ("", "a"));
    assertFalse (StringHelper.endsWithIgnoreCase (null, "a"));
    assertFalse (StringHelper.endsWithIgnoreCase ("a", null));

    assertTrue (StringHelper.endsWithIgnoreCase ("ABC", "c"));
    assertTrue (StringHelper.endsWithIgnoreCase ("ABC", "bc"));
    assertTrue (StringHelper.endsWithIgnoreCase ("ABC", "abc"));
    assertFalse (StringHelper.endsWithIgnoreCase ("ABC", "b"));
    assertTrue (StringHelper.endsWithIgnoreCase ("A", "a"));
    assertFalse (StringHelper.endsWithIgnoreCase ("", "a"));
    assertFalse (StringHelper.endsWithIgnoreCase (null, "a"));
    assertFalse (StringHelper.endsWithIgnoreCase ("A", null));

    assertTrue (StringHelper.endsWithIgnoreCase ("abc", "C"));
    assertTrue (StringHelper.endsWithIgnoreCase ("abc", "BC"));
    assertTrue (StringHelper.endsWithIgnoreCase ("abc", "ABC"));
    assertFalse (StringHelper.endsWithIgnoreCase ("abc", "B"));
    assertTrue (StringHelper.endsWithIgnoreCase ("a", "A"));
    assertFalse (StringHelper.endsWithIgnoreCase ("", "A"));
    assertFalse (StringHelper.endsWithIgnoreCase (null, "A"));
    assertFalse (StringHelper.endsWithIgnoreCase ("a", null));
  }

  @Test
  public void testIndexOfString ()
  {
    assertEquals (-1, StringHelper.indexOf (null, null));
    assertEquals (-1, StringHelper.indexOf (null, "a"));
    assertEquals (-1, StringHelper.indexOf ("b", null));
    assertEquals (-1, StringHelper.indexOf ("b", "cd"));
    assertEquals (-1, StringHelper.indexOf ("bla foo", "z"));
    assertEquals (0, StringHelper.indexOf ("bla foo", "b"));
    assertEquals (2, StringHelper.indexOf ("bla foo", "a"));
  }

  @Test
  public void testIndexOfChar ()
  {
    assertEquals (-1, StringHelper.indexOf (null, '\0'));
    assertEquals (-1, StringHelper.indexOf (null, 'a'));
    assertEquals (-1, StringHelper.indexOf ("b", '\0'));
    assertEquals (-1, StringHelper.indexOf ("b", 'c'));
    assertEquals (-1, StringHelper.indexOf ("bla foo", 'z'));
    assertEquals (0, StringHelper.indexOf ("bla foo", 'b'));
    assertEquals (2, StringHelper.indexOf ("bla foo", 'a'));
  }

  @Test
  public void testContainsString ()
  {
    assertTrue (StringHelper.contains ("Test", "Test"));
    assertTrue (StringHelper.contains ("Test", "est"));
    assertTrue (StringHelper.contains ("Test", "Tes"));
    assertTrue (StringHelper.contains ("Test", "es"));
    assertTrue (StringHelper.contains ("Test", ""));

    assertFalse (StringHelper.contains ("Test", null));
    assertFalse (StringHelper.contains (null, "Test"));
    assertFalse (StringHelper.contains ("Tes", "Test"));
    assertFalse (StringHelper.contains ("est", "Test"));
    assertFalse (StringHelper.contains ("es", "Test"));
    assertFalse (StringHelper.contains ("", "Test"));

    assertFalse (StringHelper.contains ("Test", "TEST"));
    assertFalse (StringHelper.contains ("Test", "EST"));
    assertFalse (StringHelper.contains ("Test", "TES"));
    assertFalse (StringHelper.contains ("Test", "ES"));
  }

  @Test
  public void testContainsChar ()
  {
    assertTrue (StringHelper.contains ("Test", 'T'));
    assertTrue (StringHelper.contains ("Test", 'e'));
    assertTrue (StringHelper.contains ("Test", 's'));
    assertTrue (StringHelper.contains ("Test", 't'));
    assertFalse (StringHelper.contains ("Test", '\0'));

    assertFalse (StringHelper.contains ("Test", null));
    assertFalse (StringHelper.contains (null, 'T'));
  }

  @Test
  public void testIndexOfIgnoreCaseString ()
  {
    assertEquals (-1, StringHelper.indexOfIgnoreCase (null, null, L_DE));
    assertEquals (-1, StringHelper.indexOfIgnoreCase (null, "a", L_DE));
    assertEquals (-1, StringHelper.indexOfIgnoreCase ("b", null, L_DE));
    assertEquals (-1, StringHelper.indexOfIgnoreCase ("b", "cd", L_DE));
    assertEquals (-1, StringHelper.indexOfIgnoreCase ("bla foo", "z", L_DE));
    assertEquals (0, StringHelper.indexOfIgnoreCase ("bla foo", "b", L_DE));
    assertEquals (2, StringHelper.indexOfIgnoreCase ("bla foo", "a", L_DE));
    assertEquals (0, StringHelper.indexOfIgnoreCase ("bla foo", "B", L_DE));
    assertEquals (2, StringHelper.indexOfIgnoreCase ("bla foo", "A", L_DE));
    assertEquals (0, StringHelper.indexOfIgnoreCase ("BLA FOO", "b", L_DE));
    assertEquals (2, StringHelper.indexOfIgnoreCase ("BLA FOO", "a", L_DE));
  }

  @Test
  public void testContainsIgnoreCaseString ()
  {
    final Locale aLocale = Locale.ENGLISH;
    assertTrue (StringHelper.containsIgnoreCase ("Test", "Test", aLocale));
    assertTrue (StringHelper.containsIgnoreCase ("Test", "est", aLocale));
    assertTrue (StringHelper.containsIgnoreCase ("Test", "Tes", aLocale));
    assertTrue (StringHelper.containsIgnoreCase ("Test", "es", aLocale));
    assertTrue (StringHelper.containsIgnoreCase ("Test", "", aLocale));

    assertFalse (StringHelper.containsIgnoreCase ("Test", null, aLocale));
    assertFalse (StringHelper.containsIgnoreCase (null, "Test", aLocale));
    assertFalse (StringHelper.containsIgnoreCase ("Tes", "Test", aLocale));
    assertFalse (StringHelper.containsIgnoreCase ("est", "Test", aLocale));
    assertFalse (StringHelper.containsIgnoreCase ("es", "Test", aLocale));
    assertFalse (StringHelper.containsIgnoreCase ("", "Test", aLocale));

    assertTrue (StringHelper.containsIgnoreCase ("Test", "TEST", aLocale));
    assertTrue (StringHelper.containsIgnoreCase ("Test", "EST", aLocale));
    assertTrue (StringHelper.containsIgnoreCase ("Test", "TES", aLocale));
    assertTrue (StringHelper.containsIgnoreCase ("Test", "ES", aLocale));
  }

  @Test
  public void testGetOccurrenceCount ()
  {
    assertEquals (0, StringHelper.getOccurrenceCount ("Test", null));
    assertEquals (0, StringHelper.getOccurrenceCount (null, "Test"));
    assertEquals (1, StringHelper.getOccurrenceCount ("Test", "Test"));
    assertEquals (1, StringHelper.getOccurrenceCount ("Test", "Tes"));
    assertEquals (1, StringHelper.getOccurrenceCount ("Test", "est"));
    assertEquals (1, StringHelper.getOccurrenceCount ("Test", "es"));
    assertEquals (2, StringHelper.getOccurrenceCount ("Testen", "e"));
    assertEquals (0, StringHelper.getOccurrenceCount ("Testen", ""));
    assertEquals (4, StringHelper.getOccurrenceCount ("eeee", "e"));
    assertEquals (2, StringHelper.getOccurrenceCount ("eeee", "ee"));
    assertEquals (1, StringHelper.getOccurrenceCount ("eeee", "eee"));

    // Invalid case
    assertEquals (0, StringHelper.getOccurrenceCount ("eeee", "E"));
    assertEquals (0, StringHelper.getOccurrenceCount ("eeee", "EE"));
    assertEquals (0, StringHelper.getOccurrenceCount ("eeee", "EEE"));
  }

  @Test
  public void testGetOccurrenceCountIgnoreCase ()
  {
    final Locale aLocale = Locale.ENGLISH;
    assertEquals (0, StringHelper.getOccurrenceCountIgnoreCase ("Test", null, aLocale));
    assertEquals (0, StringHelper.getOccurrenceCountIgnoreCase (null, "Test", aLocale));
    assertEquals (1, StringHelper.getOccurrenceCountIgnoreCase ("Test", "Test", aLocale));
    assertEquals (1, StringHelper.getOccurrenceCountIgnoreCase ("Test", "Tes", aLocale));
    assertEquals (1, StringHelper.getOccurrenceCountIgnoreCase ("Test", "est", aLocale));
    assertEquals (1, StringHelper.getOccurrenceCountIgnoreCase ("Test", "es", aLocale));
    assertEquals (2, StringHelper.getOccurrenceCountIgnoreCase ("Testen", "e", aLocale));
    assertEquals (0, StringHelper.getOccurrenceCountIgnoreCase ("Testen", "", aLocale));
    assertEquals (4, StringHelper.getOccurrenceCountIgnoreCase ("eeee", "e", aLocale));
    assertEquals (2, StringHelper.getOccurrenceCountIgnoreCase ("eeee", "ee", aLocale));
    assertEquals (1, StringHelper.getOccurrenceCountIgnoreCase ("eeee", "eee", aLocale));

    // Ignoring case
    assertEquals (2, StringHelper.getOccurrenceCountIgnoreCase ("Test", "t", aLocale));
    assertEquals (4, StringHelper.getOccurrenceCountIgnoreCase ("eeee", "E", aLocale));
    assertEquals (2, StringHelper.getOccurrenceCountIgnoreCase ("eeee", "EE", aLocale));
    assertEquals (1, StringHelper.getOccurrenceCountIgnoreCase ("eeee", "EEE", aLocale));
  }

  @Test
  public void testTrimLeadingWhitespaces ()
  {
    assertEquals ("Hallo Welt", StringHelper.trimLeadingWhitespaces ("Hallo Welt"));
    assertEquals ("Hallo Welt ", StringHelper.trimLeadingWhitespaces (" Hallo Welt "));
    assertEquals ("Hallo Welt  ", StringHelper.trimLeadingWhitespaces ("  Hallo Welt  "));
    assertEquals ("", StringHelper.trimLeadingWhitespaces ("  "));
    assertEquals ("", StringHelper.trimLeadingWhitespaces (""));
    assertSame (null, StringHelper.trimLeadingWhitespaces (null));
  }

  @Test
  public void testTrimTrailingWhitespaces ()
  {
    assertEquals ("Hallo Welt", StringHelper.trimTrailingWhitespaces ("Hallo Welt"));
    assertEquals (" Hallo Welt", StringHelper.trimTrailingWhitespaces (" Hallo Welt "));
    assertEquals ("  Hallo Welt", StringHelper.trimTrailingWhitespaces ("  Hallo Welt  "));
    assertEquals ("", StringHelper.trimTrailingWhitespaces ("  "));
    assertEquals ("", StringHelper.trimTrailingWhitespaces (""));
    assertSame (null, StringHelper.trimTrailingWhitespaces (null));
  }

  @Test
  public void testTrimEnd ()
  {
    assertEquals ("Hallo Welt", StringHelper.trimEnd ("Hallo Welt", ""));
    assertEquals ("Hallo Welt", StringHelper.trimEnd ("Hallo Welt", "asd"));
    assertEquals (" Hallo We", StringHelper.trimEnd (" Hallo Welt", "lt"));
    assertEquals ("Hallo Wel", StringHelper.trimEnd ("Hallo Welt", "t"));
    assertEquals ("", StringHelper.trimEnd ("", "lt"));
    assertEquals ("", StringHelper.trimEnd ("", ""));
    assertSame (null, StringHelper.trimEnd (null, null));
  }

  @Test
  public void testTrimStart ()
  {
    assertEquals ("Hallo Welt", StringHelper.trimStart ("Hallo Welt", ""));
    assertEquals ("Hallo Welt", StringHelper.trimStart ("Hallo Welt", "asd"));
    assertEquals ("allo Welt", StringHelper.trimStart (" Hallo Welt", " H"));
    assertEquals ("allo Welt", StringHelper.trimStart ("Hallo Welt", "H"));
    assertEquals ("", StringHelper.trimStart ("", "lt"));
    assertEquals ("", StringHelper.trimStart ("", ""));
    assertSame (null, StringHelper.trimStart (null, null));
  }

  @Test
  public void testTrim ()
  {
    assertEquals ("Hallo Welt", StringHelper.trim ("Hallo Welt"));
    assertEquals ("Hallo Welt", StringHelper.trim (" Hallo Welt"));
    assertEquals ("Hallo Welt", StringHelper.trim ("Hallo Welt "));
    assertEquals ("Hallo Welt", StringHelper.trim (" Hallo Welt "));
    assertEquals ("Hallo Welt", StringHelper.trim ("   Hallo Welt   "));
    assertEquals ("", StringHelper.trim (""));
    assertEquals ("", StringHelper.trim (""));
    assertSame (null, StringHelper.trim (null));
  }

  @Test
  public void testGetFirstChar ()
  {
    assertEquals ('a', StringHelper.getFirstChar ("abc"));
    assertEquals ('a', StringHelper.getFirstChar ("a"));
    assertEquals (CGlobal.ILLEGAL_CHAR, StringHelper.getFirstChar (""));
    assertEquals (CGlobal.ILLEGAL_CHAR, StringHelper.getFirstChar (null));
  }

  @Test
  public void testGetLastChar ()
  {
    assertEquals ('c', StringHelper.getLastChar ("abc"));
    assertEquals ('a', StringHelper.getLastChar ("a"));
    assertEquals (CGlobal.ILLEGAL_CHAR, StringHelper.getLastChar (""));
    assertEquals (CGlobal.ILLEGAL_CHAR, StringHelper.getLastChar (null));
  }

  @Test
  public void testGetCharCount ()
  {
    assertEquals (0, StringHelper.getCharCount ("abc", 'x'));
    assertEquals (1, StringHelper.getCharCount ("xabc", 'x'));
    assertEquals (1, StringHelper.getCharCount ("abxc", 'x'));
    assertEquals (1, StringHelper.getCharCount ("abcx", 'x'));
    assertEquals (0, StringHelper.getCharCount (null, 'x'));
    assertEquals (0, StringHelper.getCharCount ("", 'x'));
    for (int i = 0; i < 1000; ++i)
      assertEquals (i, StringHelper.getCharCount (StringHelper.getRepeated ('x', i), 'x'));
  }

  @Test
  public void testGetLineCount ()
  {
    assertEquals (1, StringHelper.getLineCount ("abc"));
    assertEquals (2, StringHelper.getLineCount ("ab\nc"));
    assertEquals (2, StringHelper.getLineCount ("ab\r\nc"));
    assertEquals (1, StringHelper.getLineCount ("ab\rc"));
  }

  @Test
  public void testGetCharacterCountInt ()
  {
    int iVal = 1;
    for (int i = 1; i <= 10; ++i)
    {
      assertEquals (i, StringHelper.getCharacterCount (iVal));
      iVal *= 10;
    }
    iVal = -1;
    for (int i = 1; i <= 10; ++i)
    {
      assertEquals (1 + i, StringHelper.getCharacterCount (iVal));
      iVal *= 10;
    }
    assertEquals (11, StringHelper.getCharacterCount (Integer.MIN_VALUE + 1));
    assertEquals (10, StringHelper.getCharacterCount (Integer.MAX_VALUE));
  }

  @Test
  public void testGetCharacterCountLong ()
  {
    long lVal = 1;
    for (int i = 1; i <= 19; ++i)
    {
      assertEquals (i, StringHelper.getCharacterCount (lVal));
      lVal *= 10;
    }
    lVal = -1;
    for (int i = 1; i <= 19; ++i)
    {
      assertEquals (1 + i, StringHelper.getCharacterCount (lVal));
      lVal *= 10;
    }
    assertEquals (20, StringHelper.getCharacterCount (Long.MIN_VALUE + 1));
    assertEquals (19, StringHelper.getCharacterCount (Long.MAX_VALUE));
  }

  @Test
  public void testCutAfterLength ()
  {
    assertEquals ("abc...", StringHelper.cutAfterLength ("abc die Katze lief im Schnee", 3, "..."));
    assertEquals ("ab", StringHelper.cutAfterLength ("ab", 3, "..."));
    assertEquals ("abc", StringHelper.cutAfterLength ("abc", 3, "..."));
    assertEquals ("", StringHelper.cutAfterLength ("", 3, "..."));
    assertEquals ("abc", StringHelper.cutAfterLength ("abcdef", 3, ""));

    try
    {
      StringHelper.cutAfterLength (null, 3, "...");
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      StringHelper.cutAfterLength ("abc", -1, "...");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      StringHelper.cutAfterLength ("abc", 3, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGetNotNullString ()
  {
    assertEquals ("abc", StringHelper.getNotNull ("abc"));
    assertEquals ("", StringHelper.getNotNull (""));
    assertEquals ("", StringHelper.getNotNull (null));
    assertEquals ("xy", StringHelper.getNotNull (null, "xy"));
  }

  @Test
  public void testGetNotNullCharSeq ()
  {
    assertEquals ("abc", StringHelper.getNotNull (new StringBuilder ("abc")).toString ());
    assertEquals ("", StringHelper.getNotNull (new StringBuilder ()).toString ());
    assertEquals ("", StringHelper.getNotNull ((StringBuilder) null));
  }

  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testReplaceAllString ()
  {
    assertEquals ("abc", StringHelper.replaceAll ("abc", "d", "e"));
    assertEquals ("abd", StringHelper.replaceAll ("abc", "c", "d"));
    assertEquals ("adc", StringHelper.replaceAll ("abc", "b", "d"));
    assertEquals ("dbc", StringHelper.replaceAll ("abc", "a", "d"));
    assertEquals ("ddd", StringHelper.replaceAll ("aaa", "a", "d"));
    assertEquals ("xyxyxy", StringHelper.replaceAll ("aaa", "a", "xy"));
    assertEquals ("", StringHelper.replaceAll ("", "anything", "nothing"));
    assertEquals ("", StringHelper.replaceAll ("aaa", "a", ""));
    assertEquals ("bb", StringHelper.replaceAll ("ababa", "a", ""));
    assertEquals ("acd", StringHelper.replaceAll ("abcd", "ab", "a"));
    assertEquals ("abd", StringHelper.replaceAll ("abcd", "bc", "b"));
    assertEquals ("abc", StringHelper.replaceAll ("abcd", "cd", "c"));
    assertEquals ("abc", StringHelper.replaceAll ("abcd", "d", ""));
    assertEquals ("bcbc", StringHelper.replaceAll ("bcbcbc", "bcbc", "bc"));
    assertEquals ("aa", StringHelper.replaceAll ("aaaa", "aa", "a"));
    assertEquals ("a  a b ", StringHelper.replaceAll ("a    a  b ", "  ", " "));
    assertNull (StringHelper.replaceAll (null, "aa", "a"));
    assertEquals ("aaaa", StringHelper.replaceAll ("aaaa", "aa", "aa"));

    try
    {
      StringHelper.replaceAll ("aaaaach", null, "a");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      StringHelper.replaceAll ("aaaaach", "aa", null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testReplaceAllChar ()
  {
    assertEquals ("abc", StringHelper.replaceAll ("abc", 'd', 'e'));
    assertEquals ("abd", StringHelper.replaceAll ("abc", 'c', 'd'));
    assertEquals ("adc", StringHelper.replaceAll ("abc", 'b', 'd'));
    assertEquals ("dbc", StringHelper.replaceAll ("abc", 'a', 'd'));
    assertEquals ("ddd", StringHelper.replaceAll ("aaa", 'a', 'd'));
    assertEquals ("", StringHelper.replaceAll ("", 'a', 'b'));
    assertEquals ("aaa", StringHelper.replaceAll ("aaa", 'a', 'a'));
    assertEquals ("aaa", StringHelper.replaceAll ("aaa", 'b', 'b'));
    assertEquals ("bbbbb", StringHelper.replaceAll ("ababa", 'a', 'b'));
    assertEquals ("\0b\0b\0", StringHelper.replaceAll ("ababa", 'a', '\0'));
  }

  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testReplaceAllSafe ()
  {
    assertEquals ("abc", StringHelper.replaceAllSafe ("abc", "d", "e"));
    assertEquals ("abd", StringHelper.replaceAllSafe ("abc", "c", "d"));
    assertEquals ("adc", StringHelper.replaceAllSafe ("abc", "b", "d"));
    assertEquals ("dbc", StringHelper.replaceAllSafe ("abc", "a", "d"));
    assertEquals ("ddd", StringHelper.replaceAllSafe ("aaa", "a", "d"));
    assertEquals ("xyxyxy", StringHelper.replaceAllSafe ("aaa", "a", "xy"));
    assertEquals ("", StringHelper.replaceAllSafe ("", "anything", "nothing"));
    assertEquals ("", StringHelper.replaceAllSafe ("aaa", "a", ""));
    assertEquals ("bb", StringHelper.replaceAllSafe ("ababa", "a", ""));
    assertEquals ("acd", StringHelper.replaceAllSafe ("abcd", "ab", "a"));
    assertEquals ("abd", StringHelper.replaceAllSafe ("abcd", "bc", "b"));
    assertEquals ("abc", StringHelper.replaceAllSafe ("abcd", "cd", "c"));
    assertEquals ("abc", StringHelper.replaceAllSafe ("abcd", "d", ""));
    assertEquals ("bcbc", StringHelper.replaceAllSafe ("bcbcbc", "bcbc", "bc"));
    assertEquals ("aa", StringHelper.replaceAllSafe ("aaaa", "aa", "a"));
    assertEquals ("a  a b ", StringHelper.replaceAllSafe ("a    a  b ", "  ", " "));
    assertNull (StringHelper.replaceAllSafe (null, "aa", "a"));
    assertEquals ("aaaa", StringHelper.replaceAllSafe ("aaaa", "aa", "aa"));

    try
    {
      StringHelper.replaceAllSafe ("aaaaach", null, "a");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    assertEquals ("ach", StringHelper.replaceAllSafe ("aaaaach", "aa", null));
  }

  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testReplaceAllRepeatedly ()
  {
    assertEquals ("abc", StringHelper.replaceAllRepeatedly ("abc", "d", "e"));
    assertEquals ("dbc", StringHelper.replaceAllRepeatedly ("abc", "a", "d"));
    assertEquals ("ddd", StringHelper.replaceAllRepeatedly ("aaa", "a", "d"));
    assertEquals ("a a b ", StringHelper.replaceAllRepeatedly ("a    a  b ", "  ", " "));
    assertEquals ("", StringHelper.replaceAllRepeatedly ("", " a", "b"));
    assertNull (StringHelper.replaceAllRepeatedly (null, " a", "b"));

    try
    {
      StringHelper.replaceAllRepeatedly ("aaaaach", null, "a");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      StringHelper.replaceAllRepeatedly ("aaaaach", "aa", null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      StringHelper.replaceAllRepeatedly ("aaaaach", "a", "aa");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testReplaceMultipleMap ()
  {
    final Map <String, String> aMap = new HashMap <String, String> ();
    aMap.put ("Hallo", "Hi");
    aMap.put ("Welt", "world");
    aMap.put ("!", "???");
    assertEquals ("Abc die Katze lief im Schnee", StringHelper.replaceMultiple ("Abc die Katze lief im Schnee", aMap));
    assertEquals ("Hi Katze", StringHelper.replaceMultiple ("Hallo Katze", aMap));
    assertEquals ("Moin world", StringHelper.replaceMultiple ("Moin Welt", aMap));
    assertEquals ("Moin welt", StringHelper.replaceMultiple ("Moin welt", aMap));
    assertEquals ("Hi", StringHelper.replaceMultiple ("Hallo", aMap));
    assertEquals ("Hi Hi", StringHelper.replaceMultiple ("Hallo Hallo", aMap));
    assertEquals ("HiHiHi", StringHelper.replaceMultiple ("HalloHalloHallo", aMap));
    assertEquals ("Hi world???", StringHelper.replaceMultiple ("Hallo Welt!", aMap));
    assertEquals ("Hi world???Hi world???", StringHelper.replaceMultiple ("Hallo Welt!Hallo Welt!", aMap));
  }

  @Test
  public void testReplaceMultipleCharArrays ()
  {
    assertArrayEquals ("bb".toCharArray (),
                       StringHelper.replaceMultiple ("a", new char [] { 'a' }, new char [] [] { "bb".toCharArray () }));
    assertArrayEquals ("bbbb".toCharArray (),
                       StringHelper.replaceMultiple ("aa", new char [] { 'a' }, new char [] [] { "bb".toCharArray () }));
    assertArrayEquals ("cdc".toCharArray (),
                       StringHelper.replaceMultiple ("cdc", new char [] { 'a' }, new char [] [] { "bb".toCharArray () }));
    assertArrayEquals ("cbbc".toCharArray (),
                       StringHelper.replaceMultiple ("cac", new char [] { 'a' }, new char [] [] { "bb".toCharArray () }));
    assertArrayEquals ("ddbbdd".toCharArray (),
                       StringHelper.replaceMultiple ("cac",
                                                     new char [] { 'a', 'c' },
                                                     new char [] [] { "bb".toCharArray (), "dd".toCharArray () }));
    assertArrayEquals ("<ddbbdd>".toCharArray (),
                       StringHelper.replaceMultiple ("<cac>",
                                                     new char [] { 'a', 'c' },
                                                     new char [] [] { "bb".toCharArray (), "dd".toCharArray () }));
    assertArrayEquals (new char [0], StringHelper.replaceMultiple ("",
                                                                   new char [] { 'a', 'c' },
                                                                   new char [] [] { "bb".toCharArray (),
                                                                                   "dd".toCharArray () }));
    assertArrayEquals ("any".toCharArray (), StringHelper.replaceMultiple ("any", new char [0], new char [0] []));
    try
    {
      StringHelper.replaceMultiple ("any", (char []) null, new char [] [] { "bb".toCharArray (), "dd".toCharArray () });
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      StringHelper.replaceMultiple ("any", "an".toCharArray (), null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      StringHelper.replaceMultiple ("any", new char [1], new char [2] []);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testReplaceMultipleTo () throws IOException
  {
    NonBlockingStringWriter aSW = new NonBlockingStringWriter ();
    StringHelper.replaceMultipleTo ("a", new char [] { 'a' }, new char [] [] { "bb".toCharArray () }, aSW);
    assertEquals ("bb", aSW.toString ());

    aSW = new NonBlockingStringWriter ();
    StringHelper.replaceMultipleTo ("aa", new char [] { 'a' }, new char [] [] { "bb".toCharArray () }, aSW);
    assertEquals ("bbbb", aSW.toString ());

    aSW = new NonBlockingStringWriter ();
    StringHelper.replaceMultipleTo ("cdc", new char [] { 'a' }, new char [] [] { "bb".toCharArray () }, aSW);
    assertEquals ("cdc", aSW.toString ());

    aSW = new NonBlockingStringWriter ();
    StringHelper.replaceMultipleTo ("cac", new char [] { 'a' }, new char [] [] { "bb".toCharArray () }, aSW);
    assertEquals ("cbbc", aSW.toString ());

    aSW = new NonBlockingStringWriter ();
    StringHelper.replaceMultipleTo ("cac",
                                    new char [] { 'a', 'c' },
                                    new char [] [] { "bb".toCharArray (), "dd".toCharArray () },
                                    aSW);
    assertEquals ("ddbbdd", aSW.toString ());

    aSW = new NonBlockingStringWriter ();
    StringHelper.replaceMultipleTo ("<cac>",
                                    new char [] { 'a', 'c' },
                                    new char [] [] { "bb".toCharArray (), "dd".toCharArray () },
                                    aSW);
    assertEquals ("<ddbbdd>", aSW.toString ());

    aSW = new NonBlockingStringWriter ();
    StringHelper.replaceMultipleTo ("",
                                    new char [] { 'a', 'c' },
                                    new char [] [] { "bb".toCharArray (), "dd".toCharArray () },
                                    aSW);
    assertEquals ("", aSW.toString ());

    aSW = new NonBlockingStringWriter ();
    StringHelper.replaceMultipleTo ("any", new char [0], new char [0] [], aSW);
    assertEquals ("any", aSW.toString ());

    try
    {
      StringHelper.replaceMultipleTo ("any", null, new char [0] [], aSW);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      StringHelper.replaceMultipleTo ("any", new char [0], null, aSW);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      StringHelper.replaceMultipleTo ("any", new char [0], new char [1] [], aSW);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      StringHelper.replaceMultipleTo ("any", new char [0], new char [0] [], null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testToString ()
  {
    assertEquals ("1", StringHelper.toString (I1));
    assertEquals ("any", StringHelper.toString ("any"));
    assertEquals ("", StringHelper.toString (null));

    assertEquals ("1", StringHelper.toString (I1, "default"));
    assertEquals ("any", StringHelper.toString ("any", "default"));
    assertEquals ("default", StringHelper.toString (null, "default"));

    assertEquals ("1", StringHelper.toString (I1, null));
    assertEquals ("any", StringHelper.toString ("any", null));
    assertNull (StringHelper.toString (null, null));
  }

  @Test
  public void testMultiContains ()
  {
    final char [] aIn = "abcde".toCharArray ();
    assertTrue (StringHelper.multiContains (aIn, "a".toCharArray ()));
    assertFalse (StringHelper.multiContains (aIn, "z".toCharArray ()));
    assertFalse (StringHelper.multiContains (aIn, new char [0]));
    assertFalse (StringHelper.multiContains (new char [0], "a".toCharArray ()));
  }
}
