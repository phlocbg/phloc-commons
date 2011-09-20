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
package com.phloc.commons.text.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ResourceBundle;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link Utf8ResourceBundle}.
 * 
 * @author philip
 */
public final class Utf8ResourceBundleTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    assertNotNull (Utf8ResourceBundle.getBundle ("properties/test-utf8"));
    assertNotNull (Utf8ResourceBundle.getBundle ("properties/test-utf8", L_DE));
    final ResourceBundle rb = Utf8ResourceBundle.getBundle ("properties/test-utf8",
                                                            L_DE,
                                                            ResourceBundleKey.getClassLoader ());
    assertNotNull (rb);
    assertTrue (rb instanceof Utf8PropertyResourceBundle);
    assertEquals (2, ContainerHelper.newList (rb.getKeys ()).size ());
  }
}
