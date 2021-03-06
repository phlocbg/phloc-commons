/**
 * Copyright (C) 2013-2014 phloc systems
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
package com.phloc.settings.xchange.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import com.phloc.commons.CGlobal;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.NonBlockingBufferedWriter;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.StringHelper;

/**
 * The <code>Properties</code> class represents a persistent set of properties.
 * The <code>Properties</code> can be saved to a stream or loaded from a stream.
 * Each key and its corresponding value in the property list is a string.
 * <p>
 * A property list can contain another property list as its "defaults"; this
 * second property list is searched if the property key is not found in the
 * original property list.
 * <p>
 * Because <code>Properties</code> inherits from <code>Hashtable</code>, the
 * <code>put</code> and <code>putAll</code> methods can be applied to a
 * <code>Properties</code> object. Their use is strongly discouraged as they
 * allow the caller to insert entries whose keys or values are not
 * <code>Strings</code>. The <code>setProperty</code> method should be used
 * instead. If the <code>store</code> or <code>save</code> method is called on a
 * "compromised" <code>Properties</code> object that contains a non-
 * <code>String</code> key or value, the call will fail. Similarly, the call to
 * the <code>propertyNames</code> or <code>list</code> method will fail if it is
 * called on a "compromised" <code>Properties</code> object that contains a non-
 * <code>String</code> key.
 * <p>
 * The {@link #load(java.io.Reader) load(Reader)} <tt>/</tt>
 * {@link #store(java.io.Writer, java.lang.String) store(Writer, String)}
 * methods load and store properties from and to a character based stream in a
 * simple line-oriented format specified below. The
 * {@link #load(java.io.InputStream) load(InputStream)} <tt>/</tt>
 * {@link #store(java.io.OutputStream, java.lang.String) store(OutputStream,
 * String)} methods work the same way as the load(Reader)/store(Writer, String)
 * pair, except the input/output stream is encoded in ISO 8859-1 character
 * encoding. Characters that cannot be directly represented in this encoding can
 * be written using <a href=
 * "http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.3"
 * >Unicode escapes</a> ; only a single 'u' character is allowed in an escape
 * sequence. The native2ascii tool can be used to convert property files to and
 * from other character encodings.
 * 
 * @author Arthur van Hoff
 * @author Michael McCloskey
 * @author Xueming Shen
 * @author Philip Helger
 */
public class NonBlockingProperties extends TreeMap <String, String>
{
  /**
   * A property list that contains default values for any keys not found in this
   * property list.
   * 
   * @serial
   */
  protected NonBlockingProperties m_aDefaults;

  /**
   * Creates an empty property list with no default values.
   */
  public NonBlockingProperties ()
  {
    this (null);
  }

  /**
   * Creates an empty property list with the specified defaults.
   * 
   * @param defaults
   *        the defaults.
   */
  public NonBlockingProperties (final NonBlockingProperties defaults)
  {
    m_aDefaults = defaults;
  }

  /**
   * Calls the <tt>Hashtable</tt> method <code>put</code>. Provided for
   * parallelism with the <tt>getProperty</tt> method. Enforces use of strings
   * for property keys and values. The value returned is the result of the
   * <tt>Hashtable</tt> call to <code>put</code>.
   * 
   * @param key
   *        the key to be placed into this property list.
   * @param value
   *        the value corresponding to <tt>key</tt>.
   * @return the previous value of the specified key in this property list, or
   *         <code>null</code> if it did not have one.
   * @see #getProperty
   * @since 1.2
   */
  public String setProperty (final String key, final String value)
  {
    return put (key, value);
  }

