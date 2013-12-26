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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.phloc.commons.callback.DoNothingExceptionHandler;
import com.phloc.commons.xml.XMLFactory;

/**
 * Test class for {@link DOMReaderSettings}
 * 
 * @author Philip Helger
 */
public final class DOMReaderSettingsTest
{
  @BeforeClass
  public static void bc ()
  {
    DOMReaderSettings.setDefaultExceptionHandler (new DoNothingExceptionHandler ());
  }

  @AfterClass
  public static void ac ()
  {
    DOMReaderSettings.setDefaultExceptionHandler (new XMLLoggingExceptionHandler ());
  }

  @Test
  public void testDefault ()
  {
    assertNotNull (DOMReaderSettings.DEFAULT_SETTINGS);
    assertFalse (DOMReaderSettings.DEFAULT_SETTINGS.requiresSeparateDocumentBuilderFactory ());
    assertTrue (XMLFactory.DEFAULT_DOM_NAMESPACE_AWARE == DOMReaderSettings.DEFAULT_SETTINGS.isNamespaceAware ());
    assertTrue (XMLFactory.DEFAULT_DOM_VALIDATING == DOMReaderSettings.DEFAULT_SETTINGS.isValidating ());
    assertTrue (XMLFactory.DEFAULT_DOM_IGNORING_ELEMENT_CONTENT_WHITESPACE == DOMReaderSettings.DEFAULT_SETTINGS.isIgnoringElementContentWhitespace ());
    assertTrue (XMLFactory.DEFAULT_DOM_EXPAND_ENTITY_REFERENCES == DOMReaderSettings.DEFAULT_SETTINGS.isExpandEntityReferences ());
    assertTrue (XMLFactory.DEFAULT_DOM_IGNORING_COMMENTS == DOMReaderSettings.DEFAULT_SETTINGS.isIgnoringComments ());
    assertTrue (XMLFactory.DEFAULT_DOM_COALESCING == DOMReaderSettings.DEFAULT_SETTINGS.isCoalescing ());
    assertNull (DOMReaderSettings.DEFAULT_SETTINGS.getSchema ());
    assertTrue (XMLFactory.DEFAULT_DOM_XINCLUDE_AWARE == DOMReaderSettings.DEFAULT_SETTINGS.isXIncludeAware ());
    assertNull (DOMReaderSettings.DEFAULT_SETTINGS.getEntityResolver ());
    assertNull (DOMReaderSettings.DEFAULT_SETTINGS.getErrorHandler ());
    assertNotNull (DOMReaderSettings.DEFAULT_SETTINGS.getExceptionHandler ());
  }
}
