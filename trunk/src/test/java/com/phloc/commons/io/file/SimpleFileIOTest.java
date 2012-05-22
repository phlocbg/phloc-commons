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
package com.phloc.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.resource.ClassPathResource;

/**
 * Test class for class {@link SimpleFileIO}.
 * 
 * @author philip
 */
public final class SimpleFileIOTest
{
  @Test
  public void testReadFileBytes () throws UnsupportedEncodingException
  {
    final String s = "äöü text";
    final File f = new File ("dummy.txt");
    assertTrue (SimpleFileIO.writeFile (f, s.getBytes (CCharset.CHARSET_ISO_8859_1)).isSuccess ());
    try
    {
      final byte [] aBytes = SimpleFileIO.readFileBytes (f);
      assertNotNull (aBytes);
      assertTrue (Arrays.equals (aBytes, s.getBytes (CCharset.CHARSET_ISO_8859_1)));
      assertNull (SimpleFileIO.readFileBytes (null));
      assertNull (SimpleFileIO.readFileBytes (new File ("non existing file")));
    }
    finally
    {
      FileOperations.deleteFile (f);
    }
  }

  @Test
  public void testReaFileLines ()
  {
    assertNull (SimpleFileIO.readFileLines (null, CCharset.CHARSET_ISO_8859_1));
    assertNull (SimpleFileIO.readFileLines (new File ("ha ha said the clown"), CCharset.CHARSET_ISO_8859_1));
    final File aFile = ClassPathResource.getAsFile ("streamutils-lines");
    assertTrue (aFile.exists ());
    final List <String> lines = SimpleFileIO.readFileLines (aFile, CCharset.CHARSET_ISO_8859_1);
    assertEquals (10, lines.size ());
  }

  @Test
  public void testReadFileAsString ()
  {
    final File aFile = new File ("umlaut-tests.txt");
    final String s = "defäöüabc";
    assertEquals ("Source encoding of the Java file must be UTF-8!", 9, s.length ());
    assertNull (SimpleFileIO.readFileAsString (null, CCharset.CHARSET_ISO_8859_1));
    assertTrue (SimpleFileIO.writeFile (aFile, s, CCharset.CHARSET_UTF_8).isSuccess ());
    try
    {
      final String t = SimpleFileIO.readFileAsString (aFile, CCharset.CHARSET_UTF_8);
      assertEquals (s, t);
    }
    finally
    {
      assertTrue (FileOperations.deleteFile (aFile).isSuccess ());
    }
    assertTrue (SimpleFileIO.writeFile (aFile, s, CCharset.CHARSET_ISO_8859_1).isSuccess ());
    try
    {
      final String t = SimpleFileIO.readFileAsString (aFile, CCharset.CHARSET_ISO_8859_1);
      assertEquals (s, t);
    }
    finally
    {
      assertTrue (FileOperations.deleteFile (aFile).isSuccess ());
    }
  }

  @Test
  public void testWriteFile ()
  {
    final File aFile = new File ("hahatwf.txt");
    try
    {
      assertTrue (SimpleFileIO.writeFile (aFile, new byte [10]).isSuccess ());
      assertTrue (SimpleFileIO.writeFile (aFile, new byte [10], 0, 5).isSuccess ());
      assertTrue (SimpleFileIO.writeFile (aFile, "abc", CCharset.CHARSET_ISO_8859_1).isSuccess ());
    }
    finally
    {
      FileOperations.deleteFile (aFile);
    }

    try
    {
      SimpleFileIO.writeFile (null, new byte [10]);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      SimpleFileIO.writeFile (null, new byte [10], 0, 5);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      SimpleFileIO.writeFile (null, "abc", CCharset.CHARSET_ISO_8859_1);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
