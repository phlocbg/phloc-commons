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
package com.phloc.commons.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.xml.XMLConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.string.StringHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class contains multiple XML utility methods.
 * 
 * @author Philip Helger
 */
@Immutable
public final class XMLHelper
{
  // Order is important!
  // Note: for performance reasons they are all char arrays!
  private static final char [] MASK_PATTERNS_XML10 = new char [] { 0, '&', '<', '>', '"', '\'' };

  // Control characters (except 9 - \t and 10 - \n and 13 - \r)
  private static final char [] MASK_PATTERNS_CONTROL = new char [] { 1, 2, 3, 4, 5, 6, 7, 8,
                                                                    // 9 and 10
                                                                    // are OK
                                                                    11,
                                                                    12,
                                                                    13,
                                                                    14,
                                                                    15,
                                                                    16,
                                                                    17,
                                                                    18,
                                                                    19,
                                                                    20,
                                                                    21,
                                                                    22,
                                                                    23,
                                                                    24,
                                                                    25,
                                                                    26,
                                                                    27,
                                                                    28,
                                                                    29,
                                                                    30,
                                                                    31,
                                                                    127,
                                                                    128,
                                                                    129,
                                                                    130,
                                                                    131,
                                                                    132,
                                                                    133,
                                                                    134,
                                                                    135,
                                                                    136,
                                                                    137,
                                                                    138,
                                                                    139,
                                                                    140,
                                                                    141,
                                                                    142,
                                                                    143,
                                                                    144,
                                                                    145,
                                                                    146,
                                                                    147,
                                                                    148,
                                                                    149,
                                                                    150,
                                                                    151,
                                                                    152,
                                                                    153,
                                                                    154,
                                                                    155,
                                                                    156,
                                                                    157,
                                                                    158,
                                                                    159,
                                                                    '\u2028' };
  private static final char [] MASK_PATTERNS_ALL = ArrayHelper.getConcatenated (MASK_PATTERNS_XML10,
                                                                                MASK_PATTERNS_CONTROL);

  /**
   * IE8 emits an error when using &apos; - that's why the work around with
   * &#39; is used!<br>
   * Note: &#0; cannot be read so it is emitted as ""<br>
   */
  private static final char [][] MASK_REPLACE_XML10 = new char [] [] { "".toCharArray (),
                                                                      "&amp;".toCharArray (),
                                                                      "&lt;".toCharArray (),
                                                                      "&gt;".toCharArray (),
                                                                      "&quot;".toCharArray (),
                                                                      "&#39;".toCharArray () };

  /**
   * Control character replacements for removal<br>
   * All other numeric mappings &#1; - &#31; can only be read by XML 1.1
   */
  private static final char [][] MASK_REPLACE_CONTROL_EMPTY = new char [] [] { "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "".toCharArray (),
                                                                              "\n".toCharArray () };

  private static final char [][] MASK_REPLACE_ALL_EMPTY = ArrayHelper.getConcatenated (MASK_REPLACE_XML10,
                                                                                       MASK_REPLACE_CONTROL_EMPTY);

  /**
   * Control character replacements<br>
   * All other numeric mappings &#1; - &#31; can only be read by XML 1.1
   */
  private static final char [][] MASK_REPLACE_CONTROL_XML11 = new char [] [] { "&#1;".toCharArray (),
                                                                              "&#2;".toCharArray (),
                                                                              "&#3;".toCharArray (),
                                                                              "&#4;".toCharArray (),
                                                                              "&#5;".toCharArray (),
                                                                              "&#6;".toCharArray (),
                                                                              "&#7;".toCharArray (),
                                                                              "&#8;".toCharArray (),
                                                                              "&#11;".toCharArray (),
                                                                              "&#12;".toCharArray (),
                                                                              "&#13;".toCharArray (),
                                                                              "&#14;".toCharArray (),
                                                                              "&#15;".toCharArray (),
                                                                              "&#16;".toCharArray (),
                                                                              "&#17;".toCharArray (),
                                                                              "&#18;".toCharArray (),
                                                                              "&#19;".toCharArray (),
                                                                              "&#20;".toCharArray (),
                                                                              "&#21;".toCharArray (),
                                                                              "&#22;".toCharArray (),
                                                                              "&#23;".toCharArray (),
                                                                              "&#24;".toCharArray (),
                                                                              "&#25;".toCharArray (),
                                                                              "&#26;".toCharArray (),
                                                                              "&#27;".toCharArray (),
                                                                              "&#28;".toCharArray (),
                                                                              "&#29;".toCharArray (),
                                                                              "&#30;".toCharArray (),
                                                                              "&#31;".toCharArray (),
                                                                              "&#127;".toCharArray (),
                                                                              "&#128;".toCharArray (),
                                                                              "&#129;".toCharArray (),
                                                                              "&#130;".toCharArray (),
                                                                              "&#131;".toCharArray (),
                                                                              "&#132;".toCharArray (),
                                                                              "&#133;".toCharArray (),
                                                                              "&#134;".toCharArray (),
                                                                              "&#135;".toCharArray (),
                                                                              "&#136;".toCharArray (),
                                                                              "&#137;".toCharArray (),
                                                                              "&#138;".toCharArray (),
                                                                              "&#139;".toCharArray (),
                                                                              "&#140;".toCharArray (),
                                                                              "&#141;".toCharArray (),
                                                                              "&#142;".toCharArray (),
                                                                              "&#143;".toCharArray (),
                                                                              "&#144;".toCharArray (),
                                                                              "&#145;".toCharArray (),
                                                                              "&#146;".toCharArray (),
                                                                              "&#147;".toCharArray (),
                                                                              "&#148;".toCharArray (),
                                                                              "&#149;".toCharArray (),
                                                                              "&#150;".toCharArray (),
                                                                              "&#151;".toCharArray (),
                                                                              "&#152;".toCharArray (),
                                                                              "&#153;".toCharArray (),
                                                                              "&#154;".toCharArray (),
                                                                              "&#155;".toCharArray (),
                                                                              "&#156;".toCharArray (),
                                                                              "&#157;".toCharArray (),
                                                                              "&#158;".toCharArray (),
                                                                              "&#159;".toCharArray (),
                                                                              "&#2028;".toCharArray () };

  private static final char [][] MASK_REPLACE_ALL_XML11 = ArrayHelper.getConcatenated (MASK_REPLACE_XML10,
                                                                                       MASK_REPLACE_CONTROL_XML11);

  /**
   * Contains a boolean mask for all characters from 0x00-0xff which are invalid
   * (marked as true) and which are valid (marked as false)
   */
  private static final boolean [] ILLEGAL_XML_CHARS = new boolean [] { true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      false,
                                                                      false,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      // 16
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      // 32
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      // 48
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      // 64
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      // 80
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      // 96
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      // 112
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      true,
                                                                      // 128
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      // 144
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      // 160
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      // 176
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      // 192
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      // 208
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      // 224
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      // 240
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      false };

  private static final BitSet XML_INVALID_NAME_START_CHAR_XML10 = new BitSet (0x10000);
  private static final BitSet XML_INVALID_NAME_START_CHAR_XML11 = new BitSet (0x10000);
  private static final BitSet XML_INVALID_NAME_CHAR_XML10 = new BitSet (0x10000);
  private static final BitSet XML_INVALID_NAME_CHAR_XML11 = new BitSet (0x10000);
  private static final BitSet XML_INVALID_TEXT_VALUE_CHAR_XML10 = new BitSet (0x10000);
  private static final BitSet XML_INVALID_TEXT_VALUE_CHAR_XML11 = new BitSet (0x10000);
  private static final BitSet XML_INVALID_ATTRCDATA_VALUE_CHAR_XML10 = new BitSet (0x10000);
  private static final BitSet XML_INVALID_ATTRCDATA_VALUE_CHAR_XML11 = new BitSet (0x10000);

