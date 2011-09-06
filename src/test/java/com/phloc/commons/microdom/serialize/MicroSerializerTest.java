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
package com.phloc.commons.microdom.serialize;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroFactory;
import com.phloc.commons.timing.StopWatch;
import com.phloc.commons.xml.DefaultXMLIterationHandler;
import com.phloc.commons.xml.serialize.EXMLSerializeDocType;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.EXMLSerializeIndent;

/**
 * Test class for class {@link MicroSerializer}
 * 
 * @author philip
 */
public final class MicroSerializerTest
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MicroSerializerTest.class);

  @Test
  public void testSetter ()
  {
    final MicroSerializer aMS = new MicroSerializer (CCharset.CHARSET_ISO_8859_1);
    for (final EXMLSerializeFormat eFormat : EXMLSerializeFormat.values ())
    {
      aMS.setFormat (eFormat);
      for (final EXMLSerializeIndent eIndent : EXMLSerializeIndent.values ())
      {
        aMS.setIndent (eIndent);
        for (final EXMLSerializeDocType eDocType : EXMLSerializeDocType.values ())
        {
          aMS.setSerializeDocType (eDocType);
        }
      }
    }
  }

  private IMicroDocument _createLargeDoc (final IMicroDocument doc, final boolean bWithText)
  {
    final IMicroElement aDocElement = doc.appendElement ("root");
    for (int i = 1; i <= 10; ++i)
    {
      final IMicroElement e1 = aDocElement.appendElement ("level1");
      for (int j = 1; j <= 20; ++j)
      {
        final IMicroElement e2 = e1.appendElement ("level2");
        for (int k = 1; k <= 100; ++k)
        {
          final IMicroElement e3 = e2.appendElement ("level3");
          if (bWithText)
            e3.appendText ("Level 3 text <> " + Double.toString (Math.random ()));
        }
        if (bWithText)
          e2.appendText ("Level 2 text " + Double.toString (Math.random ()));
      }
      if (bWithText)
        e1.appendText ("Level 1 text " + Double.toString (Math.random ()));
    }
    return doc;
  }

  @Test
  public void testLargeTree ()
  {
    final MicroSerializer aMS = new MicroSerializer (CCharset.CHARSET_ISO_8859_1);
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    final boolean bWithText = false;
    final IMicroDocument doc = _createLargeDoc (MicroFactory.newDocument (), bWithText);

    int nMilliSecs = 0;
    int nRun = 0;
    int nWarmUpRuns = 0;
    final StopWatch aSW = new StopWatch ();
    for (; nRun < 20; ++nRun)
    {
      aBAOS.reset ();

      /**
       * Current averages:<br>
       * Average: 122.1 millisecs<br>
       * After getNextSibling/getPrevSibling replacement:<br>
       * Average: 80.85 millisecs<br>
       * After changing StringBuilder to Writer:<br>
       * Average: 47.0 millisecs<br>
       */
      if (nRun < 2)
      {
        aMS.write (doc, aBAOS);
        nWarmUpRuns++;
      }
      else
      {
        aSW.start ();
        aMS.write (doc, aBAOS);
        aSW.stop ();
        nMilliSecs += aSW.getMillis ();
        aSW.reset ();
      }
      assertTrue (aBAOS.size () > 0);
    }
    s_aLogger.info ("Average MicroDOM write: " + ((double) nMilliSecs / (nRun - nWarmUpRuns)) + " millisecs");

    // Just do nothing :)
    aMS.write (doc, new DefaultXMLIterationHandler ());
  }
}