  /**
   * Reads a property list (key and element pairs) from the input character
   * stream in a simple line-oriented format.
   * <p>
   * Properties are processed in terms of lines. There are two kinds of line,
   * <i>natural lines</i> and <i>logical lines</i>. A natural line is defined as
   * a line of characters that is terminated either by a set of line terminator
   * characters (<code>\n</code> or <code>\r</code> or <code>\r\n</code>) or by
   * the end of the stream. A natural line may be either a blank line, a comment
   * line, or hold all or some of a key-element pair. A logical line holds all
   * the data of a key-element pair, which may be spread out across several
   * adjacent natural lines by escaping the line terminator sequence with a
   * backslash character <code>\</code>. Note that a comment line cannot be
   * extended in this manner; every natural line that is a comment must have its
   * own comment indicator, as described below. Lines are read from input until
   * the end of the stream is reached.
   * <p>
   * A natural line that contains only white space characters is considered
   * blank and is ignored. A comment line has an ASCII <code>'#'</code> or
   * <code>'!'</code> as its first non-white space character; comment lines are
   * also ignored and do not encode key-element information. In addition to line
   * terminators, this format considers the characters space (<code>' '</code>,
   * <code>'&#92;u0020'</code>), tab (<code>'\t'</code>,
   * <code>'&#92;u0009'</code>), and form feed (<code>'\f'</code>,
   * <code>'&#92;u000C'</code>) to be white space.
   * <p>
   * If a logical line is spread across several natural lines, the backslash
   * escaping the line terminator sequence, the line terminator sequence, and
   * any white space at the start of the following line have no affect on the
   * key or element values. The remainder of the discussion of key and element
   * parsing (when loading) will assume all the characters constituting the key
   * and element appear on a single natural line after line continuation
   * characters have been removed. Note that it is <i>not</i> sufficient to only
   * examine the character preceding a line terminator sequence to decide if the
   * line terminator is escaped; there must be an odd number of contiguous
   * backslashes for the line terminator to be escaped. Since the input is
   * processed from left to right, a non-zero even number of 2<i>n</i>
   * contiguous backslashes before a line terminator (or elsewhere) encodes
   * <i>n</i> backslashes after escape processing.
   * <p>
   * The key contains all of the characters in the line starting with the first
   * non-white space character and up to, but not including, the first unescaped
   * <code>'='</code>, <code>':'</code>, or white space character other than a
   * line terminator. All of these key termination characters may be included in
   * the key by escaping them with a preceding backslash character; for example,
   * <p>
   * <code>\:\=</code>
   * <p>
   * would be the two-character key <code>":="</code>. Line terminator
   * characters can be included using <code>\r</code> and <code>\n</code> escape
   * sequences. Any white space after the key is skipped; if the first non-white
   * space character after the key is <code>'='</code> or <code>':'</code>, then
   * it is ignored and any white space characters after it are also skipped. All
   * remaining characters on the line become part of the associated element
   * string; if there are no remaining characters, the element is the empty
   * string <code>&quot;&quot;</code>. Once the raw character sequences
   * constituting the key and element are identified, escape processing is
   * performed as described above.
   * <p>
   * As an example, each of the following three lines specifies the key
   * <code>"Truth"</code> and the associated element value <code>"Beauty"</code>:
   * <p>
   * 
   * <pre>
   * Truth = Beauty
   *  Truth:Beauty
   * Truth      :Beauty
   * </pre>
   * 
   * As another example, the following three lines specify a single property:
   * <p>
   * 
   * <pre>
   * fruits                           apple, banana, pear, \
   *                                  cantaloupe, watermelon, \
   *                                  kiwi, mango
   * </pre>
   * 
   * The key is <code>"fruits"</code> and the associated element is:
   * <p>
   * 
   * <pre>
   * &quot;apple, banana, pear, cantaloupe, watermelon, kiwi, mango&quot;
   * </pre>
   * 
   * Note that a space appears before each <code>\</code> so that a space will
   * appear after each comma in the final result; the <code>\</code>, line
   * terminator, and leading white space on the continuation line are merely
   * discarded and are <i>not</i> replaced by one or more other characters.
   * <p>
   * As a third example, the line:
   * <p>
   * 
   * <pre>
   * cheeses
   * </pre>
   * 
   * specifies that the key is <code>"cheeses"</code> and the associated element
   * is the empty string <code>""</code>.
   * <p>
   * <p>
   * <a name="unicodeescapes"></a> Characters in keys and elements can be
   * represented in escape sequences similar to those used for character and
   * string literals (see <a href=
   * "http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.3"
   * >&sect;3.3</a> and <a href=
   * "http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.10.6"
   * >&sect;3.10.6</a> of the <i>Java Language Specification</i>). The
   * differences from the character escape sequences and Unicode escapes used
   * for characters and strings are:
   * <ul>
   * <li>Octal escapes are not recognized.
   * <li>The character sequence <code>\b</code> does <i>not</i> represent a
   * backspace character.
   * <li>The method does not treat a backslash character, <code>\</code>, before
   * a non-valid escape character as an error; the backslash is silently
   * dropped. For example, in a Java string the sequence <code>"\z"</code> would
   * cause a compile time error. In contrast, this method silently drops the
   * backslash. Therefore, this method treats the two character sequence
   * <code>"\b"</code> as equivalent to the single character <code>'b'</code>.
   * <li>Escapes are not necessary for single and double quotes; however, by the
   * rule above, single and double quote characters preceded by a backslash
   * still yield single and double quote characters, respectively.
   * <li>Only a single 'u' character is allowed in a Uniocde escape sequence.
   * </ul>
   * <p>
   * The specified stream remains open after this method returns.
   * 
   * @param reader
   *        the input character stream.
   * @throws IOException
   *         if an error occurred when reading from the input stream.
   * @throws IllegalArgumentException
   *         if a malformed Unicode escape appears in the input.
   * @since 1.6
   */
  public void load (final Reader reader) throws IOException
  {
    _load0 (new LineReader (reader));
  }