  static
  {
    /**
     * Unicode code points in the following ranges are valid in XML 1.0
     * documents:<br>
     * U+0009, U+000A, U+000D: these are the only C0 controls accepted in XML
     * 1.0<br>
     * U+0020–U+D7FF, U+E000–U+FFFD: this excludes some (not all) non-characters
     * in the BMP (all surrogates, U+FFFE and U+FFFF are forbidden)<br>
     * U+10000–U+10FFFF: this includes all code points in supplementary planes,
     * including non-characters.<br>
     * <br>
     * XML 1.1 extends the set of allowed characters to include all the above,
     * plus the remaining characters in the range U+0001–U+001F. At the same
     * time, however, it restricts the use of C0 and C1 control characters other
     * than U+0009, U+000A, U+000D, and U+0085 by requiring them to be written
     * in escaped form (for example U+0001 must be written as &#x01; or its
     * equivalent). In the case of C1 characters, this restriction is a
     * backwards incompatibility; it was introduced to allow common encoding
     * errors to be detected.<br>
     * <br>
     * The code point U+0000 is the only character that is not permitted in any
     * XML 1.0 or 1.1 document.
     */

    // Check integrity
    if (MASK_PATTERNS_XML10.length != MASK_REPLACE_XML10.length)
      throw new IllegalStateException ("Regular arrays have different length!");
    if (MASK_PATTERNS_CONTROL.length != MASK_REPLACE_CONTROL_EMPTY.length)
      throw new IllegalStateException ("Empty arrays have different length!");
    if (MASK_PATTERNS_CONTROL.length != MASK_REPLACE_CONTROL_XML11.length)
      throw new IllegalStateException ("Control arrays have different length!");
    if (MASK_PATTERNS_ALL.length != MASK_REPLACE_ALL_EMPTY.length)
      throw new IllegalStateException ("Empty arrays have different length!");
    if (MASK_PATTERNS_ALL.length != MASK_REPLACE_ALL_XML11.length)
      throw new IllegalStateException ("Overall arrays have different length!");

    for (int c = Character.MIN_VALUE; c <= Character.MAX_VALUE; ++c)
    {
      XML_INVALID_NAME_START_CHAR_XML10.set (c, (c >= 0x0 && c <= 0x39) ||
                                                (c >= 0x3b && c <= 0x40) ||
                                                (c >= 0x5b && c <= 0x5e) ||
                                                (c == 0x60) ||
                                                (c >= 0x7b && c <= 0xbf) ||
                                                (c == 0xd7) ||
                                                (c == 0xf7) ||
                                                (c >= 0x132 && c <= 0x133) ||
                                                (c >= 0x13f && c <= 0x140) ||
                                                (c == 0x149) ||
                                                (c == 0x17f) ||
                                                (c >= 0x1c4 && c <= 0x1cc) ||
                                                (c >= 0x1f1 && c <= 0x1f3) ||
                                                (c >= 0x1f6 && c <= 0x1f9) ||
                                                (c >= 0x218 && c <= 0x24f) ||
                                                (c >= 0x2a9 && c <= 0x2ba) ||
                                                (c >= 0x2c2 && c <= 0x385) ||
                                                (c == 0x387) ||
                                                (c == 0x38b) ||
                                                (c == 0x38d) ||
                                                (c == 0x3a2) ||
                                                (c == 0x3cf) ||
                                                (c >= 0x3d7 && c <= 0x3d9) ||
                                                (c == 0x3db) ||
                                                (c == 0x3dd) ||
                                                (c == 0x3df) ||
                                                (c == 0x3e1) ||
                                                (c >= 0x3f4 && c <= 0x400) ||
                                                (c == 0x40d) ||
                                                (c == 0x450) ||
                                                (c == 0x45d) ||
                                                (c >= 0x482 && c <= 0x48f) ||
                                                (c >= 0x4c5 && c <= 0x4c6) ||
                                                (c >= 0x4c9 && c <= 0x4ca) ||
                                                (c >= 0x4cd && c <= 0x4cf) ||
                                                (c >= 0x4ec && c <= 0x4ed) ||
                                                (c >= 0x4f6 && c <= 0x4f7) ||
                                                (c >= 0x4fa && c <= 0x530) ||
                                                (c >= 0x557 && c <= 0x558) ||
                                                (c >= 0x55a && c <= 0x560) ||
                                                (c >= 0x587 && c <= 0x5cf) ||
                                                (c >= 0x5eb && c <= 0x5ef) ||
                                                (c >= 0x5f3 && c <= 0x620) ||
                                                (c >= 0x63b && c <= 0x640) ||
                                                (c >= 0x64b && c <= 0x670) ||
                                                (c >= 0x6b8 && c <= 0x6b9) ||
                                                (c == 0x6bf) ||
                                                (c == 0x6cf) ||
                                                (c == 0x6d4) ||
                                                (c >= 0x6d6 && c <= 0x6e4) ||
                                                (c >= 0x6e7 && c <= 0x904) ||
                                                (c >= 0x93a && c <= 0x93c) ||
                                                (c >= 0x93e && c <= 0x957) ||
                                                (c >= 0x962 && c <= 0x984) ||
                                                (c >= 0x98d && c <= 0x98e) ||
                                                (c >= 0x991 && c <= 0x992) ||
                                                (c == 0x9a9) ||
                                                (c == 0x9b1) ||
                                                (c >= 0x9b3 && c <= 0x9b5) ||
                                                (c >= 0x9ba && c <= 0x9db) ||
                                                (c == 0x9de) ||
                                                (c >= 0x9e2 && c <= 0x9ef) ||
                                                (c >= 0x9f2 && c <= 0xa04) ||
                                                (c >= 0xa0b && c <= 0xa0e) ||
                                                (c >= 0xa11 && c <= 0xa12) ||
                                                (c == 0xa29) ||
                                                (c == 0xa31) ||
                                                (c == 0xa34) ||
                                                (c == 0xa37) ||
                                                (c >= 0xa3a && c <= 0xa58) ||
                                                (c == 0xa5d) ||
                                                (c >= 0xa5f && c <= 0xa71) ||
                                                (c >= 0xa75 && c <= 0xa84) ||
                                                (c == 0xa8c) ||
                                                (c == 0xa8e) ||
                                                (c == 0xa92) ||
                                                (c == 0xaa9) ||
                                                (c == 0xab1) ||
                                                (c == 0xab4) ||
                                                (c >= 0xaba && c <= 0xabc) ||
                                                (c >= 0xabe && c <= 0xadf) ||
                                                (c >= 0xae1 && c <= 0xb04) ||
                                                (c >= 0xb0d && c <= 0xb0e) ||
                                                (c >= 0xb11 && c <= 0xb12) ||
                                                (c == 0xb29) ||
                                                (c == 0xb31) ||
                                                (c >= 0xb34 && c <= 0xb35) ||
                                                (c >= 0xb3a && c <= 0xb3c) ||
                                                (c >= 0xb3e && c <= 0xb5b) ||
                                                (c == 0xb5e) ||
                                                (c >= 0xb62 && c <= 0xb84) ||
                                                (c >= 0xb8b && c <= 0xb8d) ||
                                                (c == 0xb91) ||
                                                (c >= 0xb96 && c <= 0xb98) ||
                                                (c == 0xb9b) ||
                                                (c == 0xb9d) ||
                                                (c >= 0xba0 && c <= 0xba2) ||
                                                (c >= 0xba5 && c <= 0xba7) ||
                                                (c >= 0xbab && c <= 0xbad) ||
                                                (c == 0xbb6) ||
                                                (c >= 0xbba && c <= 0xc04) ||
                                                (c == 0xc0d) ||
                                                (c == 0xc11) ||
                                                (c == 0xc29) ||
                                                (c == 0xc34) ||
                                                (c >= 0xc3a && c <= 0xc5f) ||
                                                (c >= 0xc62 && c <= 0xc84) ||
                                                (c == 0xc8d) ||
                                                (c == 0xc91) ||
                                                (c == 0xca9) ||
                                                (c == 0xcb4) ||
                                                (c >= 0xcba && c <= 0xcdd) ||
                                                (c == 0xcdf) ||
                                                (c >= 0xce2 && c <= 0xd04) ||
                                                (c == 0xd0d) ||
                                                (c == 0xd11) ||
                                                (c == 0xd29) ||
                                                (c >= 0xd3a && c <= 0xd5f) ||
                                                (c >= 0xd62 && c <= 0xe00) ||
                                                (c == 0xe2f) ||
                                                (c == 0xe31) ||
                                                (c >= 0xe34 && c <= 0xe3f) ||
                                                (c >= 0xe46 && c <= 0xe80) ||
                                                (c == 0xe83) ||
                                                (c >= 0xe85 && c <= 0xe86) ||
                                                (c == 0xe89) ||
                                                (c >= 0xe8b && c <= 0xe8c) ||
                                                (c >= 0xe8e && c <= 0xe93) ||
                                                (c == 0xe98) ||
                                                (c == 0xea0) ||
                                                (c == 0xea4) ||
                                                (c == 0xea6) ||
                                                (c >= 0xea8 && c <= 0xea9) ||
                                                (c == 0xeac) ||
                                                (c == 0xeaf) ||
                                                (c == 0xeb1) ||
                                                (c >= 0xeb4 && c <= 0xebc) ||
                                                (c >= 0xebe && c <= 0xebf) ||
                                                (c >= 0xec5 && c <= 0xf3f) ||
                                                (c == 0xf48) ||
                                                (c >= 0xf6a && c <= 0x109f) ||
                                                (c >= 0x10c6 && c <= 0x10cf) ||
                                                (c >= 0x10f7 && c <= 0x10ff) ||
                                                (c == 0x1101) ||
                                                (c == 0x1104) ||
                                                (c == 0x1108) ||
                                                (c == 0x110a) ||
                                                (c == 0x110d) ||
                                                (c >= 0x1113 && c <= 0x113b) ||
                                                (c == 0x113d) ||
                                                (c == 0x113f) ||
                                                (c >= 0x1141 && c <= 0x114b) ||
                                                (c == 0x114d) ||
                                                (c == 0x114f) ||
                                                (c >= 0x1151 && c <= 0x1153) ||
                                                (c >= 0x1156 && c <= 0x1158) ||
                                                (c >= 0x115a && c <= 0x115e) ||
                                                (c == 0x1162) ||
                                                (c == 0x1164) ||
                                                (c == 0x1166) ||
                                                (c == 0x1168) ||
                                                (c >= 0x116a && c <= 0x116c) ||
                                                (c >= 0x116f && c <= 0x1171) ||
                                                (c == 0x1174) ||
                                                (c >= 0x1176 && c <= 0x119d) ||
                                                (c >= 0x119f && c <= 0x11a7) ||
                                                (c >= 0x11a9 && c <= 0x11aa) ||
                                                (c >= 0x11ac && c <= 0x11ad) ||
                                                (c >= 0x11b0 && c <= 0x11b6) ||
                                                (c == 0x11b9) ||
                                                (c == 0x11bb) ||
                                                (c >= 0x11c3 && c <= 0x11ea) ||
                                                (c >= 0x11ec && c <= 0x11ef) ||
                                                (c >= 0x11f1 && c <= 0x11f8) ||
                                                (c >= 0x11fa && c <= 0x1dff) ||
                                                (c >= 0x1e9c && c <= 0x1e9f) ||
                                                (c >= 0x1efa && c <= 0x1eff) ||
                                                (c >= 0x1f16 && c <= 0x1f17) ||
                                                (c >= 0x1f1e && c <= 0x1f1f) ||
                                                (c >= 0x1f46 && c <= 0x1f47) ||
                                                (c >= 0x1f4e && c <= 0x1f4f) ||
                                                (c == 0x1f58) ||
                                                (c == 0x1f5a) ||
                                                (c == 0x1f5c) ||
                                                (c == 0x1f5e) ||
                                                (c >= 0x1f7e && c <= 0x1f7f) ||
                                                (c == 0x1fb5) ||
                                                (c == 0x1fbd) ||
                                                (c >= 0x1fbf && c <= 0x1fc1) ||
                                                (c == 0x1fc5) ||
                                                (c >= 0x1fcd && c <= 0x1fcf) ||
                                                (c >= 0x1fd4 && c <= 0x1fd5) ||
                                                (c >= 0x1fdc && c <= 0x1fdf) ||
                                                (c >= 0x1fed && c <= 0x1ff1) ||
                                                (c == 0x1ff5) ||
                                                (c >= 0x1ffd && c <= 0x2125) ||
                                                (c >= 0x2127 && c <= 0x2129) ||
                                                (c >= 0x212c && c <= 0x212d) ||
                                                (c >= 0x212f && c <= 0x217f) ||
                                                (c >= 0x2183 && c <= 0x3006) ||
                                                (c >= 0x3008 && c <= 0x3020) ||
                                                (c >= 0x302a && c <= 0x3040) ||
                                                (c >= 0x3095 && c <= 0x30a0) ||
                                                (c >= 0x30fb && c <= 0x3104) ||
                                                (c >= 0x312d && c <= 0x4dff) ||
                                                (c >= 0x9fa6 && c <= 0xabff) ||
                                                (c >= 0xd7a4 && c <= 0xffff));
      XML_INVALID_NAME_START_CHAR_XML11.set (c, (c >= 0x0 && c <= 0x39) ||
                                                (c >= 0x3b && c <= 0x40) ||
                                                (c >= 0x5b && c <= 0x5e) ||
                                                (c == 0x60) ||
                                                (c >= 0x7b && c <= 0xbf) ||
                                                (c == 0xd7) ||
                                                (c == 0xf7) ||
                                                (c >= 0x300 && c <= 0x36f) ||
                                                (c == 0x37e) ||
                                                (c >= 0x2000 && c <= 0x200b) ||
                                                (c >= 0x200e && c <= 0x206f) ||
                                                (c >= 0x2190 && c <= 0x2bff) ||
                                                (c >= 0x2ff0 && c <= 0x3000) ||
                                                (c >= 0xd800 && c <= 0xf8ff) ||
                                                (c >= 0xfdd0 && c <= 0xfdef) ||
                                                (c >= 0xfffe && c <= 0xffff));
      XML_INVALID_NAME_CHAR_XML10.set (c, (c >= 0x0 && c <= 0x2c) ||
                                          (c == 0x2f) ||
                                          (c >= 0x3b && c <= 0x40) ||
                                          (c >= 0x5b && c <= 0x5e) ||
                                          (c == 0x60) ||
                                          (c >= 0x7b && c <= 0xb6) ||
                                          (c >= 0xb8 && c <= 0xbf) ||
                                          (c == 0xd7) ||
                                          (c == 0xf7) ||
                                          (c >= 0x132 && c <= 0x133) ||
                                          (c >= 0x13f && c <= 0x140) ||
                                          (c == 0x149) ||
                                          (c == 0x17f) ||
                                          (c >= 0x1c4 && c <= 0x1cc) ||
                                          (c >= 0x1f1 && c <= 0x1f3) ||
                                          (c >= 0x1f6 && c <= 0x1f9) ||
                                          (c >= 0x218 && c <= 0x24f) ||
                                          (c >= 0x2a9 && c <= 0x2ba) ||
                                          (c >= 0x2c2 && c <= 0x2cf) ||
                                          (c >= 0x2d2 && c <= 0x2ff) ||
                                          (c >= 0x346 && c <= 0x35f) ||
                                          (c >= 0x362 && c <= 0x385) ||
                                          (c == 0x38b) ||
                                          (c == 0x38d) ||
                                          (c == 0x3a2) ||
                                          (c == 0x3cf) ||
                                          (c >= 0x3d7 && c <= 0x3d9) ||
                                          (c == 0x3db) ||
                                          (c == 0x3dd) ||
                                          (c == 0x3df) ||
                                          (c == 0x3e1) ||
                                          (c >= 0x3f4 && c <= 0x400) ||
                                          (c == 0x40d) ||
                                          (c == 0x450) ||
                                          (c == 0x45d) ||
                                          (c == 0x482) ||
                                          (c >= 0x487 && c <= 0x48f) ||
                                          (c >= 0x4c5 && c <= 0x4c6) ||
                                          (c >= 0x4c9 && c <= 0x4ca) ||
                                          (c >= 0x4cd && c <= 0x4cf) ||
                                          (c >= 0x4ec && c <= 0x4ed) ||
                                          (c >= 0x4f6 && c <= 0x4f7) ||
                                          (c >= 0x4fa && c <= 0x530) ||
                                          (c >= 0x557 && c <= 0x558) ||
                                          (c >= 0x55a && c <= 0x560) ||
                                          (c >= 0x587 && c <= 0x590) ||
                                          (c == 0x5a2) ||
                                          (c == 0x5ba) ||
                                          (c == 0x5be) ||
                                          (c == 0x5c0) ||
                                          (c == 0x5c3) ||
                                          (c >= 0x5c5 && c <= 0x5cf) ||
                                          (c >= 0x5eb && c <= 0x5ef) ||
                                          (c >= 0x5f3 && c <= 0x620) ||
                                          (c >= 0x63b && c <= 0x63f) ||
                                          (c >= 0x653 && c <= 0x65f) ||
                                          (c >= 0x66a && c <= 0x66f) ||
                                          (c >= 0x6b8 && c <= 0x6b9) ||
                                          (c == 0x6bf) ||
                                          (c == 0x6cf) ||
                                          (c == 0x6d4) ||
                                          (c == 0x6e9) ||
                                          (c >= 0x6ee && c <= 0x6ef) ||
                                          (c >= 0x6fa && c <= 0x900) ||
                                          (c == 0x904) ||
                                          (c >= 0x93a && c <= 0x93b) ||
                                          (c >= 0x94e && c <= 0x950) ||
                                          (c >= 0x955 && c <= 0x957) ||
                                          (c >= 0x964 && c <= 0x965) ||
                                          (c >= 0x970 && c <= 0x980) ||
                                          (c == 0x984) ||
                                          (c >= 0x98d && c <= 0x98e) ||
                                          (c >= 0x991 && c <= 0x992) ||
                                          (c == 0x9a9) ||
                                          (c == 0x9b1) ||
                                          (c >= 0x9b3 && c <= 0x9b5) ||
                                          (c >= 0x9ba && c <= 0x9bb) ||
                                          (c == 0x9bd) ||
                                          (c >= 0x9c5 && c <= 0x9c6) ||
                                          (c >= 0x9c9 && c <= 0x9ca) ||
                                          (c >= 0x9ce && c <= 0x9d6) ||
                                          (c >= 0x9d8 && c <= 0x9db) ||
                                          (c == 0x9de) ||
                                          (c >= 0x9e4 && c <= 0x9e5) ||
                                          (c >= 0x9f2 && c <= 0xa01) ||
                                          (c >= 0xa03 && c <= 0xa04) ||
                                          (c >= 0xa0b && c <= 0xa0e) ||
                                          (c >= 0xa11 && c <= 0xa12) ||
                                          (c == 0xa29) ||
                                          (c == 0xa31) ||
                                          (c == 0xa34) ||
                                          (c == 0xa37) ||
                                          (c >= 0xa3a && c <= 0xa3b) ||
                                          (c == 0xa3d) ||
                                          (c >= 0xa43 && c <= 0xa46) ||
                                          (c >= 0xa49 && c <= 0xa4a) ||
                                          (c >= 0xa4e && c <= 0xa58) ||
                                          (c == 0xa5d) ||
                                          (c >= 0xa5f && c <= 0xa65) ||
                                          (c >= 0xa75 && c <= 0xa80) ||
                                          (c == 0xa84) ||
                                          (c == 0xa8c) ||
                                          (c == 0xa8e) ||
                                          (c == 0xa92) ||
                                          (c == 0xaa9) ||
                                          (c == 0xab1) ||
                                          (c == 0xab4) ||
                                          (c >= 0xaba && c <= 0xabb) ||
                                          (c == 0xac6) ||
                                          (c == 0xaca) ||
                                          (c >= 0xace && c <= 0xadf) ||
                                          (c >= 0xae1 && c <= 0xae5) ||
                                          (c >= 0xaf0 && c <= 0xb00) ||
                                          (c == 0xb04) ||
                                          (c >= 0xb0d && c <= 0xb0e) ||
                                          (c >= 0xb11 && c <= 0xb12) ||
                                          (c == 0xb29) ||
                                          (c == 0xb31) ||
                                          (c >= 0xb34 && c <= 0xb35) ||
                                          (c >= 0xb3a && c <= 0xb3b) ||
                                          (c >= 0xb44 && c <= 0xb46) ||
                                          (c >= 0xb49 && c <= 0xb4a) ||
                                          (c >= 0xb4e && c <= 0xb55) ||
                                          (c >= 0xb58 && c <= 0xb5b) ||
                                          (c == 0xb5e) ||
                                          (c >= 0xb62 && c <= 0xb65) ||
                                          (c >= 0xb70 && c <= 0xb81) ||
                                          (c == 0xb84) ||
                                          (c >= 0xb8b && c <= 0xb8d) ||
                                          (c == 0xb91) ||
                                          (c >= 0xb96 && c <= 0xb98) ||
                                          (c == 0xb9b) ||
                                          (c == 0xb9d) ||
                                          (c >= 0xba0 && c <= 0xba2) ||
                                          (c >= 0xba5 && c <= 0xba7) ||
                                          (c >= 0xbab && c <= 0xbad) ||
                                          (c == 0xbb6) ||
                                          (c >= 0xbba && c <= 0xbbd) ||
                                          (c >= 0xbc3 && c <= 0xbc5) ||
                                          (c == 0xbc9) ||
                                          (c >= 0xbce && c <= 0xbd6) ||
                                          (c >= 0xbd8 && c <= 0xbe6) ||
                                          (c >= 0xbf0 && c <= 0xc00) ||
                                          (c == 0xc04) ||
                                          (c == 0xc0d) ||
                                          (c == 0xc11) ||
                                          (c == 0xc29) ||
                                          (c == 0xc34) ||
                                          (c >= 0xc3a && c <= 0xc3d) ||
                                          (c == 0xc45) ||
                                          (c == 0xc49) ||
                                          (c >= 0xc4e && c <= 0xc54) ||
                                          (c >= 0xc57 && c <= 0xc5f) ||
                                          (c >= 0xc62 && c <= 0xc65) ||
                                          (c >= 0xc70 && c <= 0xc81) ||
                                          (c == 0xc84) ||
                                          (c == 0xc8d) ||
                                          (c == 0xc91) ||
                                          (c == 0xca9) ||
                                          (c == 0xcb4) ||
                                          (c >= 0xcba && c <= 0xcbd) ||
                                          (c == 0xcc5) ||
                                          (c == 0xcc9) ||
                                          (c >= 0xcce && c <= 0xcd4) ||
                                          (c >= 0xcd7 && c <= 0xcdd) ||
                                          (c == 0xcdf) ||
                                          (c >= 0xce2 && c <= 0xce5) ||
                                          (c >= 0xcf0 && c <= 0xd01) ||
                                          (c == 0xd04) ||
                                          (c == 0xd0d) ||
                                          (c == 0xd11) ||
                                          (c == 0xd29) ||
                                          (c >= 0xd3a && c <= 0xd3d) ||
                                          (c >= 0xd44 && c <= 0xd45) ||
                                          (c == 0xd49) ||
                                          (c >= 0xd4e && c <= 0xd56) ||
                                          (c >= 0xd58 && c <= 0xd5f) ||
                                          (c >= 0xd62 && c <= 0xd65) ||
                                          (c >= 0xd70 && c <= 0xe00) ||
                                          (c == 0xe2f) ||
                                          (c >= 0xe3b && c <= 0xe3f) ||
                                          (c == 0xe4f) ||
                                          (c >= 0xe5a && c <= 0xe80) ||
                                          (c == 0xe83) ||
                                          (c >= 0xe85 && c <= 0xe86) ||
                                          (c == 0xe89) ||
                                          (c >= 0xe8b && c <= 0xe8c) ||
                                          (c >= 0xe8e && c <= 0xe93) ||
                                          (c == 0xe98) ||
                                          (c == 0xea0) ||
                                          (c == 0xea4) ||
                                          (c == 0xea6) ||
                                          (c >= 0xea8 && c <= 0xea9) ||
                                          (c == 0xeac) ||
                                          (c == 0xeaf) ||
                                          (c == 0xeba) ||
                                          (c >= 0xebe && c <= 0xebf) ||
                                          (c == 0xec5) ||
                                          (c == 0xec7) ||
                                          (c >= 0xece && c <= 0xecf) ||
                                          (c >= 0xeda && c <= 0xf17) ||
                                          (c >= 0xf1a && c <= 0xf1f) ||
                                          (c >= 0xf2a && c <= 0xf34) ||
                                          (c == 0xf36) ||
                                          (c == 0xf38) ||
                                          (c >= 0xf3a && c <= 0xf3d) ||
                                          (c == 0xf48) ||
                                          (c >= 0xf6a && c <= 0xf70) ||
                                          (c == 0xf85) ||
                                          (c >= 0xf8c && c <= 0xf8f) ||
                                          (c == 0xf96) ||
                                          (c == 0xf98) ||
                                          (c >= 0xfae && c <= 0xfb0) ||
                                          (c == 0xfb8) ||
                                          (c >= 0xfba && c <= 0x109f) ||
                                          (c >= 0x10c6 && c <= 0x10cf) ||
                                          (c >= 0x10f7 && c <= 0x10ff) ||
                                          (c == 0x1101) ||
                                          (c == 0x1104) ||
                                          (c == 0x1108) ||
                                          (c == 0x110a) ||
                                          (c == 0x110d) ||
                                          (c >= 0x1113 && c <= 0x113b) ||
                                          (c == 0x113d) ||
                                          (c == 0x113f) ||
                                          (c >= 0x1141 && c <= 0x114b) ||
                                          (c == 0x114d) ||
                                          (c == 0x114f) ||
                                          (c >= 0x1151 && c <= 0x1153) ||
                                          (c >= 0x1156 && c <= 0x1158) ||
                                          (c >= 0x115a && c <= 0x115e) ||
                                          (c == 0x1162) ||
                                          (c == 0x1164) ||
                                          (c == 0x1166) ||
                                          (c == 0x1168) ||
                                          (c >= 0x116a && c <= 0x116c) ||
                                          (c >= 0x116f && c <= 0x1171) ||
                                          (c == 0x1174) ||
                                          (c >= 0x1176 && c <= 0x119d) ||
                                          (c >= 0x119f && c <= 0x11a7) ||
                                          (c >= 0x11a9 && c <= 0x11aa) ||
                                          (c >= 0x11ac && c <= 0x11ad) ||
                                          (c >= 0x11b0 && c <= 0x11b6) ||
                                          (c == 0x11b9) ||
                                          (c == 0x11bb) ||
                                          (c >= 0x11c3 && c <= 0x11ea) ||
                                          (c >= 0x11ec && c <= 0x11ef) ||
                                          (c >= 0x11f1 && c <= 0x11f8) ||
                                          (c >= 0x11fa && c <= 0x1dff) ||
                                          (c >= 0x1e9c && c <= 0x1e9f) ||
                                          (c >= 0x1efa && c <= 0x1eff) ||
                                          (c >= 0x1f16 && c <= 0x1f17) ||
                                          (c >= 0x1f1e && c <= 0x1f1f) ||
                                          (c >= 0x1f46 && c <= 0x1f47) ||
                                          (c >= 0x1f4e && c <= 0x1f4f) ||
                                          (c == 0x1f58) ||
                                          (c == 0x1f5a) ||
                                          (c == 0x1f5c) ||
                                          (c == 0x1f5e) ||
                                          (c >= 0x1f7e && c <= 0x1f7f) ||
                                          (c == 0x1fb5) ||
                                          (c == 0x1fbd) ||
                                          (c >= 0x1fbf && c <= 0x1fc1) ||
                                          (c == 0x1fc5) ||
                                          (c >= 0x1fcd && c <= 0x1fcf) ||
                                          (c >= 0x1fd4 && c <= 0x1fd5) ||
                                          (c >= 0x1fdc && c <= 0x1fdf) ||
                                          (c >= 0x1fed && c <= 0x1ff1) ||
                                          (c == 0x1ff5) ||
                                          (c >= 0x1ffd && c <= 0x20cf) ||
                                          (c >= 0x20dd && c <= 0x20e0) ||
                                          (c >= 0x20e2 && c <= 0x2125) ||
                                          (c >= 0x2127 && c <= 0x2129) ||
                                          (c >= 0x212c && c <= 0x212d) ||
                                          (c >= 0x212f && c <= 0x217f) ||
                                          (c >= 0x2183 && c <= 0x3004) ||
                                          (c == 0x3006) ||
                                          (c >= 0x3008 && c <= 0x3020) ||
                                          (c == 0x3030) ||
                                          (c >= 0x3036 && c <= 0x3040) ||
                                          (c >= 0x3095 && c <= 0x3098) ||
                                          (c >= 0x309b && c <= 0x309c) ||
                                          (c >= 0x309f && c <= 0x30a0) ||
                                          (c == 0x30fb) ||
                                          (c >= 0x30ff && c <= 0x3104) ||
                                          (c >= 0x312d && c <= 0x4dff) ||
                                          (c >= 0x9fa6 && c <= 0xabff) ||
                                          (c >= 0xd7a4 && c <= 0xffff));
      XML_INVALID_NAME_CHAR_XML11.set (c, (c >= 0x0 && c <= 0x2c) ||
                                          (c == 0x2f) ||
                                          (c >= 0x3b && c <= 0x40) ||
                                          (c >= 0x5b && c <= 0x5e) ||
                                          (c == 0x60) ||
                                          (c >= 0x7b && c <= 0xb6) ||
                                          (c >= 0xb8 && c <= 0xbf) ||
                                          (c == 0xd7) ||
                                          (c == 0xf7) ||
                                          (c == 0x37e) ||
                                          (c >= 0x2000 && c <= 0x200b) ||
                                          (c >= 0x200e && c <= 0x203e) ||
                                          (c >= 0x2041 && c <= 0x206f) ||
                                          (c >= 0x2190 && c <= 0x2bff) ||
                                          (c >= 0x2ff0 && c <= 0x3000) ||
                                          (c >= 0xd800 && c <= 0xf8ff) ||
                                          (c >= 0xfdd0 && c <= 0xfdef) ||
                                          (c >= 0xfffe && c <= 0xffff));
      XML_INVALID_TEXT_VALUE_CHAR_XML10.set (c,
                                             (c == 0x0) ||
                                                 Character.isHighSurrogate ((char) c) ||
                                                 Character.isLowSurrogate ((char) c) ||
                                                 (c >= 0xfffe && c <= 0xffff));
      XML_INVALID_TEXT_VALUE_CHAR_XML11.set (c,
                                             (c == 0x0) ||
                                                 Character.isHighSurrogate ((char) c) ||
                                                 Character.isLowSurrogate ((char) c) ||
                                                 (c >= 0xfffe && c <= 0xffff));
      XML_INVALID_ATTRCDATA_VALUE_CHAR_XML10.set (c,
                                                  (c == 0x0) ||
                                                      Character.isHighSurrogate ((char) c) ||
                                                      Character.isLowSurrogate ((char) c) ||
                                                      (c >= 0xfffe && c <= 0xffff));
      XML_INVALID_ATTRCDATA_VALUE_CHAR_XML11.set (c, (c == 0x0) ||
                                                     (c >= 0x7f && c <= 0x84) ||
                                                     (c >= 0x86 && c <= 0x9f) ||
                                                     Character.isHighSurrogate ((char) c) ||
                                                     Character.isLowSurrogate ((char) c) ||
                                                     (c >= 0xfffe && c <= 0xffff));
    }
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final XMLHelper s_aInstance = new XMLHelper ();

  private XMLHelper ()
  {}

  /**
   * Get the first direct child element of the passed element.
   * 
   * @param aStartNode
   *        The element to start searching.
   * @return <code>null</code> if the passed element does not have any direct
   *         child element.
   */
  @Nullable
  public static Element getFirstChildElement (@Nonnull final Node aStartNode)
  {
    final NodeList aNodeList = aStartNode.getChildNodes ();
    final int nLen = aNodeList.getLength ();
    for (int i = 0; i < nLen; ++i)
    {
      final Node aNode = aNodeList.item (i);
      if (aNode.getNodeType () == Node.ELEMENT_NODE)
        return (Element) aNode;
    }
    return null;
  }

  /**
   * Check if the passed node has at least one direct child element or not.
   * 
   * @param aStartNode
   *        The parent element to be searched. May not be <code>null</code>.
   * @return <code>true</code> if the passed node has at least one child
   *         element, <code>false</code> otherwise.
   */
  public static boolean hasChildElementNodes (@Nonnull final Node aStartNode)
  {
    return getFirstChildElement (aStartNode) != null;
  }

  /**
   * Search all child nodes of the given for the first element that has the
   * specified tag name.
   * 
   * @param aStartNode
   *        The parent element to be searched. May not be <code>null</code>.
   * @param sName
   *        The tag name to search.
   * @return <code>null</code> if the parent element has no such child element.
   */
  @Nullable
  public static Element getFirstChildElementOfName (@Nonnull final Node aStartNode, @Nullable final String sName)
  {
    final NodeList aNodeList = aStartNode.getChildNodes ();
    final int nLen = aNodeList.getLength ();
    for (int i = 0; i < nLen; ++i)
    {
      final Node aNode = aNodeList.item (i);
      if (aNode.getNodeType () == Node.ELEMENT_NODE)
      {
        final Element aElement = (Element) aNode;
        if (aElement.getTagName ().equals (sName))
          return aElement;
      }
    }
    return null;
  }

  /**
   * Get the owner document of the passed node. If the node itself is a
   * document, only a cast is performed.
   * 
   * @param aNode
   *        The node to get the document from. May be <code>null</code>.
   * @return <code>null</code> if the passed node was <code>null</code>.
   */
  @Nullable
  public static Document getOwnerDocument (@Nullable final Node aNode)
  {
    return aNode == null ? null : aNode instanceof Document ? (Document) aNode : aNode.getOwnerDocument ();
  }

  @Nonnull
  public static Node append (@Nonnull final Node aParentNode, @Nullable final Object aChild)
  {
    if (aParentNode == null)
      throw new NullPointerException ("parentNode");

    if (aChild != null)
      if (aChild instanceof Document)
      {
        // Special handling for Document comes first, as this is a special case
        // of "Node"

        // Cannot add complete documents!
        append (aParentNode, ((Document) aChild).getDocumentElement ());
      }
      else
        if (aChild instanceof Node)
        {
          // directly append Node
          final Node aChildNode = (Node) aChild;
          final Document aParentDoc = getOwnerDocument (aParentNode);
          if (getOwnerDocument (aChildNode).equals (aParentDoc))
          {
            // Nodes have the same parent
            aParentNode.appendChild (aChildNode);
          }
          else
          {
            // Node to be added belongs to a different document
            aParentNode.appendChild (aParentDoc.adoptNode (aChildNode.cloneNode (true)));
          }
        }
        else
          if (aChild instanceof String)
          {
            // append a string node
            aParentNode.appendChild (getOwnerDocument (aParentNode).createTextNode ((String) aChild));
          }
          else
            if (aChild instanceof Iterable <?>)
            {
              // it's a nested collection -> recursion
              for (final Object aSubChild : (Iterable <?>) aChild)
                append (aParentNode, aSubChild);
            }
            else
              if (ArrayHelper.isArray (aChild))
              {
                // it's a nested collection -> recursion
                for (final Object aSubChild : (Object []) aChild)
                  append (aParentNode, aSubChild);
              }
              else
              {
                // unsupported type
                throw new IllegalArgumentException ("Passed object cannot be appended to a DOMNode (type=" +
                                                    aChild.getClass ().getName () +
                                                    ".");
              }
    return aParentNode;
  }

  public static void append (@Nonnull final Node aSrcNode, @Nonnull final Collection <?> aNodesToAppend)
  {
    for (final Object aNode : aNodesToAppend)
      append (aSrcNode, aNode);
  }

  @Nonnegative
  public static int getDirectChildElementCountNoNS (@Nullable final Element aParent)
  {
    return aParent == null ? 0 : ContainerHelper.getSize (getChildElementIteratorNoNS (aParent));
  }

  @Nonnegative
  public static int getDirectChildElementCountNoNS (@Nullable final Element aParent,
                                                    @Nonnull @Nonempty final String sTagName)
  {
    return aParent == null ? 0 : ContainerHelper.getSize (getChildElementIteratorNoNS (aParent, sTagName));
  }

  @Nonnegative
  public static int getDirectChildElementCountNS (@Nullable final Element aParent, @Nullable final String sNamespaceURI)
  {
    return aParent == null ? 0 : ContainerHelper.getSize (getChildElementIteratorNS (aParent, sNamespaceURI));
  }

  @Nonnegative
  public static int getDirectChildElementCountNS (@Nullable final Element aParent,
                                                  @Nullable final String sNamespaceURI,
                                                  @Nonnull @Nonempty final String sLocalName)
  {
    return aParent == null ? 0
                          : ContainerHelper.getSize (getChildElementIteratorNS (aParent, sNamespaceURI, sLocalName));
  }

  /**
   * Get an iterator over all child elements that have no namespace.
   * 
   * @param aStartNode
   *        the parent element
   * @return a non-null Iterator
   */
  @Nonnull
  public static IIterableIterator <Element> getChildElementIteratorNoNS (@Nonnull final Node aStartNode)
  {
    return new ChildElementIterator (aStartNode, FilterElementWithoutNamespace.getInstance ());
  }

  /**
   * Get an iterator over all child elements that have no namespace and the
   * desired tag name.
   * 
   * @param aStartNode
   *        the parent element
   * @param sTagName
   *        the name of the tag that is desired
   * @return a non-null Iterator
   * @throws IllegalArgumentException
   *         if the passed tag name is null or empty
   */
  @Nonnull
  public static IIterableIterator <Element> getChildElementIteratorNoNS (@Nonnull final Node aStartNode,
                                                                         @Nonnull @Nonempty final String sTagName)
  {
    if (StringHelper.hasNoText (sTagName))
      throw new IllegalArgumentException ("Passed tag name is illegal");

    return new ChildElementIterator (aStartNode, new FilterElementWithTagName (sTagName));
  }

  @Nonnull
  public static IIterableIterator <Element> getChildElementIteratorNS (@Nonnull final Node aStartNode,
                                                                       @Nullable final String sNamespaceURI)
  {
    return new ChildElementIterator (aStartNode, new FilterElementWithNamespace (sNamespaceURI));
  }

  @Nonnull
  public static IIterableIterator <Element> getChildElementIteratorNS (@Nonnull final Node aStartNode,
                                                                       @Nullable final String sNamespaceURI,
                                                                       @Nonnull @Nonempty final String sLocalName)
  {
    if (StringHelper.hasNoText (sLocalName))
      throw new IllegalArgumentException ("Passed local name is illegal");

    return new ChildElementIterator (aStartNode, new FilterElementWithNamespaceAndLocalName (sNamespaceURI, sLocalName));
  }

  public static boolean hasNamespaceURI (@Nullable final Node aNode, @Nullable final String sNamespaceURI)
  {
    final String sNSURI = aNode == null ? null : aNode.getNamespaceURI ();
    return sNSURI != null && sNSURI.equals (sNamespaceURI);
  }

  /**
   * Shortcut for {@link #getPathToNode(Node, String)} using "/" as the
   * separator.
   * 
   * @param aNode
   *        The node to check.
   * @return A non-<code>null</code> path.
   */
  @Nonnull
  public static String getPathToNode (@Nonnull final Node aNode)
  {
    return getPathToNode (aNode, "/");
  }

  /**
   * Get the path from root node to the passed node.
   * 
   * @param aNode
   *        The node to start. May not be <code>null</code>.
   * @param sSep
   *        The separator string to use. May not be <code>null</code>.
   * @return The path to the node.
   */
  @Nonnull
  @SuppressFBWarnings ("IL_INFINITE_LOOP")
  public static String getPathToNode (@Nonnull final Node aNode, @Nonnull final String sSep)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (sSep == null)
      throw new NullPointerException ("separator");

    final StringBuilder aRet = new StringBuilder ();
    Node aCurNode = aNode;
    while (aCurNode != null)
    {
      final StringBuilder aName = new StringBuilder (aCurNode.getNodeName ());
      if (aCurNode.getNodeType () == Node.ELEMENT_NODE && aCurNode.getParentNode () != null)
      {
        // get index of my current element
        final Element aCurElement = (Element) aCurNode;
        int nIndex = 0;
        for (final Element x : getChildElementIteratorNoNS (aCurNode.getParentNode ()))
        {
          if (x == aCurNode)// NOPMD
            break;
          if (x.getTagName ().equals (aCurElement.getTagName ()))
            ++nIndex;
        }
        aName.append ('[').append (nIndex).append (']');
      }

      aRet.insert (0, sSep).insert (0, aName);

      // goto parent
      aCurNode = aCurNode.getParentNode ();
    }
    return aRet.toString ();
  }

