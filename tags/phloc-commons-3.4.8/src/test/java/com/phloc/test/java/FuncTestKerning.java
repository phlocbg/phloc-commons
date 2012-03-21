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
package com.phloc.test.java;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.collections.ContainerHelper;

/**
 * Reads a TTF font file and provides access to kerning information. Thanks to
 * the Apache FOP project for their inspiring work!
 * 
 * @author Nathan Sweet <misc@n4te.com>
 */
public class FuncTestKerning
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FuncTestJavaPrinterTrayFinder.class);

  private static final class Kerning
  {
    private final Map <Key, Integer> m_aKerning;
    private int m_dUnitsPerEm;
    private long bytePosition;
    private long m_nHeadOffset = -1;
    private long m_nKernOffset = -1;

    /**
     * @param input
     *        The data for the TTF font.
     * @throws IOException
     *         If the font could not be read.
     */
    public Kerning (final InputStream input) throws IOException
    {
      if (input == null)
        throw new IllegalArgumentException ("input cannot be null.");
      _readTableDirectory (input);
      if (m_nHeadOffset == -1)
        throw new IOException ("HEAD table not found.");
      if (m_nKernOffset == -1)
      {
        m_aKerning = ContainerHelper.newUnmodifiableMap ();
        return;
      }
      m_aKerning = new HashMap <Key, Integer> (2048);
      if (m_nHeadOffset < m_nKernOffset)
      {
        readHEAD (input);
        readKERN (input);
      }
      else
      {
        readKERN (input);
        readHEAD (input);
      }
      input.close ();
    }

    /**
     * Returns the kerning value for the specified glyphs. The glyph code for a
     * Unicode codepoint can be retrieved with
     * {@link GlyphVector#getGlyphCode(int)}.
     */
    public float getValue (final int firstGlyphCode, final int secondGlyphCode, final int size)
    {
      final Integer value = m_aKerning.get (new Key (firstGlyphCode, secondGlyphCode));
      if (value == null)
        return 0;
      return value.intValue () * (float) size / m_dUnitsPerEm;
    }

    private void _readTableDirectory (final InputStream input) throws IOException
    {
      skip (input, 4);
      final int tableCount = readUnsignedShort (input);
      skip (input, 6);

      final byte [] tagBytes = new byte [4];
      for (int i = 0; i < tableCount; i++)
      {
        tagBytes[0] = readByte (input);
        tagBytes[1] = readByte (input);
        tagBytes[2] = readByte (input);
        tagBytes[3] = readByte (input);
        skip (input, 4);
        final long offset = readUnsignedLong (input);
        skip (input, 4);

        final String tag = CharsetManager.getAsString (tagBytes, CCharset.CHARSET_ISO_8859_1);
        if (tag.equals ("head"))
        {
          m_nHeadOffset = offset;
          if (m_nKernOffset != -1)
            break;
        }
        else
          if (tag.equals ("kern"))
          {
            m_nKernOffset = offset;
            if (m_nHeadOffset != -1)
              break;
          }
      }
    }

    private void readHEAD (final InputStream input) throws IOException
    {
      seek (input, m_nHeadOffset + 2 * 4 + 2 * 4 + 2);
      m_dUnitsPerEm = readUnsignedShort (input);
    }

    private void readKERN (final InputStream input) throws IOException
    {
      seek (input, m_nKernOffset + 2);
      for (int n = readUnsignedShort (input); n > 0; n--)
      {
        skip (input, 2 * 2);
        int k = readUnsignedShort (input);
        if (!((k & 1) != 0) || (k & 2) != 0 || (k & 4) != 0)
          return;
        if (k >> 8 != 0)
          continue;
        k = readUnsignedShort (input);
        skip (input, 3 * 2);
        while (k-- > 0)
        {
          final int firstGlyphCode = readUnsignedShort (input);
          final int secondGlyphCode = readUnsignedShort (input);
          final int value = readShort (input);
          if (value != 0)
            m_aKerning.put (new Key (firstGlyphCode, secondGlyphCode), Integer.valueOf (value));
        }
      }
    }

    private int readUnsignedByte (final InputStream input) throws IOException
    {
      bytePosition++;
      final int b = input.read ();
      if (b == -1)
        throw new EOFException ("Unexpected end of file.");
      return b;
    }

    private byte readByte (final InputStream input) throws IOException
    {
      return (byte) readUnsignedByte (input);
    }

    private int readUnsignedShort (final InputStream input) throws IOException
    {
      return (readUnsignedByte (input) << 8) + readUnsignedByte (input);
    }

    private short readShort (final InputStream input) throws IOException
    {
      return (short) readUnsignedShort (input);
    }

    private long readUnsignedLong (final InputStream input) throws IOException
    {
      long value = readUnsignedByte (input);
      value = (value << 8) + readUnsignedByte (input);
      value = (value << 8) + readUnsignedByte (input);
      value = (value << 8) + readUnsignedByte (input);
      return value;
    }

    private void skip (final InputStream input, final int bytes) throws IOException
    {
      if (input.skip (bytes) != bytes)
        throw new IllegalStateException ("skip failed");
      bytePosition += bytes;
    }

    private void seek (final InputStream input, final long position) throws IOException
    {
      if (input.skip (position - bytePosition) != (position - bytePosition))
        throw new IllegalStateException ("skip failed");
      bytePosition = position;
    }

    private static final class Key
    {
      private final int m_nFirstGlyphCode, m_nSecondGlyphCode;

      public Key (final int firstGlyphCode, final int secondGlyphCode)
      {
        m_nFirstGlyphCode = firstGlyphCode;
        m_nSecondGlyphCode = secondGlyphCode;
      }

      @Override
      public int hashCode ()
      {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_nFirstGlyphCode;
        result = prime * result + m_nSecondGlyphCode;
        return result;
      }

      @Override
      public boolean equals (final Object obj)
      {
        if (this == obj)
          return true;
        if (!(obj instanceof Key))
          return false;
        final Key other = (Key) obj;
        if (m_nFirstGlyphCode != other.m_nFirstGlyphCode)
          return false;
        if (m_nSecondGlyphCode != other.m_nSecondGlyphCode)
          return false;
        return true;
      }
    }
  }

  @Test
  public void testKerning () throws IOException
  {
    // required to get graphics up and running...
    GraphicsEnvironment.getLocalGraphicsEnvironment ();
    final Map <TextAttribute, Object> textAttributes = new HashMap <TextAttribute, Object> ();
    textAttributes.put (TextAttribute.FAMILY, "Arial");
    textAttributes.put (TextAttribute.SIZE, Float.valueOf (25f));
    final Font font = Font.getFont (textAttributes);
    final char [] chars = "T,".toCharArray ();
    final GlyphVector vector = font.layoutGlyphVector (new FontRenderContext (new AffineTransform (), false, true),
                                                       chars,
                                                       0,
                                                       chars.length,
                                                       Font.LAYOUT_LEFT_TO_RIGHT);
    final int tCode = vector.getGlyphCode (0);
    final int commaCode = vector.getGlyphCode (1);
    final Kerning kerning = new Kerning (new FileInputStream (System.getenv ("windir") + "/fonts/ARIAL.TTF"));
    s_aLogger.info (Float.toString (kerning.getValue (tCode, commaCode, 25)));
  }
}
