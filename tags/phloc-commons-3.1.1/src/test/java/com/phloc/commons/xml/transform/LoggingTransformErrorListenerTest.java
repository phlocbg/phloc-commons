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
package com.phloc.commons.xml.transform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;

import org.junit.Test;

import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link LoggingTransformErrorListener}.
 * 
 * @author philip
 */
public final class LoggingTransformErrorListenerTest
{
  @Test
  public void testAll ()
  {
    final LoggingTransformErrorListener el = LoggingTransformErrorListener.getInstance ();
    final TransformerFactory fac = XMLTransformerFactory.createTransformerFactory (el,
                                                                                   new LoggingTransformURIResolver ());
    assertNotNull (fac);

    // Read valid XSLT
    Templates t1 = XMLTransformerFactory.newTemplates (fac, new ClassPathResource ("xml/test1.xslt"));
    assertNotNull (t1);

    // Read valid XSLT (with import)
    t1 = XMLTransformerFactory.newTemplates (fac, new ClassPathResource ("xml/test2.xslt"));
    assertNotNull (t1);

    // Read invalid XSLT
    assertNull (XMLTransformerFactory.newTemplates (fac, new ClassPathResource ("test1.txt")));

    PhlocTestUtils.testToStringImplementation (el);
  }
}