  /**
   * Remove all child nodes of the given node.
   * 
   * @param aElement
   *        The element whose children are to be removed.
   */
  public static void removeAllChildElements (@Nonnull final Element aElement)
  {
    while (aElement.getChildNodes ().getLength () > 0)
      aElement.removeChild (aElement.getChildNodes ().item (0));
  }

  @Nonnull
  public static char [] getMaskedXMLText (@Nonnull final EXMLVersion eXMLVersion,
                                          @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                          @Nullable final String s)
  {
    if (eIncorrectCharHandling.isTestRequired () && s != null)
    {
      final char [] aChars = s.toCharArray ();
      if (containsInvalidXMLCharacter (aChars))
      {
        eIncorrectCharHandling.notifyOnInvalidXMLCharacter (s, getAllInvalidXMLCharacters (aChars));
        if (eIncorrectCharHandling.isReplaceWithNothing ())
          return StringHelper.replaceMultiple (s, MASK_PATTERNS_ALL, MASK_REPLACE_ALL_EMPTY);
      }
    }

    if (eXMLVersion.equals (EXMLVersion.XML_10))
    {
      return StringHelper.replaceMultiple (s, MASK_PATTERNS_XML10, MASK_REPLACE_XML10);
    }
    return StringHelper.replaceMultiple (s, MASK_PATTERNS_ALL, MASK_REPLACE_ALL_XML11);
  }

