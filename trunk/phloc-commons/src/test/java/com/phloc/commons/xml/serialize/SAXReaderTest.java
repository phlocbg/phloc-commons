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
package com.phloc.commons.xml.serialize;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.helpers.DefaultHandler;

import com.phloc.commons.callback.DoNothingExceptionHandler;
import com.phloc.commons.callback.IThrowingRunnable;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.xml.sax.CachingSAXInputSource;

/**
 * Test class for {@link SAXReader}
 * 
 * @author Philip Helger
 */
public final class SAXReaderTest
{
  @SuppressWarnings ("unused")
  private static final Logger s_aLogger = LoggerFactory.getLogger (SAXReaderTest.class);

  @BeforeClass
  public static void bc ()
  {
    SAXReaderSettings.setDefaultExceptionHandler (new DoNothingExceptionHandler ());
  }

  @AfterClass
  public static void ac ()
  {
    SAXReaderSettings.setDefaultExceptionHandler (new XMLLoggingExceptionHandler ());
  }

  @Test
  public void testMultithreadedSAX_CachingSAXInputSource ()
  {
    PhlocTestUtils.testInParallel (1000, new IThrowingRunnable ()
    {
      public void run () throws Exception
      {
        assertTrue (SAXReader.readXMLSAX (new CachingSAXInputSource (new ClassPathResource ("xml/buildinfo.xml")),
                                          new SAXReaderSettings ().setContentHandler (new DefaultHandler ()))
                             .isSuccess ());
      }
    });
  }

  @Test
  public void testMultithreadedSAX_ReadableResourceSAXInputSource ()
  {
    PhlocTestUtils.testInParallel (1000, new IThrowingRunnable ()
    {
      public void run () throws Exception
      {
        assertTrue (SAXReader.readXMLSAX (new ClassPathResource ("xml/buildinfo.xml"),
                                          new SAXReaderSettings ().setContentHandler (new DefaultHandler ()))
                             .isSuccess ());
      }
    });
  }
}
