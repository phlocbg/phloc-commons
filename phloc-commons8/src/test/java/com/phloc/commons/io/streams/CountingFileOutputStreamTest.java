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
package com.phloc.commons.io.streams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.file.FileOperations;

/**
 * Test class for class {@link CountingFileOutputStream}.
 *
 * @author Philip Helger
 */
public final class CountingFileOutputStreamTest
{
  @Test
  public void testAll () throws IOException
  {
    final File f = new File ("CFOS.txt");
    try
    {
      try (CountingFileOutputStream aCFOS = new CountingFileOutputStream (f))
      {
        assertEquals (0, aCFOS.getBytesWritten ());
        StreamUtils.copyInputStreamToOutputStream (new StringInputStream ("abc", CCharset.CHARSET_ISO_8859_1_OBJ),
                                                   aCFOS);
        aCFOS.write ('a');
        aCFOS.write (CharsetManager.getAsBytes ("axy", CCharset.CHARSET_ISO_8859_1_OBJ));
        assertEquals (7, aCFOS.getBytesWritten ());
      }
      try (CountingFileOutputStream aCFOS = new CountingFileOutputStream (f, EAppend.APPEND))
      {
        aCFOS.write (CharsetManager.getAsBytes ("axy", CCharset.CHARSET_ISO_8859_1_OBJ));
        assertEquals (3, aCFOS.getBytesWritten ());
      }
      try (CountingFileOutputStream aCFOS = new CountingFileOutputStream (f.getAbsolutePath ()))
      {
        aCFOS.write (CharsetManager.getAsBytes ("axy", CCharset.CHARSET_ISO_8859_1_OBJ));
        assertEquals (3, aCFOS.getBytesWritten ());
      }
      try (CountingFileOutputStream aCFOS = new CountingFileOutputStream (f.getAbsolutePath (), EAppend.APPEND))
      {
        aCFOS.write (CharsetManager.getAsBytes ("axy", CCharset.CHARSET_ISO_8859_1_OBJ));
        assertEquals (3, aCFOS.getBytesWritten ());
        assertNotNull (aCFOS.toString ());
      }
    }
    finally
    {
      FileOperations.deleteFile (f);
    }
  }
}