  @Nonnegative
  public static int getMaskedXMLTextLength (@Nonnull final EXMLVersion eXMLVersion,
                                            @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                            @Nullable final String s)
  {
    if (StringHelper.hasNoText (s))
      return 0;

    final char [] aChars = s.toCharArray ();
    if (eIncorrectCharHandling.isTestRequired () && containsInvalidXMLCharacter (aChars))
    {
      final Set <Character> aInvalidCharacters = getAllInvalidXMLCharacters (aChars);
      eIncorrectCharHandling.notifyOnInvalidXMLCharacter (s, aInvalidCharacters);
      if (eIncorrectCharHandling.isReplaceWithNothing ())
      {
        final int nResLen = StringHelper.getReplaceMultipleResultLength (aChars,
                                                                         MASK_PATTERNS_ALL,
                                                                         MASK_REPLACE_ALL_EMPTY);
        return nResLen == CGlobal.ILLEGAL_UINT ? s.length () : nResLen;
      }
    }

    int nResLen;
    if (eXMLVersion.equals (EXMLVersion.XML_10))
      nResLen = StringHelper.getReplaceMultipleResultLength (aChars, MASK_PATTERNS_XML10, MASK_REPLACE_XML10);
    else
      nResLen = StringHelper.getReplaceMultipleResultLength (aChars, MASK_PATTERNS_ALL, MASK_REPLACE_ALL_XML11);
    return nResLen == CGlobal.ILLEGAL_UINT ? s.length () : nResLen;
  }

