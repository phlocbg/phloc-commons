/**
 * Copyright (C) 2006-2015 phloc systems
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
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.io.resource.ClassPathResource;

/**
 * Test class for class {@link SimpleFileIO}.
 * 
 * @author Philip Helger
 */
public final class SimpleFileIOTest
{
  @Test
  public void testReadFileBytes ()
  {
    final String s = "äöü text";
    final File f = new File ("dummy.txt");
    assertTrue (SimpleFileIO.writeFile (f, CharsetManager.getAsBytes (s, CCharset.CHARSET_ISO_8859_1_OBJ)).isSuccess ());
    try
    {
      final byte [] aBytes = SimpleFileIO.readFileBytes (f);
      assertNotNull (aBytes);
      assertTrue (Arrays.equals (aBytes, CharsetManager.getAsBytes (s, CCharset.CHARSET_ISO_8859_1_OBJ)));
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
    assertNull (SimpleFileIO.readFileLines (null, CCharset.CHARSET_ISO_8859_1_OBJ));
    assertNull (SimpleFileIO.readFileLines (new File ("ha ha said the clown"), CCharset.CHARSET_ISO_8859_1_OBJ));
    final File aFile = ClassPathResource.getAsFile ("streamutils-lines");
    assertTrue (aFile.exists ());
    final List <String> lines = SimpleFileIO.readFileLines (aFile, CCharset.CHARSET_ISO_8859_1_OBJ);
    assertEquals (10, lines.size ());
  }

  @Test
  public void testReadFileAsString () throws InterruptedException
  {
    final File aFile1 = new File ("umlaut-tests1.txt");
    final File aFile2 = new File ("umlaut-tests2.txt");
    final String s = "defäöüabc";
    assertEquals ("Source encoding of the Java file must be UTF-8!", 9, s.length ());
    assertNull (SimpleFileIO.readFileAsString (null, CCharset.CHARSET_ISO_8859_1_OBJ));
    assertTrue (SimpleFileIO.writeFile (aFile1, s, CCharset.CHARSET_UTF_8_OBJ).isSuccess ());
    try
    {
      final String t = SimpleFileIO.readFileAsString (aFile1, CCharset.CHARSET_UTF_8_OBJ);
      assertEquals (s, t);
    }
    finally
    {
      assertTrue (FileOperations.deleteFile (aFile1).isSuccess ());
    }
    assertTrue (SimpleFileIO.writeFile (aFile2, s, CCharset.CHARSET_ISO_8859_1_OBJ).isSuccess ());
    try
    {
      final String t = SimpleFileIO.readFileAsString (aFile2, CCharset.CHARSET_ISO_8859_1_OBJ);
      assertEquals (s, t);
    }
    finally
    {
      assertTrue (FileOperations.deleteFile (aFile2).isSuccess ());
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
      assertTrue (SimpleFileIO.writeFile (aFile, "abc", CCharset.CHARSET_ISO_8859_1_OBJ).isSuccess ());
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
      SimpleFileIO.writeFile (null, "abc", CCharset.CHARSET_ISO_8859_1_OBJ);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