  /**
   * Reads a property list (key and element pairs) from the input byte stream.
   * The input stream is in a simple line-oriented format as specified in
   * {@link #load(java.io.Reader) load(Reader)} and is assumed to use the ISO
   * 8859-1 character encoding; that is each byte is one Latin1 character.
   * Characters not in Latin1, and certain special characters, are represented
   * in keys and elements using <a href=
   * "http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.3"
   * >Unicode escapes</a>.
   * <p>
   * The specified stream remains open after this method returns.
   * 
   * @param inStream
   *        the input stream.
   * @exception IOException
   *            if an error occurred when reading from the input stream.
   * @throws IllegalArgumentException
   *         if the input stream contains a malformed Unicode escape sequence.
   * @since 1.2
   */
  public void load (final InputStream inStream) throws IOException
  {
    _load0 (new LineReader (inStream));
  }

  private void _load0 (final LineReader lr) throws IOException
  {
    final char [] convtBuf = new char [1024];
    int limit;
    int keyLen;
    int valueStart;
    char c;
    boolean hasSep;
    boolean precedingBackslash;

    while ((limit = lr.readLine ()) >= 0)
    {
      c = 0;
      keyLen = 0;
      valueStart = limit;
      hasSep = false;

      // System.out.println("line=<" + new String(lineBuf, 0, limit) + ">");
      precedingBackslash = false;
      while (keyLen < limit)
      {
        c = lr.lineBuf[keyLen];
        // need check if escaped.
        if ((c == '=' || c == ':') && !precedingBackslash)
        {
          valueStart = keyLen + 1;
          hasSep = true;
          break;
        }
        else
          if ((c == ' ' || c == '\t' || c == '\f') && !precedingBackslash)
          {
            valueStart = keyLen + 1;
            break;
          }
        if (c == '\\')
        {
          precedingBackslash = !precedingBackslash;
        }
        else
        {
          precedingBackslash = false;
        }
        keyLen++;
      }
      while (valueStart < limit)
      {
        c = lr.lineBuf[valueStart];
        if (c != ' ' && c != '\t' && c != '\f')
        {
          if (!hasSep && (c == '=' || c == ':'))
          {
            hasSep = true;
          }
          else
          {
            break;
          }
        }
        valueStart++;
      }
      final String key = _loadConvert (lr.lineBuf, 0, keyLen, convtBuf);
      final String value = _loadConvert (lr.lineBuf, valueStart, limit - valueStart, convtBuf);
      put (key, value);
    }
  }