  public static void maskXMLTextTo (@Nonnull final EXMLVersion eXMLVersion,
                                    @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                    @Nullable final String sText,
                                    @Nonnull final Writer aWriter) throws IOException
  {
    if (StringHelper.hasNoText (sText))
      return;

    if (eIncorrectCharHandling.isTestRequired ())
    {
      final char [] aChars = sText.toCharArray ();
      if (containsInvalidXMLCharacter (aChars))
      {
        final Set <Character> aInvalidCharacters = getAllInvalidXMLCharacters (aChars);
        eIncorrectCharHandling.notifyOnInvalidXMLCharacter (sText, aInvalidCharacters);
        if (eIncorrectCharHandling.isReplaceWithNothing ())
        {
          StringHelper.replaceMultipleTo (sText, MASK_PATTERNS_ALL, MASK_REPLACE_ALL_EMPTY, aWriter);
          return;
        }
      }
    }

    if (eXMLVersion.equals (EXMLVersion.XML_10))
      StringHelper.replaceMultipleTo (sText, MASK_PATTERNS_XML10, MASK_REPLACE_XML10, aWriter);
    else
      StringHelper.replaceMultipleTo (sText, MASK_PATTERNS_ALL, MASK_REPLACE_ALL_XML11, aWriter);
  }

