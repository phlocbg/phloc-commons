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
package com.phloc.commons.microdom.serialize;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

/**
 * Test class for class {@link MicroDOMInputStreamProvider}.
 * 
 * @author Philip Helger
 */
public final class MicroDOMInputStreamProviderTest
{
  @Test
  public void testSimple ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    aDoc.appendElement ("test");
    assertNotNull (new MicroDOMInputStreamProvider (aDoc, XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ).getInputStream ());
  }
}
