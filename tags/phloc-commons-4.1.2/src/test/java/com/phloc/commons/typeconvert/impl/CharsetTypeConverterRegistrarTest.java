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
package com.phloc.commons.typeconvert.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.charset.Charset;

import org.junit.Test;

import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.typeconvert.TypeConverter;

/**
 * Test class for class {@link CharsetTypeConverterRegistrar}.
 * 
 * @author Philip Helger
 */
public final class CharsetTypeConverterRegistrarTest
{
  @Test
  public void testConvert ()
  {
    for (final Charset aCS : CharsetManager.getAllCharsets ().values ())
    {
      final String sCS = TypeConverter.convertIfNecessary (aCS, String.class);
      assertNotNull (sCS);
      assertEquals (aCS, TypeConverter.convertIfNecessary (sCS, Charset.class));
    }
  }
}