  /**
   * Check if the passed node is a text node. This includes all nodes derived
   * from {@link CharacterData} which are not {@link Comment} nodes as well as
   * {@link EntityReference} nodes.
   * 
   * @param aNode
   *        The node to be checked.
   * @return <code>true</code> if the passed node is a text node,
   *         <code>false</code> otherwise.
   */
  public static boolean isTextNode (@Nullable final Node aNode)
  {
    return (aNode instanceof CharacterData && !(aNode instanceof Comment)) || aNode instanceof EntityReference;
  }

  /**
   * Get the content of the first Text child element of the passed element.
   * 
   * @param aStartNode
   *        the element to scan for a TextNode child
   * @return <code>null</code> if the element contains no text node as child
   */
  @Nullable
  public static String getFirstChildText (@Nullable final Node aStartNode)
  {
    if (aStartNode != null)
    {
      final NodeList aNodeList = aStartNode.getChildNodes ();
      final int nLen = aNodeList.getLength ();
      for (int i = 0; i < nLen; ++i)
      {
        final Node aNode = aNodeList.item (i);
        if (aNode instanceof Text)
        {
          final Text aText = (Text) aNode;

          // ignore whitespace-only content
          if (!aText.isElementContentWhitespace ())
            return aText.getData ();
        }
      }
    }
    return null;
  }

