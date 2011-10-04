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
package com.phloc.commons.microdom.convert.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistrarSPI;
import com.phloc.commons.microdom.convert.MicroTypeConverterRegistry;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.state.EEnabled;
import com.phloc.commons.state.EFinish;
import com.phloc.commons.state.EInterrupt;
import com.phloc.commons.state.ELeftRight;
import com.phloc.commons.state.EMandatory;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.state.ETopBottom;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.state.EValidity;

@Immutable
@IsSPIImplementation
public final class BasicMicroTypeConverterRegistrar implements IMicroTypeConverterRegistrarSPI
{
  public void registerMicroTypeConverter ()
  {
    // String converter
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (String.class, StringMicroTypeConverter.getInstance ());

    // Other base type based on the String converter
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Boolean.class,
                                                                  new StringBasedMicroTypeConverter (Boolean.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Byte.class,
                                                                  new StringBasedMicroTypeConverter (Byte.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Character.class,
                                                                  new StringBasedMicroTypeConverter (Character.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Double.class,
                                                                  new StringBasedMicroTypeConverter (Double.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Float.class,
                                                                  new StringBasedMicroTypeConverter (Float.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Integer.class,
                                                                  new StringBasedMicroTypeConverter (Integer.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Long.class,
                                                                  new StringBasedMicroTypeConverter (Long.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Short.class,
                                                                  new StringBasedMicroTypeConverter (Short.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (BigDecimal.class,
                                                                  new StringBasedMicroTypeConverter (BigDecimal.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (BigInteger.class,
                                                                  new StringBasedMicroTypeConverter (BigInteger.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (StringBuffer.class,
                                                                  new StringBasedMicroTypeConverter (StringBuffer.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (StringBuilder.class,
                                                                  new StringBasedMicroTypeConverter (StringBuilder.class));

    // State enums
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EChange.class,
                                                                  new StringBasedMicroTypeConverter (EChange.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EContinue.class,
                                                                  new StringBasedMicroTypeConverter (EContinue.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EEnabled.class,
                                                                  new StringBasedMicroTypeConverter (EEnabled.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EFinish.class,
                                                                  new StringBasedMicroTypeConverter (EFinish.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EInterrupt.class,
                                                                  new StringBasedMicroTypeConverter (EInterrupt.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (ELeftRight.class,
                                                                  new StringBasedMicroTypeConverter (ELeftRight.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EMandatory.class,
                                                                  new StringBasedMicroTypeConverter (EMandatory.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (ESuccess.class,
                                                                  new StringBasedMicroTypeConverter (ESuccess.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (ETopBottom.class,
                                                                  new StringBasedMicroTypeConverter (ETopBottom.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (ETriState.class,
                                                                  new StringBasedMicroTypeConverter (ETriState.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EValidity.class,
                                                                  new StringBasedMicroTypeConverter (EValidity.class));
  }
}
