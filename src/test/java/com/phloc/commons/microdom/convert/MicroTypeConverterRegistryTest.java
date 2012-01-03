/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.commons.microdom.convert;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.phloc.commons.state.EContinue;

/**
 * Test class for class {@link MicroTypeConverterRegistry}.
 * 
 * @author philip
 */
public final class MicroTypeConverterRegistryTest
{
  @Test
  public void testIterate ()
  {
    MicroTypeConverterRegistry.iterateAllRegisteredMicroTypeConverters (new IMicroTypeConverterCallback ()
    {
      @Nonnull
      public EContinue call (@Nonnull final Class <?> aClass, @Nonnull final IMicroTypeConverter aConverter)
      {
        assertNotNull (aClass);
        assertNotNull (aConverter);
        return EContinue.CONTINUE;
      }
    });
  }
}
