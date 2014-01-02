/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.charset;

import java.util.Arrays;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasByteSize;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;

/**
 * Defines the most common Byte Order Markers for Unicode encoded text files.<br>
 * Source: http://de.wikipedia.org/wiki/Byte_Order_Mark<br>
 * Important: BOMS with more bytes should come first to avoid wrong detections.
 * 
 * @author Philip Helger
 */
// ESCA-JAVA0076:
public enum EUnicodeBOM implements IHasByteSize
{
  // 4 bytes
  BOM_UTF_32_BIG_ENDIAN (new byte [] { 0x00, 0x00, (byte) 0xfe, (byte) 0xff }),
  BOM_UTF_32_LITTLE_ENDIAN (new byte [] { (byte) 0xff, (byte) 0xfe, 0x00, 0x00 }),
  BOM_UTF_7 (new byte [] { 0x2b, 0x2f, 0x76, 0x38 }),
  BOM_UTF_7_ALT2 (new byte [] { 0x2b, 0x2f, 0x76, 0x39 }),
  BOM_UTF_7_ALT3 (new byte [] { 0x2b, 0x2f, 0x76, 0x2b }),
  BOM_UTF_7_ALT4 (new byte [] { 0x2b, 0x2f, 0x76, 0x2f }),
  BOM_UTF_EBCDIC (new byte [] { (byte) 0xdd, 0x73, 0x66, 0x73 }),
  BOM_BOCU_1_ALT2 (new byte [] { (byte) 0xfb, (byte) 0xee, 0x28, (byte) 0xff }),
  BOM_GB_18030 (new byte [] { (byte) 0x84, 0x31, (byte) 0x95, 0x33 }),
  // 3 bytes
  BOM_UTF_8 (new byte [] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf }),
  BOM_UTF_1 (new byte [] { (byte) 0xf7, 0x64, 0x4c }),
  BOM_BOCU_1 (new byte [] { (byte) 0xfb, (byte) 0xee, 0x28 }),
  BOM_SCSU (new byte [] { 0x0e, (byte) 0xfe, (byte) 0xff }),
  // 2 bytes
  BOM_UTF_16_BIG_ENDIAN (new byte [] { (byte) 0xfe, (byte) 0xff }),
  BOM_UTF_16_LITTLE_ENDIAN (new byte [] { (byte) 0xff, (byte) 0xfe });

  private final byte [] m_aBOMBytes;

  private EUnicodeBOM (@Nonnull @Nonempty final byte [] aBytes)// NOPMD
  {
    m_aBOMBytes = aBytes;
  }

  /**
   * @return A copy of the byte array that identifies this BOM.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public byte [] getBytes ()
  {
    return ArrayHelper.getCopy (m_aBOMBytes);
  }

  /**
   * @return The number of bytes defining this BOM
   */
  @Nonnegative
  public int getByteCount ()
  {
    return m_aBOMBytes.length;
  }

  /**
   * @return The number of bytes defining this BOM
   */
  @Nonnegative
  public long getSizeInBytes ()
  {
    return m_aBOMBytes.length;
  }

  /**
   * Check if the passed byte array starts with this BOM's bytes.
   * 
   * @param aBytes
   *        The byte array to search for a BOM. May be <code>null</code> or
   *        empty.
   * @return <code>true</code> if the passed byte array starts with this BOM,
   *         <code>false</code> otherwise.
   */
  public boolean isPresent (@Nullable final byte [] aBytes)
  {
    final int nLength = m_aBOMBytes.length;
    return aBytes != null &&
           aBytes.length >= nLength &&
           Arrays.equals (m_aBOMBytes, ArrayHelper.getCopy (aBytes, 0, nLength));
  }

  /**
   * @return The maximum number of bytes a BOM may have.
   */
  @Nonnegative
  public static int getMaximumByteCount ()
  {
    int ret = 0;
    for (final EUnicodeBOM eBOM : values ())
      if (eBOM.getByteCount () > ret)
        ret = eBOM.getByteCount ();
    return ret;
  }

  /**
   * Find the BOM that is matching the passed byte array.
   * 
   * @param aBytes
   *        The bytes to be checked for the BOM. May be <code>null</code>. To
   *        check all BOMs, this array must have at least 4 (=
   *        {@link #getMaximumByteCount()}) bytes.
   * @return <code>null</code> if the passed bytes do not resemble a BOM.
   */
  @Nullable
  public static EUnicodeBOM getFromBytesOrNull (@Nullable final byte [] aBytes)
  {
    if (ArrayHelper.isNotEmpty (aBytes))
      for (final EUnicodeBOM eBOM : values ())
        if (eBOM.isPresent (aBytes))
          return eBOM;
    return null;
  }
}