  /*
   * Read in a "logical line" from an InputStream/Reader, skip all comment and
   * blank lines and filter out those leading whitespace characters ( , and )
   * from the beginning of a "natural line". Method returns the char length of
   * the "logical line" and stores the line in "lineBuf".
   */
  static class LineReader
  {
    private byte [] m_aInByteBuf;
    private char [] m_aInCharBuf;
    private char [] lineBuf = new char [1024];
    private int inLimit = 0;
    private int inOff = 0;
    private InputStream m_aIS;
    private Reader m_aReader;

    public LineReader (final InputStream inStream)
    {
      m_aIS = inStream;
      m_aInByteBuf = new byte [8192];
    }

    public LineReader (final Reader reader)
    {
      m_aReader = reader;
      m_aInCharBuf = new char [8192];
    }

    int readLine () throws IOException
    {
      int len = 0;
      char c = 0;

      boolean skipWhiteSpace = true;
      boolean isCommentLine = false;
      boolean isNewLine = true;
      boolean appendedLineBegin = false;
      boolean precedingBackslash = false;
      boolean skipLF = false;

      while (true)
      {
        if (inOff >= inLimit)
        {
          inLimit = (m_aIS == null) ? m_aReader.read (m_aInCharBuf) : m_aIS.read (m_aInByteBuf);
          inOff = 0;
          if (inLimit <= 0)
          {
            if (len == 0 || isCommentLine)
            {
              return -1;
            }
            return len;
          }
        }
        if (m_aIS != null)
        {
          // The line below is equivalent to calling a
          // ISO8859-1 decoder.
          c = (char) (0xff & m_aInByteBuf[inOff++]);
        }
        else
        {
          c = m_aInCharBuf[inOff++];
        }
        if (skipLF)
        {
          skipLF = false;
          if (c == '\n')
          {
            continue;
          }
        }
        if (skipWhiteSpace)
        {
          if (c == ' ' || c == '\t' || c == '\f')
          {
            continue;
          }
          if (!appendedLineBegin && (c == '\r' || c == '\n'))
          {
            continue;
          }
          skipWhiteSpace = false;
          appendedLineBegin = false;
        }
        if (isNewLine)
        {
          isNewLine = false;
          if (c == '#' || c == '!')
          {
            isCommentLine = true;
            continue;
          }
        }

        if (c != '\n' && c != '\r')
        {
          lineBuf[len++] = c;
          if (len == lineBuf.length)
          {
            int newLength = lineBuf.length * 2;
            if (newLength < 0)
            {
              newLength = Integer.MAX_VALUE;
            }
            final char [] buf = new char [newLength];
            System.arraycopy (lineBuf, 0, buf, 0, lineBuf.length);
            lineBuf = buf;
          }
          // flip the preceding backslash flag
          if (c == '\\')
          {
            precedingBackslash = !precedingBackslash;
          }
          else
          {
            precedingBackslash = false;
          }
        }
        else
        {
          // reached EOL
          if (isCommentLine || len == 0)
          {
            isCommentLine = false;
            isNewLine = true;
            skipWhiteSpace = true;
            len = 0;
            continue;
          }
          if (inOff >= inLimit)
          {
            inLimit = (m_aIS == null) ? m_aReader.read (m_aInCharBuf) : m_aIS.read (m_aInByteBuf);
            inOff = 0;
            if (inLimit <= 0)
            {
              return len;
            }
          }
          if (precedingBackslash)
          {
            len -= 1;
            // skip the leading whitespace characters in following line
            skipWhiteSpace = true;
            appendedLineBegin = true;
            precedingBackslash = false;
            if (c == '\r')
            {
              skipLF = true;
            }
          }
          else
          {
            return len;
          }
        }
      }
    }
  }