  /**
   * The latest version of XercesJ 2.9 returns an empty string for non existing
   * attributes. To differentiate between empty attributes and non-existing
   * attributes, this method returns null for non existing attributes.
   * 
   * @param aElement
   *        the source element to get the attribute from
   * @param sAttrName
   *        the name of the attribute to query
   * @return <code>null</code> if the attribute does not exists, the string
   *         value otherwise
   */
  @Nullable
  public static String getAttributeValue (@Nonnull final Element aElement, @Nonnull final String sAttrName)
  {
    return getAttributeValue (aElement, sAttrName, null);
  }

  /**
   * The latest version of XercesJ 2.9 returns an empty string for non existing
   * attributes. To differentiate between empty attributes and non-existing
   * attributes, this method returns a default value for non existing
   * attributes.
   * 
   * @param aElement
   *        the source element to get the attribute from. May not be
   *        <code>null</code>.
   * @param sAttrName
   *        the name of the attribute to query. May not be <code>null</code>.
   * @param sDefault
   *        the value to be returned if the attribute is not present.
   * @return the default value if the attribute does not exists, the string
   *         value otherwise
   */
  @Nullable
  public static String getAttributeValue (@Nonnull final Element aElement,
                                          @Nonnull final String sAttrName,
                                          @Nullable final String sDefault)
  {
    final Attr aAttr = aElement.getAttributeNode (sAttrName);
    return aAttr == null ? sDefault : aAttr.getValue ();
  }

  @Nullable
  public static Map <String, String> getAllAttributesAsMap (@Nullable final Element aSrcNode)
  {
    if (aSrcNode != null)
    {
      final NamedNodeMap aNNM = aSrcNode.getAttributes ();
      if (aNNM != null)
      {
        final Map <String, String> aMap = new LinkedHashMap <String, String> (aNNM.getLength ());
        final int nMax = aNNM.getLength ();
        for (int i = 0; i < nMax; ++i)
        {
          final Attr aAttr = (Attr) aNNM.item (i);
          aMap.put (aAttr.getName (), aAttr.getValue ());
        }
        return aMap;
      }
    }
    return null;
  }

