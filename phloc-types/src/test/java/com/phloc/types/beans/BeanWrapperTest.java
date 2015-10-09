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
package com.phloc.types.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


/**
 * Test class for class {@link BeanWrapper}
 * 
 * @author Philip Helger
 */
public final class BeanWrapperTest
{
  @Test
  public void testBasic ()
  {
    for (int i = 0; i < 2; ++i)
    {
      final MockBean aMB = new MockBean ();
      final BeanWrapper aBW = new BeanWrapper (aMB, i == 0);

      // Arbitrary
      assertEquals (Integer.valueOf (0), aBW.getPropertyValue ("x"));
      assertTrue (aBW.setPropertyValue ("x", 2).isSuccess ());
      assertEquals (Integer.valueOf (2), aBW.getPropertyValue ("x"));

      assertEquals ("", aBW.getPropertyValue ("YY"));
      assertTrue (aBW.setPropertyValue ("YY", "Juhu").isSuccess ());
      assertEquals ("Juhu", aBW.getPropertyValue ("YY"));

      // Basic types
      assertTrue (aBW.setPropertyValue ("boolean", true).isSuccess ());
      assertTrue (aBW.getPropertyValueBoolean ("boolean"));
      assertTrue (aBW.setPropertyValue ("boolean", false).isSuccess ());
      assertFalse (aBW.getPropertyValueBoolean ("boolean"));

      assertTrue (aBW.setPropertyValue ("byte", (byte) 0xff).isSuccess ());
      assertEquals ((byte) 0xff, aBW.getPropertyValueByte ("byte"));
      assertTrue (aBW.setPropertyValue ("byte", (byte) 12).isSuccess ());
      assertEquals (12, aBW.getPropertyValueByte ("byte"));

      assertTrue (aBW.setPropertyValue ("char", '\u1234').isSuccess ());
      assertEquals ('\u1234', aBW.getPropertyValueChar ("char"));
      assertTrue (aBW.setPropertyValue ("char", 'x').isSuccess ());
      assertEquals ('x', aBW.getPropertyValueChar ("char"));

      assertTrue (aBW.setPropertyValue ("double", 31.415d).isSuccess ());
      assertEquals (31.415d, aBW.getPropertyValueDouble ("double"), 0);
      assertTrue (aBW.setPropertyValue ("double", -12).isSuccess ());
      assertEquals (-12, aBW.getPropertyValueDouble ("double"), 0);

      assertTrue (aBW.setPropertyValue ("float", 3.1415f).isSuccess ());
      assertEquals (3.1415f, aBW.getPropertyValueFloat ("float"), 0);
      assertTrue (aBW.setPropertyValue ("float", -42.003f).isSuccess ());
      assertEquals (-42.003f, aBW.getPropertyValueFloat ("float"), 0);

      assertTrue (aBW.setPropertyValue ("int", 0xff00ccdd).isSuccess ());
      assertEquals (0xff00ccdd, aBW.getPropertyValueInt ("int"));
      assertTrue (aBW.setPropertyValue ("int", -56).isSuccess ());
      assertEquals (-56, aBW.getPropertyValueInt ("int"));
      assertEquals (-56, aMB.getInt ());

      assertTrue (aBW.setPropertyValue ("long", 0xffeeddccbbaa9988L).isSuccess ());
      assertEquals (0xffeeddccbbaa9988L, aBW.getPropertyValueLong ("long"));
      assertTrue (aBW.setPropertyValue ("long", 1234L).isSuccess ());
      assertEquals (1234L, aBW.getPropertyValueLong ("long"));
      assertEquals (1234L, aMB.getLong ());

      assertTrue (aBW.setPropertyValue ("short", (short) 0xffff).isSuccess ());
      assertEquals ((short) 0xffff, aBW.getPropertyValueShort ("short"));
      assertTrue (aBW.setPropertyValue ("short", (short) 112).isSuccess ());
      assertEquals (112, aBW.getPropertyValueShort ("short"));
      assertEquals (112, aMB.getShort ());

      final Map <String, Object> aValues = new HashMap <String, Object> ();
      aValues.put ("boolean", Boolean.TRUE);
      aValues.put ("short", Short.valueOf ((short) 1111));
      assertTrue (aBW.setPropertyValues (aValues).isSuccess ());
      assertTrue (aBW.getPropertyValueBoolean ("boolean"));
      assertEquals (1111, aBW.getPropertyValueShort ("short"));
      assertTrue (aMB.getBoolean ());
      assertEquals (1111, aMB.getShort ());
    }

    try
    {
      // null not allowed
      new BeanWrapper (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testInvalidType ()
  {
    final MockBean aMB = new MockBean ();

    {
      // Without type conversion
      final BeanWrapper aBW = new BeanWrapper (aMB);
      assertFalse (aMB.getBoolean ());
      assertEquals (0, aMB.getInt ());
      assertFalse (aBW.setPropertyValue ("boolean", 12345).isSuccess ());
      assertFalse (aMB.getBoolean ());
      assertEquals (0, aMB.getInt ());

      try
      {
        // "int" is not a boolean
        aBW.getPropertyValueBoolean ("int");
        fail ();
      }
      catch (final ClassCastException ex)
      {}
    }

    {
      // With type conversion
      final BeanWrapper aBW = new BeanWrapper (aMB, true);
      assertFalse (aMB.getBoolean ());
      assertEquals (0, aMB.getInt ());
      assertTrue (aBW.setPropertyValue ("boolean", 12345).isSuccess ());
      assertTrue (aMB.getBoolean ());
      assertEquals (0, aMB.getInt ());
    }
  }
}