  /*
   * Converts encoded &#92;uxxxx to unicode chars and changes special saved
   * chars to their original forms
   */
  private String _loadConvert (final char [] in, final int poff, final int len, final char [] pconvtBuf)
  {
    int off = poff;
    char [] convtBuf = pconvtBuf;
    if (convtBuf.length < len)
    {
      int newLen = len * 2;
      if (newLen < 0)
      {
        newLen = Integer.MAX_VALUE;
      }
      convtBuf = new char [newLen];
    }
    char aChar;
    final char [] out = convtBuf;
    int outLen = 0;
    final int end = off + len;

    while (off < end)
    {
      aChar = in[off++];
      if (aChar == '\\')
      {
        aChar = in[off++];
        if (aChar == 'u')
        {
          // Read the xxxx
          int value = 0;
          for (int i = 0; i < 4; i++)
          {
            aChar = in[off++];
            switch (aChar)
            {
              case '0':
              case '1':
              case '2':
              case '3':
              case '4':
              case '5':
              case '6':
              case '7':
              case '8':
              case '9':
                value = (value << 4) + aChar - '0';
                break;
              case 'a':
              case 'b':
              case 'c':
              case 'd':
              case 'e':
              case 'f':
                value = (value << 4) + 10 + aChar - 'a';
                break;
              case 'A':
              case 'B':
              case 'C':
              case 'D':
              case 'E':
              case 'F':
                value = (value << 4) + 10 + aChar - 'A';
                break;
              default:
                throw new IllegalArgumentException ("Malformed \\uxxxx encoding.");
            }
          }
          out[outLen++] = (char) value;
        }
        else
        {
          if (aChar == 't')
            aChar = '\t';
          else
            if (aChar == 'r')
              aChar = '\r';
            else
              if (aChar == 'n')
                aChar = '\n';
              else
                if (aChar == 'f')
                  aChar = '\f';
          out[outLen++] = aChar;
        }
      }
      else
      {
        out[outLen++] = aChar;
      }
    }
    return new String (out, 0, outLen);
  }

