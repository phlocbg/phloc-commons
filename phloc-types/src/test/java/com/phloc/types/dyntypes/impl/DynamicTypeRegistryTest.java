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
package com.phloc.types.dyntypes.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.state.ESuccess;
import com.phloc.types.dyntypes.IDynamicValue;
import com.phloc.types.dyntypes.MockDynamicValueBoolean;
import com.phloc.types.dyntypes.base.DynamicValueBigDecimal;
import com.phloc.types.dyntypes.base.DynamicValueBigInteger;
import com.phloc.types.dyntypes.base.DynamicValueBoolean;
import com.phloc.types.dyntypes.base.DynamicValueByte;
import com.phloc.types.dyntypes.base.DynamicValueCharacter;
import com.phloc.types.dyntypes.base.DynamicValueDouble;
import com.phloc.types.dyntypes.base.DynamicValueFloat;
import com.phloc.types.dyntypes.base.DynamicValueInteger;
import com.phloc.types.dyntypes.base.DynamicValueLong;
import com.phloc.types.dyntypes.base.DynamicValueMicroNode;
import com.phloc.types.dyntypes.base.DynamicValueShort;
import com.phloc.types.dyntypes.base.DynamicValueString;

/**
 * Test class for class {@link DynamicTypeRegistry}.
 * 
 * @author Philip Helger
 */
public final class DynamicTypeRegistryTest
{
  @Test
  public void testAll ()
  {
    IDynamicValue aDynValue = DynamicTypeRegistry.createDynamicValue (Boolean.TRUE);
    assertTrue (aDynValue instanceof DynamicValueBoolean);
    assertEquals (Boolean.TRUE, aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (Byte.valueOf ((byte) 27));
    assertTrue (aDynValue instanceof DynamicValueByte);
    assertEquals (Byte.valueOf ((byte) 27), aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (Character.valueOf ('a'));
    assertTrue (aDynValue instanceof DynamicValueCharacter);
    assertEquals (Character.valueOf ('a'), aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (Float.valueOf (3.1415f));
    assertTrue (aDynValue instanceof DynamicValueFloat);
    assertEquals (Float.valueOf (3.1415f), aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (Double.valueOf (3.1415));
    assertTrue (aDynValue instanceof DynamicValueDouble);
    assertEquals (Double.valueOf (3.1415), aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (Integer.valueOf (-42));
    assertTrue (aDynValue instanceof DynamicValueInteger);
    assertEquals (Integer.valueOf (-42), aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (Long.valueOf (-42));
    assertTrue (aDynValue instanceof DynamicValueLong);
    assertEquals (Long.valueOf (-42), aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (Short.valueOf ((short) -42));
    assertTrue (aDynValue instanceof DynamicValueShort);
    assertEquals (Short.valueOf ((short) -42), aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue ("Hallo zusammen");
    assertTrue (aDynValue instanceof DynamicValueString);
    assertEquals ("Hallo zusammen", aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (BigInteger.valueOf (1212));
    assertTrue (aDynValue instanceof DynamicValueBigInteger);
    assertEquals (BigInteger.valueOf (1212), aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (BigDecimal.TEN);
    assertTrue (aDynValue instanceof DynamicValueBigDecimal);
    assertEquals (BigDecimal.TEN, aDynValue.getValue ());
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (new MicroDocument ().appendElement ("root"));
    assertTrue (aDynValue instanceof DynamicValueMicroNode);
    assertTrue (new MicroDocument ().appendElement ("root").isEqualContent ((IMicroNode) aDynValue.getValue ()));
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    aDynValue = DynamicTypeRegistry.createDynamicValue (new MicroElement ("element"));
    assertTrue (aDynValue instanceof DynamicValueMicroNode);
    assertTrue (new MicroElement ("element").isEqualContent ((IMicroNode) aDynValue.getValue ()));
    assertEquals (ESuccess.SUCCESS, aDynValue.setAsSerializationText (aDynValue.getAsSerializationText ()));

    // No mapping for lists!
    aDynValue = DynamicTypeRegistry.createDynamicValue (ContainerHelper.newList ("ha", "he"));
    assertNull (aDynValue);

    try
    {
      DynamicTypeRegistry.createDynamicValue (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGetAllRegisteredTypes ()
  {
    assertNotNull (DynamicTypeRegistry.getAllRegisteredTypes ());
    assertTrue (!DynamicTypeRegistry.getAllRegisteredTypes ().isEmpty ());
  }

  @Test
  public void testRegisterDynamicType ()
  {
    try
    {
      // Boolean.class is already registered!
      DynamicTypeRegistry.registerDynamicType (Boolean.class, DynamicValueBoolean.class);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      DynamicTypeRegistry.registerDynamicType (null, DynamicValueBoolean.class);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      DynamicTypeRegistry.registerDynamicType (Boolean.class, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      // Not instancable class
      DynamicTypeRegistry.registerDynamicType (Boolean.class, AbstractDynamicValue.class);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testOverwriteDynamicType ()
  {
    IDynamicValue aDynValue = DynamicTypeRegistry.createNewDynamicValue (Boolean.class);
    assertNotNull (aDynValue);
    assertTrue (aDynValue instanceof DynamicValueBoolean);

    // Boolean.class is already registered! => overwrite
    DynamicTypeRegistry.overwriteDynamicType (Boolean.class, MockDynamicValueBoolean.class);

    aDynValue = DynamicTypeRegistry.createNewDynamicValue (Boolean.class);
    assertNotNull (aDynValue);
    assertTrue (aDynValue instanceof MockDynamicValueBoolean);

    // revert to old dynamic value class
    DynamicTypeRegistry.overwriteDynamicType (Boolean.class, DynamicValueBoolean.class);

    aDynValue = DynamicTypeRegistry.createNewDynamicValue (Boolean.class);
    assertNotNull (aDynValue);
    assertTrue (aDynValue instanceof DynamicValueBoolean);
  }

  @Test
  public void testReplaceDynamicType ()
  {
    IDynamicValue aDynValue = DynamicTypeRegistry.createNewDynamicValue (Boolean.class);
    assertNotNull (aDynValue);
    assertTrue (aDynValue instanceof DynamicValueBoolean);

    // Boolean.class is already registered! => overwrite
    assertTrue (DynamicTypeRegistry.replaceDynamicType (DynamicValueBoolean.class, MockDynamicValueBoolean.class)
                                   .isChanged ());
    assertFalse (DynamicTypeRegistry.replaceDynamicType (DynamicValueBoolean.class, MockDynamicValueBoolean.class)
                                    .isChanged ());
    assertFalse (DynamicTypeRegistry.replaceDynamicType (MockDynamicValueBoolean.class, MockDynamicValueBoolean.class)
                                    .isChanged ());

    aDynValue = DynamicTypeRegistry.createNewDynamicValue (Boolean.class);
    assertNotNull (aDynValue);
    assertTrue (aDynValue instanceof MockDynamicValueBoolean);

    assertTrue (DynamicTypeRegistry.replaceDynamicType (MockDynamicValueBoolean.class, DynamicValueBoolean.class)
                                   .isChanged ());

    aDynValue = DynamicTypeRegistry.createNewDynamicValue (Boolean.class);
    assertNotNull (aDynValue);
    assertTrue (aDynValue instanceof DynamicValueBoolean);

    try
    {
      DynamicTypeRegistry.replaceDynamicType (null, MockDynamicValueBoolean.class);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      DynamicTypeRegistry.replaceDynamicType (DynamicValueBoolean.class, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
