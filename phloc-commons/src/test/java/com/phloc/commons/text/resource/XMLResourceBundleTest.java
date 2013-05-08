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
package com.phloc.commons.text.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link XMLResourceBundle}.
 * 
 * @author Philip Helger
 */
// SKIPJDK5
public final class XMLResourceBundleTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll () throws IOException
  {
    final File aFile = new File ("target/test-classes/unittest-xml-props.xml");
    try
    {
      // Create dummy properties file
      final Properties p = new Properties ();
      p.setProperty ("prop1", "Value 1");
      p.setProperty ("prop2", "äöü");
      p.storeToXML (FileUtils.getOutputStream (aFile), null, CCharset.CHARSET_UTF_8);

      // Read again
      final ResourceBundle aRB = XMLResourceBundle.getXMLBundle ("unittest-xml-props");
      assertNotNull (aRB);
      assertEquals ("Value 1", aRB.getString ("prop1"));
      assertEquals ("äöü", aRB.getString ("prop2"));
      try
      {
        aRB.getObject ("prop3");
        fail ();
      }
      catch (final MissingResourceException ex)
      {
        // expected
      }
    }
    finally
    {
      FileOperations.deleteFile (aFile);
    }
  }
}