  /*
   * Converts unicodes to encoded &#92;uxxxx and escapes special characters with
   * a preceding slash
   */
  private String _saveConvert (final String theString, final boolean escapeSpace, final boolean escapeUnicode)
  {
    final int len = theString.length ();
    int bufLen = len * 2;
    if (bufLen < 0)
    {
      bufLen = Integer.MAX_VALUE;
    }
    final StringBuffer outBuffer = new StringBuffer (bufLen);

    for (int x = 0; x < len; x++)
    {
      final char aChar = theString.charAt (x);
      // Handle common case first, selecting largest block that
      // avoids the specials below
      if ((aChar > 61) && (aChar < 127))
      {
        if (aChar == '\\')
        {
          outBuffer.append ('\\');
          outBuffer.append ('\\');
          continue;
        }
        outBuffer.append (aChar);
        continue;
      }
      switch (aChar)
      {
        case ' ':
          if (x == 0 || escapeSpace)
            outBuffer.append ('\\');
          outBuffer.append (' ');
          break;
        case '\t':
          outBuffer.append ('\\');
          outBuffer.append ('t');
          break;
        case '\n':
          outBuffer.append ('\\');
          outBuffer.append ('n');
          break;
        case '\r':
          outBuffer.append ('\\');
          outBuffer.append ('r');
          break;
        case '\f':
          outBuffer.append ('\\');
          outBuffer.append ('f');
          break;
        case '=': // Fall through
        case ':': // Fall through
        case '#': // Fall through
        case '!':
          outBuffer.append ('\\');
          outBuffer.append (aChar);
          break;
        default:
          if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode)
          {
            outBuffer.append ('\\');
            outBuffer.append ('u');
            outBuffer.append (StringHelper.getHexChar ((aChar >> 12) & 0xF));
            outBuffer.append (StringHelper.getHexChar ((aChar >> 8) & 0xF));
            outBuffer.append (StringHelper.getHexChar ((aChar >> 4) & 0xF));
            outBuffer.append (StringHelper.getHexChar (aChar & 0xF));
          }
          else
          {
            outBuffer.append (aChar);
          }
      }
    }
    return outBuffer.toString ();
  }

  private static void _writeComments (final Writer bw, final String comments) throws IOException
  {
    bw.write ("#");
    final int len = comments.length ();
    int current = 0;
    int last = 0;
    final char [] uu = new char [6];
    uu[0] = '\\';
    uu[1] = 'u';
    while (current < len)
    {
      final char c = comments.charAt (current);
      if (c > '\u00ff' || c == '\n' || c == '\r')
      {
        if (last != current)
          bw.write (comments.substring (last, current));
        if (c > '\u00ff')
        {
          uu[2] = StringHelper.getHexChar ((c >> 12) & 0xf);
          uu[3] = StringHelper.getHexChar ((c >> 8) & 0xf);
          uu[4] = StringHelper.getHexChar ((c >> 4) & 0xf);
          uu[5] = StringHelper.getHexChar (c & 0xf);
          bw.write (uu);
        }
        else
        {
          bw.write (CGlobal.LINE_SEPARATOR);
          if (c == '\r' && current != len - 1 && comments.charAt (current + 1) == '\n')
          {
            current++;
          }
          if (current == len - 1 || (comments.charAt (current + 1) != '#' && comments.charAt (current + 1) != '!'))
            bw.write ("#");
        }
        last = current + 1;
      }
      current++;
    }
    if (last != current)
      bw.write (comments.substring (last, current));
    bw.write (CGlobal.LINE_SEPARATOR);
  }

  /**
   * Writes this property list (key and element pairs) in this
   * <code>Properties</code> table to the output character stream in a format
   * suitable for using the {@link #load(java.io.Reader) load(Reader)} method.
   * <p>
   * Properties from the defaults table of this <code>Properties</code> table
   * (if any) are <i>not</i> written out by this method.
   * <p>
   * If the comments argument is not null, then an ASCII <code>#</code>
   * character, the comments string, and a line separator are first written to
   * the output stream. Thus, the <code>comments</code> can serve as an
   * identifying comment. Any one of a line feed ('\n'), a carriage return
   * ('\r'), or a carriage return followed immediately by a line feed in
   * comments is replaced by a line separator generated by the
   * <code>Writer</code> and if the next character in comments is not character
   * <code>#</code> or character <code>!</code> then an ASCII <code>#</code> is
   * written out after that line separator.
   * <p>
   * Next, a comment line is always written, consisting of an ASCII
   * <code>#</code> character, the current date and time (as if produced by the
   * <code>toString</code> method of <code>Date</code> for the current time),
   * and a line separator as generated by the <code>Writer</code>.
   * <p>
   * Then every entry in this <code>Properties</code> table is written out, one
   * per line. For each entry the key string is written, then an ASCII
   * <code>=</code>, then the associated element string. For the key, all space
   * characters are written with a preceding <code>\</code> character. For the
   * element, leading space characters, but not embedded or trailing space
   * characters, are written with a preceding <code>\</code> character. The key
   * and element characters <code>#</code>, <code>!</code>, <code>=</code>, and
   * <code>:</code> are written with a preceding backslash to ensure that they
   * are properly loaded.
   * <p>
   * After the entries have been written, the output stream is flushed. The
   * output stream remains open after this method returns.
   * <p>
   * 
   * @param writer
   *        an output character stream writer.
   * @param comments
   *        a description of the property list.
   * @exception IOException
   *            if writing this property list to the specified output stream
   *            throws an <tt>IOException</tt>.
   * @exception ClassCastException
   *            if this <code>Properties</code> object contains any keys or
   *            values that are not <code>Strings</code>.
   * @exception NullPointerException
   *            if <code>writer</code> is null.
   * @since 1.6
   */
  public void store (@Nonnull final Writer writer, final String comments) throws IOException
  {
    _store0 (StreamUtils.getBuffered (writer), comments, false);
  }

  /**
   * Writes this property list (key and element pairs) in this
   * <code>Properties</code> table to the output stream in a format suitable for
   * loading into a <code>Properties</code> table using the
   * {@link #load(InputStream) load(InputStream)} method.
   * <p>
   * Properties from the defaults table of this <code>Properties</code> table
   * (if any) are <i>not</i> written out by this method.
   * <p>
   * This method outputs the comments, properties keys and values in the same
   * format as specified in {@link #store(java.io.Writer, java.lang.String)
   * store(Writer)}, with the following differences:
   * <ul>
   * <li>The stream is written using the ISO 8859-1 character encoding.
   * <li>Characters not in Latin-1 in the comments are written as
   * <code>&#92;u</code><i>xxxx</i> for their appropriate unicode hexadecimal
   * value <i>xxxx</i>.
   * <li>Characters less than <code>&#92;u0020</code> and characters greater
   * than <code>&#92;u007E</code> in property keys or values are written as
   * <code>&#92;u</code><i>xxxx</i> for the appropriate hexadecimal value
   * <i>xxxx</i>.
   * </ul>
   * <p>
   * After the entries have been written, the output stream is flushed. The
   * output stream remains open after this method returns.
   * <p>
   * 
   * @param out
   *        an output stream.
   * @param comments
   *        a description of the property list.
   * @exception IOException
   *            if writing this property list to the specified output stream
   *            throws an <tt>IOException</tt>.
   * @exception ClassCastException
   *            if this <code>Properties</code> object contains any keys or
   *            values that are not <code>Strings</code>.
   * @exception NullPointerException
   *            if <code>out</code> is null.
   * @since 1.2
   */
  public void store (@Nonnull final OutputStream out, final String comments) throws IOException
  {
    _store0 (new NonBlockingBufferedWriter (new OutputStreamWriter (out, CCharset.CHARSET_ISO_8859_1_OBJ)),
             comments,
             true);
  }

  private void _store0 (@Nonnull final Writer bw, final String comments, final boolean escUnicode) throws IOException
  {
    if (comments != null)
    {
      _writeComments (bw, comments);
    }
    bw.write ("#" + new Date ().toString ());
    bw.write (CGlobal.LINE_SEPARATOR);
    for (final Map.Entry <String, String> aEntry : entrySet ())
    {
      String key = aEntry.getKey ();
      String val = aEntry.getValue ();
      key = _saveConvert (key, true, escUnicode);
      /*
       * No need to escape embedded and trailing spaces for value, hence pass
       * false to flag.
       */
      val = _saveConvert (val, false, escUnicode);
      bw.write (key + "=" + val);
      bw.write (CGlobal.LINE_SEPARATOR);
    }
    bw.flush ();
  }

  /**
   * Searches for the property with the specified key in this property list. If
   * the key is not found in this property list, the default property list, and
   * its defaults, recursively, are then checked. The method returns
   * <code>null</code> if the property is not found.
   * 
   * @param key
   *        the property key.
   * @return the value in this property list with the specified key value.
   * @see #setProperty
   * @see #m_aDefaults
   */
  public String getProperty (final String key)
  {
    final String sval = super.get (key);
    return (sval == null) && (m_aDefaults != null) ? m_aDefaults.getProperty (key) : sval;
  }

  /**
   * Searches for the property with the specified key in this property list. If
   * the key is not found in this property list, the default property list, and
   * its defaults, recursively, are then checked. The method returns the default
   * value argument if the property is not found.
   * 
   * @param key
   *        the hashtable key.
   * @param defaultValue
   *        a default value.
   * @return the value in this property list with the specified key value.
   * @see #setProperty
   * @see #m_aDefaults
   */
  public String getProperty (final String key, final String defaultValue)
  {
    final String val = getProperty (key);
    return (val == null) ? defaultValue : val;
  }
}