  /**
   * Get the full qualified attribute name to use for the given namespace
   * prefix. The result will e.g. be <code>xmlns</code> or
   * <code>xmlns:foo</code>.
   * 
   * @param sNSPrefix
   *        The namespace prefix to build the attribute name from. May be
   *        <code>null</code> or empty.
   * @return If the namespace prefix is empty (if it equals
   *         {@link XMLConstants#DEFAULT_NS_PREFIX} or <code>null</code>) than
   *         "xmlns" is returned, else "xmlns:<i>prefix</i>" is returned.
   */
  @Nonnull
  public static String getXMLNSAttrName (@Nullable final String sNSPrefix)
  {
    if (sNSPrefix != null && sNSPrefix.contains (CXML.XML_PREFIX_NAMESPACE_SEP_STR))
      throw new IllegalArgumentException ("prefix is invalid: " + sNSPrefix);
    if (sNSPrefix == null || sNSPrefix.equals (XMLConstants.DEFAULT_NS_PREFIX))
      return CXML.XML_ATTR_XMLNS;
    return CXML.XML_ATTR_XMLNS_WITH_SEP + sNSPrefix;
  }

  /**
   * Get the full qualified attribute name to use for the given prefix.
   * 
   * @param sNSPrefix
   *        The namespace prefix to build the attribute name from. May neither
   *        be <code>null</code> nor empty.
   * @return "xmlns:<i>prefix</i>"
   */
  @Nonnull
  @Deprecated
  @DevelopersNote ("Use getXMLNSAttrName instead!")
  public static String getXMLNSPrefix (@Nonnull final String sNSPrefix)
  {
    if (StringHelper.hasNoText (sNSPrefix) || sNSPrefix.contains (CXML.XML_PREFIX_NAMESPACE_SEP_STR))
      throw new IllegalArgumentException ("prefix is invalid: " + sNSPrefix);
    return getXMLNSAttrName (sNSPrefix);
  }

  @Nullable
  public static String getNamespaceURI (@Nullable final Node aNode)
  {
    if (aNode instanceof Document)
      return getNamespaceURI (((Document) aNode).getDocumentElement ());
    if (aNode != null)
      return aNode.getNamespaceURI ();
    return null;
  }

  /**
   * Check if the passed character is valid for XML content. Works for XML 1.0
   * and XML 1.1.<br>
   * Note: makes no difference between the runtime JAXP solution and the
   * explicit Xerces version
   * 
   * @param c
   *        The character to be checked.
   * @return <code>true</code> if the character is valid in XML,
   *         <code>false</code> otherwise.
   */
  @Deprecated
  public static boolean isInvalidXMLCharacter (final char c)
  {
    // Based on: http://www.w3.org/TR/2006/REC-xml11-20060816/#charsets

    // Speed up by separating the most common use cases first
    if (c < 256)
    {
      // Character <= 0x00ff - use precomposed table
      return ILLEGAL_XML_CHARS[c];
    }

    // Character >= 0x0100
    // For completeness, the Unicode line separator character, #x2028, is
    // also supported.
    // Surrogate blocks (no Java IDs found)
    // High surrogate: 0xd800-0xdbff
    // Low surrogate: 0xdc00-0xdfff
    return c == '\u2028' ||
           (c >= '\ufdd0' && c <= '\ufddf') ||
           c == '\ufffe' ||
           c == '\uffff' ||
           Character.isHighSurrogate (c) ||
           Character.isLowSurrogate (c);
  }

  @Deprecated
  public static boolean containsInvalidXMLCharacter (@Nullable final String s)
  {
    return s != null && containsInvalidXMLCharacter (s.toCharArray ());
  }

  @Deprecated
  public static boolean containsInvalidXMLCharacter (@Nullable final char [] aChars)
  {
    if (aChars != null)
      for (final char c : aChars)
        if (isInvalidXMLCharacter (c))
          return true;
    return false;
  }

  @Nullable
  @Deprecated
  public static Set <Character> getAllInvalidXMLCharacters (@Nullable final String s)
  {
    return s == null ? null : getAllInvalidXMLCharacters (s.toCharArray ());
  }

  @Nullable
  @Deprecated
  public static Set <Character> getAllInvalidXMLCharacters (@Nullable final char [] aChars)
  {
    if (ArrayHelper.isEmpty (aChars))
      return null;

    final Set <Character> aRes = new HashSet <Character> ();
    for (final char c : aChars)
      if (isInvalidXMLCharacter (c))
        aRes.add (Character.valueOf (c));
    return aRes;
  }

  @Nonnegative
  public static int getLength (@Nullable final NodeList aNL)
  {
    return aNL == null ? 0 : aNL.getLength ();
  }

  public static boolean isEmpty (@Nullable final NodeList aNL)
  {
    return getLength (aNL) == 0;
  }

  /**
   * Check if the passed character is invalid for an element or attribute name
   * on the first position
   * 
   * @param eVersion
   *        XML version to be used. May not be <code>null</code>.
   * @param c
   *        char to check
   * @return <code>true</code> if the char is invalid
   */
  public static boolean isInvalidNameStartChar (@Nonnull final EXMLVersion eVersion, final int c)
  {
    switch (eVersion)
    {
      case XML_10:
        return XML_INVALID_NAME_START_CHAR_XML10.get (c);
      case XML_11:
        return XML_INVALID_NAME_START_CHAR_XML11.get (c);
      default:
        throw new IllegalArgumentException ("Unsupported XML version " + eVersion + "!");
    }
  }

  /**
   * Check if the passed character is invalid for an element or attribute name
   * after the first position
   * 
   * @param eVersion
   *        XML version to be used. May not be <code>null</code>.
   * @param c
   *        char to check
   * @return <code>true</code> if the char is invalid
   */
  public static boolean isInvalidNameChar (@Nonnull final EXMLVersion eVersion, final int c)
  {
    switch (eVersion)
    {
      case XML_10:
        return XML_INVALID_NAME_CHAR_XML10.get (c);
      case XML_11:
        return XML_INVALID_NAME_CHAR_XML11.get (c);
      default:
        throw new IllegalArgumentException ("Unsupported XML version " + eVersion + "!");
    }
  }

  /**
   * Check if the passed character is invalid for a text node.
   * 
   * @param eVersion
   *        XML version to be used. May not be <code>null</code>.
   * @param c
   *        char to check
   * @return <code>true</code> if the char is invalid
   */
  public static boolean isInvalidTextChar (@Nonnull final EXMLVersion eVersion, final int c)
  {
    switch (eVersion)
    {
      case XML_10:
        return XML_INVALID_TEXT_VALUE_CHAR_XML10.get (c);
      case XML_11:
        return XML_INVALID_TEXT_VALUE_CHAR_XML11.get (c);
      default:
        throw new IllegalArgumentException ("Unsupported XML version " + eVersion + "!");
    }
  }

  /**
   * Check if the passed character is invalid for a CDATA node.
   * 
   * @param eVersion
   *        XML version to be used. May not be <code>null</code>.
   * @param c
   *        char to check
   * @return <code>true</code> if the char is invalid
   */
  public static boolean isInvalidCDATAChar (@Nonnull final EXMLVersion eVersion, final int c)
  {
    switch (eVersion)
    {
      case XML_10:
        return XML_INVALID_ATTRCDATA_VALUE_CHAR_XML10.get (c);
      case XML_11:
        return XML_INVALID_ATTRCDATA_VALUE_CHAR_XML11.get (c);
      default:
        throw new IllegalArgumentException ("Unsupported XML version " + eVersion + "!");
    }
  }

  /**
   * Check if the passed character is invalid for a attribute value node.
   * 
   * @param eVersion
   *        XML version to be used. May not be <code>null</code>.
   * @param c
   *        char to check
   * @return <code>true</code> if the char is invalid
   */
  public static boolean isInvalidAttributeValueChar (@Nonnull final EXMLVersion eVersion, final int c)
  {
    switch (eVersion)
    {
      case XML_10:
        return XML_INVALID_ATTRCDATA_VALUE_CHAR_XML10.get (c);
      case XML_11:
        return XML_INVALID_ATTRCDATA_VALUE_CHAR_XML11.get (c);
      default:
        throw new IllegalArgumentException ("Unsupported XML version " + eVersion + "!");
    }
  }
}
