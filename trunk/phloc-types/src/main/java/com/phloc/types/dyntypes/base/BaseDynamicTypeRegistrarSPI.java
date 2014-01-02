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
package com.phloc.types.dyntypes.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.w3c.dom.Node;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.types.dyntypes.IDynamicTypeRegistrarSPI;
import com.phloc.types.dyntypes.impl.DynamicTypeRegistry;

@IsSPIImplementation
public class BaseDynamicTypeRegistrarSPI implements IDynamicTypeRegistrarSPI
{
  public void registerDynamicTypes ()
  {
    DynamicTypeRegistry.registerDynamicType (boolean.class, DynamicValueBoolean.class);
    DynamicTypeRegistry.registerDynamicType (Boolean.class, DynamicValueBoolean.class);
    DynamicTypeRegistry.registerDynamicType (byte.class, DynamicValueByte.class);
    DynamicTypeRegistry.registerDynamicType (Byte.class, DynamicValueByte.class);
    DynamicTypeRegistry.registerDynamicType (char.class, DynamicValueCharacter.class);
    DynamicTypeRegistry.registerDynamicType (Character.class, DynamicValueCharacter.class);
    DynamicTypeRegistry.registerDynamicType (double.class, DynamicValueDouble.class);
    DynamicTypeRegistry.registerDynamicType (Double.class, DynamicValueDouble.class);
    DynamicTypeRegistry.registerDynamicType (float.class, DynamicValueFloat.class);
    DynamicTypeRegistry.registerDynamicType (Float.class, DynamicValueFloat.class);
    DynamicTypeRegistry.registerDynamicType (int.class, DynamicValueInteger.class);
    DynamicTypeRegistry.registerDynamicType (Integer.class, DynamicValueInteger.class);
    DynamicTypeRegistry.registerDynamicType (long.class, DynamicValueLong.class);
    DynamicTypeRegistry.registerDynamicType (Long.class, DynamicValueLong.class);
    DynamicTypeRegistry.registerDynamicType (short.class, DynamicValueShort.class);
    DynamicTypeRegistry.registerDynamicType (Short.class, DynamicValueShort.class);
    DynamicTypeRegistry.registerDynamicType (String.class, DynamicValueString.class);
    DynamicTypeRegistry.registerDynamicType (BigDecimal.class, DynamicValueBigDecimal.class);
    DynamicTypeRegistry.registerDynamicType (BigInteger.class, DynamicValueBigInteger.class);
    DynamicTypeRegistry.registerDynamicType (Node.class, DynamicValueDOMNode.class);
    DynamicTypeRegistry.registerDynamicType (IMicroNode.class, DynamicValueMicroNode.class);
    DynamicTypeRegistry.registerDynamicType (Locale.class, DynamicValueLocale.class);
    DynamicTypeRegistry.registerDynamicType (LocalDate.class, DynamicValueLocalDate.class);
    DynamicTypeRegistry.registerDynamicType (LocalTime.class, DynamicValueLocalTime.class);
    DynamicTypeRegistry.registerDynamicType (LocalDateTime.class, DynamicValueLocalDateTime.class);
    DynamicTypeRegistry.registerDynamicType (DateTime.class, DynamicValueDateTime.class);
  }
}
